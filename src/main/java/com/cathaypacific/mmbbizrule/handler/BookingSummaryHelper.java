package com.cathaypacific.mmbbizrule.handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mbcommon.model.common.BookingRefMapping;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OjConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.oj.service.OJBookingService;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.summary.BookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.BookingSummaryIntegrationBean;
import com.cathaypacific.mmbbizrule.model.booking.summary.EventBookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.EventSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummaryConvertBean;
import com.cathaypacific.mmbbizrule.model.booking.summary.HotelBookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.HotelSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.OjBookingSummaryConvertBean;
import com.cathaypacific.mmbbizrule.model.booking.summary.SectorSummaryBase;
import com.cathaypacific.mmbbizrule.model.booking.summary.SegmentSummary;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRebookInfo;
import com.cathaypacific.mmbbizrule.service.EodsBookingSummaryService;
import com.cathaypacific.mmbbizrule.service.OneABookingSummaryService;
import com.cathaypacific.mmbbizrule.service.TempLinkedBookingSummaryService;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;

@Service
public class BookingSummaryHelper{

	private static LogAgent logger = LogAgent.getLogAgent(BookingSummaryHelper.class);
	
	@Autowired
	private OneABookingSummaryService oneABookingSummaryService;
	
	@Autowired
	private EodsBookingSummaryService eodsBookingSummaryService;
	
	@Autowired
	private OJBookingService ojBookingService;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private RetrieveProfileService retrieveProfileService;
	
	@Autowired
	private TempLinkedBookingSummaryService tempLinkedBookingSummaryService;
	
	@LogPerformance(message = "Build booking list spend time(ms).")
	@NotNull
	public BookingSummaryIntegrationBean getMemberBookings(LoginInfo loginInfo, BookingBuildRequired required) throws BusinessBaseException {		
		
		BookingSummaryIntegrationBean response = new BookingSummaryIntegrationBean();

		
		ProfilePersonInfo profilePersonInfo = retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken());
		
		// 1) OneA booking, async to get 1A booking
		Future<List<FlightBookingSummaryConvertBean>> asyncOneA = null;
		if(!MMBUtil.isRuLogin(loginInfo)){
			asyncOneA = oneABookingSummaryService.asyncGetOneABookingList(loginInfo.getMemberId());
		}
				
		// 2) EODS booking, async to get EODS booking 
		Future<List<FlightBookingSummaryConvertBean>> asyncEODS = eodsBookingSummaryService.asynGetEodsBookingList(loginInfo, loginInfo.getMmbToken());
		
		// 3) OJ booking, async to get OJ booking 
		Future<List<BookingSummary>> asyncOJ = ojBookingService.asynGetOjBookingList(profilePersonInfo, loginInfo.getMmbToken());
	
		// 4) Temp linked booking
		List<FlightBookingSummaryConvertBean> tempLinkedBookings =  tempLinkedBookingSummaryService.getTempLinkedABookingList(loginInfo.getMmbToken());
		
		// Split temp linked booking by booking type, with flight and without flight 
		List<FlightBookingSummaryConvertBean> tempLinkedFlightBookings = tempLinkedBookings.stream().filter(convertBean -> BizRulesUtil.isFlightRloc(convertBean.getRloc())).collect(Collectors.toList());
		List<OjBookingSummaryConvertBean> tempLinkedNonFlightBookings = tempLinkedBookings
				.stream()
				.filter(convertBean -> !BizRulesUtil.isFlightRloc(convertBean.getRloc()))
				.map(flt -> (OjBookingSummaryConvertBean) flt)
				.collect(Collectors.toList());

		// Open the 
		List<Future<BookingSummary>> ojSecondParseByNonMemberFutures = retrieveTempLinkedOJBooking(tempLinkedNonFlightBookings);
		
		List<FlightBookingSummaryConvertBean> oneABookings = null;
		List<FlightBookingSummaryConvertBean> eodsBookings = null;
		List<BookingSummary> ojBookings = null;
		
		try {
			if (asyncOneA != null) {
				oneABookings = asyncOneA.get();
			}

		} catch (Exception e) {
			response.setRetrieveOneASuccess(false);
			logger.warn("Error to retrieve OneA booking list", e);
		}
		
		try {
			eodsBookings = asyncEODS.get();
		} catch (Exception e) {
			response.setRetrieveEODSSuccess(false);
			logger.warn("Error to retrieve EODS booking list",e);
		}
		// parser flight booking first
		List<FlightBookingSummaryConvertBean> flightBookingSummarys = distinctFlightBookingSummarys(oneABookings, eodsBookings,tempLinkedFlightBookings);
		
