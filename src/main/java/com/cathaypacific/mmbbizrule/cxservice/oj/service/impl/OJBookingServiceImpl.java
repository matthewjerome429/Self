package com.cathaypacific.mmbbizrule.cxservice.oj.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.cxservice.oj.OJBookingResponseParser;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.NameInput;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.OJBookingResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.RetrieveOJBookingResponse;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJEvent;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJEventBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJFlightBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotel;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.member.RetrieveOJBookingDetailResponse;
import com.cathaypacific.mmbbizrule.cxservice.oj.service.OJBookingService;
import com.cathaypacific.mmbbizrule.model.booking.summary.BookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.EventBookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.EventSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.HotelBookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.HotelSummary;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;

@Service
public class OJBookingServiceImpl implements OJBookingService {
	
	private static LogAgent logger = LogAgent.getLogAgent(OJBookingServiceImpl.class);
	
	@Value("${endpoint.oj.retrieveBooking}")
	private String ojBookingEndpoint;
	
	@Value("${endpoint.oj.retrieveSummaryDetailBooking}")
	private String ojBookingSummaryDetailEndpoint;
	
	@Autowired
	public RestTemplate restTemplate;
	
	@Autowired
	private OJBookingResponseParser ojBookingResponseParser;
	
	@Async
	@Override
	public Future<BookingSummary> asyncGetBookingSummary(String firstName, String surName, String reference) throws BusinessBaseException {
		return new AsyncResult<>(buildBookingSummary(this.getBooking(firstName, surName, reference)));
	}
	
	@Async
	@Override
	public Future<List<BookingSummary>> asynGetOjBookingList(ProfilePersonInfo profilePersonInfo , String mbToken) throws BusinessBaseException {
		return new AsyncResult<>(this.getOjBookingList(profilePersonInfo, mbToken));
	}

	@Async
	@Override
	public Future<OJBooking> asyncGetBooking(String firstName, String surName, String reference)
			throws BusinessBaseException {
		return new AsyncResult<>(this.getBooking(firstName, surName, reference));
	}
	
	@Override
	public List<BookingSummary> getOjBookingList(ProfilePersonInfo profilePersonInfo, String mbToken) throws BusinessBaseException {
		RetrieveOJBookingDetailResponse retrieveOJBookingDetailResponse = this.getOjBookingSummaryDetail(profilePersonInfo, mbToken);
		if(retrieveOJBookingDetailResponse!=null && retrieveOJBookingDetailResponse.getRetrieveOJBookingResponses()!=null){
			String rlocs = retrieveOJBookingDetailResponse.getRetrieveOJBookingResponses().stream().filter(ojb->ojb.getManageBooking()!=null).map(ojb->ojb.getManageBooking().getSuperPNRID()).collect(Collectors.joining(" "));
			logger.info(String.format("Retrieved OJ rloc(s) from OJ server for member:[%s]",rlocs));
		}
		return buildBookingSummarys(retrieveOJBookingDetailResponse);
	}
	
	private List<BookingSummary> buildBookingSummarys(RetrieveOJBookingDetailResponse retrieveOJBookingDetailResponse) {
		List<BookingSummary> bookingSummaries = new ArrayList<>();
		if(retrieveOJBookingDetailResponse != null && !CollectionUtils.isEmpty(retrieveOJBookingDetailResponse.getRetrieveOJBookingResponses())) {
			for(RetrieveOJBookingResponse retrieveOJBookingResponse : retrieveOJBookingDetailResponse.getRetrieveOJBookingResponses()) {
				if(retrieveOJBookingResponse.getManageBooking() !=null && !CollectionUtils.isEmpty(retrieveOJBookingResponse.getManageBooking().getBooking())) {
					OJBookingResponseDTO ojBookingDTO = retrieveOJBookingResponse.getManageBooking().getBooking().get(0);
					bookingSummaries.add(buildBookingSummary(ojBookingResponseParser.paserResponse(ojBookingDTO)));						
				}
			}
		}
		return bookingSummaries;
	}
	
