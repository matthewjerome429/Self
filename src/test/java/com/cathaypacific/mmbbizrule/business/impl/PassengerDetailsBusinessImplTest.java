package com.cathaypacific.mmbbizrule.business.impl;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.passengerdetails.PassengerDetailsResponseDTO;
import com.cathaypacific.mmbbizrule.handler.DTOConverter;
import com.cathaypacific.mmbbizrule.handler.MaskHelper;
import com.cathaypacific.mmbbizrule.handler.ValidateHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.service.UpdatePassengerService;

@RunWith(MockitoJUnitRunner.class)
public class PassengerDetailsBusinessImplTest {
	
	@InjectMocks
	PassengerDetailsBusinessImpl passengerDetailsBusinessImpl;
	
	@Mock
	private PnrInvokeService pnrInvokeService;
	
	@Mock
	private UpdatePassengerService updatePassengerService;
	
	@Mock
	private BookingBuildService bookingBuildService;
	
	@Mock
	private MbTokenCacheRepository mbTokenCacheRepository;

	@Mock
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Mock
	private DTOConverter dtoConverter;
	
	@Mock
	private MaskHelper maskHelper;
	
	private BookingBuildRequired required;
	@Mock
	private ValidateHelper validateHelper;
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp() {
		required = new BookingBuildRequired();
	}
	
	@Test
	public void test() throws BusinessBaseException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("LAIN");
		loginInfo.setLoginType("R");
		UpdatePassengerDetailsRequestDTO requestDTO=new UpdatePassengerDetailsRequestDTO();
		requestDTO.setRloc("IU123");
		RetrievePnrBooking retrievePnrBooking=new RetrievePnrBooking();
		Booking booking=new Booking();
		Segment segment = new Segment();
		segment.setSegmentStatus(new SegmentStatus());
		segment.getSegmentStatus().setFlown(false);
		segment.setMarketCompany("CX");
		booking.setSegments(new ArrayList<>());
		booking.getSegments().add(segment);	
		FlightBookingDTO flightBookingDTO=new FlightBookingDTO();
		flightBookingDTO.setOneARloc("IU123");
		
