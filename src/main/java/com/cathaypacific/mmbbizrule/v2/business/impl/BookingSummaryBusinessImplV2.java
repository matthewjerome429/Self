package com.cathaypacific.mmbbizrule.v2.business.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.handler.BookingSummaryHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneySegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Journey;
import com.cathaypacific.mmbbizrule.model.booking.detail.RebookMapping;
import com.cathaypacific.mmbbizrule.model.booking.summary.BookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.BookingSummaryIntegrationBean;
import com.cathaypacific.mmbbizrule.model.booking.summary.EventBookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.EventSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.HotelBookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.HotelSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.PassengerSegmentSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.SegmentSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.TicketIssueInfoSummary;
import com.cathaypacific.mmbbizrule.v2.business.BookingSummaryBusinessV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.CprJourneyPassengerDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.CprJourneyPassengerSegmentDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.CprJourneySegmentDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.DepartureArrivalTimeDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.JourneyDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.BookingSummaryDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.BookingSummaryResponseDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.EventBookingSummaryDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.EventSummaryDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.FlightBookingSummaryDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.HotelBookingSummaryDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.HotelSummaryDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.PassengerSegmentSummaryDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.RebookMappingDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.SegmentSummaryDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.TicketIssueInfoSummaryDTOV2;

@Service
public class BookingSummaryBusinessImplV2 implements BookingSummaryBusinessV2{

	private static LogAgent logger = LogAgent.getLogAgent(BookingSummaryBusinessImplV2.class);
	
	@Autowired
	private BookingSummaryHelper bookingSummaryHelper;
	
	@Override
	@LogPerformance(message = "Build booking(v2) list spend time(ms).")
	public BookingSummaryResponseDTOV2 getMemberBookings(LoginInfo loginInfo, BookingBuildRequired required)
			throws BusinessBaseException {

		BookingSummaryResponseDTOV2 responseDTO = new BookingSummaryResponseDTOV2();

		BookingSummaryIntegrationBean bookingSummaryIntegrationBean = bookingSummaryHelper.getMemberBookings(loginInfo,
				required);

		responseDTO.setRetrieveEODSSuccess(bookingSummaryIntegrationBean.getRetrieveEODSSuccess());
		responseDTO.setRetrieveOJSuccess(bookingSummaryIntegrationBean.getRetrieveOJSuccess());
		responseDTO.setRetrieveOneASuccess(bookingSummaryIntegrationBean.getRetrieveOneASuccess());
		responseDTO.setBookings(convertToDto(bookingSummaryIntegrationBean.getBookings()));
		responseDTO.setBookingCount(bookingSummaryIntegrationBean.getBookings().size());

		return responseDTO;
	}

	/**
	 * convert List<BookingSummary> to List<BookingSummaryDTOV2>
	 * @param bookingSummarys
	 * @param pageSize
	 * @param lastRloc
	 * @return List<BookingSummaryDTOV2>
	 */
	private List<BookingSummaryDTOV2> convertToDto(List<BookingSummary> bookingSummarys) {
		List<BookingSummaryDTOV2> bookingSummaryDTOList = new ArrayList<>();
		for(BookingSummary bookingSummary : bookingSummarys) {
			 
			BookingSummaryDTOV2 bookingSummaryDTOV2 = new BookingSummaryDTOV2();
			bookingSummaryDTOV2.setSpnr(bookingSummary.getSpnr());
			bookingSummaryDTOV2.setDisplayRloc(bookingSummary.getDisplayRloc());
			String bookingType = bookingSummary.getBookingType();
			bookingSummaryDTOV2.setBookingType(bookingType);
			bookingSummaryDTOV2.setBooingStatus(bookingSummary.getBookingStatus());
		
			bookingSummaryDTOV2.setTempLinkedBooking(bookingSummary.getFlightSummary() == null ? false
					: bookingSummary.getFlightSummary().isTempLinkedBooking());

			if(MMBBizruleConstants.BOOKING_TYPE_FLIGHT.equals(bookingType)) {
				bookingSummaryDTOV2.setFlightSummary(convertToFlightBookingSummaryDTOV2(bookingSummary.getFlightSummary()));
			} else if(MMBBizruleConstants.BOOKING_TYPE_HOTEL.equals(bookingType)) {
				bookingSummaryDTOV2.setHotelSummary(convertToHotelBookingSummaryDTOV2(bookingSummary.getHotelSummary()));
			} else if(MMBBizruleConstants.BOOKING_TYPE_PACKAGE.equals(bookingType)) {
				bookingSummaryDTOV2.setFlightSummary(convertToFlightBookingSummaryDTOV2(bookingSummary.getFlightSummary()));
				bookingSummaryDTOV2.setHotelSummary(convertToHotelBookingSummaryDTOV2(bookingSummary.getHotelSummary()));
				bookingSummaryDTOV2.setEventSummary(convertToEventBookingSummaryDTOV2(bookingSummary.getEventSummary()));
			}
			bookingSummaryDTOList.add(bookingSummaryDTOV2);
		}
		// reset bookingType by flag(packageBooking in flightBooking)
		resetBookingType(bookingSummaryDTOList);
		return bookingSummaryDTOList;
	}