	@Override
	//@LogPerformance(message = "Time required to get the booking by SPNR")
	public OJBooking getBooking(String firstName, String surName, String reference) throws BusinessBaseException {
		URI uri = UriComponentsBuilder
				.fromHttpUrl(ojBookingEndpoint)
				.queryParam("firstName", firstName)
				.queryParam("surName", surName)
				.queryParam("reference", reference)
				.build()
				.toUri();

		RequestEntity<Void> requestEntity = RequestEntity.get(uri).header(MMBConstants.HEADER_KEY_MMB_TOKEN_ID, MMBUtil.getCurrentMMBToken()).build();
		OJBookingResponseDTO ojBookingResponseDTO = null;
		try {
			ResponseEntity<RetrieveOJBookingResponse> response = restTemplate.exchange(requestEntity, RetrieveOJBookingResponse.class);

			if (response.getStatusCode().is2xxSuccessful() && response.hasBody()
					&& response.getBody().getManageBooking() != null) {
				OJBookingResponseDTO ojResponse = response.getBody().getManageBooking().getBooking().get(0);
				setInputNames(ojResponse, firstName, surName);
				ojBookingResponseDTO = ojResponse;
			}
		} catch (HttpClientErrorException clientEx) {
			ErrorInfo errorInfo = null;
			String errorMsg = "";
			if(clientEx.getStatusCode().equals(HttpStatus.NOT_FOUND)){
				errorMsg = "OJ booking not found by spnr:" + reference;
				errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN);
			}else if(clientEx.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				errorMsg = "unauthorized access to OJ web service with mmb-token";
				errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_TOKEN_INVALID);
			}
			
