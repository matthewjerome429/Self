package com.cathaypacific.mmbbizrule.util;

import static java.util.stream.Collectors.toList;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.enums.flightstatus.RtfsStatusEnum;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.config.BookingStatusConfig;
import com.cathaypacific.mmbbizrule.config.RtfsStatusConfig;
import com.cathaypacific.mmbbizrule.constant.CommonConstants;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.FlightStatusData;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.SectorDTO;
import com.cathaypacific.mmbbizrule.db.dao.CabinClassDAO;
import com.cathaypacific.mmbbizrule.db.model.BookingStatus;
import com.cathaypacific.mmbbizrule.db.model.CabinClass;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.ETicket;
import com.cathaypacific.mmbbizrule.model.booking.detail.OSIBookingSite;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialService;
import com.cathaypacific.mmbbizrule.model.booking.summary.SegmentSummary;
import com.cathaypacific.mmbbizrule.model.umnreform.MultiLineOTRemark;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.model.AirFlightInfoBean;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookingPackageInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElementsOtherData;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRemark;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTicket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.util.FreeTextUtil;
import com.google.common.collect.Lists;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

public class BookingBuildUtil {

	@Autowired
	private BizRuleConfig bizRuleConfig;

	private static LogAgent logger = LogAgent.getLogAgent(BookingBuildUtil.class);

	//private static final long TWENTY_FOUR_HRS_MILLIS = 86400000l;
	
	//private static final long NINETY_MINS_MILLIS = 5400000l;
	
	private static final Pattern  BOH_OFFICEID_PATTERN= Pattern.compile(MMBBizruleConstants.BOH_OFFICEID_PATTERN);
	private BookingBuildUtil(){
		
	}
	
	/**
	 * filter out hide segments
	 * 
	 * @param pnrSegmentList
	 * @param availableSegmentStatusList
	 * @return 
	 */
	public static BookingStatus getFirstAvailableStatus(List<String> pnrStatusList,
			List<BookingStatus> availableBookingStatusList) {
		
		if (!CollectionUtils.isEmpty(pnrStatusList)) {
			for (String pnrBookingStatus : pnrStatusList) {
				BookingStatus availableBookingStatus = availableBookingStatusList.stream()
						.filter(bs -> pnrBookingStatus.equals(bs.getStatusCode())).findFirst().orElse(null);
				if (availableBookingStatus != null) {
					return availableBookingStatus;
				}
			}
		}
		
		return null;
	}
	
