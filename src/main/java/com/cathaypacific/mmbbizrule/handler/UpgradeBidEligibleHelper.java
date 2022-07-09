package com.cathaypacific.mmbbizrule.handler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.db.dao.TBSsrSkMappingDAO;
import com.cathaypacific.mmbbizrule.db.model.TBSsrSkMapping;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;

@Component
public class UpgradeBidEligibleHelper {
	
	private static LogAgent logger = LogAgent.getLogAgent(UpgradeBidEligibleHelper.class);
	
	@Autowired
	AEMService aemService;
	
	@Autowired
	private TBSsrSkMappingDAO tBSsrSkMappingDao;
	
	private static final String FLIGHT_CODE_2700 = "2700";

	private static final int FLIGHT_CODE_LENGTH_4 = 4;
	
	/**
	 * build upgrade bid eligibility
	 * @param passengerSegmentPropertiesList
	 * @param booking
	 */
	public List<Segment> getUpgradeBidEligibleSegments(Booking booking) {
		if(CollectionUtils.isEmpty(booking.getSegments())||CollectionUtils.isEmpty(booking.getPassengerSegments())){
			return Collections.emptyList();
		}
		
		boolean bookingContainsInfant = BookingBuildUtil.bookingContainsInfant(booking);
		boolean groupBooking = booking.isGroupBooking();
		boolean redemptionBooking = booking.isRedemptionBooking();
		boolean idOrADBooking = isIDOrADBooking(booking);
		boolean mobileAppBooking = booking.getBookingPackageInfo() != null && booking.getBookingPackageInfo().isMobBooking(); 
		boolean corporateBooking = booking.isCorporateBooking();
		boolean paxCanUpgrade =anyPaxCanUpgradeBid(booking);
		if (bookingContainsInfant || groupBooking || redemptionBooking 
				|| idOrADBooking || mobileAppBooking || corporateBooking || !paxCanUpgrade) { // if these conditions are met, all passengers are ineligible to upgrade bid 
		}
		List<Segment> upgradeBidEligiblePS = new ArrayList<>();
		for(Segment segment : booking.getSegments()){
			List<PassengerSegment> psList =booking.getPassengerSegments().stream().filter(ps->Objects.equals(ps.getSegmentId(), segment.getSegmentID())).collect(Collectors.toList());
			boolean econOrPreEconCabin = isEconOrPreEconCabin(segment);
			boolean eligibleFlightCode = isEligibleFlightCodeForUpgradeBid(segment);
			boolean issuedcxKaTicket = psList.stream().anyMatch(ps->BookingBuildUtil.isCxKaET(ps.getEticketNumber()));
			boolean hasNonCheckedInPax = psList.stream().anyMatch(ps->!ps.isCheckedIn());
			if (segment.isFlown() || !hasNonCheckedInPax) {
				return Collections.emptyList();
			}
			boolean stdWithin50Hours = isSTDWithin50Hours(segment);
			if(econOrPreEconCabin && eligibleFlightCode && issuedcxKaTicket && !stdWithin50Hours){
				upgradeBidEligiblePS.add(segment);
			}
		}
		return upgradeBidEligiblePS;
	}
	
	/**
	 * check if there is any passenger can upgrade bid (without ineligible ssr/sk)
	 * @param passengerSegment
	 * @param booking
	 * @return boolean
	 */
	private boolean anyPaxCanUpgradeBid(Booking booking) {
		// all SSR/SK in the booking
		List<RetrievePnrDataElements> allSsrSks = new ArrayList<>();
		if (!CollectionUtils.isEmpty(booking.getSsrList())) {
			allSsrSks.addAll(booking.getSsrList());
		}
		if (!CollectionUtils.isEmpty(booking.getSkList())) {
			allSsrSks.addAll(booking.getSkList());
		}
		if (CollectionUtils.isEmpty(allSsrSks)) {
			return true;
		}
		Map<String, List<RetrievePnrDataElements>> paxIdSsrSkMap = allSsrSks.stream().filter(ssrSk -> !StringUtils.isEmpty(ssrSk.getPassengerId()))
				.collect(Collectors.groupingBy(RetrievePnrDataElements::getPassengerId));
		// ineligible SSR/SK list
		List<String> ineligibleSsrSks = tBSsrSkMappingDao.findByAppCodeAndUpgradeBid(MMBConstants.APP_CODE,TBConstants.UPGRADE_BID_INHIBIT).stream()
				.map(TBSsrSkMapping::getSsrSkCode).collect(Collectors.toList());

		for (Passenger passenger : booking.getPassengers()) {
			if (CollectionUtils.isEmpty(paxIdSsrSkMap.get(passenger.getPassengerId()))
					|| !(paxIdSsrSkMap.get(passenger.getPassengerId()).stream()
							.anyMatch(ssrsk -> ineligibleSsrSks.contains(ssrsk.getType())))) {
				return true;
			}

		}
		return false;
	}
	
