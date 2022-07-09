package com.cathaypacific.mmbbizrule.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.common.booking.DepartureArrivalTimeDTO;
import com.cathaypacific.mmbbizrule.handler.BookingBuildValidationHelpr;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialService;
import com.cathaypacific.mmbbizrule.model.booking.detail.TicketIssueInfo;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummaryConvertBean;
import com.cathaypacific.mmbbizrule.model.booking.summary.PassengerSegmentSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.SegmentSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.TicketIssueInfoSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.UMFormInfoSummary;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.model.PnrSearchBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.model.PnrSearchSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.service.PnrSearchService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.OneABookingSummaryService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

@Service
public class OneABookingSummaryServiceImpl implements OneABookingSummaryService {

	private static LogAgent logger = LogAgent.getLogAgent(OneABookingSummaryServiceImpl.class);
	
	@Value("${flight.flown.limithours}")
	private int flightFlownLimithours;
	
	@Value("${mmb.flight.passed.time}")
	private long flightPassedTime;

	@Autowired
	private PnrSearchService pnrSearchService;

	@Autowired
	private BookingBuildService bookingBuildService;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Autowired
	private BookingBuildValidationHelpr bookingBuildValidationHelpr;
	
	@Async
	@Override
	public Future<FlightBookingSummary> asyncGetFlightBookingSummary(FlightBookingSummaryConvertBean summary, LoginInfo loginInfo,  BookingBuildRequired required) throws BusinessBaseException {
		String rloc = summary.getRloc();
		if(StringUtils.isEmpty(rloc)) {
			return new AsyncResult<>(null);
		}
		
		//add passenger matching for member profile/travle doc and companion
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		if(pnrBooking == null){
			return new AsyncResult<>(null);
		}
		
		try {
		    // mice primay only according to profile name
		    if(required.isMiceBookingCheck() && BookingBuildUtil.isMiceBooking(pnrBooking.getSkList())) {
		        paxNameIdentificationService.primaryPaxIdentificationForMice(loginInfo, pnrBooking);
		    } else {
		        paxNameIdentificationService.primaryPaxIdentificationForMember(loginInfo, pnrBooking);
		    }
		} catch (Exception e) {
			logger.info(String.format("filter out booking from booking list because name primary passenger dentify failed,memberid[], bookingref[%s]. details message:%s", loginInfo.getMemberId(),pnrBooking.getOneARloc()),e.getMessage());
			return new AsyncResult<>(null);
		}
		
		//if booking is IDBooking, remove FQTV
		if(BooleanUtils.isTrue(pnrBooking.isIDBooking())) {
			//check staff id booking before parser
			bookingBuildValidationHelpr.removeAllFqtvForIDBooking(pnrBooking);
			summary.setInOneA(false);//remove from One A 
			//if booking rloc is from 1A search API and not from EODS, then filter out
			if(BooleanUtils.isFalse(summary.isInEods())) {
				return new AsyncResult<>(null);
			}
		}
		
		Booking booking = bookingBuildService.buildBooking(pnrBooking, loginInfo, required);
		//move the check in booking build
	/*	if (required.operateInfoAndStops() && booking.isIncompleteRedemptionBooking()) {
			logger.info(String.format("The incomplete redemption booking [%s] is filtered out", booking.getDisplayRloc()));
			return new AsyncResult<>(null);
		}*/
		return new AsyncResult<>(buildFlightBookingSummary(summary, booking,loginInfo));
	}
	
	/**
	 * check the booking is display only in summary page
	 * @param booking
	 * @return
	 */
	private boolean displayOnlyBooking(Booking booking){
	    // non mice group
	    // hasInvalidPri
	    // has unissue eticket
	    // mice c
        return isGroupBooking(booking) || BookingBuildUtil.hasInvalidPri(booking)
                || BookingBuildUtil.isTicketUnissuedForMice(booking) || BookingBuildUtil.isGRMC(booking);
	}
	