			throw new ExpectedException(errorMsg, errorInfo, clientEx);
		} catch (HttpServerErrorException serverEx) {
			ErrorInfo errorInfo = null;
			String errorMsg = "";
			if(serverEx.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)){
				errorMsg = "OJ Service internal error [HTTP 500]";
				errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			}else if(serverEx.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)){
				errorMsg = "OJ Service unavailable [HTTP 503]";
				errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			}else if(serverEx.getStatusCode().equals(HttpStatus.GATEWAY_TIMEOUT)){
				errorMsg = "OJ Service gateway timeout [HTTP 504]";
				errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			}
			
			throw new UnexpectedException(errorMsg, errorInfo,serverEx);
		} catch (RestClientException e) {
			throw e;
		}
		return ojBookingResponseDTO != null ? ojBookingResponseParser.paserResponse(ojBookingResponseDTO):null;
	}
	
	/**
	 * set input names
	 * @param ojResponse
	 * @param firstName
	 * @param surName
	 */
	private void setInputNames(OJBookingResponseDTO ojResponse, String firstName, String surName) {
		if(ojResponse == null || firstName == null || surName == null) {
			return;
		}
		
		NameInput nameInput = new NameInput();
		nameInput.setFirstNameInput(firstName);
		nameInput.setLastNameInput(surName);
		if(ojResponse.getContactDetails() != null 
				&& ojResponse.getContactDetails().getName() != null
				&& surName.equals(ojResponse.getContactDetails().getName().getGivenName())
				&& firstName.equals(ojResponse.getContactDetails().getName().getSurName())) {
			nameInput.setTitle(ojResponse.getContactDetails().getName().getPrefix());
		}
		
		ojResponse.setNameInput(nameInput);
	}

	private RetrieveOJBookingDetailResponse getOjBookingSummaryDetail(ProfilePersonInfo profilePersonInfo, String mbToken) throws BusinessBaseException {
		URI uri = UriComponentsBuilder
				.fromHttpUrl(ojBookingSummaryDetailEndpoint)
				.queryParam("memberId", profilePersonInfo.getMemberId())
				.queryParam("firstName", profilePersonInfo.getGivenName())
				.queryParam("surName", profilePersonInfo.getFamilyName())
				.build()
				.toUri();
		RequestEntity<Void> requestEntity = RequestEntity.get(uri).header(MMBConstants.HEADER_KEY_MMB_TOKEN_ID, MMBUtil.getCurrentMMBToken()).build();

		ResponseEntity<RetrieveOJBookingDetailResponse> response;
		try {
			response = restTemplate.exchange(requestEntity, RetrieveOJBookingDetailResponse.class);
			if(response.getStatusCode().is2xxSuccessful()){
				if(response.hasBody() && !CollectionUtils.isEmpty(response.getBody().getRetrieveOJBookingResponses())){
					return response.getBody();
				}else{
					return null;
				}
			}
		} catch (HttpClientErrorException clientEx) {
			
			ErrorInfo errorInfo = null;
			String errorMsg = "";
			if(clientEx.getStatusCode().equals(HttpStatus.NOT_FOUND)){
				errorMsg = "OJ booking not found by memberId:" + profilePersonInfo.getMemberId();
				errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN);
			}else if(clientEx.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				errorMsg = "unauthorized access to OJ web service with mmb-token";
				errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_TOKEN_INVALID);
			}
			
			throw new ExpectedException(errorMsg, errorInfo, clientEx);
		} catch (HttpServerErrorException serverEx) {
			
			ErrorInfo errorInfo = null;
			String errorMsg = "";
			if(serverEx.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)){
				errorMsg = "OJ Service internal error [HTTP 500]";
				errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			}else if(serverEx.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)){
				errorMsg = "OJ Service unavailable [HTTP 503]";
				errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			}else if(serverEx.getStatusCode().equals(HttpStatus.GATEWAY_TIMEOUT)){
				errorMsg = "OJ Service gateway timeout [HTTP 504]";
				errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			}
			
			throw new UnexpectedException(errorMsg, errorInfo,serverEx);
		} catch (RestClientException e) {
			logger.error("Error to retrieve Oj bookings " + e );
			throw e;
		}
		
		return null;
	}

	
	private BookingSummary buildBookingSummary(OJBooking ojBooking) {
		BookingSummary bookingSummary = new BookingSummary();
		
		bookingSummary.setBookingType(ojBooking.getBookingType());
		bookingSummary.setSpnr(ojBooking.getBookingReference());
		
		bookingSummary.setFlightSummary(buildFlightSummaryRloc(ojBooking.getFlightBooking()));
		bookingSummary.setHotelSummary(buildHotelSummary(ojBooking));
		bookingSummary.setEventSummary(buildEventSummary(ojBooking.getEventBooking()));
		
		return bookingSummary;
	}

	private EventBookingSummary buildEventSummary(OJEventBooking eventBooking) {
		if(eventBooking == null || CollectionUtils.isEmpty(eventBooking.getDetails())) {
			return null;
		}
		List<OJEvent> ojEvents = eventBooking.getDetails();
		
		EventBookingSummary eventBookingSummary = new EventBookingSummary();
		List<EventSummary> details = new ArrayList<>();
		eventBookingSummary.setDetails(details);
		for(OJEvent ojEvent : ojEvents) {
			if(ojEvent == null) {
				continue;
			}
			
			EventSummary eventSummary = new EventSummary();
			eventSummary.setId(ojEvent.getId());
			eventSummary.setType(MMBBizruleConstants.BOOKING_TYPE_EVENT);
			eventSummary.setOrderDate(ojEvent.getOrderDate());
			
			eventSummary.setBookingStatus(ojEvent.getBookingStatus());
			
			eventSummary.setName(ojEvent.getName());
			eventSummary.setBookingStatus(ojEvent.getBookingStatus());
			eventSummary.setDate(ojEvent.getDate());
			eventSummary.setTime(ojEvent.getTime());
			details.add(eventSummary);
		}
		return eventBookingSummary;
	}

	private HotelBookingSummary buildHotelSummary(OJBooking ojBooking) {
		
		OJHotelBooking ojHotelBooking = ojBooking.getHotelBooking();
		
		if(ojHotelBooking == null || CollectionUtils.isEmpty(ojHotelBooking.getDetails())) {
			return null;
		}

		List<OJHotel> ojHotels = ojHotelBooking.getDetails();
			
		HotelBookingSummary hotelBookingSummary = new HotelBookingSummary();
		hotelBookingSummary.setStatus(ojBooking.getBookingStatus());
		
		List<HotelSummary> details = new ArrayList<>();
		hotelBookingSummary.setDetails(details);
		
		for(OJHotel ojHotel : ojHotels) {
			if(ojHotel == null) {
				continue;
			}
			
			HotelSummary hotelSummary = new HotelSummary();
			hotelSummary.setId(ojHotel.getId());
			hotelSummary.setType(MMBBizruleConstants.BOOKING_TYPE_HOTEL);
			hotelSummary.setOrderDate(ojHotel.getOrderDate());
			hotelSummary.setTimeZoneOffset(ojHotel.getTimeZoneOffset());
			hotelSummary.setCheckOutGMTDate(ojHotel.getOutDate());
			
			hotelSummary.setHotelStatus(ojHotel.getBookingStatus());
			
			hotelSummary.setHotelName(ojHotel.getName());
			hotelSummary.setBookingDate(ojHotel.getBookingDate());
			hotelSummary.setCheckInDate(ojHotel.getCheckInDate());
			hotelSummary.setCheckOutDate(ojHotel.getCheckOutDate());
			hotelSummary.setCheckOutTime(ojHotel.getCheckOutTime());
			if(ojHotel.getAdress() != null) {
				hotelSummary.setCityName(ojHotel.getAdress().getCityName());
			}
			if(!CollectionUtils.isEmpty(ojHotel.getRooms())) {
				hotelSummary.setNightOfStay(ojHotel.getRooms().get(0).getDuration());
			}
			
			details.add(hotelSummary);
		}
		
		return hotelBookingSummary;
	}

	private FlightBookingSummary buildFlightSummaryRloc(OJFlightBooking flightBooking) {
		if(flightBooking == null || StringUtils.isEmpty(flightBooking.getBookingReference())) {
			return null;
		}
		FlightBookingSummary flightSummary = new FlightBookingSummary();
		flightSummary.setOneARloc(flightBooking.getBookingReference());
		return flightSummary;
	}

}