	/**
	 * reset bookingType by flag(packageBooking in flightBooking)
	 * @param BookingSummaryDTOV2s
	 */
	private void resetBookingType(List<BookingSummaryDTOV2> bookingSummaryDTOV2List) {
		for(BookingSummaryDTOV2 BookingSummaryDTOV2 : bookingSummaryDTOV2List) {
			if(!MMBBizruleConstants.BOOKING_TYPE_FLIGHT.equals(BookingSummaryDTOV2.getBookingType()) 
					|| BookingSummaryDTOV2.getFlightSummary() == null) {
				continue;
			}
			if(BooleanUtils.isTrue(BookingSummaryDTOV2.getFlightSummary().isPackageBooking())) {
				BookingSummaryDTOV2.setBookingType(MMBBizruleConstants.BOOKING_TYPE_PACKAGE);
			}
		}
	}

	/**
	 * convert eventBookingSummary to EventBookingSummaryDTOV2
	 * @param eventBookingSummary
	 * @return EventBookingSummaryDTOV2
	 */
	private EventBookingSummaryDTOV2 convertToEventBookingSummaryDTOV2(EventBookingSummary eventBookingSummary) {
		if(eventBookingSummary == null) {
			return null;
		}
		EventBookingSummaryDTOV2 eventBookingSummaryDTOV2 = new EventBookingSummaryDTOV2();
		
		List<EventSummaryDTOV2> eventSummaryDTOs = new ArrayList<>();
		List<EventSummary> eventSummarys = eventBookingSummary.getDetails();
		if(!CollectionUtils.isEmpty(eventSummarys)) {
			for(EventSummary eventSummary : eventSummarys) {
				EventSummaryDTOV2 eventSummaryDTO = new EventSummaryDTOV2();
				
				eventSummaryDTO.setOrder(eventSummary.getOrder());
				eventSummaryDTO.setFirstAvlSector(eventSummary.isFirstAvlSector());
				
				eventSummaryDTO.setBookingStatus(eventSummary.getBookingStatus());
				eventSummaryDTO.setDate(eventSummary.getDate());
				eventSummaryDTO.setTime(eventSummary.getTime());
				eventSummaryDTO.setName(eventSummary.getName());
				
				eventSummaryDTOs.add(eventSummaryDTO);
			}
		}
		
		eventBookingSummaryDTOV2.setDetails(eventSummaryDTOs);
		
		return eventBookingSummaryDTOV2;
	}