	// web channel will only set non mice to display
	private boolean isGroupBooking(Booking booking) {
        if(MMBBizruleConstants.ACCESS_CHANNEL_WEB.equalsIgnoreCase(MMBUtil.getCurrentAccessChannel())) {
            return booking.isGroupBooking();
        } else {
            return booking.isGroupBooking() || booking.isMiceBooking();
        }
    }

    private FlightBookingSummary buildFlightBookingSummary(FlightBookingSummaryConvertBean summary, Booking booking,LoginInfo loginInfo) {
		if(booking == null || summary == null) {
			return null;
		}
		
		FlightBookingSummary flightBookingSummary = new FlightBookingSummary();
		flightBookingSummary.setTempLinkedBooking(booking.isTempLinkedBooking());
		flightBookingSummary.setDisplayOnly(displayOnlyBooking(booking));
		if(booking.getBookingPackageInfo() != null) {
			flightBookingSummary.setFlightOnly(booking.getBookingPackageInfo().isFlightOnly());
			flightBookingSummary.setPackageBooking(booking.getBookingPackageInfo().isPackageBooking());
		}
		flightBookingSummary.setOneARloc(booking.getOneARloc());
		flightBookingSummary.setSpnr(booking.getSpnr());
		flightBookingSummary.setLoginRloc(booking.getLoginRloc());
		flightBookingSummary.setDisplayRloc(booking.getDisplayRloc());
		
		flightBookingSummary.setCreateDate(booking.getCreateDate());
		flightBookingSummary.setOfficeId(booking.getOfficeId());
		
		flightBookingSummary.setIbeBooking(booking.isIbeBooking());
		flightBookingSummary.setTrpBooking(booking.isTrpBooking());
		flightBookingSummary.setAppBooking(booking.isAppBooking());
		flightBookingSummary.setRedBooking(booking.isRedemptionBooking());
		flightBookingSummary.setGdsBooking(booking.isGdsBooking());
		flightBookingSummary.setGroupBooking(booking.isGroupBooking());
		flightBookingSummary.setGccBooking(booking.isGccBooking());
		flightBookingSummary.setStaffBooking(booking.isStaffBooking());
		flightBookingSummary.setRedUpgrade(booking.isHasFqtu());
		flightBookingSummary.setCanCheckIn(BooleanUtils.isTrue(booking.getCanCheckIn()));
		
		flightBookingSummary.setPassengers(booking.getPassengers());
		
		flightBookingSummary.setCompanionBooking(BookingBuildUtil.isCompanionBooking(booking,loginInfo));
		flightBookingSummary.setOnHoldBooking(booking.isBookingOnhold());
		flightBookingSummary.setEncryptedRloc(booking.getEncryptedRloc());
		flightBookingSummary.setCanIssueTicket(booking.isCanIssueTicket());
		flightBookingSummary.setIssueTicketsExisting(booking.isHasIssuedTicket());
		flightBookingSummary.setAdtk(booking.isAdtk());
		flightBookingSummary.setPcc(booking.isPcc());
		flightBookingSummary.setTktl(booking.isTktl());
		flightBookingSummary.setTkxl(booking.isTkxl());
		
		flightBookingSummary.setRebookMapping(booking.getRebookMapping());
		flightBookingSummary.setBookingDwCode(booking.getBookingDwCode());
		flightBookingSummary.setCprJourneys(booking.getCprJourneys());
		flightBookingSummary.setMiceBooking(booking.isMiceBooking());
		
		TicketIssueInfo ticketIssueInfo = booking.getTicketIssueInfo();
		if (ticketIssueInfo != null) {
			TicketIssueInfoSummary ticketIssueInfoSummary = new TicketIssueInfoSummary();
			ticketIssueInfoSummary.setDate(ticketIssueInfo.getDate());
			ticketIssueInfoSummary.setRpLocalDeadLineTime(ticketIssueInfo.getRpLocalDeadLineTime());
			ticketIssueInfoSummary.setIndicator(ticketIssueInfo.getIndicator());
			ticketIssueInfoSummary.setOfficeId(ticketIssueInfo.getOfficeId());
			ticketIssueInfoSummary.setTime(ticketIssueInfo.getTime());
			ticketIssueInfoSummary.setTimeZoneOffset(ticketIssueInfo.getTimeZoneOffset());
			flightBookingSummary.setTicketIssueInfo(ticketIssueInfoSummary);
		}
		
		List<SegmentSummary> segmentSummarys = new ArrayList<>();
		List<Segment> segments = booking.getSegments();
		for(Segment segment : segments) {
			SegmentSummary segmentSummary = new SegmentSummary();
			
			segmentSummary.setId(MMBBizruleConstants.BOOKING_TYPE_FLIGHT + segment.getSegmentID());
			try {
				segmentSummary.setOrderDate(DateUtil.getStrToDate(DepartureArrivalTimeDTO.TIME_FORMAT, segment.getDepartureTime().getTime()));
			} catch (ParseException e) {
				logger.error("Paser segment[ID: %s] departureTime failure for orderDate", segment.getSegmentID(), e);
			}
			segmentSummary.setType(MMBBizruleConstants.BOOKING_TYPE_FLIGHT);
			segmentSummary.setSegmentId(segment.getSegmentID());
			segmentSummary.setSegmentStatus(segment.getSegmentStatus());
			segmentSummary.setArrivalTime(segment.getArrivalTime());
			segmentSummary.setDepartureTime(segment.getDepartureTime());
			segmentSummary.setDestPort(segment.getDestPort());
			segmentSummary.setMarketCompany(segment.getMarketCompany());
			segmentSummary.setMarketSegmentNumber(segment.getMarketSegmentNumber());
			segmentSummary.setOriginPort(segment.getOriginPort());
			segmentSummary.setOperateCompany(segment.getOperateCompany());
			segmentSummary.setOperateSegmentNumber(segment.getOperateSegmentNumber());
			segmentSummary.setAirCraftType(segment.getAirCraftType());
			segmentSummary.setNumberOfStops(segment.getNumberOfStops());
			segmentSummary.setCabinClass(segment.getCabinClass());
			segmentSummary.setSubClass(segment.getSubClass());
			segmentSummary.setCheckedIn(segment.isCheckedIn());
			segmentSummary.setCanCheckIn(segment.isCanCheckIn());
			segmentSummary.setOpenToCheckIn(segment.isOpenToCheckIn());
			segmentSummary.setPostCheckIn(segment.isPostCheckIn());
			segmentSummary.setPcc(segment.isPcc());
			segmentSummary.setAdtk(segment.isAdtk());
			segmentSummary.setRebookInfo(segment.getRebookInfo());
			segmentSummarys.add(segmentSummary);
		}
		flightBookingSummary.setDetails(segmentSummarys);
		
		List<PassengerSegmentSummary> passengerSegmentSummarys = new ArrayList<>();
		
		List<PassengerSegment> passengerSegments = booking.getPassengerSegments();
		
		if (!CollectionUtils.isEmpty(passengerSegments)) {
			for (PassengerSegment passengerSegment : passengerSegments) {
				judgeAllHasUMEForm(flightBookingSummary, passengerSegment); // set AllHasUMForm field
				
				PassengerSegmentSummary passengerSegmentSummary = new PassengerSegmentSummary();
				passengerSegmentSummary.setPassengerId(passengerSegment.getPassengerId());
				passengerSegmentSummary.setSegmentId(passengerSegment.getSegmentId());
				passengerSegmentSummary.setCheckedIn(passengerSegment.isCheckedIn());
				passengerSegmentSummary.setReverseCheckinCarrier(passengerSegment.getReverseCheckinCarrier());
				passengerSegmentSummarys.add(passengerSegmentSummary);
			}
		}
		flightBookingSummary.setPassengerSegments(passengerSegmentSummarys);
		
		return flightBookingSummary;
	}

