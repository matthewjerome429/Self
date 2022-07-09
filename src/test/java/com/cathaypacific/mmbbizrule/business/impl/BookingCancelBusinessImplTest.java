package com.cathaypacific.mmbbizrule.business.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.RetrievePnrBusiness;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.cancelbookingeligibility.response.CancelBookingEligibilityError;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.cancelbookingeligibility.response.CancelBookingEligibilityResponse;
import com.cathaypacific.mmbbizrule.cxservice.ibe.service.IBEService;
import com.cathaypacific.mmbbizrule.cxservice.sentemail.service.FIFBelowService;
import com.cathaypacific.mmbbizrule.dto.response.bookingcancelcheck.BookingCancelCheckResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcancelcheck.CancelFlow;
import com.cathaypacific.mmbbizrule.dto.response.email.FIFBelowReponseDTO;
import com.cathaypacific.mmbbizrule.handler.BookingBuildHelper;
import com.cathaypacific.mmbbizrule.handler.FlightBookingConverterHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.ContactInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Email;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatSelection;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookingCerateInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
@RunWith(MockitoJUnitRunner.class)
public class BookingCancelBusinessImplTest {
	@InjectMocks
	BookingCancelBusinessImpl bookingCancelBusinessImpl;

	@Mock
	private RetrievePnrBusiness retrievePnrBusiness;
	
	@Mock
	private DeletePnrService deletePnrService;
	
	@Mock
	private PnrInvokeService pnrInvokeService;
	
	@Mock
	private BookingBuildHelper bookingBuildHelper;
	
	@Mock
	private FlightBookingConverterHelper flightBookingConverterHelper;
	
	@Mock
	private FIFBelowService fifBlowService;
	
	@Mock
	private IBEService ibeService;
	
	LoginInfo loginInfo;
	
	Booking booking;
	
	FIFBelowReponseDTO fifBelowReponse;
	
	private String rloc;
	
	@Before
	public void setUp() throws BusinessBaseException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		
		Field ibeCancelBookingCheckedInErrorCodeField = BookingCancelBusinessImpl.class.getDeclaredField("ibeCancelBookingCheckedInErrorCode");
		ibeCancelBookingCheckedInErrorCodeField.setAccessible(true);
		ibeCancelBookingCheckedInErrorCodeField.set(bookingCancelBusinessImpl, "IBE_REF00008_S007");
		
		Field ibeCheckActiveField = BookingCancelBusinessImpl.class.getDeclaredField("ibeCheckActive");
		ibeCheckActiveField.setAccessible(true);
		ibeCheckActiveField.set(bookingCancelBusinessImpl, true);
		
		rloc = "LE5XOE";
		
		loginInfo=new LoginInfo();
		loginInfo.setMmbToken("qwertyu");
		
		booking =new Booking();
		booking.setOneARloc("LE5XOE");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();    
		calendar.setTime(date);    
		calendar.add(Calendar.MONTH, 1);
		calendar.getTime();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sf.format(calendar.getTime());
		RetrievePnrBookingCerateInfo bookingCreateInfo = new RetrievePnrBookingCerateInfo();
		bookingCreateInfo.setRpOfficeId("AKLHF2105");
		bookingCreateInfo.setCreateTime(sf.format(calendar.getTime()).toString());
		bookingCreateInfo.setCreateDate("191118");
		booking.setBookingCreateInfo(bookingCreateInfo);
		booking.setRedemptionBooking(false);
		booking.setHasFqtu(false);
		
		//passengers
		List<Passenger> passengers = new ArrayList<>();
		Passenger passenger = new Passenger();
		passenger.setFamilyName("CHEUNG");
		passenger.setGivenName("SAU YI WINNIE");
		passenger.setParentId("1");
		passenger.setTitle("ms");
		passenger.setPassengerId("1");
		passenger.setLoginMember(true);
		//contactInfo
		ContactInfo contactInfo = new ContactInfo();
		List<Email> emails =new ArrayList<>();
		Email email = new Email();
		email.setEmailAddress("fangfang_zhang@cathaypacific.com");
		email.setType("CT");
		emails.add(email);
		contactInfo.setEmail(email);
		contactInfo.setNotificationEmails(emails);
		passenger.setContactInfo(contactInfo);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		
		//segments
		List<Segment> segments = new ArrayList<>();
		Segment segment=new Segment();
		segment.setSegmentID("1");
		DepartureArrivalTime departureTime =new DepartureArrivalTime();
		SegmentStatus segmentStatus = new SegmentStatus();
		segmentStatus.setPnrStatus("UN");
		segmentStatus.setFlown(false);
		segment.setSegmentStatus(segmentStatus);
		
		departureTime.setRtfsActualTime(sf.format(calendar.getTime()).toString());
		departureTime.setPnrTime(sf.format(calendar.getTime()).toString());
		departureTime.setTimeZoneOffset("+0800");
		segment.setDepartureTime(departureTime);
		segment.setArrivalTime(departureTime);
		segment.setCabinClass("J");
		segment.setOriginPort("HKG");
		segment.setDestPort("TPE");
		segment.setOperateCompany("CX");
		segment.setMarketCabinClass("J");
		segment.setSubClass("U");
		segment.setOperateSegmentNumber("564");
		segments.add(segment);
		booking.setSegments(segments);

		//passengerSegments
		List<PassengerSegment> passengerSegments = new ArrayList<>();
		PassengerSegment passengerSegment=new PassengerSegment();
		passengerSegment.setSegmentId("1");
		passengerSegment.setPassengerId("1");
		FQTVInfo fQTVInfo = new FQTVInfo();
		fQTVInfo.setCompanyId("CX");
		fQTVInfo.setTierLevel("MPO");
		fQTVInfo.setMembershipNumber("1910026122");
		