		List<Future<FlightBookingSummary>> flightFutures = new ArrayList<>();
		for(FlightBookingSummaryConvertBean flightBookingSummary : flightBookingSummarys) {
			flightFutures.add(oneABookingSummaryService.asyncGetFlightBookingSummary(flightBookingSummary, loginInfo, required));
		}
		
		//get details(retrieve pnr-> build booking) of flight booking
		List<FlightBookingSummary> flightDetailSummarys = new ArrayList<>();
		for(Future<FlightBookingSummary> flightFuture : flightFutures) {
			try {
				FlightBookingSummary flightDetailSummary = flightFuture.get();
				if(flightDetailSummary != null) {
					flightDetailSummarys.add(flightDetailSummary);
				}
			} catch (Exception e) {
				logger.warn("Error to retrieve OneA booking",e);
			}
		}
		
		// Get OJ booking 
		try {
			ojBookings = asyncOJ.get();
			Set<String> memebrFlightBookings = flightBookingSummarys.stream().map(FlightBookingSummaryConvertBean::getRloc).collect(Collectors.toSet());
			//remove the package booking in OJ but not found in 1A/EODS
			ojBookings = Optional.ofNullable(ojBookings).orElseGet(Collections::emptyList).stream()
					.filter(fb -> fb.getFlightSummary() == null || memebrFlightBookings.contains(fb.getFlightSummary().getOneARloc())).collect(Collectors.toList());
		} catch (Exception e) {
			response.setRetrieveOJSuccess(false);
			logger.warn("Error to retrieve OJ booking list",e);
		}
		
		//result List of BookingSummary
		List<FlightBookingSummary> packageBookingCannotFindInOjList = new ArrayList<>();
		List<BookingSummary>  bookingSummarys = mergeOJOneABooking(flightDetailSummarys, ojBookings,packageBookingCannotFindInOjList);
		
		//************** Start second parser (call oJ through non member flow to get missing package booking in member flow) ******************
		ojSecondParseByNonMemberFutures.addAll(retrieveMissedOJBooking(packageBookingCannotFindInOjList,tempLinkedNonFlightBookings));
		
		mergeOjOneABooking(bookingSummarys, ojSecondParseByNonMemberFutures);
		
		//************** End second parser (call oJ through non member flow to get missing package booking in member flow) ******************
		
		filterAndSetBookingStatus(bookingSummarys);
		
		//set order number in each sectors, set firstAvailableSector flag, set date in bookingSummary for sorting
		sortSectors(bookingSummarys);
		
		//sort bookingSummary by each first available sector date
		sortBookingSummarys(bookingSummarys);
		
		response.setBookings(bookingSummarys);
		
		if (CollectionUtils.isEmpty(bookingSummarys)) {
			logger.warn(String.format("Cannot find booking for member:%s", loginInfo.getMemberId()));
			return response;
		}
		//save valid flight rloc to session cache
		saveValidFlightRlocs(bookingSummarys, loginInfo.getMmbToken());
		

