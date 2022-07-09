package com.cathaypacific.mmbbizrule.business.impl;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.cathaypacific.mbcommon.enums.staff.StaffBookingType;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mmbbizrule.business.RetrievePnrBusiness;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.updateseat.UpdateSeatResponseDTO;
import com.cathaypacific.mmbbizrule.handler.DTOConverter;
import com.cathaypacific.mmbbizrule.handler.FlightBookingConverterHelper;
import com.cathaypacific.mmbbizrule.handler.MaskHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrStaffDetail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.KeepSeatPaymentService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.service.UpdateSeatService;

@RunWith(MockitoJUnitRunner.class)
public class UpdateSeatBusinessImplTest {
	
	@InjectMocks
	UpdateSeatBusinessImpl updateSeatBusinessImpl;
	
	@Mock
	UpdateSeatService updateSeatService;
	
	@Mock
	private RetrievePnrBusiness retrievePnrBusiness;
	
	@Mock
	private PnrInvokeService pnrInvokeService;
	
	@Mock
	private BookingBuildService bookingBuildService;
	
	@Mock
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Mock
	private KeepSeatPaymentService keepSeatPaymentService;
	
	@Mock
	private DTOConverter dtoConverter;
	
	@Mock
	private FlightBookingConverterHelper flightBookingConverterHelper;
	
	@Mock
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Mock
	private MaskHelper maskHelper;
	
	private BookingBuildRequired required;
	
	@Before
	public void setUp(){
		required = new BookingBuildRequired();
	}

    @Test
	public void test() throws BusinessBaseException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginRloc("OI14785");
		loginInfo.setLoginType("M");
		UpdateSeatRequestDTO requestDTO=new UpdateSeatRequestDTO();
		requestDTO.setRloc("OI14785");
		Booking booking=new Booking();
		booking.setOneARloc("OI14785");
		RetrievePnrBooking pnrBookingBefore=new RetrievePnrBooking();
		RetrievePnrBooking pnrBookingAfter=new RetrievePnrBooking();
		Booking bookingBefore=new Booking();
		FlightBookingDTO booking1=new FlightBookingDTO();
		booking1.setOneARloc("TY789");
		Booking bookingAfter=new Booking();
		Booking booking2=new Booking();