		passengerSegment.setFqtvInfo(fQTVInfo);
		SeatSelection seatSelection = new SeatSelection();
		seatSelection.setEligible(true);
		seatSelection.setXlFOC(false);
		passengerSegment.setMmbSeatSelection(seatSelection);
		passengerSegments.add(passengerSegment);
		booking.setPassengerSegments(passengerSegments);
		fifBelowReponse =  new FIFBelowReponseDTO();
		fifBelowReponse.setSuccess(true);
	}
	
	@Test
	public void checkBookingCanCancel_MMB_case1() throws BusinessBaseException,ParseException {
		
		booking.getSegments().get(0).getSegmentStatus().setPnrStatus("UN");
		when(retrievePnrBusiness.retrieveFlightBooking(anyObject(),anyObject(),anyObject())).thenReturn(booking);
		when(bookingBuildHelper.isCBWLBooking(anyObject())).thenReturn(true);
		when(fifBlowService.sentEmail(anyObject())).thenReturn(fifBelowReponse);
		when(pnrInvokeService.retrievePnrReplyByOneARloc(rloc)).thenReturn(new PNRReply());
		when(flightBookingConverterHelper.getFlightBooking(anyObject(), anyObject(), anyObject(),anyBoolean())).thenReturn(booking);
		when(ibeService.checkCancelBookingEligibility(anyObject())).thenReturn(null);
		BookingCancelCheckResponseDTO response = bookingCancelBusinessImpl.checkBookingCanCancel(rloc, loginInfo, false);
		
		assertEquals(CancelFlow.MMB,response.getSuggestedCancelflow());
			
	}
	
	
	@Test
	public void checkBookingCanCancel_MMB_case2() throws BusinessBaseException,ParseException {
		CancelBookingEligibilityResponse ibeResponse = new CancelBookingEligibilityResponse();
		ibeResponse.setFlag(false);
		booking.getSegments().get(0).getSegmentStatus().setPnrStatus("HK");
		when(retrievePnrBusiness.retrieveFlightBooking(anyObject(),anyObject(),anyObject())).thenReturn(booking);
		when(bookingBuildHelper.isCBWLBooking(anyObject())).thenReturn(true);
		when(fifBlowService.sentEmail(anyObject())).thenReturn(fifBelowReponse);
		when(pnrInvokeService.retrievePnrReplyByOneARloc(rloc)).thenReturn(new PNRReply());
		when(flightBookingConverterHelper.getFlightBooking(anyObject(), anyObject(), anyObject(),anyBoolean())).thenReturn(booking);
		
		when(ibeService.checkCancelBookingEligibility(anyObject())).thenReturn(ibeResponse);
		
		BookingCancelCheckResponseDTO response = bookingCancelBusinessImpl.checkBookingCanCancel(rloc, loginInfo, false);
		
		assertEquals(CancelFlow.MMB, response.getSuggestedCancelflow());
			
	}

	@Test
	public void checkBookingCanCancel_IBE_case1() throws BusinessBaseException,ParseException {
		CancelBookingEligibilityResponse ibeResponse = new CancelBookingEligibilityResponse();
		ibeResponse.setFlag(true);
		booking.getSegments().get(0).getSegmentStatus().setPnrStatus("HK");
		when(retrievePnrBusiness.retrieveFlightBooking(anyObject(),anyObject(),anyObject())).thenReturn(booking);
		when(bookingBuildHelper.isCBWLBooking(anyObject())).thenReturn(true);
		when(fifBlowService.sentEmail(anyObject())).thenReturn(fifBelowReponse);
		when(pnrInvokeService.retrievePnrReplyByOneARloc(rloc)).thenReturn(new PNRReply());
		when(flightBookingConverterHelper.getFlightBooking(anyObject(), anyObject(), anyObject(),anyBoolean())).thenReturn(booking);
		
		when(ibeService.checkCancelBookingEligibility(anyObject())).thenReturn(ibeResponse);
		
		BookingCancelCheckResponseDTO response = bookingCancelBusinessImpl.checkBookingCanCancel(rloc, loginInfo, false);
		
		assertEquals(CancelFlow.IBE, response.getSuggestedCancelflow());
			
	}
	
	@Test
	public void checkBookingCanCancel_IBE_case2() throws BusinessBaseException,ParseException {
		CancelBookingEligibilityResponse ibeResponse = new CancelBookingEligibilityResponse();
		CancelBookingEligibilityError error = new CancelBookingEligibilityError();
		error.setCode("IBE_REF00008_S007");
		ibeResponse.setErrors(Arrays.asList(error));
		ibeResponse.setFlag(false);
		booking.getSegments().get(0).getSegmentStatus().setPnrStatus("HK");
		booking.getPassengerSegments().get(0).setCheckedIn(true);
		when(retrievePnrBusiness.retrieveFlightBooking(anyObject(),anyObject(),anyObject())).thenReturn(booking);
		when(bookingBuildHelper.isCBWLBooking(anyObject())).thenReturn(true);
		when(fifBlowService.sentEmail(anyObject())).thenReturn(fifBelowReponse);
		when(pnrInvokeService.retrievePnrReplyByOneARloc(rloc)).thenReturn(new PNRReply());
		when(flightBookingConverterHelper.getFlightBooking(anyObject(), anyObject(), anyObject(),anyBoolean())).thenReturn(booking);
		
		when(ibeService.checkCancelBookingEligibility(anyObject())).thenReturn(ibeResponse);
		
		BookingCancelCheckResponseDTO response = bookingCancelBusinessImpl.checkBookingCanCancel(rloc, loginInfo, false);
		
		assertEquals(CancelFlow.IBE, response.getSuggestedCancelflow());
			
	}
 
}