		when(validateHelper.validate(anyObject(), anyObject())).thenReturn(null);
		when(pnrInvokeService.retrievePnrByRloc("IU123")).thenReturn(retrievePnrBooking);
		when(updatePassengerService.updatePassenger(anyObject(), anyObject(), anyObject(), anyObject(), anyObject())).thenReturn(retrievePnrBooking);
		when( bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, required)).thenReturn(booking);
		when(dtoConverter.convertToBookingDTO(booking, loginInfo)).thenReturn(flightBookingDTO);
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		PassengerDetailsResponseDTO passengerDetailsResponseDTO=passengerDetailsBusinessImpl.updatePaxDetails(loginInfo, requestDTO, required);
		Assert.assertEquals("IU123", passengerDetailsResponseDTO.getBooking().getOneARloc());
	}
	
	@Test
	public void test1() throws BusinessBaseException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("LAIN");
		loginInfo.setLoginType("M");
		UpdatePassengerDetailsRequestDTO requestDTO=new UpdatePassengerDetailsRequestDTO();
		requestDTO.setRloc("IU123");
		RetrievePnrBooking retrievePnrBooking=new RetrievePnrBooking();
		Booking booking=new Booking();
		Segment segment = new Segment();
		segment.setSegmentStatus(new SegmentStatus());
		segment.getSegmentStatus().setFlown(false);
		segment.setMarketCompany("CX");
		booking.setSegments(new ArrayList<>());
		booking.getSegments().add(segment);	
		FlightBookingDTO flightBookingDTO=new FlightBookingDTO();
		flightBookingDTO.setOneARloc("IU123");
		when(validateHelper.validate(anyObject(), anyObject())).thenReturn(null);
		when(pnrInvokeService.retrievePnrByRloc("IU123")).thenReturn(retrievePnrBooking);
		when(updatePassengerService.updatePassenger(anyObject(), anyObject(), anyObject(), anyObject(), anyObject())).thenReturn(retrievePnrBooking);
		when( bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, required)).thenReturn(booking);
		when(dtoConverter.convertToBookingDTO(booking, loginInfo)).thenReturn(flightBookingDTO);
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		doNothing().when(maskHelper).mask(anyObject());
		PassengerDetailsResponseDTO passengerDetailsResponseDTO=passengerDetailsBusinessImpl.updatePaxDetails(loginInfo, requestDTO, required);
		Assert.assertEquals("IU123", passengerDetailsResponseDTO.getBooking().getOneARloc());
	}
	
	@Test
	public void test2() throws BusinessBaseException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("LAIN");
		loginInfo.setLoginType("E");
		UpdatePassengerDetailsRequestDTO requestDTO=new UpdatePassengerDetailsRequestDTO();
		requestDTO.setRloc("IU123");
		RetrievePnrBooking retrievePnrBooking=new RetrievePnrBooking();
		Booking booking=new Booking();
		Segment segment = new Segment();
		segment.setSegmentStatus(new SegmentStatus());
		segment.getSegmentStatus().setFlown(false);
		segment.setMarketCompany("CX");
		booking.setSegments(new ArrayList<>());
		booking.getSegments().add(segment);	
		FlightBookingDTO flightBookingDTO=new FlightBookingDTO();
		flightBookingDTO.setOneARloc("IU123");
		when(validateHelper.validate(anyObject(), anyObject())).thenReturn(null);
		when(pnrInvokeService.retrievePnrByRloc("IU123")).thenReturn(retrievePnrBooking);
		when(updatePassengerService.updatePassenger(anyObject(), anyObject(), anyObject(), anyObject(), anyObject())).thenReturn(retrievePnrBooking);
		when( bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, required)).thenReturn(booking);
		when(dtoConverter.convertToBookingDTO(booking, loginInfo)).thenReturn(flightBookingDTO);
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		doNothing().when(maskHelper).mask(anyObject());
		PassengerDetailsResponseDTO passengerDetailsResponseDTO=passengerDetailsBusinessImpl.updatePaxDetails(loginInfo, requestDTO, required);
		Assert.assertEquals("IU123", passengerDetailsResponseDTO.getBooking().getOneARloc());
	}
	
	@Test
	public void test_updateValidation() throws BusinessBaseException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("LAIN");
		loginInfo.setLoginType("E");
		UpdatePassengerDetailsRequestDTO requestDTO=new UpdatePassengerDetailsRequestDTO();
		requestDTO.setRloc("IU123");
		RetrievePnrBooking retrievePnrBooking=new RetrievePnrBooking();
		Booking booking=new Booking();
		Segment segment = new Segment();
		segment.setSegmentStatus(new SegmentStatus());
		segment.getSegmentStatus().setFlown(false);
		segment.setMarketCompany("BA");
		booking.setSegments(new ArrayList<>());
		booking.getSegments().add(segment);	
		booking.setOneARloc("IU123");
		FlightBookingDTO flightBookingDTO=new FlightBookingDTO();
		flightBookingDTO.setOneARloc("IU123");
		when(pnrInvokeService.retrievePnrByRloc("IU123")).thenReturn(retrievePnrBooking);
		when(validateHelper.validate(anyObject(), anyObject())).thenReturn(null);
		when(updatePassengerService.updatePassenger(anyObject(), anyObject(), anyObject(), anyObject(), anyObject())).thenReturn(retrievePnrBooking);
		when( bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, required)).thenReturn(booking);
		when(dtoConverter.convertToBookingDTO(booking, loginInfo)).thenReturn(flightBookingDTO);
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		doNothing().when(maskHelper).mask(anyObject());
		thrown.expect(com.cathaypacific.mbcommon.exception.ExpectedException.class);
		thrown.expectMessage("There is no any upcoming sector operated or marketed by CX/KA in booking:IU123, cannot update passenger info");
		passengerDetailsBusinessImpl.updatePaxDetails(loginInfo, requestDTO, required);
	}

}