	/**
	 *Async to get  Get OneA Booking List
	 * @param memberId
	 * @return
	 * @throws BusinessBaseException
	 */
	@Async
	@Override
	public Future<List<FlightBookingSummaryConvertBean>> asyncGetOneABookingList(String memberId) throws BusinessBaseException {
		List<FlightBookingSummaryConvertBean> result = this.getOneABookingList(memberId);
		return new AsyncResult<>(result);
	}
	
	/**
	 * 
	 * @param memberId
	 * @return
	 * @throws BusinessBaseException
	 */
	@Override
	public List<FlightBookingSummaryConvertBean> getOneABookingList(String memberId) throws BusinessBaseException {
		List<PnrSearchBooking> pnrBookings = pnrSearchService.retrieveBookingList(memberId);
		if (CollectionUtils.isEmpty(pnrBookings)) {
			return Collections.emptyList();
		}
		
		
		List<FlightBookingSummaryConvertBean> flightBookingSummarys = new ArrayList<>();
		
		StringBuilder sb = new StringBuilder();
		pnrBookings.stream().forEach(pb -> sb.append(pb.getRloc() + " "));
		logger.info(String.format("Retrieved 1A rloc(s) for member [%s]:[%s]", memberId,sb.toString()));
		
		for (PnrSearchBooking pnrBooking : pnrBookings) {
			try {				
				//check booking status
				if (!isValidBooking(pnrBooking)) {
					continue;
				}
				FlightBookingSummaryConvertBean flightBookingSummary = new FlightBookingSummaryConvertBean();
				flightBookingSummarys.add(flightBookingSummary);
				
				flightBookingSummary.setRloc(pnrBooking.getRloc());
				flightBookingSummary.setInOneA(true);
				
			} catch (Exception e) {
				logger.warn(String.format("Booking :%s is filtered out", pnrBooking.getRloc()), e);
			}
		}
		return flightBookingSummarys;
	}

