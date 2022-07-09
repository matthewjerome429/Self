package com.cathaypacific.mmbbizrule.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.BookingSummaryBusiness;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.dto.common.booking.DepartureArrivalTimeDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.BookingSummaryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.BookingSummaryResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.EventBookingSummaryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.EventSummaryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.FlightBookingSummaryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.HotelBookingSummaryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.HotelSummaryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.PassengerSegmentSummaryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.RebookMappingDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.SegmentSummaryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.TicketIssueInfoSummaryDTO;
import com.cathaypacific.mmbbizrule.handler.BookingSummaryHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
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

@Service
public class BookingSummaryBusinessImpl implements BookingSummaryBusiness{

	private static LogAgent logger = LogAgent.getLogAgent(BookingSummaryBusinessImpl.class);
	
	@Value("${bookingsummary.defaultpagesize}")
	private int defaultPageSize;
	
	@Value("${bookingsummary.buffersize}")
	private int bufferSize;
	
	@Autowired
	private BookingSummaryHelper bookingSummaryHelper;
	
	@Override
	@LogPerformance(message = "Build booking list spend time(ms).")
	public BookingSummaryResponseDTO getMemberBookings(LoginInfo loginInfo, String lastRloc, String requirePageSize, BookingBuildRequired required) throws BusinessBaseException {		
		
		logger.debug("buildbookingdebuglog Build booking list start:"+System.currentTimeMillis());
		BookingSummaryResponseDTO responseDTO = new BookingSummaryResponseDTO();
	
		BookingSummaryIntegrationBean bookingSummaryIntegrationBean = bookingSummaryHelper.getMemberBookings(loginInfo,
				required);

		responseDTO.setRetrieveEODSSuccess(bookingSummaryIntegrationBean.getRetrieveEODSSuccess());
		responseDTO.setRetrieveOJSuccess(bookingSummaryIntegrationBean.getRetrieveOJSuccess());
		responseDTO.setRetrieveOneASuccess(bookingSummaryIntegrationBean.getRetrieveOneASuccess());
		responseDTO.setBookings(convertToDto(bookingSummaryIntegrationBean.getBookings(), getPageSize(requirePageSize), lastRloc));
		responseDTO.setBookingCount(bookingSummaryIntegrationBean.getBookings().size());
		
		
		return responseDTO;
	}
	  
	
	/**
	 * Get page size, will use default value if the request page size is invalid.
	 * @param requireBookingCount
	 * @return
	 */
	private int getPageSize(String requireBookingCount) {
		int pageSize = defaultPageSize;
		try {
			if (!StringUtils.isEmpty(requireBookingCount)) {
				pageSize = Integer.valueOf(requireBookingCount);
			}
		} catch (Exception e) {
			logger.error(String.format(
					"Cannot covert page size to int, will use default page size. Required page size:%s, default page size:%s",
					requireBookingCount, pageSize));
		}
		return pageSize;
	}
	
	/**
	 * convert List<BookingSummary> to List<BookingSummaryDTO>
	 * @param bookingSummarys
	 * @param pageSize
	 * @param lastRloc
	 * @return List<BookingSummaryDTO>
	 */
	private List<BookingSummaryDTO> convertToDto(List<BookingSummary> bookingSummarys, int pageSize, String lastRloc) {
		List<BookingSummaryDTO> bookingSummaryDTOs = new ArrayList<>();
		int count = 0;
		for(BookingSummary bookingSummary : bookingSummarys) {
			if(count == pageSize) {
				break;
			}
			
			String rloc = bookingSummary.getDisplayRloc();
			if(lastRloc != null && count == 0 && !lastRloc.equals(rloc)) {
				continue;
			}
			
			BookingSummaryDTO bookingSummaryDTO = new BookingSummaryDTO();
			bookingSummaryDTO.setRloc(rloc);
			String bookingType = bookingSummary.getBookingType();
			bookingSummaryDTO.setBookingType(bookingType);
			bookingSummaryDTO.setBooingStatus(bookingSummary.getBookingStatus());
		
			bookingSummaryDTO.setTempLinkedBooking(bookingSummary.getFlightSummary() == null ? false
					: bookingSummary.getFlightSummary().isTempLinkedBooking());

			if(MMBBizruleConstants.BOOKING_TYPE_FLIGHT.equals(bookingType)) {
				bookingSummaryDTO.setFlightSummary(convertToFlightBookingSummaryDTO(bookingSummary.getFlightSummary()));
			} else if(MMBBizruleConstants.BOOKING_TYPE_HOTEL.equals(bookingType)) {
				bookingSummaryDTO.setHotelSummary(convertToHotelBookingSummaryDTO(bookingSummary.getHotelSummary()));
			} else if(MMBBizruleConstants.BOOKING_TYPE_PACKAGE.equals(bookingType)) {
				bookingSummaryDTO.setFlightSummary(convertToFlightBookingSummaryDTO(bookingSummary.getFlightSummary()));
				bookingSummaryDTO.setHotelSummary(convertToHotelBookingSummaryDTO(bookingSummary.getHotelSummary()));
				bookingSummaryDTO.setEventSummary(convertToEventBookingSummaryDTO(bookingSummary.getEventSummary()));
			}
			count++;
			bookingSummaryDTOs.add(bookingSummaryDTO);
		}
		// reset bookingType by flag(packageBooking in flightBooking)
		resetBookingType(bookingSummaryDTOs);
		return bookingSummaryDTOs;
	}

