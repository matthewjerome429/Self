package com.cathaypacific.mmbbizrule.business.impl;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.common.UnlockedPaxInfo;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mmbbizrule.BaseTest;
import com.cathaypacific.mmbbizrule.business.MemberProfileBusiness;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.cxservice.oj.service.OJBookingService;
import com.cathaypacific.mmbbizrule.dto.common.booking.ContactInfoDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EmailDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.PassengerDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.PassengerSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.TravelDocDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.TravelDocsDTO;
import com.cathaypacific.mmbbizrule.dto.request.retrievepnr.RetrievePnrByRlocMemberRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ConsentInfoRecordResponseDTO;
import com.cathaypacific.mmbbizrule.handler.DTOConverter;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.ContactInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Email;
import com.cathaypacific.mmbbizrule.model.booking.detail.EmrContactInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PhoneInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.TravelDoc;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.service.impl.ConsentInfoServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class RetrievePnrBusinessImplTest extends BaseTest {

	@InjectMocks
	RetrievePnrBusinessImpl retrievePnrBusinessImpl;
	
	@Mock
	PaxNameIdentificationService paxNameIdentificationService;
	
	@Mock
	BookingBuildService bookingBuildService;
	
	@Mock
	private OJBookingService ojBookingService;
	
	@Mock
	PnrInvokeService pnrInvokeService;
	
	@Mock
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Mock
	BizRuleConfig bizRuleConfig;
	
	@Mock
	TicketProcessInvokeService ticketProcessInvokeService;
	
	@Mock
	ConsentInfoServiceImpl consentInfoService;
	
	@Mock
	DTOConverter dtoConverter;
	
	@Mock
	private MemberProfileBusiness memberProfileBusiness;

	private Booking booking;
	private String rloc;
	private RetrievePnrBooking retrievePnrBooking;
	private String familyName;
	private String givenName;
	private String eticket;
	private RetrievePnrByRlocMemberRequestDTO receivePnrByRlocResponseDTO;
	private FlightBookingDTO flightBookingDTO;
	private BookingBuildRequired required;

	@Before
	public void setUp(){
		rloc     = "MNOWGI";
		Boolean canCheckIn = true;
		eticket   = "ABDCDR123456";
		familyName = "dongdong";
		givenName  = "Qin";
		Passenger passenger = new Passenger();
		ContactInfo contactInfo = new ContactInfo();
		Email  email = new Email();
		PhoneInfo phoneInfo = new PhoneInfo();
		
		phoneInfo.setPhoneNo("123456789");
		email.setEmailAddress("dongdong@163.com");
		contactInfo.setEmail(email);
		contactInfo.setPhoneInfo(phoneInfo);
		passenger.setContactInfo(contactInfo);
		passenger.setGivenName("Dongdong");
		passenger.setFamilyName("Qin");
		passenger.setPrimaryPassenger(true);
		passenger.setPassengerId("1234567");
		List<TravelDoc> priTravelDocs = new ArrayList<>();
	
		TravelDoc travelDoc = new TravelDoc();
		travelDoc.setTravelDocumentNumber("89898989");
		priTravelDocs.add(travelDoc);
		passenger.setPriTravelDocs(priTravelDocs);
		
		List<TravelDoc> secTravelDocs = new ArrayList<>();
		TravelDoc travelDoc1 = new TravelDoc();
		travelDoc1.setTravelDocumentNumber("12121212");
		secTravelDocs.add(travelDoc1);
		passenger.setSecTravelDocs(secTravelDocs);
		
		EmrContactInfo emrContactInfo = new EmrContactInfo();
		emrContactInfo.setPhoneNumber("123456789");
		passenger.setEmrContactInfo(emrContactInfo);
		
		List<Passenger> passengers = new ArrayList<>();
		passengers.add(passenger);
		
		booking = new Booking();
		booking.setPassengers(passengers);
		booking.setCanCheckIn(canCheckIn);
		booking.setOneARloc(rloc);
		
		retrievePnrBooking = new RetrievePnrBooking();
		List<RetrievePnrPassenger> retrievePnrPassengerList = new ArrayList<>();
		RetrievePnrPassenger retrievePnrPassenger = new RetrievePnrPassenger();
		retrievePnrPassenger.setPassengerID("234567");
		retrievePnrPassengerList.add(retrievePnrPassenger);
		retrievePnrBooking.setPassengers(retrievePnrPassengerList);
		receivePnrByRlocResponseDTO = new RetrievePnrByRlocMemberRequestDTO();
		receivePnrByRlocResponseDTO.setRloc("MNOWGI");
		
		flightBookingDTO = new FlightBookingDTO();
		List<PassengerDTO> passenger2 = new ArrayList<>();
		PassengerDTO passengerDTO = new PassengerDTO();
		passengerDTO.setPassengerId("234567");
		ContactInfoDTO contactDTO = new ContactInfoDTO();
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.setEmail("dongdong@163.com");
		contactDTO.setEmail(emailDTO);
		passengerDTO.setContactInfo(contactDTO);
		passengerDTO.setPrimaryPassenger(true);
		passenger2.add(passengerDTO);
		flightBookingDTO.setPassengers(passenger2);
		List<PassengerSegmentDTO> passengerSegments=new ArrayList<>();
		PassengerSegmentDTO passengerSegment=new PassengerSegmentDTO();
		TravelDocsDTO travelDocTs=new TravelDocsDTO();
		TravelDocDTO priTravelDoc =new TravelDocDTO();
		priTravelDoc.setCompanyId("CX");
		priTravelDoc.setBirthDateDay("");
		priTravelDoc.setBirthDateYear("");
		priTravelDoc.setFamilyName(familyName);
		priTravelDoc.setGivenName(givenName);
		travelDocTs.setPriTravelDoc(priTravelDoc);
		
		TravelDocDTO secTravelDoc =new TravelDocDTO();
		secTravelDoc.setCompanyId("CX");
		secTravelDoc.setBirthDateDay("");
		secTravelDoc.setBirthDateYear("");
		secTravelDoc.setGivenName(givenName);
		secTravelDoc.setFamilyName(familyName);
		travelDocTs.setSecTravelDoc(secTravelDoc);
		passengerSegment.setPassengerId("23456");
		passengerSegment.setEticket("147");
		passengerSegment.setTravelDoc(travelDocTs);
		passengerSegments.add(passengerSegment);
		flightBookingDTO.setPassengerSegments(passengerSegments);
		flightBookingDTO.setOneARloc("MNOWGI");
		
		required = new BookingBuildRequired();
	}
	
	@Test
	public void markRetrievePnrByRlocTest() throws BusinessBaseException {
		when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(retrievePnrBooking);
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(anyObject(), anyObject());
		when(bookingBuildService.buildBooking(anyObject(), anyObject(), anyObject())).thenReturn(booking);
		when(dtoConverter.convertToBookingDTO(anyObject(),anyObject())).thenReturn(flightBookingDTO);
		/*ReceivePnrByRlocResponseDTO receivePnrByRlocResponseDTO = retrievePnrBusinessImpl.bookingLoginByReference(rloc, familyName, givenName, "MMCD12345", required);
		FlightBookingDTO  flightBookingDTO = (FlightBookingDTO) receivePnrByRlocResponseDTO.getBooking();
		Assert.assertEquals("MNOWGI", flightBookingDTO.getOneARloc());
		List<PassengerDTO> passengers = flightBookingDTO.getPassengers();
		Assert.assertEquals("dongdong@●●●●●●●●●●", passengers.get(0).getContactInfo().getEmail().getEmail());*/
	}
	
	@Test
	public void marketRetrievePnrByEticketTest() throws Exception  {
		retrievePnrBooking.getPassengers().get(0).setPrimaryPassenger(true);
		retrievePnrBooking.getPassengers().get(0).setPassengerID(booking.getPassengers().get(0).getPassengerId());
		retrievePnrBooking.setOneARloc(booking.getOneARloc());
		List<UnlockedPaxInfo> UnlockedPaxInfos = new ArrayList<>();
		UnlockedPaxInfo unlockedPaxInfo = new UnlockedPaxInfo();
		unlockedPaxInfo.setPassengerId(retrievePnrBooking.getPassengers().get(0).getPassengerID());
		UnlockedPaxInfos.add(unlockedPaxInfo);
		when(mbTokenCacheRepository.get(any(), any(), any(), any())).thenReturn(UnlockedPaxInfos).thenReturn(UnlockedPaxInfos);
		when(ticketProcessInvokeService.getRlocByEticket(eticket)).thenReturn(rloc);
		when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(retrievePnrBooking);
		when(dtoConverter.convertToBookingDTO(anyObject(), anyObject())).thenReturn(flightBookingDTO);
		doNothing().when(paxNameIdentificationService).primaryPaxIdentificationForRloc(familyName, givenName, retrievePnrBooking);
		when(bookingBuildService.buildBooking(anyObject(), anyObject(), anyObject())).thenReturn(booking);
//		ReceivePnrByEticketResponseDTO receivePnrByEticketResponseDTO = retrievePnrBusinessImpl.bookingLoginByEticket(familyName, givenName, eticket, "MMCD12345", required);
//		FlightBookingDTO flightBookingDTO = (FlightBookingDTO) receivePnrByEticketResponseDTO.getBooking();
//		Assert.assertEquals("MNOWGI", flightBookingDTO.getOneARloc());
//		List<PassengerDTO> passengers = flightBookingDTO.getPassengers();
//		Assert.assertEquals("dongdong@163.com", passengers.get(0).getContactInfo().getEmail().getEmail());

	}
	
	@Test
	public void markRetrievePnrByRlocForMemberTest() throws BusinessBaseException, ParseException {	
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginType("M");
		retrievePnrBooking.getPassengers().get(0).setPrimaryPassenger(true);
		retrievePnrBooking.getPassengers().get(0).setPassengerID(booking.getPassengers().get(0).getPassengerId());
		retrievePnrBooking.setOneARloc(booking.getOneARloc());
		
		List<UnlockedPaxInfo> unlockedPaxInfos = new ArrayList<>();
		UnlockedPaxInfo unlockedPaxInfo = new UnlockedPaxInfo();
		unlockedPaxInfo.setUnlocked(true);
		unlockedPaxInfo.setPassengerId(retrievePnrBooking.getPassengers().get(0).getPassengerID());
		unlockedPaxInfos.add(unlockedPaxInfo);

		when(mbTokenCacheRepository.get(any(), any(), any(), any())).thenReturn(unlockedPaxInfos).thenReturn(unlockedPaxInfos);
		when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(retrievePnrBooking);
		when(dtoConverter.convertToBookingDTO(anyObject(), anyObject())).thenReturn(flightBookingDTO);
		when(bookingBuildService.buildBooking(anyObject(), anyObject(), anyObject())).thenReturn(booking);
		doNothing().when(paxNameIdentificationService).primaryPaxIdentificationForRloc(familyName, givenName, retrievePnrBooking);

//		RefreshBookingResponseDTO  receivePnrByMemeberId =  retrievePnrBusinessImpl.refreshBooking(loginInfo, rloc, required);
//		BookingDTO  bookingDTO =   receivePnrByMemeberId.getBooking();

//		Assert.assertEquals("MNOWGI",bookingDTO.getOneARloc());
	}
	
	@Test
	public void consentInfoRecordTest() throws BusinessBaseException{
		LoginInfo loginInfo = new LoginInfo();
		String rloc = "6PZN5Y";
		String acceptLanguage = "en-US";
		RetrievePnrPassenger retrievePnrPassenger = new RetrievePnrPassenger();
		retrievePnrPassenger.setFamilyName("QIN");
		retrievePnrPassenger.setGivenName("Dongndong");
		retrievePnrPassenger.setPassengerID("1234567");
		retrievePnrBooking.setOneARloc(rloc);
		ConsentInfoRecordResponseDTO recordResponse = new ConsentInfoRecordResponseDTO();
		recordResponse.setConsentInfoRecord(true);
		
		when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(retrievePnrBooking);
		when(consentInfoService.saveConsentInfo(retrievePnrBooking, loginInfo, rloc, acceptLanguage)).thenReturn(recordResponse);
		ConsentInfoRecordResponseDTO recordResponse2 = consentInfoService.saveConsentInfo(retrievePnrBooking, loginInfo, rloc, acceptLanguage);
		Assert.assertEquals(true, recordResponse2.isConsentInfoRecord());
	}
	
	@Test
	public void bookingLoginByReferenceTest() throws BusinessBaseException{
		String rloc="LNSL69"; 
		String familyName="TEST"; 
		String givenName="L IVAN";
		String unAuthMbToken="2142C04DEF4443489E50D00390DF899D";
		doNothing().when(mbTokenCacheRepository).add(Matchers.any(),Matchers.any(),Matchers.any(),Matchers.any());
		when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(retrievePnrBooking);
		when(dtoConverter.convertToBookingDTO(anyObject(),anyObject())).thenReturn(flightBookingDTO);
		/*ReceivePnrByRlocResponseDTO receivePnrByRlocResponseDTO=retrievePnrBusinessImpl.bookingLoginByReference(rloc, familyName, givenName, unAuthMbToken, required);
		Assert.assertEquals("MNOWGI", receivePnrByRlocResponseDTO.getBooking().getOneARloc());
		Assert.assertEquals("2142C04DEF4443489E50D00390DF899D", receivePnrByRlocResponseDTO.getMmbToken());
		Assert.assertTrue(((FlightBookingDTO)receivePnrByRlocResponseDTO.getBooking()).isMandatoryContactInfo());*/
	}
	
	@Test
	public void retrieveFlightBookingTest() throws BusinessBaseException{
		LoginInfo loginInfo=new  LoginInfo();
		loginInfo.setLoginRloc("HJKLA");
		loginInfo.setLoginType("R");
		String rloc="HJKLA";
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		when(bookingBuildService.buildBooking(anyObject(), anyObject(), anyObject())).thenReturn(booking);
		when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(retrievePnrBooking);
		when(pnrInvokeService.retrievePnrByRloc(loginInfo.getLoginRloc())).thenReturn(retrievePnrBooking);

		Booking booking=retrievePnrBusinessImpl.retrieveFlightBooking(loginInfo, rloc, required);
		
		Assert.assertEquals("89898989", booking.getPassengers().get(0).getPriTravelDocs().get(0).getTravelDocumentNumber());
		Assert.assertEquals("12121212", booking.getPassengers().get(0).getSecTravelDocs().get(0).getTravelDocumentNumber());
		Assert.assertEquals("123456789", booking.getPassengers().get(0).getEmrContactInfo().getPhoneNumber());
		Assert.assertEquals("Dongdong", booking.getPassengers().get(0).getGivenName());
	}
	
	@Test
	public void consentInfoRecordTest1() throws BusinessBaseException{
		LoginInfo loginInfo=new LoginInfo();
		String rloc="BNMl"; 
		String acceptLanguage="en";
		ConsentInfoRecordResponseDTO recordResponse = new ConsentInfoRecordResponseDTO();
		recordResponse.setConsentInfoRecord(true);
		when(consentInfoService.saveConsentInfo(retrievePnrBooking, loginInfo, rloc, acceptLanguage)).thenReturn(recordResponse);
		when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(retrievePnrBooking);
		ConsentInfoRecordResponseDTO consentInfoRecordResponseDTO=retrievePnrBusinessImpl.consentInfoRecord(loginInfo, rloc, acceptLanguage);
		Assert.assertEquals(true,consentInfoRecordResponseDTO.isConsentInfoRecord());
	}
	
	@Test
	public void unlockPnrByEticketTest() throws BusinessBaseException{
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginType("M");
		String rloc="BNMl"; 
		String eticket="147";
		String passengerId="23456";
		ConsentInfoRecordResponseDTO recordResponse = new ConsentInfoRecordResponseDTO();
		recordResponse.setConsentInfoRecord(true);
		//when(consentInfoService.saveConsentInfo(retrievePnrBooking, loginInfo, rloc, acceptLanguage)).thenReturn(recordResponse);
		when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(retrievePnrBooking);
		when(dtoConverter.convertToBookingDTO(anyObject(),anyObject())).thenReturn(flightBookingDTO);
		/*RefreshBookingResponseDTO refreshBookingResponseDTO=retrievePnrBusinessImpl.unlockPnrByEticket(loginInfo, eticket, rloc, passengerId, required);
		Assert.assertEquals("MNOWGI",refreshBookingResponseDTO.getBooking().getOneARloc());*/
	}
	
	
	@Test
	public void unlockPnrByEticketTest2() throws BusinessBaseException{
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginType("M");
		String rloc="BNMl"; 
		String eticket="147";
		String passengerId="2345";
		ConsentInfoRecordResponseDTO recordResponse = new ConsentInfoRecordResponseDTO();
		recordResponse.setConsentInfoRecord(true);
		when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(retrievePnrBooking);
		when(dtoConverter.convertToBookingDTO(anyObject(),anyObject())).thenReturn(flightBookingDTO);
		 Throwable t = null; 
//		 try{
//			 retrievePnrBusinessImpl.unlockPnrByEticket(loginInfo, eticket, rloc, passengerId, required);
//			}catch(Exception ex){
//				t=ex;
//				 assertNotNull(t);  
//				    assertTrue(t instanceof com.cathaypacific.mbcommon.exception.ExpectedException);  
//				    assertTrue(t.getMessage().contains("cannot find match passenger with passenger Id [2345], under booking [MNOWGI]")); 
//			}
	}
	
	@Test
	public void markRetrievePnrByRlocForMemberTest1() throws BusinessBaseException {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginType("R");
		loginInfo.setLoginRloc("147256");
		retrievePnrBooking.getPassengers().get(0).setPrimaryPassenger(true);
		retrievePnrBooking.getPassengers().get(0).setPassengerID(booking.getPassengers().get(0).getPassengerId());
		retrievePnrBooking.setOneARloc(booking.getOneARloc());
		
		List<UnlockedPaxInfo> unlockedPaxInfos = new ArrayList<>();
		UnlockedPaxInfo unlockedPaxInfo = new UnlockedPaxInfo();
		unlockedPaxInfo.setUnlocked(true);
		unlockedPaxInfo.setPassengerId(retrievePnrBooking.getPassengers().get(0).getPassengerID());
		unlockedPaxInfos.add(unlockedPaxInfo);
		when(pnrInvokeService.retrievePnrByRloc(loginInfo.getLoginRloc())).thenReturn(retrievePnrBooking);
		when(mbTokenCacheRepository.get(any(), any(), any(), any())).thenReturn(unlockedPaxInfos).thenReturn(unlockedPaxInfos);
		when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(retrievePnrBooking);
		when(dtoConverter.convertToBookingDTO(anyObject(), anyObject())).thenReturn(flightBookingDTO);
		when(bookingBuildService.buildBooking(anyObject(), anyObject(), anyObject())).thenReturn(booking);
		doNothing().when(paxNameIdentificationService).primaryPaxIdentificationForRloc(familyName, givenName, retrievePnrBooking);

		/*RefreshBookingResponseDTO  receivePnrByMemeberId =  retrievePnrBusinessImpl.refreshBooking(loginInfo, rloc, required);
		BookingDTO  bookingDTO =   receivePnrByMemeberId.getBooking();
		
		Assert.assertEquals("MNOWGI",bookingDTO.getOneARloc());*/
	}
	
}