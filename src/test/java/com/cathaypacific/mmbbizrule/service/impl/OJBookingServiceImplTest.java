package com.cathaypacific.mmbbizrule.service.impl;

import static org.mockito.Mockito.when;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OjConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Address;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.CityNameInfo;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Hotel;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.ManageBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.OJBookingResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Product;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.RetrieveOJBookingResponse;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.Room;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.RoomInfo;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.member.RetrieveOJBookingDetailResponse;
import com.cathaypacific.mmbbizrule.cxservice.oj.service.impl.OJBookingServiceImpl;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;

@RunWith(MockitoJUnitRunner.class)
public class OJBookingServiceImplTest {
	@Rule
    public org.junit.rules.ExpectedException thrown= org.junit.rules.ExpectedException.none();
	
	@InjectMocks
	private OJBookingServiceImpl ojBookingService;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private RetrieveProfileService retrieveProfileService;
	
	@Test
	public void validateException_whenOJReturnHttpStatus404() throws BusinessBaseException{
		 String firstName="Test";
	     String surName="Lain";
	     String reference="1234567";
		thrown.expect(ExpectedException.class);
		ReflectionTestUtils.setField(
				ojBookingService, "ojBookingEndpoint", "https://testURL");
		when(restTemplate.exchange(Matchers.anyObject(), Matchers.eq(RetrieveOJBookingResponse.class)))
			.thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));

			ojBookingService.getBooking(firstName, surName, reference);
		 
	}
	
	@Test
	public void validateException_whenOJReturnHttpStatus503() throws Exception{
		 String firstName="Test";
	     String surName="Lain";
	     String reference="1234567";
		thrown.expect(UnexpectedException.class);
		ReflectionTestUtils.setField(
				ojBookingService, "ojBookingEndpoint", "https://testURL");
		
		when(restTemplate.exchange(Matchers.anyObject(), Matchers.eq(RetrieveOJBookingResponse.class)))
			.thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase()));

		ojBookingService.getBooking(firstName, surName, reference);
	}
	
	@Test
	public void validateException_whenOJReturnHttpStatus500() throws Exception{
		 String firstName="Test";
	     String surName="Lain";
	     String reference="1234567";
		thrown.expect(UnexpectedException.class);
		ReflectionTestUtils.setField(
				ojBookingService, "ojBookingEndpoint", "https://testURL");
		
		when(restTemplate.exchange(Matchers.anyObject(), Matchers.eq(RetrieveOJBookingResponse.class)))
			.thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));

		ojBookingService.getBooking(firstName, surName, reference);
	}
	
	@Test
	public void test_GetOjBookingListHappyPass() throws BusinessBaseException {
		String mbToken="mmbToken";
		String memberId = "1";
		ReflectionTestUtils.setField(
				ojBookingService, "ojBookingSummaryDetailEndpoint", "https://testURL");
		ProfilePersonInfo personInfo = new ProfilePersonInfo();
		RetrieveOJBookingDetailResponse  response = new RetrieveOJBookingDetailResponse();
		List<RetrieveOJBookingResponse> responses = new ArrayList<>();
		RetrieveOJBookingResponse retrieveOJBookingResponse = new RetrieveOJBookingResponse();
		responses.add(retrieveOJBookingResponse);
		ManageBooking manageBooking = new ManageBooking();
		List<OJBookingResponseDTO> ojBookingDTOs = new ArrayList<>();
		OJBookingResponseDTO ojBookingDTO = new OJBookingResponseDTO();
		ojBookingDTOs.add(ojBookingDTO);
		manageBooking.setBooking(ojBookingDTOs);
		retrieveOJBookingResponse.setManageBooking(manageBooking);
		
		List<Product> products = new ArrayList<>();
		Product product = new Product();
		products.add(product);
		Hotel hotel = new Hotel();
		product.setHotel(hotel);
		product.setType(MMBBizruleConstants.BOOKING_TYPE_HOTEL);
		product.setBookingStatus(OjConstants.BOOKED);
		hotel.setName("TEST");
		RoomInfo roomInfo = new RoomInfo();
		hotel.setRoomInfo(roomInfo);
		List<Room> rooms = new ArrayList<>();
		Room room = new Room();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OjConstants.TIME_FORMAT);
		
		final Calendar fourDayBefore = Calendar.getInstance();
		fourDayBefore.add(Calendar.DATE, -7);
		room.setCheckInDate(simpleDateFormat.format(fourDayBefore.getTime()));
		room.setCheckOutDate(simpleDateFormat.format(fourDayBefore.getTime()));
		Address address = new Address();
		List<CityNameInfo> cityNameInfos = new ArrayList<>();
		CityNameInfo cityNameInfo = new CityNameInfo();
		cityNameInfos.add(cityNameInfo);
		cityNameInfo.setCityName("CityName");
		address.setCityNameInfos(cityNameInfos);
		rooms.add(room);
		hotel.setAddress(address);
		roomInfo.setRoomList(rooms);
		ojBookingDTO.setProduct(products);
		response.setRetrieveOJBookingResponses(responses);
		when(retrieveProfileService.retrievePersonInfo(memberId, mbToken)).thenReturn(personInfo);
		when(restTemplate.getForEntity(Matchers.anyString(), Matchers.eq(RetrieveOJBookingDetailResponse.class))).thenReturn(new ResponseEntity<RetrieveOJBookingDetailResponse>(response, HttpStatus.OK));
//		List<BookingSummary> result = ojBookingService.getOjBookingList(memberId, mbToken);
//		assertEquals(1, result.size());
//		assertEquals(1, result.get(0).getSummarys().size());
//		assertEquals(simpleDateFormat.format(fourDayBefore.getTime()), ((HotelSummary) result.get(0).getSummarys().get(0)).getCheckInDate());
//		assertEquals(OjConstants.COMPLETED, ((HotelSummary) result.get(0).getSummarys().get(0)).getHotelStatus());
	}
}