	/**
	 * check booking status
	 * @param pnrBooking
	 * @param memberId
	 * @return
	 */
	private boolean isValidBooking(PnrSearchBooking pnrBooking) {
		// 2)check departure time roughly
		if (!anyFlightInFlownLimitTime(pnrBooking.getSegments())) {
			logger.debug(String.format("All flights flown before 3 days: %s", pnrBooking.getRloc()));
			return false;
		}
		return true;
	}
	
	/**
	 * filter out the flights which flown < Limit time
	 * 
	 * @param pnrSegments
	 * @return
	 */
	private boolean anyFlightInFlownLimitTime(List<PnrSearchSegment> pnrSegments) {

		if (CollectionUtils.isEmpty(pnrSegments)) {
			return false;
		}

		for (PnrSearchSegment pnrSearchSegment : pnrSegments) {
			if (StringUtils.isEmpty(pnrSearchSegment.getDepDateTime())) {
				continue;
			}

			String pnrDepDateTime = pnrSearchSegment.getDepDateTime();
			try {
				// assume -1200 as time zone, if this not in the 3 days, that's
				// mean the flight depart time not in 3 days in any time zone
				if (BookingBuildUtil.checkFlightInFlownLimitTimeWithoutTz(PnrSearchSegment.TIME_FORMAT, flightFlownLimithours, pnrDepDateTime)) {
					return true;
				}
				
				// since there no time zone, so just filter out the flight roughly,
				// e.g. system time - departure time <= flightFlownLimithours + 24 hours
				/**
				 * if (System.currentTimeMillis() - pnrDate.getTime() <=
				 * (flightFlownLimithours + 24) * 3600000) { return true; }
				 */
			} catch (ParseException e) {
				logger.error(String.format(
						"Parse segment departure time error, filghtNum:%s, departure time in pnr search response:%s, expect format:%s",
						pnrSearchSegment.getCompany() + pnrSearchSegment.getNumber(), pnrSearchSegment.getDepDateTime(),
						PnrSearchSegment.TIME_FORMAT));
			}
		}
		return false;
	}