	public static int convertCompareResultToInt(long compareResult) {
		if (compareResult > 0) {
			return 1;
		} else if (compareResult < 0) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public static boolean isUnaccompaniedMinor(RetrievePnrBooking pnrBooking,String passengerId){
		List<RetrievePnrDataElements> umnrList= Optional.ofNullable(pnrBooking.getSsrList()).orElse(Collections.emptyList()).stream().filter(ssr->OneAConstants.UNACCOMPANIED_MINOR_SSR_CODE_UMNR.equals(ssr.getType())).collect(Collectors.toList());
		if(!umnrList.isEmpty()){
			if(pnrBooking.getPassengers().size() == 1){
				return true;
			}else{
				return umnrList.stream().anyMatch(ssr->Objects.equals(passengerId, ssr.getPassengerId()));
			}
		}
		return false;
	}
	
	/**
	 * Retrieve age of a specified UMNR passenger
	 * @param pnrBooking
	 * @param passengerId
	 * @return a integer age
	 */
	public static String retrieveUMNRAge(RetrievePnrBooking pnrBooking, String passengerId){
		RetrievePnrDataElements pnrSsr = pnrBooking.getSsrList().stream().filter(ssr ->
			StringUtils.equalsIgnoreCase(OneAConstants.UNACCOMPANIED_MINOR_SSR_CODE_UMNR, ssr.getType()) &&
			(StringUtils.isEmpty(ssr.getPassengerId()) || StringUtils.equalsIgnoreCase(ssr.getPassengerId(), passengerId))
		).findFirst().orElse(null);
		
		// Parse UMNR Freetext to get the age
		return FreeTextUtil.getUMNRAge(pnrSsr.getFreeText());
	}
	
	public static String retrieveNameFromOSI(RetrievePnrDataElements osi){
		if (osi != null && osi.getOtherDataList() != null && !osi.getOtherDataList().isEmpty()) {
			return FreeTextUtil.getNameFromOSIFreeText(osi.getOtherDataList().get(0).getFreeText());
		}
		return null;
	}
	
	public static String retrievePhoneNumberFromOSI(RetrievePnrDataElements osi){
		if (osi != null && osi.getOtherDataList() != null && !osi.getOtherDataList().isEmpty()) {
			return FreeTextUtil.getPhoneNumberFromOSIFreeText(osi.getOtherDataList().get(0).getFreeText());
		}
		return null;
	}
	
	/**
	 * set time zone offset
	 * 
	 * @param originPortOffset
	 * @param destPortOffset
	 * @param segmentSummary
	 */
	public static void setTimeZone(String originPortOffset, String destPortOffset,SegmentSummary segmentSummary) {
		if (segmentSummary.getArrivalTime() == null) {
			segmentSummary.setArrivalTime(new DepartureArrivalTime());
		}
		if (segmentSummary.getDepartureTime() == null) {
			segmentSummary.setDepartureTime(new DepartureArrivalTime());
		}
		segmentSummary.getArrivalTime().setTimeZoneOffset(destPortOffset);
		segmentSummary.getDepartureTime().setTimeZoneOffset(originPortOffset);
	}
	
	/**
	 * set flown flag and flight status
	 * @param segmentSummary
	 * @param statusList
	 * @param availableBookingStatus
	 * @throws UnexpectedException 
	 * @throws ParseException 
	 */
	public static SegmentStatus generateFlightStatus(DepartureArrivalTime departureTime, List<String> statusList,
			List<BookingStatus> availableBookingStatusList, BookingStatusConfig bookingStatusConfig, long flightPassedTime) throws UnexpectedException {

		SegmentStatus segmentStatus = new SegmentStatus();
		/** find the first available flight status as flight status */
		BookingStatus availableBookingStatus = BookingBuildUtil.getFirstAvailableStatus(statusList,
				availableBookingStatusList);
		if (availableBookingStatus != null) {
			segmentStatus.setPnrStatus(availableBookingStatus.getStatusCode());
			if (TBConstants.BOOKINGSTATUS_DISABLE.equals(availableBookingStatus.getAction())) {
				segmentStatus.setDisable(true);
			}
			 if (containsAvlBookingStatus(availableBookingStatus, bookingStatusConfig.getConfirmedList())) {
				segmentStatus.setStatus(FlightStatusEnum.CONFIRMED);
			} else if (containsAvlBookingStatus(availableBookingStatus, bookingStatusConfig.getCancelledList())) {
				segmentStatus.setStatus(FlightStatusEnum.CANCELLED);
			} else if (containsAvlBookingStatus(availableBookingStatus, bookingStatusConfig.getWaitlistedList())) {
				segmentStatus.setStatus(FlightStatusEnum.WAITLISTED);
			} else if (containsAvlBookingStatus(availableBookingStatus, bookingStatusConfig.getStandbyList())) {
				segmentStatus.setStatus(FlightStatusEnum.STANDBY);
			}
		}
		
		if(!FlightStatusEnum.CANCELLED.equals(segmentStatus.getStatus()) && isFlown(statusList, departureTime, flightPassedTime)) {
			segmentStatus.setFlown(true);
		}
		
		return segmentStatus;
	}
	
	/**
	 * Judge availableBookingStatus is in statusList or not.
	 * 
	 * @param availableBookingStatus
	 * @param statusList
	 * @return
	 */
	public static boolean containsAvlBookingStatus(BookingStatus availableBookingStatus, List<String> statusList) {
		return statusList.contains(availableBookingStatus.getStatusCode());
	}
	
	/**
	 * Check if flight in flown limit without timeZone
	 * 
	 * @param timeFormat
	 * @param flightFlownLimithours
	 * @param eodsDepDateTime
	 * @return
	 */
	public static boolean checkFlightInFlownLimitTimeWithoutTz (String timeFormat, int flightFlownLimithours, String depDateTime) throws ParseException {
		Date depatureGMTDate = DateUtil.getStrToGMTDate(timeFormat, "-1200", depDateTime);
		// assume -1200 as time zone, if this not in the 3 days, that's
		// mean the flight depart time not in 3 days in any time zone
		return System.currentTimeMillis() - depatureGMTDate.getTime() <= flightFlownLimithours * 3600000;
	}
	
	/**
	 * filter out the flights which flown < Limit time
	 * 
	 * @param pnrSegments
	 * @return
	 */
	public static boolean departureTimeLimitCheck(List<SegmentSummary> segmentSummarys, int flightFlownLimithours) {
		try {
			for (SegmentSummary segmentSummary : segmentSummarys) {
				String departTime = segmentSummary.getDepartureTime().getTime();
				if (StringUtils.isEmpty(departTime)) {
					return false;
				}
				Date departDate = DateUtil.getStrToGMTDate(DepartureArrivalTime.TIME_FORMAT,
						segmentSummary.getDepartureTime().getTimeZoneOffset(), departTime);
				if (System.currentTimeMillis() - departDate.getTime() <= flightFlownLimithours * 3600000) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Parser  depart time error.", e);
		}

		return false;
	}
	
	/**
	 * Check the flight is flown or not.</br>
	 * 1. Check the status B in PNR response. 
	 * @param pnrSegmengtStatusList
	 * @param segment
	 * @return
	 * @throws ExpectedException 
	 * @throws ParseException 
	 */
	public static boolean isFlown(List<String> pnrSegmengtStatusList, DepartureArrivalTime departureTime, long flightPassedTime) throws UnexpectedException{
		boolean flag = false;
		 
		if(departureTime==null){
			 return flag;
		 }
		 
		if (!CollectionUtils.isEmpty(pnrSegmengtStatusList)) {
			flag = pnrSegmengtStatusList.contains(OneAConstants.SEGMENTSTATUS_FLOWN);
		}
		
		if(!StringUtils.isEmpty(departureTime.getRtfsActualTime())){
			flag = true;
		}
		
		if(!flag){
			try {
				Date flightDate = DateUtil.getStrToDate( DepartureArrivalTime.TIME_FORMAT, departureTime.getPnrTime(), departureTime.getTimeZoneOffset());
				flag= !StringUtils.isEmpty(departureTime.getRtfsActualTime())|| (StringUtils.isEmpty(departureTime.getRtfsScheduledTime()) && (flightDate.getTime() + flightPassedTime) <= System.currentTimeMillis());
			} catch (ParseException e) {
				String errorMsg= String.format("Unexpect date format: +%s,the expect format is %s", departureTime.getPnrTime(),DepartureArrivalTime.TIME_FORMAT);
				logger.error(errorMsg);
				throw new UnexpectedException(String.format("Unexpect date format: +%s,the expect format is %s", departureTime.getPnrTime(),DepartureArrivalTime.TIME_FORMAT),
						new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW),e);
			}
		}
		
		return flag;
	}
		
	/**
	 * Check the booking is ARO booking or not.
	 * @param officeId
	 */
	public static boolean isAROBooking(String officeId) {
		return !StringUtils.isEmpty(officeId) && officeId.length() == 9 
				&& (OneAConstants.OFFICE_ID_CX0.equals(officeId.substring(3, 6)) || OneAConstants.OFFICE_ID_KA0.equals(officeId.substring(3, 6)));
	}
	
	/**
	 * Check the passenger is infant, passenger.type is INF or INS
	 * @param pnrPax
	 * @return
	 */
	public static boolean isInfant(String pnrPaxType) {
		return OneAConstants.PASSENGER_TYPE_INF.equals(pnrPaxType)||OneAConstants.PASSENGER_TYPE_INS.equals(pnrPaxType);
	}
	
	/**
	 * Check the booking is GDS booking or not.
	 * @param officeId
	 */
	public static boolean isGDSBooking(String officeId) {
		// check GDS booking by the oppsite of direct booking
		return StringUtils.isEmpty(officeId) || officeId.length() != 9 
				|| (!OneAConstants.OFFICE_ID_CX0.equals(officeId.substring(3, 6)) && !OneAConstants.OFFICE_ID_KA0.equals(officeId.substring(3, 6)));
	}
	//OLSS-9388 Identifying 1A GDS bookings using office ID
	public static boolean is1AGDSBooking(String officeId){
		return !StringUtils.isEmpty(officeId) && officeId.length() == 9 && (officeId.charAt(5) == '2' || officeId.charAt(5) == '3');

	}
	
	/**
	 * Check the booking is GCC or not
	 * @param officeId
	 * @return
	 */
	public static boolean isGCCBooking(String officeId) {
		if (!StringUtils.isEmpty(officeId)) {
			String ibeOfficeId = officeId.substring(3);
			return (
							OneAConstants.OFFICE_ID_GCC_CX0001_0012.equals(ibeOfficeId) ||
							OneAConstants.OFFICE_ID_GCC_CX00AT.equals(ibeOfficeId) ||
							OneAConstants.OFFICE_ID_GCC_CX00CM.equals(ibeOfficeId) ||
							OneAConstants.OFFICE_ID_GCC_CX0100.equals(ibeOfficeId) ||
							OneAConstants.OFFICE_ID_GCC_CX0101.equals(ibeOfficeId) ||
							OneAConstants.OFFICE_ID_GCC_CX0102.equals(ibeOfficeId) ||
							OneAConstants.OFFICE_ID_GCC_KA0001_0012.equals(ibeOfficeId) ||
							OneAConstants.OFFICE_ID_GCC_KA00AT.equals(ibeOfficeId) ||
							OneAConstants.OFFICE_ID_GCC_KA00CM.equals(ibeOfficeId) ||
							OneAConstants.OFFICE_ID_GCC_KA0100.equals(ibeOfficeId) ||
							OneAConstants.OFFICE_ID_GCC_KA0101.equals(ibeOfficeId) ||
							OneAConstants.OFFICE_ID_GCC_KA0102.equals(ibeOfficeId)
						);
		}
		return false;
	}
	
	/**
	 * Check the booking is App booking or not
	 * @param skList
	 * @return
	 */
	public static boolean isAppBooking(List<RetrievePnrDataElements> skList) {
		if (skList == null || skList.isEmpty()) {
			return false;
		}
		
		String spnrText = skList.stream().filter(sk -> OneAConstants.SK_TYPE_SPNR.equals(sk.getType()))
				.map(RetrievePnrDataElements::getFreeText)
				.filter(StringUtils::isNotEmpty)
				.findFirst()
				.orElse("");

		String[] spnrFreeTextArry = spnrText.split("/");

		return spnrFreeTextArry.length >= 4 && OneAConstants.SK_TYPE_SPNR_BOOKINGTRPE_MOB.equals(spnrFreeTextArry[3]);
	}
	
	/**
	 * get trainCase by officeId
	 * @param officeId
	 */
	public static String getTrainCaseByOfficeId(String officeId) {
		String trainCase = null;
		if(isAROBooking(officeId)) {
			trainCase = CommonConstants.TRAIN_CASE_ARO;
		} else if(isGDSBooking(officeId)) {
			trainCase = CommonConstants.TRAIN_CASE_GDS;
		}
		return trainCase;
	} 
	
	/**
	 * 
	 * @param statusList
	 * @param availableBookingStatusList
	 * @param bookingStatusConfig
	 */
	public static FlightStatusEnum getFlightStatusEnum(String status, BookingStatusConfig bookingStatusConfig) {
		FlightStatusEnum flightStatusEnum = null;
		if (status != null) {
			if (bookingStatusConfig.getCancelledList().contains(status)) {
				flightStatusEnum = FlightStatusEnum.CANCELLED;
			} else if (bookingStatusConfig.getWaitlistedList().contains(status)) {
				flightStatusEnum = FlightStatusEnum.WAITLISTED;
			} else if (bookingStatusConfig.getConfirmedList().contains(status)) {
				flightStatusEnum = FlightStatusEnum.CONFIRMED;
			}
		}
		
		return flightStatusEnum;
	}
	
	/**
	 * Check if departure time is within check in open window, 24 hours or 90 minutes
	 * @param segment
	 * @param openWindow
	 */
//	public static void buildCheckinWindowInfo(Segment segment, long openWindow) {
//		if(segment.getDepartureTime() != null && !StringUtils.isEmpty(segment.getDepartureTime().getTime())) {
//			try {
//				Date etd = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, segment.getDepartureTime().getTime(), segment.getDepartureTime().getTimeZoneOffset());
//				Date std = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, segment.getDepartureTime().getScheduledTime(), segment.getDepartureTime().getTimeZoneOffset());
//				Date windowOpenDate = new Date();
//				Date twentyFrouHrDate = new Date();
//				Date ninetyMinsDate = new Date();
//				Date currentDate = new Date();
//				currentDate.setTime(System.currentTimeMillis());
//				windowOpenDate.setTime(System.currentTimeMillis() + openWindow);
//				twentyFrouHrDate.setTime(System.currentTimeMillis() + TWENTY_FOUR_HRS_MILLIS);
//				ninetyMinsDate.setTime(System.currentTimeMillis() + NINETY_MINS_MILLIS);
//				
//				buildCheckinWindowFlags(segment, etd, std, currentDate, ninetyMinsDate, twentyFrouHrDate, windowOpenDate);
//				buildChekinWindowCloseRemianingTime(segment, ninetyMinsDate, etd);
//			} catch (ParseException e) {
//				logger.error("Error to convert departure time");
//			}
//		}
//	}

	/**
	 * Check group booking by ssr 
	 * @param skList
	 * @return
	 */
	public static boolean isNonMiceGroupBooking(List<RetrievePnrDataElements> skList, List<RetrievePnrDataElements> ssrList) {
		return !isMiceBooking(skList) && isGroupBooking(ssrList);
	}
	
	/**
	 * Check group booking by ssr list 
	 * @param skList
	 * @return
	 */
	public static boolean isGroupBooking(List<RetrievePnrDataElements> ssrList) {
		if (!CollectionUtils.isEmpty(ssrList)) {
			return ssrList.stream().anyMatch(ssr -> OneAConstants.SSR_TYPE_GRPF.equals(ssr.getType()));
		}
		return false;
	}
	
	/**
	 * 
	 * @param skList
	 * @param remarkList
	 * @return
	 */
	public static RetrievePnrBookingPackageInfo buildBookingPackageInfo(List<RetrievePnrDataElements> skList, List<RetrievePnrRemark> remarkList) {
		RetrievePnrBookingPackageInfo bookingPackageInfo = new RetrievePnrBookingPackageInfo();
		bookingPackageInfo.setHasFlight(true);

		if (CollectionUtils.isEmpty(skList)) {
			return bookingPackageInfo;
		}

		// mob and ndc booking
		String spnrText = skList.stream().filter(sk -> sk != null && OneAConstants.SK_TYPE_SPNR.equals(sk.getType()))
				.map(RetrievePnrDataElements::getFreeText)
				.filter(str->!StringUtils.isEmpty(str))
				.findFirst()
				.orElse("");
		
		if(StringUtils.isEmpty(spnrText)){
			return bookingPackageInfo;
		}
		
		String[] spnrInfo = FreeTextUtil.getSpnrInfo(spnrText);
		if(spnrInfo == null) {
			return null;
		}
		
		bookingPackageInfo.setSpnr(spnrInfo[0]);
		
		boolean hasHotel = StringUtils.isNotEmpty(spnrInfo[2]) && spnrInfo[2].contains(OneAConstants.SK_TYPE_SPNR_HTL);
		boolean hasEvent = StringUtils.isNotEmpty(spnrInfo[2]) && spnrInfo[2].contains(OneAConstants.SK_TYPE_SPNR_XXBEVT);
		boolean isMOB = OneAConstants.SK_TYPE_SPNR_FO.equals(spnrInfo[1])
				&& OneAConstants.SK_TYPE_SPNR_BOOKINGTRPE_MOB.equals(spnrInfo[3]);
		boolean isNDC = OneAConstants.SK_TYPE_SPNR_FO.equals(spnrInfo[1])
				&& OneAConstants.SK_TYPE_SPNR_BOOKINGTRPE_NDC.equals(spnrInfo[3]);

		bookingPackageInfo.setMobBooking(isMOB);
		bookingPackageInfo.setNdcBooking(isNDC);
		bookingPackageInfo.setHasEvent(hasEvent);
		bookingPackageInfo.setHasHotel(hasHotel);

		// hotel, event
		List<String> rmList = remarkList.stream()
				.filter(remark -> OneAConstants.REMARK_TYPE_RM.equals(remark.getType())
						&& OneAConstants.REMARK_CATEGORY_T.equals(remark.getCategory()))
				.map(RetrievePnrRemark::getFreeText).filter(str -> !StringUtils.isEmpty(str))
				.map(freetext -> freetext.split("/")).filter(array -> array.length > 1)
				.map(code -> code[0])
				.collect(Collectors.toList());

		if (rmList.contains(OneAConstants.REMARK_CX_HOT)) {
			bookingPackageInfo.setHasHotel(true);
		}
		if (rmList.contains(OneAConstants.REMARK_CX_EVT)) {
			bookingPackageInfo.setHasEvent(true);
		}
		
		return bookingPackageInfo;
	}

	/**
	 * Check the booking is mice booking
	 * @param skList
	 * @return
	 */
	public static boolean isMiceBooking(List<RetrievePnrDataElements> skList ){
		if(CollectionUtils.isEmpty(skList)){
			return false;
		}
		return skList.stream().anyMatch(sk->OneAConstants.MICE_SK_IDENTIFIER_LIST.contains(sk.getType()));
	}
	
    /**
     * is grmc
     * 
     * @param skList
     * @return
     */
    public static boolean isMiceGRMC(List<RetrievePnrDataElements> skList) {
        if (CollectionUtils.isEmpty(skList)) {
            return false;
        }
        return skList.stream().anyMatch(sk -> OneAConstants.SK_TYPE_GRMC.equals(sk.getType()));
    }
    
    /**
     * is grmc
     * 
     * @param skList
     * @return
     */
    public static boolean isMiceGRMB(List<RetrievePnrDataElements> skList) {
        if (CollectionUtils.isEmpty(skList)) {
            return false;
        }
        return skList.stream().anyMatch(sk -> OneAConstants.SK_TYPE_GRMB.equals(sk.getType()));
    }
	
	/**
	 * Check if Upgrade Bid won segment
	 * @param skList
	 * @return
	 */
	public static boolean isUpgradeBidWonSegment(List<RetrievePnrDataElements> skList){
		if(CollectionUtils.isEmpty(skList)){
			return false;
		}
		return skList.stream().anyMatch(sk->OneAConstants.SSR_TYPE_FQUG.equals(sk.getType()));
	}
	
	/**
	 * Check if all Upgrade Bid won segment
	 * @param booking
	 * @return
	 */
	public static boolean isAllUpgradeBidWonSegment(Booking booking){
		Boolean isAllPlusGradeUpgrade = true;
		List<RetrievePnrDataElements> skList = booking.getSkList();
		if(CollectionUtils.isEmpty(skList)){
			return false;
		}
		
		List<Segment> bookingSegments = booking.getSegments();
		for (Segment s: bookingSegments) {
			if (skList.stream().filter(sk -> sk.getSegmentId() == s.getSegmentID()).noneMatch(sk->OneAConstants.SSR_TYPE_FQUG.equals(sk.getType()))) {
				isAllPlusGradeUpgrade = false;
			}
		}
		
		return isAllPlusGradeUpgrade;
	}
	
	/**
	 * Check if the booking is a BKUG
	 * @param booking
	 * @return 
	 */
	public static boolean isBookingBKUG(List<RetrievePnrDataElements> skList) {
		if (!CollectionUtils.isEmpty(skList)) {
			Boolean bkugBookingExist = skList.stream().anyMatch(sk -> OneAConstants.SK_TYPE_BKUG.equals(sk.getType()));
			if (bkugBookingExist) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * check any fqtu in the booking
	 * @param pnrBooking
	 * @return
	 */
	public static boolean hasFqtu(RetrievePnrBooking pnrBooking){
		 return pnrBooking.getSegments().stream().anyMatch(seg->seg.getFqtu()!=null);
	}
	
	/**
	 * has 01 pri of staff booking 
	 * @param pnrBooking
	 * @return
	 */
	public static boolean hasInvalidPri(RetrievePnrBooking pnrBooking) {
		return pnrBooking.getPassengers().stream().anyMatch(p -> Objects.nonNull(p.getStaffDetail())
				&& MMBBizruleConstants.BLOKCED_STAFF_PRI_CODE.equals(p.getStaffDetail().getPriority()));
	}
	
	/**
	 * has 01 pri of staff booking 
	 * @param Booking
	 * @return
	 */
	public static boolean hasInvalidPri(Booking booking){
		return booking.getPassengers().stream().anyMatch(p -> Objects.nonNull(p.getStaffDetail())
				&& MMBBizruleConstants.BLOKCED_STAFF_PRI_CODE.equals(p.getStaffDetail().getPriority()));
	}
	
//	/**
//	 * Build check in window close remaining time
//	 * @param segment
//	 * @param ninetyMinsDate
//	 * @param etd
//	 */
//	private static void buildChekinWindowCloseRemianingTime(Segment segment, Date ninetyMinsDate, Date etd) {
//		if(segment.isOpenToCheckIn() || segment.isWithinTwentyFourHrs() || segment.isWithinNinetyMins()) {
//			long departureRemainingTime = etd.getTime() - ninetyMinsDate.getTime();
//			segment.setCheckInRemainingTime(String.valueOf(departureRemainingTime));
//		}
//	}
	
//	/**
//	 * Build flags base on departure time within which range of 90 minutes, 24 hours, and 48 hours
//	 * @param segment
//	 * @param etd
//	 * @param std
//	 * @param currentDate
//	 * @param ninetyMinsDate
//	 * @param twentyFrouHrDate
//	 * @param windowOpenDate
//	 */
//	private static void buildCheckinWindowFlags(Segment segment, Date etd, Date std, Date currentDate, Date ninetyMinsDate, Date twentyFrouHrDate, Date windowOpenDate) {
//		if ((etd.after(currentDate) && etd.before(windowOpenDate)) || etd.compareTo(windowOpenDate) == 0) {
//			segment.setOpenToCheckIn(true);
//		}
//		
//		if((std.after(currentDate) && std.before(twentyFrouHrDate)) || std.compareTo(twentyFrouHrDate) == 0) {
//			segment.setWithinTwentyFourHrs(true);
//		}
//		
//		if((etd.after(currentDate) && etd.before(ninetyMinsDate)) || etd.compareTo(ninetyMinsDate) == 0) {
//			segment.setWithinNinetyMins(true);
//		}
//	}
	
	/**
	 * Check if the booking is incomplete redemption booking
	 * @param loginInfo
	 * @param booking
	 * @param pnrBooking
	 * @throws ExpectedException
	 */
	public static boolean isCxIncompleteRedemptionBooking(Booking booking,RetrievePnrBooking pnrBooking) {
		
		// incomplete Redemption Booking Check
		if(booking.isRedemptionBooking() && !hasTPOSElement(pnrBooking) && !hasIssuedTicket(pnrBooking)){
			return hasCxFQTR(pnrBooking);
		}
		
		return false;
	}
	
	/**
	 * Check if booking has TPOS element in sk list
	 * @param booking
	 * @return
	 */
	private static boolean hasTPOSElement(RetrievePnrBooking booking) {
		if(!CollectionUtils.isEmpty(booking.getSkList())) {
			for(RetrievePnrDataElements sk : booking.getSkList()) {
				if(OneAConstants.TPOS.equals(sk.getType())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Retrieve list of TPOS in booking
	 * @param booking
	 * @return
	 */
	public static List<String> getListOfTPOS(RetrievePnrBooking booking) {
		List<String> topsList = Lists.newArrayList();
		if(!CollectionUtils.isEmpty(booking.getSkList())) {
			for(RetrievePnrDataElements sk : booking.getSkList()) {
				if(OneAConstants.TPOS.equals(sk.getType())) {
					topsList.add((sk.getFreeText()));
				}
			}
		}
		return topsList;
	}
	
	/**
	 * Check if booking has FQTR
	 * 
	 * @param pnrBooking
	 * @return
	 */
	private static boolean hasCxFQTR(RetrievePnrBooking pnrBooking) {
		return Optional.ofNullable(pnrBooking.getPassengerSegments()).orElse(Collections.emptyList()).stream()
				.filter(ps -> !CollectionUtils.isEmpty(ps.getFQTRInfos()))
				.flatMap(ps -> ps.getFQTRInfos().stream())
				.anyMatch(fqtr -> MMBBizruleConstants.CX_OPERATOR.equals(fqtr.getFfpCompany())|| MMBBizruleConstants.KA_OPERATOR.equals(fqtr.getFfpCompany()));

	}
	
	/**
	 * Check if booking contains infant
	 * @param booking
	 * @return
	 */
	public static boolean bookingContainsInfant(Booking booking) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengers())){
			return false;
		}
		for(Passenger passenger : booking.getPassengers()){
			if(passenger != null && OneAConstants.PASSENGER_TYPE_INF.equals(passenger.getPassengerType())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * check if current time is before certain hours of input date
	 * @param date
	 * @param hourAmount
	 * @return boolean
	 */
	public static boolean isCurrentTimeBeforCertainHoursOfDate(Date date, int hourAmount) {
		if(date == null || hourAmount < 0){
			return false;
		}
		
		Calendar currentTime = Calendar.getInstance();
		currentTime.setTime(DateUtil.getGMTTime());
		currentTime.add(Calendar.HOUR_OF_DAY, hourAmount);
		return currentTime.getTime().before(date);
	}
	
	/**
	 * get passenger by passenger
	 * @param passengers
	 * @param passengerId
	 * @return
	 */
	public static Passenger getPassengerById(List<Passenger> passengers, String passengerId) {
		if(CollectionUtils.isEmpty(passengers) || StringUtils.isEmpty(passengerId)){
			return null;
		}
		
		return passengers.stream().filter(p -> p != null && passengerId.equals(p.getPassengerId()))
				.findFirst().orElse(null);
	}
	
	/**
	 * get segment by segment ID
	 * @param segments
	 * @param segmentId
	 * @return
	 */
	public static Segment getSegmentById(List<Segment> segments, String segmentId) {
		if(CollectionUtils.isEmpty(segments) || StringUtils.isEmpty(segmentId)){
			return null;
		}
		
		return segments.stream().filter(s -> s != null && segmentId.equals(s.getSegmentID()))
				.findFirst().orElse(null);
	}
	
	/**
	 * 
	 * @Description sort segments by PNR time
	 * @param segments
	 * @return List<Segment>
	 * @author haiwei.jia
	 */
	public static List<Segment> sortSegmentsByPnrTime(List<Segment> segments) {
		return segments.stream().sorted((seg1, seg2) -> {
			if(seg1.getDepartureTime() == null 
					|| StringUtils.isEmpty(seg1.getDepartureTime().getPnrTime())
					|| StringUtils.isEmpty(seg1.getDepartureTime().getTimeZoneOffset())){
				return 1;
			} else if(seg2.getDepartureTime() == null 
					|| StringUtils.isEmpty(seg2.getDepartureTime().getPnrTime())
					|| StringUtils.isEmpty(seg2.getDepartureTime().getTimeZoneOffset())){
				return -1;
			} else{
				Date departureTime1 = null;
				Date departureTime2 = null;
				try {
					departureTime1 = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT,
							seg1.getDepartureTime().getPnrTime(), seg1.getDepartureTime().getTimeZoneOffset());
				} catch (ParseException e) {
					logger.warn(String.format("Failed to conver string %s to date", departureTime1));
					return 1;
				}
				try {
					departureTime2 = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT,
							seg2.getDepartureTime().getPnrTime(), seg2.getDepartureTime().getTimeZoneOffset());
				} catch (ParseException e) {
					logger.warn(String.format("Failed to conver string %s to date", departureTime1));
					return -1;
				}
				return departureTime1.compareTo(departureTime2);
			}
		}).collect(Collectors.toList());
	}
	
	/**
	 * Whether the booking has insurance
	 * @param retrievePnrDataElements
	 * @return
	 */
	public static boolean hasInsurance(List<RetrievePnrDataElements> retrievePnrDataElements) {
		if (retrievePnrDataElements != null) {
			RetrievePnrDataElementsOtherData retrievePnrDataElementsOtherData = retrievePnrDataElements
					.stream()
					.filter(retrievePnrDataElement -> retrievePnrDataElement != null && retrievePnrDataElement.getOtherDataList() != null)
					.flatMap(retrievePnrDataElement -> retrievePnrDataElement.getOtherDataList().stream())
					.filter(retrievePnrDataElementsOtherDataTemp -> 
							retrievePnrDataElementsOtherDataTemp != null &&
							retrievePnrDataElementsOtherDataTemp.getFreeText() != null &&
							retrievePnrDataElementsOtherDataTemp.getFreeText().startsWith(PnrResponseParser.OSI_INSURANCE))
					.findFirst()
					.orElse(null);
	
			return retrievePnrDataElementsOtherData != null;
		}
		
		return false;
	}
	
	/**
	 * judge if it is a CX/KA e-ticket
	 * 
	 * @param eticket
	 * @return
	 */
	public static Boolean isCxKaET(String eticket) {
		if(StringUtils.isEmpty(eticket)){
			return false;
		}
		return OneAConstants.CX_ETICKET_PREFIX.equals(eticket.substring(0,3)) 
				|| OneAConstants.KA_ETICKET_PREFIX.equals(eticket.substring(0,3));
	}
	
	/**
	 * get OSI site info
	 * 
	 * @param pnrBooking
	 * @return OSIBookingSite
	 */
	public static OSIBookingSite getOsiBookingSite(RetrievePnrBooking pnrBooking) {
		String[] siteInfos = Optional.ofNullable(pnrBooking.getOsiList()).orElse(Collections.emptyList()).stream()
				.filter(element->CollectionUtils.isNotEmpty(element.getOtherDataList()))
				.flatMap(element->element.getOtherDataList().stream())
				.map(RetrievePnrDataElementsOtherData::getFreeText)
				.map(FreeTextUtil::parseOSISiteInfo)
				.filter(array->array.length>0)
				.findFirst().orElse(ArrayUtils.EMPTY_STRING_ARRAY);
		
		if(siteInfos.length > 0) {
			OSIBookingSite osiBookingSite = new OSIBookingSite();
			osiBookingSite.setCountry(siteInfos[0]);
			osiBookingSite.setCompany(siteInfos[1]);
			return osiBookingSite;
		}
		return null;
	}
	
	/**
	 * build OnHoldInfo which contains on hold related information
	 * @param pnrBooking
	 * @param booking 
	 * @return return OnHoldInfo
	 */
	public static boolean isBookingOnHold(RetrievePnrBooking pnrBooking){
		
		//1)check onHold remark
		boolean onHoldStatusInRemark = pnrBooking.getOnHoldRemarkInfo() !=null && pnrBooking.getOnHoldRemarkInfo().isHasOnHoldRemark();
		if(!onHoldStatusInRemark){
			return false;
		}
		
		//2-3)check tl tl and office id
		boolean hasBOHOfficeTlXl = Optional.ofNullable(pnrBooking.getTicketList()).orElse(Collections.emptyList()).stream().anyMatch(
				rt->(OneAConstants.TICKET_INDICATOR_XL.equals(rt.getIndicator()) 
						|| OneAConstants.TICKET_INDICATOR_TL.equals(rt.getIndicator())
					 )
				  && (StringUtils.isEmpty(rt.getOfficeId()) 
						  || BOH_OFFICEID_PATTERN.matcher(rt.getOfficeId()).matches()
						  )
				  );
	
		if(!hasBOHOfficeTlXl){
			return false;
		}
		//4)check ticket last
		return !hasIssuedTicket(pnrBooking);
	 
	}
	/**
	 * check has issued ticket
	 * @param pnrBooking
	 * @return
	 */
	public static boolean hasIssuedTicket(RetrievePnrBooking pnrBooking) {
		return Optional.ofNullable(pnrBooking.getPassengerSegments()).orElse(Collections.emptyList()).stream()
				.anyMatch(passengerSegment -> !CollectionUtils.isEmpty(passengerSegment.getEtickets()));
	}
	
	/**
	 * check has issued all tickets
	 * @param booking
	 * @return
	 */
	public static boolean hasIssuedAllTickets(RetrievePnrBooking pnrBooking) {
		return Optional.ofNullable(pnrBooking.getPassengerSegments()).orElse(Collections.emptyList()).stream()
				.allMatch(passengerSegment -> !CollectionUtils.isEmpty(passengerSegment.getEtickets()));
	}
	
	/**
	 * get earliest ticketIssueDate by indicator
	 * 
	 * @param tickets
	 * @param indicators
	 * @return
	 */
	public static Date getEarliestTicketIssueDateByIndicator(List<RetrievePnrTicket> tickets, String timezoneOffset, String... indicator) {
		return tickets.stream().filter(t-> indicator == null || Arrays.asList(indicator).contains(t.getIndicator()))
				.map(t -> t.getDateTimeByTimezone(timezoneOffset)).min(new TicketDateComparator()).orElse(null);
	}
	
	/**
	 * get earliest ticketIssueDate from passengerSegments
	 * 
	 * @param passengerSegments
	 * @param timezoneOffset
	 * @return
	 */
	public static Date getEarliestTicketIssueDate(List<PassengerSegment> passengerSegments, String timezoneOffset) {
		if(CollectionUtils.isEmpty(passengerSegments)) {
			return null;
		}
		return passengerSegments.stream().filter(ps -> ps.getTicketIssueDateByTimezone(timezoneOffset) != null)
				.map(ps -> ps.getTicketIssueDateByTimezone(timezoneOffset)).min(new TicketDateComparator()).orElse(null);
	}
	
	/**
	 * get earliest ticket product departureDate from passengerSegments
	 * 
	 * @param passengerSegments
	 * @param timezoneOffset
	 * @return
	 */
	public static Date getEarliestTicketProductDepartureDate(List<PassengerSegment> passengerSegments, String timezoneOffset) {
		if(CollectionUtils.isEmpty(passengerSegments)) {
			return null;
		}
		return passengerSegments.stream().filter(ps -> ps.getTicketProductDateByTimeZone(timezoneOffset) != null)
				.map(ps -> ps.getTicketProductDateByTimeZone(timezoneOffset)).min(new TicketDateComparator()).orElse(null);
	}
	
	static class TicketDateComparator implements Comparator<Date> {
		@Override
		public int compare(Date d1, Date d2) {
	        return d1.after(d2) ? 1 : -1;
		}
	}
	
	/**
	 * Get cabinClass from DB by subClass.
	 * 
	 * @param cabinClassDAO
	 * @param subClass
	 * @return
	 */
	public static String getCabinClassBySubClass(CabinClassDAO cabinClassDAO, String subClass) {
		if(StringUtils.isEmpty(subClass)) {
			return null;
		}
		
		Map<String, String> cabinClassMap = cabinClassDAO.findByAppCode(TBConstants.APP_CODE).stream()
				.collect(Collectors.toMap(CabinClass::getSubclass, CabinClass::getBasicClass));
		return cabinClassMap.get(subClass);
	}
	
	
	/**
	 * Set PnrOperateCompany and PnrOperateSegmentNumber by Air_Flight_info
	 * @param pnrSegment
	 * @return
	 */
	public static void setOperateByCompanyAndFlightNumber(RetrievePnrSegment pnrSegment, AirFlightInfoBean airFlightInfoBean){
		if(pnrSegment == null){
			return;
		}	
		if (StringUtils.isEmpty(pnrSegment.getPnrOperateCompany()) && airFlightInfoBean != null
				&& StringUtils.isNotBlank(airFlightInfoBean.getCarrierCode())) {
			pnrSegment.setPnrOperateCompany(airFlightInfoBean.getCarrierCode());
			pnrSegment.setPnrOperateSegmentNumber(airFlightInfoBean.getFlightNumber());
		}
		if(StringUtils.isEmpty(pnrSegment.getPnrOperateCompany()) && StringUtils.isEmpty(pnrSegment.getPnrOperateSegmentNumber())){
			pnrSegment.setPnrOperateCompany(pnrSegment.getMarketCompany());
			pnrSegment.setPnrOperateSegmentNumber(pnrSegment.getMarketSegmentNumber());
		}
	}
	
	/**
	 * Check the booking has member or not
	 * 
	 * @param booking
	 * @param loginInfo
	 * @return boolean
	 */
	public static boolean hasMemberBooking(Booking booking,LoginInfo loginInfo) {
		if(!LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())){
			return false;
		}
		return booking.isHasLoginMember();
	}
	
	/**
	 * Get e-ticket mapping
	 * key: eTicketNumber, value: List<ETicket>
	 * 
	 * @param passengerSegments
	 * @param paxType
	 * @return
	 */
	public static Map<String, List<ETicket>> getEticketsByPaxType(List<PassengerSegment> passengerSegments, String paxType) {
		if(CollectionUtils.isEmpty(passengerSegments) || StringUtils.isEmpty(paxType)) {
			return Collections.emptyMap();
		}
		return passengerSegments.stream().filter(ps -> paxType.equals(ps.getEticketPaxType())).map(PassengerSegment::getEticket)
				.collect(Collectors.groupingBy(ETicket::getNumber, toList()));
	}
	
	/**
	 * Get e-ticket mapping
	 * key: eTicketNumber, value: list of cpnNumbers 
	 * 
	 * @param passengerSegments
	 * @param paxType
	 * @return
	 */
	public static Map<String, List<String>> getEticketCpnNumberMap(List<PassengerSegment> passengerSegments, String paxType) {
		if(CollectionUtils.isEmpty(passengerSegments) || StringUtils.isEmpty(paxType)) {
			return Collections.emptyMap();
		}
		Map<String, List<String>> ticketNumberMap = new HashMap<>();

		Map<String, List<ETicket>> ticketMap = getEticketsByPaxType(passengerSegments, paxType);
		for(Entry<String, List<ETicket>> entry : ticketMap.entrySet()) {
			List<String> cpnNumbers = new ArrayList<>();
			for(ETicket eticket : entry.getValue()) {
				cpnNumbers.addAll(eticket.getCpnNumbers());
			}
			ticketNumberMap.put(entry.getKey(), cpnNumbers);
		}
		return ticketNumberMap;
	}
	
	/**
	 * Get list of e-tickets by paxType[PAX/INF]
	 * 
	 * @param passengerSegments
	 * @param paxType
	 * @return
	 */
	public static List<ETicket> getAllEticketsByPaxType(List<PassengerSegment> passengerSegments, String paxType) {
		if(CollectionUtils.isEmpty(passengerSegments) || StringUtils.isEmpty(paxType)) {
			return Collections.emptyList();
		}
		return passengerSegments.stream().filter(ps -> paxType.equals(ps.getEticketPaxType()))
				.map(PassengerSegment::getEticket).collect(Collectors.toList());
	}

	static class WrapperETicket {
		private ETicket e;
		public WrapperETicket(ETicket e) {
			this.e = e;
		}
		public ETicket unwrap() {
			return this.e;
		}
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			WrapperETicket that = (WrapperETicket) o;
			return Objects.equals(e.getNumber(), that.e.getNumber());
		}
		@Override
		public int hashCode() {
			return Objects.hash(e.getNumber());
		}
	}


	/**
	 * Get RtfsStatusEnum by scenarioId use application properties
	 * @param scenarioId
	 * @param rtfsStatusConfig
	 */
	public static RtfsStatusEnum getRtfsStatusEnum(Integer scenarioId, RtfsStatusConfig rtfsStatusConfig) {
		RtfsStatusEnum rtfsStatusEnum = null;
		String scenario = scenarioId == null ? "" : scenarioId.toString();
		if (scenarioId != null) {
			if (rtfsStatusConfig.getCancelledList().contains(scenario)) {
				rtfsStatusEnum = RtfsStatusEnum.CANCELLED;
			} else if (rtfsStatusConfig.getReroutedList().contains(scenario)) {
				rtfsStatusEnum = RtfsStatusEnum.REROUTED;
			} else if (rtfsStatusConfig.getArrivedList().contains(scenario)) {
				rtfsStatusEnum = RtfsStatusEnum.ARRIVED;
			}else if (rtfsStatusConfig.getOntimeList().contains(scenario)) {
				rtfsStatusEnum = RtfsStatusEnum.ONTIME;
			}else if (rtfsStatusConfig.getDelayedList().contains(scenario)) {
				rtfsStatusEnum = RtfsStatusEnum.DELAYED;
			}
		}
		
		return rtfsStatusEnum;
	}
	
   /**
	* According to flight's OriginPort and Departure time to match
	* corresponding flight.
	*
	*/
	public static boolean isFlightStatusMatched(Segment segment, FlightStatusData flightStatus) {
		if (segment != null
				&& !StringUtils.isEmpty(segment.getOperateCompany())
				&& !StringUtils.isEmpty(segment.getOperateSegmentNumber())
				&& segment.getOperateCompany().equals(flightStatus.getOperatingFlight().getCarrierCode())
				&& segment.getOperateSegmentNumber().equals(flightStatus.getOperatingFlight().getFlightNumber())
				&& flightStatus.getSectors() != null && !flightStatus.getSectors().isEmpty()) {

			return flightStatus.getSectors().stream().anyMatch(sector -> {
				Date scheduledDate;
				try {
					scheduledDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT,
							segment.findDepartureTime().getPnrTime());
				} catch (ParseException e) {
					logger.error("Exception thrown in Parsing PNR Scheduled Time: "
							+ segment.findDepartureTime().getPnrTime(), e);
					return false;
				}
				return segment.getOriginPort().equals(sector.getOrigin()) && sector.getDepartScheduled() != null
						&& DateUtil.compareDate(scheduledDate, sector.getDepartScheduled(), Calendar.DATE) == 0;
			});
		}
		return false;
	}
	
	/**
	 * when stopover case, match all possible airline
	 *
	 * for example: Airline from RTFS: A->B->C->D->E
	 * A passenger can take an air-range: A->B->C->D->E OR A->B, A->C, B->D(part of the whole airline),
	 * OR he also can take an air-range from A->K(Not an endpoint from RTFS)
	 *
	 * @param sectorList
	 * @param originPort
	 * @param segmentDestport
	 * @return List<SectorDTO>
	 */
	public static List<SectorDTO> matchAirlineFromOriginToDestport(List<SectorDTO> sectorList,String originPort, String segmentDestport){


		ArrayList<SectorDTO> sectorDTOArrayList = new ArrayList<>();

		String nextOriginPort = originPort;

		boolean continueLoop = true;
		/**
		 * Find the sector by OD sequence, the first sector should match request originPort, and
		 * next OriginPort should match last Destination port. 
		 * End the loop if find any segment's destination same as request Destination.
		 * the loop should end also if cannot find next sector in the rtfs sector list.
		 */
		while (continueLoop) {
			continueLoop = false;
			for (SectorDTO sector : sectorList) {
				if (sector.getOrigin().equalsIgnoreCase(nextOriginPort) && !sectorDTOArrayList.contains(sector)) {
					// add the matched to list
					sectorDTOArrayList.add(sector);
					if (sector.getDestination().equalsIgnoreCase(segmentDestport)) {
						break;
					}
					nextOriginPort = sector.getDestination();
					continueLoop = true;
				}
			}
		}

		return sectorDTOArrayList;
	}
	
	/**
	 * set departure time and arrival time
	 *
	 * @param segment
	 * @param sectorDTOArrayList
	 */
	public static void setDepartureArrivalTimeForSegment(Segment segment, List<SectorDTO> sectorDTOArrayList){
		if(CollectionUtils.isEmpty(sectorDTOArrayList)){
			return;
		}else{
			if(sectorDTOArrayList.size()==1){
				SectorDTO sector = sectorDTOArrayList.get(0);
				if(!segment.getOriginPort().equalsIgnoreCase(sector.getOrigin()) ||
						!segment.getDestPort().equalsIgnoreCase(sector.getDestination())){
					return;
				}
				setRTFSDepartureTime(segment.getDepartureTime(), sector);
				setRTFSArrivalTime(segment.getArrivalTime(), sector);
			}else{
				SectorDTO firstSector = sectorDTOArrayList.get(0);
				SectorDTO lastSector = sectorDTOArrayList.get(sectorDTOArrayList.size() - 1);
				if(!segment.getOriginPort().equalsIgnoreCase(firstSector.getOrigin()) ||
						!segment.getDestPort().equalsIgnoreCase(lastSector.getDestination())){
					return;
				}
				setRTFSDepartureTime(segment.getDepartureTime(), firstSector);
				setRTFSArrivalTime(segment.getArrivalTime(), lastSector);
			}
		}
	}

	/**
	 * set departure time
	 *
	 * @param departureTime
	 * @param sector
	 */
	public static void setRTFSDepartureTime(DepartureArrivalTime departureTime, SectorDTO sector){
		departureTime.setRtfsActualTime(
				DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getDepartActual()));
		departureTime.setRtfsScheduledTime(
				DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getDepartScheduled()));
		departureTime.setRtfsEstimatedTime(
				DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getDepartEstimated()));
	}

	/**
	 * set arrival time
	 *
	 * @param arrivalTime
	 * @param sector
	 */
	public static void setRTFSArrivalTime(DepartureArrivalTime arrivalTime, SectorDTO sector){
		arrivalTime.setRtfsActualTime(
				DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getArrivalActual()));
		arrivalTime.setRtfsScheduledTime(
				DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getArrivalScheduled()));
		arrivalTime.setRtfsEstimatedTime(
				DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getArrivalEstimated()));
	}
	
	/**
	 * Get the final status sectors of flight
	 * 
	 * @param sectors
	 * @param rtfsStatusConfig
	 * @return
	 */
	public static List<SectorDTO> filterFinalStatusSectors(List<SectorDTO> originalSectors,List<SectorDTO> sectors,
			RtfsStatusConfig rtfsStatusConfig) {
		
		/**
		 * The Logic is: 
		 * 1.only one data in sectors, return directly.
		 * 2.multiple data
		 * 	a.originalsectors is rerouted and sectors has cancelled,return other status
		 * 	b.originalsectors is besides filtered order by cancelled > rerouted > delayed
		 * 
		 */
		
		if(sectors.size() == 1){
			return sectors;
		}
		
		/**
		 * sectors has rerouted and cancelled status return null.
		 */
		if(hasReroutedFlight(originalSectors,rtfsStatusConfig)){
			if(hasCancelledFlight(sectors,rtfsStatusConfig)){
				return null;
			}
		}
		
		/**
		 * sectors has cancelled status return cancelled sectors.
		 */
		if(hasCancelledFlight(originalSectors,rtfsStatusConfig)){
			return originalSectors.stream().filter(s -> isCancelledFlight(s.getScenarioID(),rtfsStatusConfig)
					).collect(Collectors.toList());
		}
		
		/**
		 * sectors has rerouted status return rerouted sectors.
		 */
		if(hasReroutedFlight(originalSectors,rtfsStatusConfig)){
			return originalSectors.stream().filter(s -> isReroutedFlight(s.getScenarioID(),rtfsStatusConfig)
					).collect(Collectors.toList());
		}

		return originalSectors.stream().filter(s -> isSpecificFlight(s.getScenarioID(),rtfsStatusConfig)
				).collect(Collectors.toList());
	}
	
	
	/**
	 * check has cancelled flight
	 * @param sectors
	 * @param rtfsStatusConfig
	 * @return
	 */
	public static boolean hasReroutedFlight(List<SectorDTO> sectors,RtfsStatusConfig rtfsStatusConfig) {
		if (!CollectionUtils.isEmpty(sectors)) {
			return sectors.stream().anyMatch(sector -> isReroutedFlight(sector.getScenarioID(),rtfsStatusConfig));
		}
		return false;
	}
	
	/**
	 * check has rerouted flight
	 * @param sectors
	 * @param rtfsStatusConfig
	 * @return
	 */
	public static boolean hasCancelledFlight(List<SectorDTO> sectors,RtfsStatusConfig rtfsStatusConfig) {
		if (!CollectionUtils.isEmpty(sectors)) {
			return sectors.stream().anyMatch(sector -> isCancelledFlight(sector.getScenarioID(),rtfsStatusConfig));
		}
		return false;
	}
	
	/**
	 * check specific(delayed,cancelled,rerouted) Flight
	 * 
	 * @param scenarioID
	 * @param rtfsStatusConfig
	 * @return
	 */
	public static boolean isCancelledFlight(Integer scenarioId,RtfsStatusConfig rtfsStatusConfig) {
		String scenario = scenarioId == null ? "" : scenarioId.toString();
		if(rtfsStatusConfig.getCancelledList().contains(scenario)){
			return true;
		}
		return false;
	}
	
	/**
	 * check specific(delayed,cancelled,rerouted) Flight
	 * 
	 * @param scenarioID
	 * @param rtfsStatusConfig
	 * @return
	 */
	public static boolean isReroutedFlight(Integer scenarioId,RtfsStatusConfig rtfsStatusConfig) {
		String scenario = scenarioId == null ? "" : scenarioId.toString();
		if(rtfsStatusConfig.getReroutedList().contains(scenario)){
			return true;
		}
		return false;
	}
	
	/**
	 * check specific(delayed,cancelled,rerouted) Flight
	 * 
	 * @param scenarioID
	 * @param rtfsStatusConfig
	 * @return
	 */
	public static boolean isSpecificFlight(Integer scenarioId,RtfsStatusConfig rtfsStatusConfig) {
		String scenario = scenarioId == null ? "" : scenarioId.toString();
		if(rtfsStatusConfig.getReroutedList().contains(scenario) ||
				rtfsStatusConfig.getCancelledList().contains(scenario) ||	
				rtfsStatusConfig.getDelayedList().contains(scenario)){
			return true;
		}
		return false;
	}
	
	/**
	 * flight has specific status
	 * @param sectors
	 * @param rtfsStatusConfig
	 * @return
	 */
	public static boolean  hasSpecificFlight(List<SectorDTO> sectors,RtfsStatusConfig rtfsStatusConfig) {
		if (!CollectionUtils.isEmpty(sectors)) {
			return sectors.stream().anyMatch(sector -> 
				isSpecificFlight(sector.getScenarioID(),rtfsStatusConfig));
		}
		return false;
	}
	
	/**
	 * Retrieve a list of multiple-lines remarks with specified beginning and ending
	 * @param rmList
	 * @param BEGINNING
	 * @param ENDING
	 * @return a list of multiple-lines remarks
	 */
	public static List<MultiLineOTRemark> retrieveMultilineRemarks(List<RetrievePnrRemark> rmList, final String BEGINNING, final String ENDING) {
		List<MultiLineOTRemark> multilineRemarks = Lists.newArrayList();
		MultiLineOTRemark otRemark;
		List<String> otNumberList = new ArrayList<>();

		String currMultilineRm = StringUtils.EMPTY;
		for (RetrievePnrRemark pnrRemark : rmList) { 			// Loop through all remarks
			String remark = pnrRemark.getFreeText();

			if (StringUtils.startsWithIgnoreCase(remark, BEGINNING)) {
				currMultilineRm = remark; 						// use the new start text, the currMultilineRm is invalid text if currMultilineRm not empty 
				otNumberList.add(pnrRemark.getOtQualifierNumber());
			} else if (!StringUtils.isEmpty(currMultilineRm)) { //handle multi-line case
				currMultilineRm += remark;
				otNumberList.add(pnrRemark.getOtQualifierNumber());
			}else{
				continue;										// not request RM or invalid end, ignore it.
			}

			if (StringUtils.endsWithIgnoreCase(currMultilineRm, ENDING)) {
				otRemark  = new MultiLineOTRemark();
				otRemark.setRemark(currMultilineRm);
				otRemark.setOtQualifierNumber(otNumberList);
				multilineRemarks.add(otRemark); 			// Ready to return and handle the next one
				currMultilineRm = StringUtils.EMPTY;
				otNumberList = new ArrayList<>();
			}
		}

		return multilineRemarks;
	}
	
	
	public static DepartureArrivalTime buildDepartArrivalTime(RetrievePnrDepartureArrivalTime pnrTime) {
		if (pnrTime != null) {
			DepartureArrivalTime result = new DepartureArrivalTime();
			result.setPnrTime(pnrTime.getPnrTime());
			result.setRtfsActualTime(pnrTime.getRtfsActualTime());
			result.setRtfsEstimatedTime(pnrTime.getRtfsEstimatedTime());
			result.setRtfsScheduledTime(pnrTime.getRtfsScheduledTime());
			result.setTimeZoneOffset(pnrTime.getTimeZoneOffset());
			return result;
		}
		return null;
	}
	
	public static String retrievePassengerTitle(String givenName, List<String> nameTitles) {
		String title = null;
		for (String nameTitle : nameTitles) {
			if (!StringUtils.isEmpty(givenName) && givenName.trim().toUpperCase().endsWith(" "+nameTitle.toUpperCase())) {
				title = nameTitle;
				break;
			}
		}
		return title;
	}
	
	/**
	 * Get upperCase title from givenName
	 * 
	 * @param givenName
	 * @param nameTitles
	 * @return
	 */
	public static String retrievePassengerUpperCaseTitle(String givenName, List<String> nameTitles) {
		String title = retrievePassengerTitle(givenName, nameTitles);
		return StringUtils.isEmpty(title) ? null : title.toUpperCase();
	}
	
	public static String removeTitleFromGivenName(String givenName, String title) {
		//if title is not empty,remove the title from given name
		if(!StringUtils.isEmpty(title)){
			return givenName.substring(0, givenName.length()-title.length()).trim();
		}
		return givenName;
	}

	public static String retrieveGenderByTitle(String title, List<String> maleNameTitles, List<String> femaleNameTitles) {
		String gender = null;
		if (title != null && maleNameTitles.contains(title.toLowerCase())) {
			gender = MMBBizruleConstants.GENDER_MALE;
		} else if (title != null && femaleNameTitles.contains(title.toLowerCase())) {
			gender = MMBBizruleConstants.GENDER_FEMALE;
		}
		return gender;
	}

	/**
	 * Check any passenger checked in, must call OLCI set the checked in flag to booking before invok this method
	 * @param booking
	 * @return
	 */
	public static boolean anyPassengerCheckedIn(Booking booking){
		return booking.getSegments().stream().anyMatch(Segment::isCheckedIn)||booking.getPassengerSegments().stream().anyMatch(PassengerSegment::isCheckedIn);
	}
	
	/**
	 * Judge the sector is flight sector or not through airCraftType
	 * 
	 * @param airCraftType
	 * @return
	 */
	public static boolean isFlightSector(String airCraftType) {
		return StringUtils.isNotEmpty(airCraftType)
				&& !OneAConstants.EQUIPMENT_TRN.equals(airCraftType) 
				&& !OneAConstants.EQUIPMENT_LCH.equals(airCraftType)
				&& !OneAConstants.EQUIPMENT_BUS.equals(airCraftType);
	}
	
	/**
	 * check whether it is a companion booking(member logged in is not traveling)
	 * 
	 * @param booking
	 * @return
	 */
	public static boolean isCompanionBooking(Booking booking, LoginInfo loginInfo) {
		if (!LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())) {
			return false;
		}

		// If all of the passengers are not login member, it is a companion booking
		return Optional.ofNullable(booking.getPassengers()).orElse(new ArrayList<Passenger>()).stream()
				.allMatch(passenger -> BooleanUtils.isNotTrue(passenger.getLoginMember()));
	}
	
	public static List<SpecialService> removeDubplicateSepcialService(List<SpecialService> specialServices) {
		return specialServices.stream().distinct().collect(toList());
	}
	
	/**
	 * Check is FQTR memberID or not
	 * 
	 * @param booking
	 * @param loginInfo
	 * @return boolean
	 */
	public static boolean isFQTRMemberID(Booking booking, String memberId) {
		if (StringUtils.isEmpty(memberId)
				|| CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return false;
		}

		return booking.getPassengerSegments().stream()
				.anyMatch(ps -> ps.getFqtrInfo() != null && memberId.equals(ps.getFqtrInfo().getMembershipNumber()));
	}
	
	/**
	 * Passenger's parental lock is locked or not.
	 * 
	 * @param pnrBooking
	 * @param passengerId
	 * @return
	 */
	public static boolean parentalLocked(RetrievePnrBooking pnrBooking, String passengerId) {
		return CollectionUtils.isNotEmpty(getParentalLockedRemark(pnrBooking, passengerId));
	}
	
	/**
	 * Get all parental lock remarks by passengerId
	 * 
	 * @param pnrBooking
	 * @param passengerId
	 * @return
	 */
	public static List<RetrievePnrRemark> getParentalLockedRemark(RetrievePnrBooking pnrBooking, String passengerId) {
		if(pnrBooking == null || CollectionUtils.isEmpty(pnrBooking.getRemarkList()) || StringUtils.isEmpty(passengerId)) {
			return Collections.emptyList();
		}
		
		List<RetrievePnrRemark> unParentalLockedRemarks = pnrBooking.getRemarkList().stream()
				.filter(rm -> rm != null && OneAConstants.UMNR_EFORM_PARENTAL_LOCK_RM_FREETEXT.equalsIgnoreCase(rm.getFreeText())).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(unParentalLockedRemarks)) {
			return Collections.emptyList();
		}
		
		if(CollectionUtils.isNotEmpty(pnrBooking.getPassengers()) && pnrBooking.getPassengers().size() == 1) {
			return unParentalLockedRemarks.stream().filter(rm -> CollectionUtils.isEmpty(rm.getPassengerIds()) || rm.getPassengerIds().contains(passengerId))
					.collect(Collectors.toList());
		}
		
		return unParentalLockedRemarks.stream().filter(rm -> CollectionUtils.isNotEmpty(rm.getPassengerIds()) && rm.getPassengerIds().contains(passengerId))
				.collect(Collectors.toList());
	}
	
	/**
     * all flight in the pax has ticket
     * @param pnrBooking
     * @return
     */
    public static boolean paxHasIssuedTicket(RetrievePnrPassenger pnrPassenger, RetrievePnrBooking pnrBooking) {
        return Optional.ofNullable(pnrBooking.getPassengerSegments()).orElse(Collections.emptyList()).stream()
                .filter(passengerSegment -> null != passengerSegment
                        && StringUtils.equals(passengerSegment.getPassengerId(), pnrPassenger.getPassengerID()))
                .allMatch(passengerSegment -> !CollectionUtils.isEmpty(passengerSegment.getEtickets()));
    }
    
    /**
     * has unissued ticket
     * @param Booking
     * @return
     */
    public static boolean isTicketUnissuedForMice(Booking booking) {
        return booking.isMiceBooking() && booking.getPassengers().stream()
                .filter(passenger -> null != passenger && BooleanUtils.isTrue(passenger.isPrimaryPassenger()))
                .anyMatch(passenger -> passenger.isTickedUnissued());
    }
    
    /**
     * grmc
     * @param Booking
     * @return
     */
    public static boolean isGRMC(Booking booking) {
        return booking.getPassengers().stream()
                .filter(passenger -> null != passenger && BooleanUtils.isTrue(passenger.isPrimaryPassenger()))
                .anyMatch(passenger -> passenger.isGrmc());
    }
    
    /**
     * 
     * 
     * @param passengerSegments
     * @param passengerId
     * @param segmentId
     * @return
     */
    public static PassengerSegment getPassegnerSegmentByIds(List<PassengerSegment> passengerSegments, String passengerId, String segmentId) {
    	if(CollectionUtils.isEmpty(passengerSegments) || StringUtils.isEmpty(passengerId)
    			|| StringUtils.isEmpty(segmentId)) {
    		return null;
    	}
    	
    	return passengerSegments.stream().filter(ps -> ps != null && Objects.equals(ps.getPassengerId(), passengerId)
    			&& Objects.equals(ps.getSegmentId(), segmentId)).findFirst().orElse(null);
    }
    
    /**
     * booking contains BCODE or not
     * SSR CRID
     * Carrier = CX/ KA
     * CRID status = all status
     * BCODE <BCODE element format = 9NNNN>
     * 
     * Sample: SSR CRID CX HK1 HK90437/S5
     * 
     * @param ssrList
     * @return
     */
    public static boolean bcodeExist(List<RetrievePnrDataElements> ssrList) {
    	if(CollectionUtils.isEmpty(ssrList)) {
    		return false;
    	}
    	
    	return ssrList.stream().filter(element -> element != null 
    			&& OneAConstants.SSR_TYPE_CRID.equals(element.getType()) && StringUtils.isNotEmpty(element.getFreeText())
    			&& (OneAConstants.COMPANY_KA.equalsIgnoreCase(element.getCompanyId()) || OneAConstants.COMPANY_CX.equalsIgnoreCase(element.getCompanyId())))
    			.map(RetrievePnrDataElements::getFreeText).anyMatch(freeText -> freeText.matches(".*9[0-9]{4}.*"));
    }

}