	/**
	 * reset bookingType by flag(packageBooking in flightBooking)
	 * @param bookingSummaryDTOs
	 */
	private void resetBookingType(List<BookingSummaryDTO> bookingSummaryDTOs) {
		for(BookingSummaryDTO bookingSummaryDTO : bookingSummaryDTOs) {
			if(!MMBBizruleConstants.BOOKING_TYPE_FLIGHT.equals(bookingSummaryDTO.getBookingType()) 
					|| bookingSummaryDTO.getFlightSummary() == null) {
				continue;
			}
			if(BooleanUtils.isTrue(bookingSummaryDTO.getFlightSummary().isPackageBooking())) {
				bookingSummaryDTO.setBookingType(MMBBizruleConstants.BOOKING_TYPE_PACKAGE);
			}
		}
	}

	/**
	 * convert eventBookingSummary to EventBookingSummaryDTO
	 * @param eventBookingSummary
	 * @return EventBookingSummaryDTO
	 */
	private EventBookingSummaryDTO convertToEventBookingSummaryDTO(EventBookingSummary eventBookingSummary) {
		if(eventBookingSummary == null) {
			return null;
		}
		EventBookingSummaryDTO eventBookingSummaryDTO = new EventBookingSummaryDTO();
		
		List<EventSummaryDTO> eventSummaryDTOs = new ArrayList<>();
		List<EventSummary> eventSummarys = eventBookingSummary.getDetails();
		for(EventSummary eventSummary : eventSummarys) {
			EventSummaryDTO eventSummaryDTO = new EventSummaryDTO();
			
			eventSummaryDTO.setOrder(eventSummary.getOrder());
			eventSummaryDTO.setFirstAvlSector(eventSummary.isFirstAvlSector());
			
			eventSummaryDTO.setBookingStatus(eventSummary.getBookingStatus());
			eventSummaryDTO.setDate(eventSummary.getDate());
			eventSummaryDTO.setTime(eventSummary.getTime());
			eventSummaryDTO.setName(eventSummary.getName());
			
			eventSummaryDTOs.add(eventSummaryDTO);
		}
		eventBookingSummaryDTO.setDetails(eventSummaryDTOs);
		
		return eventBookingSummaryDTO;
	}