		Passenger passenger =new Passenger();
		RetrievePnrStaffDetail retrievePnrStaffDetail =new RetrievePnrStaffDetail();
		retrievePnrStaffDetail.setPriority("10");
		retrievePnrStaffDetail.setType(StaffBookingType.INDUSTRY_DISCOUNT);
		passenger.setStaffDetail(retrievePnrStaffDetail);
		bookingBefore.getPassengers().add(passenger);
		when(retrievePnrBusiness.retrieveFlightBooking(loginInfo, requestDTO.getRloc(), required)).thenReturn(bookingBefore);
		when(pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc())).thenReturn(pnrBookingBefore);
		when(bookingBuildService.buildBooking(pnrBookingAfter, loginInfo, required)).thenReturn(bookingBefore);
		when(updateSeatService.updateSeat(requestDTO, loginInfo, bookingBefore)).thenReturn(pnrBookingAfter);

		when(dtoConverter.convertToBookingDTO(bookingBefore, loginInfo)).thenReturn(booking1);		
		doNothing().when(maskHelper).mask(anyObject());
		when(keepSeatPaymentService.keepSeatPayment(bookingBefore, bookingAfter)).thenReturn(booking2);
		when(dtoConverter.convertToBookingDTO(booking2, loginInfo)).thenReturn(booking1);
		when(flightBookingConverterHelper.flightBookingDTOConverter(anyObject(), anyObject(), anyObject(), anyBoolean())).thenReturn(booking1);
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(loginInfo, pnrBookingAfter);
		
		UpdateSeatResponseDTO updateSeatResponseDTO=updateSeatBusinessImpl.updateSeat(loginInfo, requestDTO);
		Assert.assertEquals("TY789", updateSeatResponseDTO.getBooking().getOneARloc());
	}
    
    @Test
	public void test1() throws BusinessBaseException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginRloc("OI14785");
		loginInfo.setLoginType("R");
		UpdateSeatRequestDTO requestDTO=new UpdateSeatRequestDTO();
		requestDTO.setRloc("OI14785");
		Booking booking=new Booking();
		booking.setOneARloc("OI14785");
		RetrievePnrBooking pnrBookingBefore=new RetrievePnrBooking();
		RetrievePnrBooking pnrBookingAfter=new RetrievePnrBooking();
		Booking bookingBefore=new Booking();
		FlightBookingDTO booking1=new FlightBookingDTO();
		booking1.setOneARloc("TY789");
		Booking bookingAfter=new Booking();
		Booking booking2=new Booking();
		Passenger passenger =new Passenger();
		RetrievePnrStaffDetail retrievePnrStaffDetail =new RetrievePnrStaffDetail();
		passenger.setStaffDetail(retrievePnrStaffDetail);
		bookingBefore.getPassengers().add(passenger);
		
		when(retrievePnrBusiness.retrieveFlightBooking(loginInfo, requestDTO.getRloc(), required)).thenReturn(bookingBefore);
		when(pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc())).thenReturn(pnrBookingBefore);
		when(updateSeatService.updateSeat(requestDTO, loginInfo, bookingBefore)).thenReturn(pnrBookingAfter);

		when(dtoConverter.convertToBookingDTO(bookingBefore, loginInfo)).thenReturn(booking1);		
		when(bookingBuildService.buildBooking(pnrBookingAfter, loginInfo, required)).thenReturn(bookingAfter);
		when(keepSeatPaymentService.keepSeatPayment(bookingBefore, bookingAfter)).thenReturn(booking2);
		when(dtoConverter.convertToBookingDTO(booking2, loginInfo)).thenReturn(booking1);
		doNothing().when(maskHelper).mask(anyObject());
		when(flightBookingConverterHelper.flightBookingDTOConverter(anyObject(), anyObject(), anyObject(), anyBoolean())).thenReturn(booking1);
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(loginInfo, pnrBookingAfter);
		UpdateSeatResponseDTO updateSeatResponseDTO=updateSeatBusinessImpl.updateSeat(loginInfo, requestDTO);
		Assert.assertEquals("TY789", updateSeatResponseDTO.getBooking().getOneARloc());
	}
    
	@Test
	public void test2() throws BusinessBaseException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginRloc("OI14785");
		loginInfo.setLoginType("R");
		UpdateSeatRequestDTO requestDTO=new UpdateSeatRequestDTO();
		requestDTO.setRloc("OI14785");
		Booking booking=new Booking();
		booking.setOneARloc("OI14785");
		RetrievePnrBooking pnrBookingBefore=new RetrievePnrBooking();
		RetrievePnrBooking pnrBookingAfter=null;
		Booking bookingBefore=new Booking();
		FlightBookingDTO booking1=new FlightBookingDTO();
		booking1.setOneARloc("HJ456");
		Booking bookingAfter=new Booking();
		bookingAfter.setOneARloc("KL123");
		Passenger passenger =new Passenger();
		RetrievePnrStaffDetail retrievePnrStaffDetail =new RetrievePnrStaffDetail();
		passenger.setStaffDetail(retrievePnrStaffDetail);
		bookingBefore.getPassengers().add(passenger);
		
		when(retrievePnrBusiness.retrieveFlightBooking(loginInfo, requestDTO.getRloc(), required)).thenReturn(bookingBefore);
		when(pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc())).thenReturn(pnrBookingBefore);
		when(updateSeatService.updateSeat(requestDTO, loginInfo, booking)).thenReturn(pnrBookingAfter);
		when(dtoConverter.convertToBookingDTO(bookingBefore, loginInfo)).thenReturn(booking1);
		when(flightBookingConverterHelper.flightBookingDTOConverter(anyObject(), anyObject(), anyObject(), anyBoolean())).thenReturn(booking1);
		when(bookingBuildService.buildBooking(pnrBookingAfter, loginInfo, required)).thenReturn(bookingAfter);
		doNothing().when(maskHelper).mask(anyObject());
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(loginInfo, pnrBookingAfter);
		UpdateSeatResponseDTO updateSeatResponseDTO=updateSeatBusinessImpl.updateSeat(loginInfo, requestDTO);
		Assert.assertEquals("HJ456", updateSeatResponseDTO.getBooking().getOneARloc());
	}
	
	@Test
	public void test3() throws BusinessBaseException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginRloc("OI14785");
		loginInfo.setLoginType("E");
		UpdateSeatRequestDTO requestDTO=new UpdateSeatRequestDTO();
		requestDTO.setRloc("OI14785");
		Booking booking=new Booking();
		booking.setOneARloc("OI14785");
		RetrievePnrBooking pnrBookingBefore=new RetrievePnrBooking();
		RetrievePnrBooking pnrBookingAfter=new RetrievePnrBooking();
		Booking bookingBefore=new Booking();
		FlightBookingDTO booking1=new FlightBookingDTO();
		booking1.setOneARloc("TY789");
		Booking bookingAfter=new Booking();
		Booking booking2=new Booking();
		Passenger passenger =new Passenger();
		RetrievePnrStaffDetail retrievePnrStaffDetail =new RetrievePnrStaffDetail();
		passenger.setStaffDetail(retrievePnrStaffDetail);
		bookingBefore.getPassengers().add(passenger);
		
		when(retrievePnrBusiness.retrieveFlightBooking(loginInfo, requestDTO.getRloc(), required)).thenReturn(bookingBefore);
		when(pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc())).thenReturn(pnrBookingBefore);
		when(updateSeatService.updateSeat(requestDTO, loginInfo, bookingBefore)).thenReturn(pnrBookingAfter);

		when(dtoConverter.convertToBookingDTO(bookingBefore, loginInfo)).thenReturn(booking1);		
		when(bookingBuildService.buildBooking(pnrBookingAfter, loginInfo, required)).thenReturn(bookingAfter);
		when(keepSeatPaymentService.keepSeatPayment(bookingBefore, bookingAfter)).thenReturn(booking2);
		when(dtoConverter.convertToBookingDTO(booking2, loginInfo)).thenReturn(booking1);
		when(flightBookingConverterHelper.flightBookingDTOConverter(anyObject(), anyObject(), anyObject(), anyBoolean())).thenReturn(booking1);
		doNothing().when(maskHelper).mask(anyObject());
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(loginInfo, pnrBookingAfter);
		UpdateSeatResponseDTO updateSeatResponseDTO=updateSeatBusinessImpl.updateSeat(loginInfo, requestDTO);
		Assert.assertEquals("TY789", updateSeatResponseDTO.getBooking().getOneARloc());
	}

}