		// save rlocMapping to redis
		saveRlocMapping(response.getBookings(), loginInfo.getMmbToken(),profilePersonInfo);
		return response;
	}
	/**
	 * merge oj response to booking list 
	 * @param bookingSummarys
	 * @param ojSecondPaseByNonMemberFutures
	 */
	private void mergeOjOneABooking(List<BookingSummary>  bookingSummarys, List<Future<BookingSummary>> ojSecondPaseByNonMemberFutures){
		if(CollectionUtils.isEmpty(ojSecondPaseByNonMemberFutures)){
			return;
		}
		Map<String, BookingSummary> spnrMapping = bookingSummarys.stream()
				.filter(bookingSummary -> bookingSummary.getFlightSummary() != null
						&& !StringUtils.isEmpty(bookingSummary.getFlightSummary().getSpnr()))
				.collect(Collectors.toMap(bs -> bs.getFlightSummary().getSpnr(), bs -> bs,
						(oldValue, newValue) -> oldValue));
		
		/*
		 *  OLSSMMB-19251 Fix
		 *  Defect root cause: We match 1A booking and OJ booking by SPNR and PNR, so it never matched.
		 *  Fix: When build OJ summary booking, it uses six-digit RLOC, so we should match it by six-digit RLOC also.
		 *  Keep the SPNR mapping to make sure original logic is not impacted
		 */
		// remove this code because I have clarifyed the spnr
				Map<String, BookingSummary> rlocMapping = bookingSummarys.stream()
						.filter(bookingSummary -> bookingSummary.getFlightSummary() != null
								&& !StringUtils.isEmpty(bookingSummary.getFlightSummary().getSpnr()))
						.collect(Collectors.toMap(bs -> bs.getFlightSummary().getOneARloc(), bs -> bs,
								(oldValue, newValue) -> oldValue));
		
		for (Future<BookingSummary> future : ojSecondPaseByNonMemberFutures) {
			try {
				BookingSummary bookingSummary = future.get();
				if(bookingSummary!=null){
					//booking in 1A member profile but not in OJ member profile case 
					if(spnrMapping.containsKey(bookingSummary.getSpnr()) || rlocMapping.containsKey(bookingSummary.getSpnr())){
						BookingSummary bs =  spnrMapping.get(bookingSummary.getSpnr());
						
						// OLSSMMB-19251 Fix - Please check the comment above -- above@@
//						if (bs == null) {
//							bs =  rlocMapping.get(bookingSummary.getRloc());
//						}
						bs.setEventSummary(bookingSummary.getEventSummary());
						bs.setHotelSummary(bookingSummary.getHotelSummary());
					// linked booking case, only non flight booking will call OJ to retrieve
					}else{
						bookingSummarys.add(bookingSummary);
					}
				}
			} catch (Exception e) {
				logger.warn("Error to retrieve OJ booking info in second OJ call(retrieve missed OJ booking use rloc in the get booking by memebr id response).",e);
			}
			
		}
	}
	/**
	 * Retrieve Missed OJBooking
	 * @param packageBookingCannotFindInOjList
	 * @throws BusinessBaseException 
	 */
	private List<Future<BookingSummary>> retrieveMissedOJBooking(List<FlightBookingSummary> packageBookingCannotFindInOjList, List<OjBookingSummaryConvertBean> tempLinkedNonFlightBookings) throws BusinessBaseException {

		if (CollectionUtils.isEmpty(packageBookingCannotFindInOjList)) {
			return Collections.emptyList();
		}

		List<Future<BookingSummary>> ojBookingSummaryFutures = new ArrayList<>();

		for (FlightBookingSummary flightBookingSummary : packageBookingCannotFindInOjList) {
		
			
			Passenger primaryPassenger = flightBookingSummary.getPrimaryPassenger();

			if (primaryPassenger == null || StringUtils.isEmpty(flightBookingSummary.getSpnr())) {
				continue;
			}
			// no need call oj if in the linked list.
			if (tempLinkedNonFlightBookings.stream().anyMatch(tb -> Objects.equals(tb.getRloc(), flightBookingSummary.getSpnr()))) {
				continue;
			}
			Future<BookingSummary> ojBookingSummaryFuture = ojBookingService.asyncGetBookingSummary(primaryPassenger.getGivenName(), primaryPassenger.getFamilyName(), flightBookingSummary.getSpnr());
			ojBookingSummaryFutures.add(ojBookingSummaryFuture);
		}

		return ojBookingSummaryFutures;
	}
	
	/**
	 * Retrieve Missed OJBooking
	 * @param packageBookingCannotFindInOjList
	 * @throws BusinessBaseException 
	 */
	private List<Future<BookingSummary>> retrieveTempLinkedOJBooking(List<OjBookingSummaryConvertBean> tempLinkedNonFlightBookings) throws BusinessBaseException {
		
		List<Future<BookingSummary>> ojBookingSummaryFutures = new ArrayList<>();
		
		if (CollectionUtils.isEmpty(tempLinkedNonFlightBookings)) {
			return ojBookingSummaryFutures;
		}

		

		for (OjBookingSummaryConvertBean ojBookingSummaryConvertBean : tempLinkedNonFlightBookings) {
			Future<BookingSummary> ojBookingSummaryFuture = ojBookingService.asyncGetBookingSummary(ojBookingSummaryConvertBean.getGivenName(), ojBookingSummaryConvertBean.getFamilyName(), ojBookingSummaryConvertBean.getRloc());
			ojBookingSummaryFutures.add(ojBookingSummaryFuture);
		}

		return ojBookingSummaryFutures;
	}
	
	/**
	 * merge 1A and oj booking
	 * @param flightDetailSummarys
	 * @param ojBookings
	 * @return
	 */
	private List<BookingSummary> mergeOJOneABooking(List<FlightBookingSummary> flightDetailSummarys,List<BookingSummary> ojBookings,List<FlightBookingSummary> packageBookingCannotFindInOjList){
		
		// hotel booking
		if(CollectionUtils.isEmpty(flightDetailSummarys)){
			return ojBookings == null? Collections.emptyList(): ojBookings;
		}
		
		List<BookingSummary>  result = new  ArrayList<>();

		for (FlightBookingSummary flightDetailSummary : flightDetailSummarys) {
			
			BookingSummary bookingSummary = new BookingSummary();
			bookingSummary.setFlightSummary(flightDetailSummary);
			//bookingSummary.setRloc(flightDetailSummary.getRloc());
			
			if(flightDetailSummary.isFlightOnly()){
				bookingSummary.setBookingType(MMBBizruleConstants.BOOKING_TYPE_FLIGHT);
			}else{
				bookingSummary.setBookingType(MMBBizruleConstants.BOOKING_TYPE_PACKAGE);
				
				if(!CollectionUtils.isEmpty(ojBookings)){
					BookingSummary	ojBooking =  ojBookings.stream().filter(ojb->ojb.getFlightSummary()!=null && Objects.equals(ojb.getFlightSummary().getOneARloc(), flightDetailSummary.getOneARloc())).findFirst().orElse(null);
					if (ojBooking != null) {
						bookingSummary.setBookingType(ojBooking.getBookingType());
						bookingSummary.setHotelSummary(ojBooking.getHotelSummary());
						bookingSummary.setEventSummary(ojBooking.getEventSummary());
					} else {
						packageBookingCannotFindInOjList.add(flightDetailSummary);
					}
				} else {
					packageBookingCannotFindInOjList.add(flightDetailSummary);
				}
			}
			result.add(bookingSummary);
		}
		
		//add non flight booking
		if(!CollectionUtils.isEmpty(ojBookings)){
			ojBookings.stream().filter(ojb->ojb.getFlightSummary() == null).forEach(result::add);
		}
		
		return result;
		
	}
	/**
	 * save rlocMapping to redis
	 * 
	 * @param bookings
	 * @param mmbToken
	 */
	private void saveRlocMapping(List<BookingSummary> bookings, String mmbToken,
			ProfilePersonInfo profilePersonInfo) {
		if (CollectionUtils.isEmpty(bookings)) {
			return;
		}

		List<BookingRefMapping> results = new ArrayList<>();

		for (BookingSummary booking : bookings) {

			BookingRefMapping bookingRefMapping = new BookingRefMapping();
			com.cathaypacific.mbcommon.model.common.Passenger passenger = new com.cathaypacific.mbcommon.model.common.Passenger();

			// save package booking to the rloc mapping only
			if (booking.getFlightSummary() != null && BooleanUtils.isTrue(booking.getFlightSummary().isPackageBooking())) {
				// 1) get from flight first
				Passenger flightPassenger = Optional.ofNullable(booking.getFlightSummary())
						.map(FlightBookingSummary::getPassengers).orElse(Collections.emptyList()).stream()
						.filter(pax -> BooleanUtils.isTrue(pax.isPrimaryPassenger())).findFirst().orElse(null);
				if (flightPassenger != null) {
					bookingRefMapping.setPrimaryPax(passenger);
					passenger.setFamilyName(flightPassenger.getFamilyName());
					passenger.setGivenName(flightPassenger.getGivenName());
					passenger.setPassengerId(flightPassenger.getParentId());
					bookingRefMapping.setOneARloc(booking.getOneARloc());
					if (booking.getFlightSummary().isPackageBooking()) {
						bookingRefMapping.setOjRloc(booking.getSpnr());
					}
				} else {
					passenger.setFamilyName(profilePersonInfo.getFamilyName());
					passenger.setGivenName(profilePersonInfo.getGivenName());
					bookingRefMapping.setOjRloc(booking.getSpnr());
				}
			}
			results.add(bookingRefMapping);
		}

		// add this rlocMapping into redis
		if (!CollectionUtils.isEmpty(results)) {
			mbTokenCacheRepository.add(mmbToken, TokenCacheKeyEnum.RLOC_MAPPING, null, results);
		}

	}
 
	/**
	 * @param bookingSummarys
	 */
	private void filterAndSetBookingStatus(List<BookingSummary> bookingSummarys) {
		if(CollectionUtils.isEmpty(bookingSummarys)) {
			return;
		}
		Date now = DateUtil.getGMTTime();
		for(int i = 0; i < bookingSummarys.size(); i++) {
			BookingSummary bookingSummary = bookingSummarys.get(i);
			
			int cancelledCount = 0;
			int completedCount = 0;
			int count = 0;
			
			List<SectorSummaryBase> summarys = new ArrayList<>();
			
			FlightBookingSummary flightSummary = bookingSummary.getFlightSummary();
			HotelBookingSummary hotelSummary = bookingSummary.getHotelSummary();
			EventBookingSummary eventSummary = bookingSummary.getEventSummary();
			
			if(flightSummary != null && !CollectionUtils.isEmpty(flightSummary.getDetails())) {
				count = flightSummary.getDetails().size();
				for(SegmentSummary segment : flightSummary.getDetails()) {
					summarys.add(segment);
					if(segment.getSegmentStatus() != null && FlightStatusEnum.CANCELLED.getCode().equals(segment.getSegmentStatus().getStatus().getCode())) {
						cancelledCount++;
					} else if(segment.isFlown()) {
						completedCount++;
					}
				}
			}
			
			if(hotelSummary != null && !CollectionUtils.isEmpty(hotelSummary.getDetails())) {
				count += hotelSummary.getDetails().size();
				for(HotelSummary hotel : hotelSummary.getDetails()) {
					summarys.add(hotel);
					if(hotel.getHotelStatus() == null) {
						continue;
					}
					if(OjConstants.CANCELLED.equals(hotel.getHotelStatus())) {
						cancelledCount++;
					} else if(OjConstants.COMPLETED.equals(hotel.getHotelStatus())) {
						completedCount++;
					}
				}
			}
			
			if(eventSummary != null && !CollectionUtils.isEmpty(eventSummary.getDetails())) {
				count += eventSummary.getDetails().size();
				for(EventSummary event : eventSummary.getDetails()) {
					summarys.add(event);
					if(event.getBookingStatus() == null) {
						continue;
					}
					if(OjConstants.CANCELLED.equals(event.getBookingStatus())) {
						cancelledCount++;
					} else if(OjConstants.COMPLETED.equals(event.getBookingStatus())) {
						completedCount++;
					}
				}
			}
			
			if(cancelledCount == count) {
				bookingSummary.setBookingStatus(OjConstants.CANCELLED);
			} else if(completedCount == count) {
				bookingSummary.setBookingStatus(OjConstants.COMPLETED);
				SectorSummaryBase lastCompletedSector = getFirstAvlSector(summarys);
				String completedSummaryType = lastCompletedSector.getType();
				
				Date completedDate = null;
				if(MMBBizruleConstants.BOOKING_TYPE_FLIGHT.equals(completedSummaryType)) {
					completedDate = ((SegmentSummary)lastCompletedSector).getGMTArrivalDate();
				} else if(MMBBizruleConstants.BOOKING_TYPE_HOTEL.equals(completedSummaryType)) {
					completedDate = ((HotelSummary)lastCompletedSector).getCheckOutGMTDate();
				} else if(MMBBizruleConstants.BOOKING_TYPE_EVENT.equals(completedSummaryType)) {
					completedDate = lastCompletedSector.getOrderDate();
				}
				
				//Judge Booking is completed more than three days
				if(iscompletedMoreThanThreeDays(completedDate, now)) {
					bookingSummarys.remove(i);
					i--;
				}
				
			} else {
				bookingSummary.setBookingStatus(OjConstants.BOOKED);
			}

		}
	}
	
	/**
	 * Judge Booking is completed more than three days
	 * @param completedDate
	 * @param now
	 * @return
	 */
	private boolean iscompletedMoreThanThreeDays(Date completedDate, Date now) {
		if(completedDate == null) {
			return false;
		}
		Calendar cal = DateUtil.getDateToCal(completedDate, Calendar.DAY_OF_YEAR, 3);
		return cal.before(now);
	}

	/**
	 * sort bookingSummary by first available sector date
	 * @param bookings
	 */
	private void sortBookingSummarys(List<BookingSummary> bookings) {
		//sort booking
		bookings.sort((b1, b2) -> {
			Date d1 = b1.getFirstAvlSectorDate();
			Date d2 = b2.getFirstAvlSectorDate();
			// the result of compare by RLOC
			int rlocCompareResult;
			if (StringUtils.isEmpty(b1.getDisplayRloc())) {
				rlocCompareResult = 1;
			} else if (StringUtils.isEmpty(b2.getDisplayRloc())) {
				rlocCompareResult = -1;
			} else {
				rlocCompareResult = b1.getDisplayRloc().compareTo(b2.getDisplayRloc());
			}
			if(d1 == null && d2 == null) {
				// if sector dates are both null, sort by RLOC
				return rlocCompareResult;
			} else if(d1 == null) {
				return -1;
			} else if (d2 == null) {
				return 1;
			} else {
				if (d1.compareTo(d2) != 0) {
					return d1.compareTo(d2);
				} else {
					// if sector date is the same, sort by RLOC
					return rlocCompareResult;
				}
			}
		});
	}

	/**
	 * sort summary by order
	 * @param summarys
	 */
	private void sortSummarys(List<SectorSummaryBase> summarys) {
		summarys.sort((s1, s2) -> s1.getOrder() - s2.getOrder());
	}

	/**
	 * set order number in each sectors, set firstAvailableSector flag, set date in bookingSummary for sorting
	 * @param bookingSummarys
	 */
	private void sortSectors(List<BookingSummary> bookingSummarys) {
		for(BookingSummary bookingSummary : bookingSummarys) {
			if(bookingSummary == null) {
				continue;
			}
			List<SectorSummaryBase> summarys = new ArrayList<>();
			if(MMBBizruleConstants.BOOKING_TYPE_HOTEL.equals(bookingSummary.getBookingType())) {
				summarys.addAll(bookingSummary.getHotelSummary().getDetails());
			} else if(MMBBizruleConstants.BOOKING_TYPE_PACKAGE.equals(bookingSummary.getBookingType())) {
				if(bookingSummary.getHotelSummary() != null)
					summarys.addAll(bookingSummary.getHotelSummary().getDetails());
				if(bookingSummary.getFlightSummary() != null)
					summarys.addAll(bookingSummary.getFlightSummary().getDetails());
				if(bookingSummary.getEventSummary() != null)
					summarys.addAll(bookingSummary.getEventSummary().getDetails());
			} else if(MMBBizruleConstants.BOOKING_TYPE_FLIGHT.equals(bookingSummary.getBookingType())
				&& bookingSummary.getFlightSummary() != null 
				&& !CollectionUtils.isEmpty(bookingSummary.getFlightSummary().getDetails())) {
				summarys.addAll(bookingSummary.getFlightSummary().getDetails());
			}
			setSectorOrder(bookingSummary, sortSectorOrders(summarys));
			setFirstAvalibleSector(bookingSummary);
		}
	}
	
	/**
	 * 
	 * @param BookingSummary booking
	 */
	private void setFirstAvalibleSector(BookingSummary booking) {
		String bookingType = booking.getBookingType();
		
		List<SectorSummaryBase> summarys = new ArrayList<>();
		if(MMBBizruleConstants.BOOKING_TYPE_HOTEL.equals(bookingType)) {
			if(booking.getHotelSummary() != null && !CollectionUtils.isEmpty(booking.getHotelSummary().getDetails()))
				summarys.addAll(booking.getHotelSummary().getDetails());
		} else if(MMBBizruleConstants.BOOKING_TYPE_PACKAGE.equals(bookingType)) {
			if(booking.getHotelSummary() != null && !CollectionUtils.isEmpty(booking.getHotelSummary().getDetails()))
				summarys.addAll(booking.getHotelSummary().getDetails());
			if(booking.getFlightSummary() != null && !CollectionUtils.isEmpty(booking.getFlightSummary().getDetails()))
				summarys.addAll(booking.getFlightSummary().getDetails());
			if(booking.getEventSummary() != null && !CollectionUtils.isEmpty(booking.getEventSummary().getDetails()))
				summarys.addAll(booking.getEventSummary().getDetails());
		} else if(MMBBizruleConstants.BOOKING_TYPE_FLIGHT.equals(bookingType) 
				&& booking.getFlightSummary() != null 
				&& !CollectionUtils.isEmpty(booking.getFlightSummary().getDetails())) {
				summarys.addAll(booking.getFlightSummary().getDetails());
		}
		//sort summary by order
		sortSummarys(summarys);
		
		//find first available sector in bookingSummary
		SectorSummaryBase firstAvailbleSummary = getFirstAvlSector(summarys);
		
		if(firstAvailbleSummary == null) {
			return;	
		}
		
		//set booking Date for sorting with other bookings
		booking.setFirstAvlSectorDate(firstAvailbleSummary.getOrderDate());
		
		//set fist available sector flag
		setFistAvlSectorFlag(booking, firstAvailbleSummary);
		
	}

	/**
	 * find first available sector in bookingSummary
	 * @param summarys
	 * @return
	 */
	private SectorSummaryBase getFirstAvlSector(List<SectorSummaryBase> summarys) {
		SectorSummaryBase firstAvailbleSummary = null;
		for(SectorSummaryBase s : summarys) {
			if(MMBBizruleConstants.BOOKING_TYPE_FLIGHT.equals(s.getType())) {
				SegmentSummary ss = (SegmentSummary) s;
				if (ss.getOrderDate() != null && !(FlightStatusEnum.CANCELLED.equals(ss.getSegmentStatus().getStatus())
						&& !checkProtectionSector(ss.getRebookInfo())) && !ss.getSegmentStatus().isDisable() && !ss.isFlown()) {
					firstAvailbleSummary = ss;
					break;
				}
			} else if(MMBBizruleConstants.BOOKING_TYPE_HOTEL.equals(s.getType())) {
				HotelSummary hs = (HotelSummary) s;
				if(hs.getOrderDate() != null && !OjConstants.CANCELLED.equals(hs.getHotelStatus()) 
						&& !OjConstants.COMPLETED.equals(hs.getHotelStatus())) {
					firstAvailbleSummary = hs;
					break;
				}
			} else if(MMBBizruleConstants.BOOKING_TYPE_EVENT.equals(s.getType())) {
				EventSummary es = (EventSummary) s;
				if(es.getOrderDate() != null && !OjConstants.CANCELLED.equals(es.getBookingStatus()) 
						&& !OjConstants.COMPLETED.equals(es.getBookingStatus())) {
					firstAvailbleSummary = es;
					break;
				}
			}
		}
		
		//if can't find available sector before, use the last sector which orderDate is not empty
		if(firstAvailbleSummary == null) {
			for (int i = summarys.size()-1; i >= 0; i--) {
				SectorSummaryBase s = summarys.get(i);
				if (s.getOrderDate() != null) {
					firstAvailbleSummary = s;
					break;
				}
			}
		}
		return firstAvailbleSummary;
	}

	/**
	 * set fist available sector flag
	 * @param booking
	 * @param firstAvailbleSummary
	 */
	private void setFistAvlSectorFlag(BookingSummary booking, SectorSummaryBase firstAvailbleSummary) {
		String firstAvlSectorId = firstAvailbleSummary.getId();
		if(firstAvlSectorId.startsWith(MMBBizruleConstants.BOOKING_TYPE_FLIGHT)) {
			for(SegmentSummary summary : booking.getFlightSummary().getDetails()) {
				if(summary.getId().equals(firstAvlSectorId)) {
					summary.setFirstAvlSector(true);
					break;
				}
			}
		} else if(firstAvlSectorId.startsWith(MMBBizruleConstants.BOOKING_TYPE_HOTEL)) {
			for(HotelSummary summary : booking.getHotelSummary().getDetails()) {
				if(summary.getId().equals(firstAvlSectorId)) {
					summary.setFirstAvlSector(true);
					break;
				}
			}
		}  else if(firstAvlSectorId.startsWith(MMBBizruleConstants.BOOKING_TYPE_EVENT)) {
			for(EventSummary summary : booking.getEventSummary().getDetails()) {
				if(summary.getId().equals(firstAvlSectorId)) {
					summary.setFirstAvlSector(true);
					break;
				}
			}
		}
	}

	/**
	 * set order number for each sectors in BookingSummary
	 * @param bookingSummary
	 * @param orders
	 */
	private void setSectorOrder(BookingSummary bookingSummary, List<SectorSummaryBase> orders) {
		Map<String, Integer> map = new HashMap<>();
		for(int i = 0; i < orders.size(); i++) {
			SectorSummaryBase order = orders.get(i);
			map.put(order.getId(), i);
		}
		
		if(MMBBizruleConstants.BOOKING_TYPE_HOTEL.equals(bookingSummary.getBookingType())) {
			List<HotelSummary> details = bookingSummary.getHotelSummary().getDetails();
			for(HotelSummary detail : details) {
				detail.setOrder(map.get(detail.getId()));
			}
		} else if(MMBBizruleConstants.BOOKING_TYPE_PACKAGE.equals(bookingSummary.getBookingType())) {
			if(bookingSummary.getFlightSummary() != null && !CollectionUtils.isEmpty(bookingSummary.getFlightSummary().getDetails())) {
				for(SegmentSummary detail : bookingSummary.getFlightSummary().getDetails()) {
					detail.setOrder(map.get(detail.getId()));
				}
			}
			if(bookingSummary.getHotelSummary() != null && !CollectionUtils.isEmpty(bookingSummary.getHotelSummary().getDetails())) {
				for(HotelSummary detail : bookingSummary.getHotelSummary().getDetails()) {
					detail.setOrder(map.get(detail.getId()));
				}				
			}
			if(bookingSummary.getEventSummary() != null && !CollectionUtils.isEmpty(bookingSummary.getEventSummary().getDetails())) {
				for(EventSummary detail : bookingSummary.getEventSummary().getDetails()) {
					detail.setOrder(map.get(detail.getId()));
				}
			}
		} else if(MMBBizruleConstants.BOOKING_TYPE_FLIGHT.equals(bookingSummary.getBookingType())
				&& bookingSummary.getFlightSummary() != null 
				&& !CollectionUtils.isEmpty(bookingSummary.getFlightSummary().getDetails())) {
			// Add sorting for flight booking, in order to handling the sort issue by protection flight
			for(SegmentSummary detail : bookingSummary.getFlightSummary().getDetails()) {
				detail.setOrder(map.get(detail.getId()));
			}
		}
	}

	/**
	 * sorting sector in mixed one list of summary
	 * @param summarys
	 * @return
	 */
	private List<SectorSummaryBase> sortSectorOrders(List<SectorSummaryBase> summarys) {
		summarys.sort((o1, o2) -> {
			if(o1.getOrderDate() == null && o2.getOrderDate() == null) {
				return 0;
			} else if(o1.getOrderDate() == null) {
				return -1;
			} else if (o2.getOrderDate() == null) {
				return 1;
			} else {
				return o1.getOrderDate().compareTo(o2.getOrderDate());
			}
		});
		return summarys;
	}

	/**
	 * distinct flightBookingSummary from list by RLOC
	 * @param oneABookings
	 * @param eodsBookings
	 * @return
	 */
	private List<FlightBookingSummaryConvertBean> distinctFlightBookingSummarys(
			List<FlightBookingSummaryConvertBean> oneABookings, List<FlightBookingSummaryConvertBean> eodsBookings,List<FlightBookingSummaryConvertBean> tempLinkedBookings ) {

		if (CollectionUtils.isEmpty(oneABookings) && CollectionUtils.isEmpty(eodsBookings)&& CollectionUtils.isEmpty(tempLinkedBookings)) {
			return Collections.emptyList();
		}
		
		final Map<String, FlightBookingSummaryConvertBean> rlocMap = new HashMap<>();
		
		//One A booking
		if (!CollectionUtils.isEmpty(oneABookings)) {
			oneABookings.stream().forEach(oneABooking -> rlocMap.put(oneABooking.getRloc(), oneABooking));
		}
		// Eods booking
		if (!CollectionUtils.isEmpty(eodsBookings)) {
			eodsBookings.stream()
					.forEach(eodsBooking -> 
						rlocMap.merge(eodsBooking.getRloc(), eodsBooking, (oneA, eods) -> {
						oneA.setInEods(eods.isInEods());
						return oneA;
					}));
		}
		// temp booking
		if (!CollectionUtils.isEmpty(tempLinkedBookings)) {
			tempLinkedBookings.stream()
					.forEach(tempLinkedBooking -> 
						rlocMap.putIfAbsent(tempLinkedBooking.getRloc(), tempLinkedBooking) );
		}
		
		return new ArrayList<>(rlocMap.values());
	}
	
	/**
	 * Wrapper class for flight bookings merging
	 */
	class WrapperFlightBookingSummary {
		private FlightBookingSummaryConvertBean e;

		public WrapperFlightBookingSummary(FlightBookingSummaryConvertBean e) {
			this.e = e;
		}

		public FlightBookingSummaryConvertBean unwrap() {
			return this.e;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			WrapperFlightBookingSummary that = (WrapperFlightBookingSummary) o;
			if(Objects.equals(e.getRloc(), that.e.getRloc())) {
				if(BooleanUtils.isTrue(e.isInEods()) || BooleanUtils.isTrue(that.e.isInEods())) {
					e.setInEods(true);
					that.e.setInEods(true);
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(e.getRloc());
		}
	}
	
	/**
	 * store the valid flight rlocs to token cache
	 * @param bookings
	 * @param mmbToken
	 */
	private void saveValidFlightRlocs(List<BookingSummary> bookings,String mmbToken) {
		List<String> validFlightRlocs = new ArrayList<>();
		for(BookingSummary bookingSummary: bookings){
			if(!StringUtils.isEmpty(bookingSummary.getOneARloc())){
				validFlightRlocs.add(bookingSummary.getOneARloc());
			}
			if(!StringUtils.isEmpty(bookingSummary.getSpnr())){
				validFlightRlocs.add(bookingSummary.getSpnr());
			}
			
			if(!StringUtils.isEmpty(bookingSummary.getSpnr())){
				validFlightRlocs.add(bookingSummary.getSpnr());
			}
		}
		 
		mbTokenCacheRepository.add(mmbToken, TokenCacheKeyEnum.VALIDFLIGHTRLOCS, null, validFlightRlocs);
	}
	
	
	/**
	 * check sector is protection 
	 * @param rebookInfo
	 * @return
	 */
	private boolean checkProtectionSector(RetrievePnrRebookInfo rebookInfo) {
		return rebookInfo != null && BooleanUtils.isTrue(rebookInfo.isRebooked()) && !CollectionUtils.isEmpty(rebookInfo.getNewBookedSegmentIds());
	}
}