	/**
	 * convert HotelBookingSummary to HotelBookingSummaryDTO
	 * @param hotelBookingSummary
	 * @return HotelBookingSummaryDTO
	 */
	private HotelBookingSummaryDTO convertToHotelBookingSummaryDTO(HotelBookingSummary hotelBookingSummary) {
		if(hotelBookingSummary == null) {
			return null;
		}
		HotelBookingSummaryDTO hotelBookingSummaryDTO = new HotelBookingSummaryDTO();
		hotelBookingSummaryDTO.setStatus(hotelBookingSummary.getStatus());
		
		List<HotelSummaryDTO> hotelSummaryDTOs = new ArrayList<>();
		List<HotelSummary> hotelSummarys = hotelBookingSummary.getDetails();
		for(HotelSummary hotelSummary : hotelSummarys) {
			HotelSummaryDTO hotelSummaryDTO = new HotelSummaryDTO();
			
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
		hotelBookingSummaryDTO.setDetails(hotelSummaryDTOs);
		
		return hotelBookingSummaryDTO;
	}

	/**
	 * convert FlightBookingSummary to FlightBookingSummaryDTO
	 * @param flightBookingSummary
	 * @return FlightBookingSummaryDTO
	 */
	private FlightBookingSummaryDTO convertToFlightBookingSummaryDTO(FlightBookingSummary flightBookingSummary) {
		if(flightBookingSummary == null) {
			return null;
		}
		FlightBookingSummaryDTO flightBookingSummaryDTO = new FlightBookingSummaryDTO();
		flightBookingSummaryDTO.setOneARloc(flightBookingSummary.getOneARloc());
		flightBookingSummaryDTO.setRloc(flightBookingSummary.getDisplayRloc());
		flightBookingSummaryDTO.setSpnr(flightBookingSummary.getSpnr());
		flightBookingSummaryDTO.setCreateDate(flightBookingSummary.getCreateDate());
		flightBookingSummaryDTO.setOfficeId(flightBookingSummary.getOfficeId());
		flightBookingSummaryDTO.setPassengers(flightBookingSummary.getPassengers());
		
		flightBookingSummaryDTO.setIbeBooking(flightBookingSummary.isIbeBooking());
		flightBookingSummaryDTO.setTrpBooking(flightBookingSummary.isTrpBooking());
		flightBookingSummaryDTO.setAppBooking(flightBookingSummary.isAppBooking());
		flightBookingSummaryDTO.setGdsBooking(flightBookingSummary.isGdsBooking());
		flightBookingSummaryDTO.setGroupBooking(flightBookingSummary.isGroupBooking());
		flightBookingSummaryDTO.setRedBooking(flightBookingSummary.isRedBooking());
		flightBookingSummaryDTO.setGccBooking(flightBookingSummary.isGccBooking());
		flightBookingSummaryDTO.setStaffBooking(flightBookingSummary.isStaffBooking());
		flightBookingSummaryDTO.setRedUpgrade(flightBookingSummary.isRedUpgrade());
		
		flightBookingSummaryDTO.setDisplayOnly(flightBookingSummary.isDisplayOnly());
		
		flightBookingSummaryDTO.setPackageBooking(flightBookingSummary.isPackageBooking());
		flightBookingSummaryDTO.setCanCheckIn(flightBookingSummary.isCanCheckIn());

		flightBookingSummaryDTO.setCompanionBooking(flightBookingSummary.isCompanionBooking());
		flightBookingSummaryDTO.setOnHoldBooking(flightBookingSummary.isOnHoldBooking());
		flightBookingSummaryDTO.setEncryptedRloc(flightBookingSummary.getEncryptedRloc());
		flightBookingSummaryDTO.setCanIssueTicket(flightBookingSummary.isCanIssueTicket());
		flightBookingSummaryDTO.setIssueTicketsExisting(flightBookingSummary.isIssueTicketsExisting());
		flightBookingSummaryDTO.setPcc(flightBookingSummary.isPcc());
		flightBookingSummaryDTO.setAdtk(flightBookingSummary.isAdtk());
		flightBookingSummaryDTO.setTktl(flightBookingSummary.isTktl());
		flightBookingSummaryDTO.setTkxl(flightBookingSummary.isTkxl());
		flightBookingSummaryDTO.setBookingDwCodeList(flightBookingSummary.getBookingDwCode());
		flightBookingSummaryDTO.setUmFormInfo(flightBookingSummary.getUmFormInfo());
		
		List<RebookMapping> rebookMappings = flightBookingSummary.getRebookMapping();
		if(!CollectionUtils.isEmpty(rebookMappings)) {
			List<RebookMappingDTO>  rebookMappingDTOs = new ArrayList<>();
			for(RebookMapping rebookMapping : rebookMappings) {
				RebookMappingDTO rebookMappingDTO = new RebookMappingDTO();
				rebookMappingDTO.setAcceptSegmentIds(rebookMapping.getAcceptSegmentIds());
				rebookMappingDTO.setCancelledSegmentIds(rebookMapping.getCancelledSegmentIds());
				rebookMappingDTOs.add(rebookMappingDTO);
			}
			flightBookingSummaryDTO.setRebookMapping(rebookMappingDTOs);
		}		
		
		TicketIssueInfoSummary ticketIssueInfoSummary = flightBookingSummary.getTicketIssueInfo();
		if (ticketIssueInfoSummary != null) {
			TicketIssueInfoSummaryDTO ticketIssueInfoSummaryDTO = new TicketIssueInfoSummaryDTO();
			ticketIssueInfoSummaryDTO.setDate(ticketIssueInfoSummary.getDate());
			ticketIssueInfoSummaryDTO.setRpLocalDeadLineTime(ticketIssueInfoSummary.getRpLocalDeadLineTime());
			ticketIssueInfoSummaryDTO.setIndicator(ticketIssueInfoSummary.getIndicator());
			ticketIssueInfoSummaryDTO.setOfficeId(ticketIssueInfoSummary.getOfficeId());
			ticketIssueInfoSummaryDTO.setTime(ticketIssueInfoSummary.getTime());
			ticketIssueInfoSummaryDTO.setTimeZoneOffset(ticketIssueInfoSummary.getTimeZoneOffset());
			flightBookingSummaryDTO.setTicketIssueInfo(ticketIssueInfoSummaryDTO);
		}

		List<SegmentSummaryDTO> segmentSummaryDTOs = new ArrayList<>();
		List<SegmentSummary> segmentSummarys = flightBookingSummary.getDetails();
		for(SegmentSummary segmentSummary : segmentSummarys) {
			SegmentSummaryDTO segmentSummaryDTO = new SegmentSummaryDTO();
			
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
				DepartureArrivalTimeDTO departureTimeDTO = new DepartureArrivalTimeDTO();
				departureTimeDTO.setPnrTime(departureTime.getPnrTime());
				departureTimeDTO.setRtfsActualTime(departureTime.getRtfsActualTime());
				departureTimeDTO.setRtfsEstimatedTime(departureTime.getRtfsEstimatedTime());
				departureTimeDTO.setRtfsScheduledTime(departureTime.getRtfsScheduledTime());
				departureTimeDTO.setTimeZoneOffset(departureTime.getTimeZoneOffset());
				segmentSummaryDTO.setDepartureTime(departureTimeDTO);
			}
			
			DepartureArrivalTime arrivalTime = segmentSummary.getArrivalTime();
			if(arrivalTime != null) {
				DepartureArrivalTimeDTO arrivalTimeDTO = new DepartureArrivalTimeDTO();
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
			segmentSummaryDTO.setCanCheckIn(segmentSummary.isCanCheckIn());
			segmentSummaryDTO.setCheckedIn(segmentSummary.isCheckedIn());
			segmentSummaryDTO.setOpenToCheckIn(segmentSummary.isOpenToCheckIn());
			segmentSummaryDTO.setPostToCheckIn(segmentSummary.isPostCheckIn());
			segmentSummaryDTO.setAdtk(segmentSummary.isAdtk());
			segmentSummaryDTO.setPcc(segmentSummary.isPcc());
			segmentSummaryDTO.setRebookInfo(segmentSummary.getRebookInfo());
			segmentSummaryDTOs.add(segmentSummaryDTO);
		}
		flightBookingSummaryDTO.setDetails(segmentSummaryDTOs);
		
		List<PassengerSegmentSummaryDTO> passengerSegmentSummaryDTOs = new ArrayList<>();
		List<PassengerSegmentSummary> passengerSegmentSummarys = flightBookingSummary.getPassengerSegments();
		if (!CollectionUtils.isEmpty(passengerSegmentSummarys)) {
			for (PassengerSegmentSummary passengerSegmentSummary : passengerSegmentSummarys) {
				PassengerSegmentSummaryDTO passengerSegmentSummaryDTO = new PassengerSegmentSummaryDTO();
				passengerSegmentSummaryDTO.setPassengerId(passengerSegmentSummary.getPassengerId());
				passengerSegmentSummaryDTO.setSegmentId(passengerSegmentSummary.getSegmentId());
				passengerSegmentSummaryDTO.setCheckedIn(passengerSegmentSummary.isCheckedIn());
				passengerSegmentSummaryDTOs.add(passengerSegmentSummaryDTO);
			}
		}
		flightBookingSummaryDTO.setPassengerSegments(passengerSegmentSummaryDTOs);
		
		return flightBookingSummaryDTO;
	}
	
}