	/**
	 * check if the flight code of the segment is eligible for upgrade bid
	 * @param segment
	 * @return
	 */
	private boolean isEligibleFlightCodeForUpgradeBid(Segment segment) {
		if (segment == null) {
			return false;
		}
		
		// if flight is not operated by CX/KA, it is ineligible for upgrade bid
		if(!OneAConstants.COMPANY_CX.equals(segment.getOperateCompany()) 
				&& !OneAConstants.COMPANY_KA.equals(segment.getOperateCompany())){
			return false;
		}
		// CX2700 is eligible for upgrade bid
		if(OneAConstants.COMPANY_CX.equals(segment.getOperateCompany()) 
				&& FLIGHT_CODE_2700.equals(segment.getOperateSegmentNumber())){
			return true;
		}
		return segment.getOperateSegmentNumber().trim().length() != FLIGHT_CODE_LENGTH_4;
	}

	/**
	 * check if segment involve Japan/India ports in O/D
	 * @param segment
	 * @return
	 */
	private boolean isIndiaPortsInvolvedInOD(Segment segment) {
		// origin country
		String originCountry = null;
		// destination country
		String desCountry = null;
		
		if(!StringUtils.isEmpty(segment.getOriginPort())){
			originCountry = aemService.getCountryCodeByPortCode(segment.getOriginPort());
		}
		if(!StringUtils.isEmpty(segment.getDestPort())){
			desCountry = aemService.getCountryCodeByPortCode(segment.getDestPort());
		}
		return MMBBizruleConstants.COUNTRY_CODE_INDIA.equals(originCountry) 
				|| MMBBizruleConstants.COUNTRY_CODE_INDIA.equals(desCountry);
	}

 

	/**
	 * check if the cabin class of the passengerSegment is Economy or Premium Economy
	 * @param passengerSegment
	 * @return boolean
	 */
	private boolean isEconOrPreEconCabin(Segment segment) {
		if(segment == null){
			return false;
		}
		return MMBBizruleConstants.CABIN_CLASS_PEY_CLASS.equals(segment.getCabinClass()) 
				|| MMBBizruleConstants.CABIN_CLASS_ECON_CLASS.equals(segment.getCabinClass());
	}

	/**
	 * check if it is ID/AD booking (RBD O or T)
	 * @param booking
	 * @return
	 */
	private boolean isIDOrADBooking(Booking booking) {
		if(booking == null || CollectionUtils.isEmpty(booking.getSegments())){
			return false;
		}

		return booking.getSegments().stream()
				.anyMatch(seg -> seg != null && !StringUtils.isEmpty(seg.getSubClass())
						&& (MMBBizruleConstants.CABIN_SUBCLASS_O.equals(seg.getSubClass())
								|| MMBBizruleConstants.CABIN_SUBCLASS_T.equals(seg.getSubClass())))
				|| booking.isADBooking() || booking.isIDBooking();
	}

	/**
	 * Check is China/India POS
	 * @param pos
	 * @return boolean
	 */
	private boolean isChinaOrIndiaPOS(String pos) {
		return MMBBizruleConstants.POS_CHINA.equals(pos) || MMBBizruleConstants.POS_INDIA.equals(pos);
	}
	
	/**
	 * Check is India POS
	 * @param pos
	 * @return boolean
	 */
	private boolean isIndiaPOS(String pos) {
		return MMBBizruleConstants.POS_INDIA.equals(pos);
	}
	

	/**
	 * check if the sector is within STD-50hrs
	 * @param segment
	 * @return
	 */
	private boolean isSTDWithin50Hours(Segment segment) {
		if (segment != null && segment.getDepartureTime() != null && segment.getDepartureTime().getScheduledTime() != null) {
			Date stdDate;
			try {
				stdDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, segment.getDepartureTime().getScheduledTime(), segment.getDepartureTime().getTimeZoneOffset());
			} catch (ParseException e) {
				logger.warn(String.format("Failed to conver string %s to date", segment.getDepartureTime().getScheduledTime()));
				return false;
			}
			return !BookingBuildUtil.isCurrentTimeBeforCertainHoursOfDate(stdDate, 50);
		}
		return false;
	}
	
}