	/**
	 * convert HotelBookingSummary to HotelBookingSummaryDTOV2
	 * @param hotelBookingSummary
	 * @return HotelBookingSummaryDTOV2
	 */
	private HotelBookingSummaryDTOV2 convertToHotelBookingSummaryDTOV2(HotelBookingSummary hotelBookingSummary) {
		if(hotelBookingSummary == null) {
			return null;
		}
		HotelBookingSummaryDTOV2 hotelBookingSummaryDTOV2 = new HotelBookingSummaryDTOV2();
		hotelBookingSummaryDTOV2.setStatus(hotelBookingSummary.getStatus());
		
		List<HotelSummaryDTOV2> hotelSummaryDTOs = new ArrayList<>();
		List<HotelSummary> hotelSummarys = hotelBookingSummary.getDetails();
		if(!CollectionUtils.isEmpty(hotelSummarys)) {
			for(HotelSummary hotelSummary : hotelSummarys) {
				HotelSummaryDTOV2 hotelSummaryDTO = new HotelSummaryDTOV2();
				
				hotelSummaryDTO.setOrder(hotelSummary.getOrder());
				hotelSummaryDTO.setFirstAvlSector(hotelSummary.isFirstAvlSector());
				
				hotelSummaryDTO.setHotelName(hotelSummary.getHotelName());
				hotelSummaryDTO.setCityName(hotelSummary.getCityName());
				hotelSummaryDTO.setTimeZone(hotelSummary.getTimeZoneOffset());
				hotelSummaryDTO.setCheckInDate(hotelSummary.getCheckInDate());
				hotelSummaryDTO.setCheckOutDate(hotelSummary.getCheckOutDate());
				hotelSummaryDTO.setBookingDate(hotelSummary.getBookingDate());
				hotelSummaryDTO.setNightOfStay(hotelSummary.getNightOfStay());
				hotelSummaryDTO.setHotelStatus(hotelSummary.getHotelStatus());
				
				hotelSummaryDTOs.add(hotelSummaryDTO);
			}
		}
		
		hotelBookingSummaryDTOV2.setDetails(hotelSummaryDTOs);
		
		return hotelBookingSummaryDTOV2;
	}