	@Async
	@Override
	public Future<Booking> asyncGetSelfBookings(FlightBookingSummaryConvertBean summary, LoginInfo loginInfo,  BookingBuildRequired required) throws BusinessBaseException {
		String rloc = summary.getRloc();
		if(StringUtils.isEmpty(rloc)) {
			return new AsyncResult<>(null);
		}
		
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		if(pnrBooking == null){
			return new AsyncResult<>(null);
		}
		
		try {
			paxNameIdentificationService.primaryPaxIdentificationForMember(loginInfo, pnrBooking);
		} catch (Exception e) {
			logger.info(String.format("filter out booking from booking list because name primary passenger dentify failed,memberid[], bookingref[%s]. details message:%s", loginInfo.getMemberId(),pnrBooking.getOneARloc()),e.getMessage());
			return new AsyncResult<>(null);
		}
		
		// the member info(FQTV/FQTR) of primary passenger should be matched with the input memberId, otherwise the booking should be filtered out
		RetrievePnrPassenger pnrPrimaryPassenger = Optional.ofNullable(pnrBooking.getPassengers()).orElse(new ArrayList<>())
				.stream().filter(pax -> BooleanUtils.isTrue(pax.isPrimaryPassenger())).findFirst().orElse(null);
		if (pnrPrimaryPassenger == null || !BooleanUtils.isTrue(pnrPrimaryPassenger.isLoginFFPMatched())) {
			logger.info("filter out booking, the member info(FQTV) of primary passenger should be matched with the input memberId");
			return new AsyncResult<>(null);
		}
		
		return new AsyncResult<>(bookingBuildService.buildBooking(pnrBooking, loginInfo, required));
	}
	
	private void judgeAllHasUMEForm(FlightBookingSummary flightBookingSummary, PassengerSegment passengerSegment) {
		UMFormInfoSummary umFormInfo = flightBookingSummary.getUmFormInfo();
		if (umFormInfo == null) {
			umFormInfo = new UMFormInfoSummary();
		}
		List<String> segmentIdList = umFormInfo.getSegmentIdList();
		if (segmentIdList == null) {
			segmentIdList = new ArrayList<>();
			umFormInfo.setSegmentIdList(segmentIdList);
		}
		
		
		if (!CollectionUtils.isEmpty(passengerSegment.getSpecialServices())) {
			List<SpecialService> noRepeatList = BookingBuildUtil.removeDubplicateSepcialService(passengerSegment.getSpecialServices());
			for (SpecialService specialService : noRepeatList) {
				// if ssr type is UMNR, judge additional status
				if (specialService.getCode().equals(OneAConstants.UNACCOMPANIED_MINOR_SSR_CODE_UMNR)
						&& !StringUtils.isEmpty(specialService.getAdditionalStatus())) {
					if (specialService.getAdditionalStatus().equals(MMBBizruleConstants.NON_UNACCOMPANIED_MINOR_EFORM)) { // no UMNR e-Form
						umFormInfo.setHasNotFilledUMForm(true);
						if (umFormInfo.getPassengerId() == null) {
							umFormInfo.setPassengerId(passengerSegment.getPassengerId());
						}
						flightBookingSummary.setUmFormInfo(umFormInfo);
						if (!segmentIdList.contains(passengerSegment.getSegmentId())) {
							segmentIdList.add(passengerSegment.getSegmentId());
						}
					} else if (specialService.getAdditionalStatus().equals(MMBBizruleConstants.HAVE_UNACCOMPANIED_MINOR_EFORM)) { // has filled UMNR e-Form
						umFormInfo.setHasFilledUMForm(true);
						flightBookingSummary.setUmFormInfo(umFormInfo);
					}
				}
			}
		}
	}
}