	/**
	 * convert FlightBookingSummary to FlightBookingSummaryDTOV2
	 * @param flightBookingSummary
	 * @return FlightBookingSummaryDTOV2
	 */
	private FlightBookingSummaryDTOV2 convertToFlightBookingSummaryDTOV2(FlightBookingSummary flightBookingSummary) {
		if(flightBookingSummary == null) {
			return null;
		}
		FlightBookingSummaryDTOV2 flightBookingSummaryDTOV2 = new FlightBookingSummaryDTOV2();
		flightBookingSummaryDTOV2.setOneARloc(flightBookingSummary.getOneARloc());
		flightBookingSummaryDTOV2.setDisplayRloc(flightBookingSummary.getDisplayRloc());
		flightBookingSummaryDTOV2.setGdsRloc(flightBookingSummary.getGdsRloc());
		flightBookingSummaryDTOV2.setSpnr(flightBookingSummary.getSpnr());
		flightBookingSummaryDTOV2.setCreateDate(flightBookingSummary.getCreateDate());
		flightBookingSummaryDTOV2.setOfficeId(flightBookingSummary.getOfficeId());
		flightBookingSummaryDTOV2.setPassengers(flightBookingSummary.getPassengers());
		
		flightBookingSummaryDTOV2.setIbeBooking(flightBookingSummary.isIbeBooking());
		flightBookingSummaryDTOV2.setTrpBooking(flightBookingSummary.isTrpBooking());
		flightBookingSummaryDTOV2.setAppBooking(flightBookingSummary.isAppBooking());
		flightBookingSummaryDTOV2.setGdsBooking(flightBookingSummary.isGdsBooking());
		flightBookingSummaryDTOV2.setGroupBooking(flightBookingSummary.isGroupBooking());
		flightBookingSummaryDTOV2.setRedBooking(flightBookingSummary.isRedBooking());
		flightBookingSummaryDTOV2.setGccBooking(flightBookingSummary.isGccBooking());
		flightBookingSummaryDTOV2.setStaffBooking(flightBookingSummary.isStaffBooking());
		flightBookingSummaryDTOV2.setRedUpgrade(flightBookingSummary.isRedUpgrade());
		flightBookingSummaryDTOV2.setMiceBooking(flightBookingSummary.isMiceBooking());
		
		flightBookingSummaryDTOV2.setDisplayOnly(flightBookingSummary.isDisplayOnly());
		
		flightBookingSummaryDTOV2.setPackageBooking(flightBookingSummary.isPackageBooking());
		flightBookingSummaryDTOV2.setCanCheckIn(flightBookingSummary.getCprJourneys()!=null && flightBookingSummary.getCprJourneys().stream().flatMap(journey->journey.getSegments().stream()).anyMatch(CprJourneySegment::getCanCheckIn));

		flightBookingSummaryDTOV2.setCompanionBooking(flightBookingSummary.isCompanionBooking());
		flightBookingSummaryDTOV2.setOnHoldBooking(flightBookingSummary.isOnHoldBooking());
		flightBookingSummaryDTOV2.setEncryptedRloc(flightBookingSummary.getEncryptedRloc());
		flightBookingSummaryDTOV2.setCanIssueTicket(flightBookingSummary.isCanIssueTicket());
		flightBookingSummaryDTOV2.setIssueTicketsExisting(flightBookingSummary.isIssueTicketsExisting());
		flightBookingSummaryDTOV2.setPcc(flightBookingSummary.isPcc());
		flightBookingSummaryDTOV2.setAdtk(flightBookingSummary.isAdtk());
		flightBookingSummaryDTOV2.setTktl(flightBookingSummary.isTktl());
		flightBookingSummaryDTOV2.setTkxl(flightBookingSummary.isTkxl());
		flightBookingSummaryDTOV2.setBookingDwCodeList(flightBookingSummary.getBookingDwCode());
		flightBookingSummaryDTOV2.setUmFormInfo(flightBookingSummary.getUmFormInfo());
		flightBookingSummaryDTOV2.setCprJourney(convertJourneyDTO(flightBookingSummary.getCprJourneys()));
		List<RebookMapping> rebookMappings = flightBookingSummary.getRebookMapping();
		if(!CollectionUtils.isEmpty(rebookMappings)) {
			List<RebookMappingDTOV2>  rebookMappingDTOs = new ArrayList<>();
			for(RebookMapping rebookMapping : rebookMappings) {
				RebookMappingDTOV2 rebookMappingDTO = new RebookMappingDTOV2();
				rebookMappingDTO.setAcceptSegmentIds(rebookMapping.getAcceptSegmentIds());
				rebookMappingDTO.setCancelledSegmentIds(rebookMapping.getCancelledSegmentIds());
				rebookMappingDTOs.add(rebookMappingDTO);
			}
			flightBookingSummaryDTOV2.setRebookMapping(rebookMappingDTOs);
		}		
		
		TicketIssueInfoSummary ticketIssueInfoSummary = flightBookingSummary.getTicketIssueInfo();
		if (ticketIssueInfoSummary != null) {
			TicketIssueInfoSummaryDTOV2 ticketIssueInfoSummaryDTO = new TicketIssueInfoSummaryDTOV2();
			ticketIssueInfoSummaryDTO.setDate(ticketIssueInfoSummary.getDate());
			ticketIssueInfoSummaryDTO.setRpLocalDeadLineTime(ticketIssueInfoSummary.getRpLocalDeadLineTime());
			ticketIssueInfoSummaryDTO.setIndicator(ticketIssueInfoSummary.getIndicator());
			ticketIssueInfoSummaryDTO.setOfficeId(ticketIssueInfoSummary.getOfficeId());
			ticketIssueInfoSummaryDTO.setTime(ticketIssueInfoSummary.getTime());
			ticketIssueInfoSummaryDTO.setTimeZoneOffset(ticketIssueInfoSummary.getTimeZoneOffset());
			flightBookingSummaryDTOV2.setTicketIssueInfo(ticketIssueInfoSummaryDTO);
		}

		List<SegmentSummaryDTOV2> segmentSummaryDTOs = new ArrayList<>();
		List<SegmentSummary> segmentSummarys = flightBookingSummary.getDetails();
		if(!CollectionUtils.isEmpty(segmentSummarys)) {
			for(SegmentSummary segmentSummary : segmentSummarys) {
				SegmentSummaryDTOV2 segmentSummaryDTO = new SegmentSummaryDTOV2();
				
				segmentSummaryDTO.setOrder(segmentSummary.getOrder());
				segmentSummaryDTO.setFirstAvlSector(segmentSummary.isFirstAvlSector());
				segmentSummaryDTO.setNumberOfStops(segmentSummary.getNumberOfStops());
				
				segmentSummaryDTO.setOriginPort(segmentSummary.getOriginPort());
				segmentSummaryDTO.setDestPort(segmentSummary.getDestPort());
				segmentSummaryDTO.setMarketCompany(segmentSummary.getMarketCompany());
				segmentSummaryDTO.setMarketSegmentNumber(segmentSummary.getMarketSegmentNumber());
				segmentSummaryDTO.setStatus(segmentSummary.getSegmentStatus().getStatus());
				
				DepartureArrivalTime departureTime = segmentSummary.getDepartureTime();
				if(departureTime != null) {
					DepartureArrivalTimeDTOV2 departureTimeDTO = new DepartureArrivalTimeDTOV2();
					departureTimeDTO.setPnrTime(departureTime.getPnrTime());
					departureTimeDTO.setRtfsActualTime(departureTime.getRtfsActualTime());
					departureTimeDTO.setRtfsEstimatedTime(departureTime.getRtfsEstimatedTime());
					departureTimeDTO.setRtfsScheduledTime(departureTime.getRtfsScheduledTime());
					departureTimeDTO.setTimeZoneOffset(departureTime.getTimeZoneOffset());
					segmentSummaryDTO.setDepartureTime(departureTimeDTO);
				}
				
				DepartureArrivalTime arrivalTime = segmentSummary.getArrivalTime();
				if(arrivalTime != null) {
					DepartureArrivalTimeDTOV2 arrivalTimeDTO = new DepartureArrivalTimeDTOV2();
					arrivalTimeDTO.setPnrTime(arrivalTime.getPnrTime());
					arrivalTimeDTO.setRtfsActualTime(arrivalTime.getRtfsActualTime());
					arrivalTimeDTO.setRtfsEstimatedTime(arrivalTime.getRtfsEstimatedTime());
					arrivalTimeDTO.setRtfsScheduledTime(arrivalTime.getRtfsScheduledTime());
					arrivalTimeDTO.setTimeZoneOffset(arrivalTime.getTimeZoneOffset());
					segmentSummaryDTO.setArrivalTime(arrivalTimeDTO);
				}
				
				segmentSummaryDTO.setSegmentId(segmentSummary.getSegmentId());
				segmentSummaryDTO.setOperateCompany(segmentSummary.getOperateCompany());
				segmentSummaryDTO.setOperateSegmentNumber(segmentSummary.getOperateSegmentNumber());
				segmentSummaryDTO.setAirCraftType(segmentSummary.getAirCraftType());
				segmentSummaryDTO.setCabinClass(segmentSummary.getCabinClass());
				segmentSummaryDTO.setSubClass(segmentSummary.getSubClass());
				segmentSummaryDTO.setFlown(segmentSummary.isFlown());
				segmentSummaryDTO.setOpenToCheckIn(segmentSummary.isOpenToCheckIn());
				segmentSummaryDTO.setAdtk(segmentSummary.isAdtk());
				segmentSummaryDTO.setPcc(segmentSummary.isPcc());
				segmentSummaryDTO.setRebookInfo(segmentSummary.getRebookInfo());
				segmentSummaryDTOs.add(segmentSummaryDTO);
			}
		}
		
		flightBookingSummaryDTOV2.setDetails(segmentSummaryDTOs);
		
		List<PassengerSegmentSummaryDTOV2> passengerSegmentSummaryDTOs = new ArrayList<>();
		List<PassengerSegmentSummary> passengerSegmentSummarys = flightBookingSummary.getPassengerSegments();
		if (!CollectionUtils.isEmpty(passengerSegmentSummarys)) {
			for (PassengerSegmentSummary passengerSegmentSummary : passengerSegmentSummarys) {
				PassengerSegmentSummaryDTOV2 passengerSegmentSummaryDTO = new PassengerSegmentSummaryDTOV2();
				passengerSegmentSummaryDTO.setPassengerId(passengerSegmentSummary.getPassengerId());
				passengerSegmentSummaryDTO.setSegmentId(passengerSegmentSummary.getSegmentId());
				passengerSegmentSummaryDTO.setReverseCheckinCarrier(passengerSegmentSummary.getReverseCheckinCarrier());
				passengerSegmentSummaryDTOs.add(passengerSegmentSummaryDTO);
			}
		}
		flightBookingSummaryDTOV2.setPassengerSegments(passengerSegmentSummaryDTOs);
		
		return flightBookingSummaryDTOV2;
	}
	/**
	 * convert cpr Journey to Journey model
	 * @return
	 */
	private List<JourneyDTOV2> convertJourneyDTO(List<Journey> cprJourneys) {
		if (CollectionUtils.isEmpty(cprJourneys)) {
			return Collections.emptyList();
		}
		
		List<JourneyDTOV2> jourenyDTOs = new ArrayList<>();

		// journey info
		for (Journey cprJourney : cprJourneys) {
			JourneyDTOV2 journeyDTO = new JourneyDTOV2();
			jourenyDTOs.add(journeyDTO);
			journeyDTO.setOpenToCheckIn(cprJourney.isOpenToCheckIn());
			journeyDTO.setBeforeCheckIn(cprJourney.isBeforeCheckIn());
			journeyDTO.setOpenCloseTime(cprJourney.getOpenCloseTime());
			journeyDTO.setNextOpenCloseTime(cprJourney.getNextOpenCloseTime());
			journeyDTO.setPostCheckIn(cprJourney.isPostCheckIn());
			journeyDTO.setCanCheckIn(cprJourney.isCanCheckIn());
			journeyDTO.setErrors(cprJourney.getErrors());
			journeyDTO.setJourneyId(cprJourney.getJourneyId());
			journeyDTO.setEnabledPriorityCheckIn(cprJourney.isEnabledPriorityCheckIn());
			journeyDTO.setCanCheckInAfterUpgrade(cprJourney.isCanCheckInAfterUPgrade());
			journeyDTO.setPriorityCheckInEligible(cprJourney.getPriorityCheckInEligible());

			if (cprJourney.getSegments() != null) {
				// segment
				journeyDTO.setSegments(new ArrayList<>());
				for (CprJourneySegment cprJourneySegment : cprJourney.getSegments()) {
					CprJourneySegmentDTOV2 segmentDTO = new CprJourneySegmentDTOV2();
					segmentDTO.setSegmentId(cprJourneySegment.getSegmentId());
					segmentDTO.setErrors(cprJourneySegment.getErrors());
					segmentDTO.setCanCheckIn(cprJourneySegment.getCanCheckIn());
					journeyDTO.getSegments().add(segmentDTO);
				}
			}
			if (cprJourney.getPassengers() != null) {
				// passenger
				journeyDTO.setPassengers(new ArrayList<>());
				for (CprJourneyPassenger cprJourneyPassenger : cprJourney.getPassengers()) {
					CprJourneyPassengerDTOV2 passengerDTO = new CprJourneyPassengerDTOV2();
					passengerDTO.setPassengerId(cprJourneyPassenger.getPassengerId());
					passengerDTO.setErrors(cprJourneyPassenger.getErrors());
					passengerDTO.setCanCheckIn(cprJourneyPassenger.getCanCheckIn());
					journeyDTO.getPassengers().add(passengerDTO);
				}
			}
			if (cprJourney.getPassengerSegments() != null) {
				// passengersegment
				journeyDTO.setPassengerSegments(new ArrayList<>());
				for (CprJourneyPassengerSegment cprJourneyPassengerSegment : cprJourney.getPassengerSegments()) {
					CprJourneyPassengerSegmentDTOV2 psDTO = new CprJourneyPassengerSegmentDTOV2();
					psDTO.setPassengerId(cprJourneyPassengerSegment.getPassengerId());
					psDTO.setSegmentId(cprJourneyPassengerSegment.getSegmentId());
					psDTO.setErrors(cprJourneyPassengerSegment.getErrors());
					psDTO.setCanCheckIn(cprJourneyPassengerSegment.getCanCheckIn());
					psDTO.setCheckedIn(cprJourneyPassengerSegment.getCheckedIn());
					journeyDTO.getPassengerSegments().add(psDTO);
				}
			}

		}
		return jourenyDTOs;
	}
}
