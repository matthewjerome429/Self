package com.cathaypacific.mmbbizrule.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.model.ConstantData;
import com.cathaypacific.mmbbizrule.model.booking.summary.TempLinkedBooking;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePreference;
import com.cathaypacific.mmbbizrule.model.profile.ProfileTravelDoc;
import com.cathaypacific.mmbbizrule.model.profile.TravelCompanion;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEmail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEticket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFFPInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSsrSk;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;

@RunWith(MockitoJUnitRunner.class)
public class PaxNameIdentificationServiceImplTest {

	@InjectMocks
	PaxNameIdentificationServiceImpl paxNameIdentificationServiceImpl;

	@Mock
	ConstantDataDAO constantDataDAO;

	@Mock
	ConstantData constantData;

	@Mock
	RetrieveProfileService retrieveProfileService;
	@Mock
	private TempLinkedBookingRepository linkTempBookingRepository;
	
	String passengerID;
	String parentId;
	String passengerType;
	String familyName;
	String givenName;
	String eTicket;
	String segmentId;
	String loginMemberId;
	RetrievePnrBooking booking = new RetrievePnrBooking();
	List<RetrievePnrPassenger> passengers = new ArrayList<>();
	List<RetrievePnrSegment> segments = new ArrayList<>();
	List<RetrievePnrPassengerSegment> passengerSegments = new ArrayList<>();
	RetrievePnrSegment Segment = new RetrievePnrSegment();
	RetrievePnrPassengerSegment passengerSegment = new RetrievePnrPassengerSegment();
	List<RetrievePnrEmail> apEmails = new ArrayList<>();
	RetrievePnrEmail email = new RetrievePnrEmail();

	@Before
	public void setUp() {
		familyName = "TEST";
		givenName = "L Ivan";
		passengerType = "ADT";
		parentId = "1";
		segmentId = "1";
		passengerID = "1";
		eTicket = "987654321";
		email.setEmail("5566@qq.com");
		email.setType("CT");
		apEmails.add(email);
		loginMemberId = "1910026122";
		booking = new RetrievePnrBooking();
		List<RetrievePnrPassengerSegment> retrievePnrPassengerSegment = new ArrayList<>();
		RetrievePnrPassengerSegment rePnrPassengerSegment = new RetrievePnrPassengerSegment();
		rePnrPassengerSegment.setPassengerId(segmentId);
		retrievePnrPassengerSegment.add(rePnrPassengerSegment);
		booking.setPassengerSegments(retrievePnrPassengerSegment);
	}

	@Test
	public void test_primaryPaxIdentificationForETicket() throws BusinessBaseException {
		String familyName = null;
		String givenName = null;
		Throwable t = null;
		RetrievePnrBooking booking = new RetrievePnrBooking();
		try {
			paxNameIdentificationServiceImpl.primaryPaxIdentificationForRloc(familyName, givenName, booking);
		} catch (Exception ex) {
			t = ex;
			assertNotNull(t);
			assertTrue(t instanceof UnexpectedException);
			assertTrue(t.getMessage().contains(""));
		}
	}

	@Test
	public void test_primaryPaxIdentificationForETicket1() throws BusinessBaseException {
		String familyName = "TEST";
		String givenName = "L Ivan";
		Throwable t = null;
		RetrievePnrBooking booking = new RetrievePnrBooking();
		try {
			paxNameIdentificationServiceImpl.primaryPaxIdentificationForRloc(familyName, givenName, booking);
		} catch (Exception ex) {
			t = ex;
			assertNotNull(t);
			assertTrue(t instanceof UnexpectedException);
			assertTrue(t.getMessage().contains("There is no passenger in booking"));
		}
	}

	@Test
	public void test_primaryPaxIdentificationForETicket3() throws BusinessBaseException {
		Throwable t = null;
		String eTicket = "123456789";
		String familyName = "TEST";
		String givenName = "L Ivan";
		String parentId = "1";
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		RetrievePnrBooking booking = new RetrievePnrBooking();
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setParentId(parentId);
		passenger.setGivenName(givenName);
		passenger.setFamilyName(familyName);
		passenger.setPassengerID("1");
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setOneARloc("MC5IRK");
		try {
			paxNameIdentificationServiceImpl.primaryPaxIdentificationForETicket(familyName, givenName, eTicket,
					booking);
		} catch (Exception ex) {
			t = ex;
			assertNotNull(t);
			assertTrue(t instanceof UnexpectedException);
			assertTrue(t.getMessage().contains("No passenger found by eTicket"));
		}

	}

	@Test
	public void test_primaryPaxIdentificationForETicket4() throws BusinessBaseException {
		String familyName = "TEST";
		String givenName = "L Ivan";
		String passengerId = "1";
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		List<String> nameTitleList = new ArrayList<>();
		List<ConstantData> constantData = new ArrayList<>();
		ConstantData constant = new ConstantData();
		constant.setValue("TITLE");
		constantData.add(constant);
		List<RetrievePnrPassengerSegment> passengerSegments = new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment = new RetrievePnrPassengerSegment();
		passengerSegment.setPassengerId(passengerId);
		List<RetrievePnrEticket> eTickets = new ArrayList<>();
		RetrievePnrEticket retrievePnrEticket = new RetrievePnrEticket();
		retrievePnrEticket.setTicketNumber(eTicket);
		eTickets.add(retrievePnrEticket);
		passengerSegment.setEtickets(eTickets);
		passengerSegments.add(passengerSegment);
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setPassengerID(passengerID);
		passenger.setEmails(apEmails);
		passenger.setFamilyName(familyName);
		passenger.setGivenName(givenName);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setPassengerSegments(passengerSegments);
		booking.setOneARloc("MC5IRK");
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB))
				.thenReturn(constantData);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList())).thenReturn(nameTitleList);
		paxNameIdentificationServiceImpl.primaryPaxIdentificationForETicket(familyName, givenName, eTicket, booking);
		// no exception if fine

	}

	@Test
	public void test_primaryPaxIdentificationForMember() throws BusinessBaseException {
		String familyName = "TEST";
		String givenName = "L Ivan";

		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("L Ivan");
		loginInfo.setLoginType("M");
		loginInfo.setMemberId("1910026122");
		String passengerId = "1";
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		List<String> nameTitleList = new ArrayList<>();
		List<ConstantData> constantData = new ArrayList<>();
		ConstantData constant = new ConstantData();
		constant.setValue("TITLE");
		constantData.add(constant);

		RetrievePnrBooking booking = new RetrievePnrBooking();
		List<RetrievePnrPassengerSegment> passengerSegments = new ArrayList<>();
		List<RetrievePnrFFPInfo> FQTVInfos = new ArrayList<>();
		RetrievePnrFFPInfo FQTVInfo = new RetrievePnrFFPInfo();
		FQTVInfo.setFfpMembershipNumber("1910026122");
		FQTVInfos.add(FQTVInfo);

		RetrievePnrPassengerSegment passengerSegment = new RetrievePnrPassengerSegment();
		passengerSegment.setPassengerId(passengerId);
		List<RetrievePnrEticket> eTickets = new ArrayList<>();
		RetrievePnrEticket retrievePnrEticket = new RetrievePnrEticket();
		retrievePnrEticket.setTicketNumber(eTicket);
		eTickets.add(retrievePnrEticket);
		passengerSegment.setEtickets(eTickets);
		passengerSegment.setFQTVInfos(FQTVInfos);
		passengerSegments.add(passengerSegment);
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setPassengerID(passengerID);
		passenger.setEmails(apEmails);
		passenger.setFamilyName(familyName);
		passenger.setGivenName(givenName);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setPassengerSegments(passengerSegments);
		booking.setOneARloc("MC5IRK");
		ProfilePreference profilePreference = new ProfilePreference();
		List<ProfileTravelDoc> personalTravelDocuments = new ArrayList<>();
		ProfileTravelDoc profileTravelDoc = new ProfileTravelDoc();
		profileTravelDoc.setFamilyName(familyName);
		profileTravelDoc.setGivenName(givenName);
		profileTravelDoc.setType("ST");
		personalTravelDocuments.add(profileTravelDoc);
		List<TravelCompanion> travelCompanions = new ArrayList<>();
		TravelCompanion travelCompanion = new TravelCompanion();
		travelCompanion.setTitle("MR");
		travelCompanion.setGender("F");
		travelCompanions.add(travelCompanion);
		profilePreference.setPersonalTravelDocuments(personalTravelDocuments);
		profilePreference.setTravelCompanions(travelCompanions);
		when(retrieveProfileService.retrievePreference(loginMemberId, null)).thenReturn(profilePreference);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB))
				.thenReturn(constantData);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList())).thenReturn(nameTitleList);
		paxNameIdentificationServiceImpl.primaryPaxIdentificationForMember(loginInfo, booking);
		// no exception is fine

	}

	@Test
	public void test_primaryPaxIdentificationForETicket8() throws BusinessBaseException {
		String familyName = "TEST";
		String givenName = "L Ivan";
		Throwable t = null;
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		RetrievePnrBooking booking = new RetrievePnrBooking();
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setParentId("1");
		passenger.setFamilyName("TTT");
		passenger.setGivenName("GGG");
		passenger.setPassengerID("2");
		passenger.setLoginMember(true);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setOneARloc("MC5IRK");
		try {
			paxNameIdentificationServiceImpl.primaryPaxIdentificationForRloc(familyName, givenName, booking);
		} catch (Exception ex) {
			t = ex;
			assertNotNull(t);
			assertTrue(t instanceof ExpectedException);
			assertTrue(t.getMessage()
					.contains("Cannot match pax name,request data[familyName:TEST givenName:L Ivan rloc:MC5IRK]"));
		}
	}

	@Test
	public void test_primaryPassengerIdentificationByInFo() throws BusinessBaseException {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("");
		loginInfo.setLoginGivenName("");
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		loginInfo.setLoginType("R");
		Throwable t = null;
		try {
			paxNameIdentificationServiceImpl.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		} catch (Exception ex) {
			t = ex;
			assertNotNull(t);
			assertTrue(t instanceof UnexpectedException);
			assertTrue(t.getMessage().contains(""));
		}
	}

	@Test
	public void test_primaryPassengerIdentificationByInFo1() throws BusinessBaseException {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("L Ivan");
		RetrievePnrBooking booking = new RetrievePnrBooking();
		loginInfo.setLoginType("E");
		Throwable t = null;
		try {
			paxNameIdentificationServiceImpl.primaryPaxIdentificationForRloc(familyName, givenName, booking);
		} catch (Exception ex) {
			t = ex;
			assertNotNull(t);
			assertTrue(t instanceof UnexpectedException);
			assertTrue(t.getMessage().contains("There is no passenger in booking"));
		}
	}

	@Test
	public void test_primaryPaxIdentificationForETicket2() throws BusinessBaseException {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("L Ivan");
		loginInfo.setLoginType("E");
		loginInfo.setLoginEticket("160123456789");
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setGivenName(givenName);
		passenger.setFamilyName(familyName);
		passenger.setPassengerID("1");
		passengers.add(passenger);
		pnrBooking.setPassengers(passengers);
		pnrBooking.setOneARloc("MC5IRK");

		RetrievePnrPassengerSegment passengerSegment = new RetrievePnrPassengerSegment();
		passengerSegment.setPassengerId("1");
		List<RetrievePnrEticket> eTickets = new ArrayList<>();
		RetrievePnrEticket retrievePnrEticket = new RetrievePnrEticket();
		retrievePnrEticket.setTicketNumber("160123456789");
		eTickets.add(retrievePnrEticket);
		passengerSegment.setEtickets(eTickets);
		passengerSegments.add(passengerSegment);

		pnrBooking.setPassengerSegments(passengerSegments);

		List<String> nameTitleList = new ArrayList<>();
		List<ConstantData> constantData = new ArrayList<>();
		ConstantData constant = new ConstantData();
		constant.setValue("TITLE");
		constantData.add(constant);

		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB))
				.thenReturn(constantData);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList())).thenReturn(nameTitleList);

		paxNameIdentificationServiceImpl.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		// no exption is fine
	}

	@Test
	public void test_primaryPaxIdentificationForETicket9() throws BusinessBaseException {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("L Ivan");
		loginInfo.setLoginType("M");
		loginInfo.setMemberId("1910026122");
		String passengerId = "1";
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		List<String> nameTitleList = new ArrayList<>();
		List<ConstantData> constantData = new ArrayList<>();
		ConstantData constant = new ConstantData();
		constant.setValue("TITLE");
		constantData.add(constant);

		RetrievePnrBooking booking = new RetrievePnrBooking();
		List<RetrievePnrPassengerSegment> passengerSegments = new ArrayList<>();
		List<RetrievePnrFFPInfo> FQTVInfos = new ArrayList<>();
		RetrievePnrFFPInfo FQTVInfo = new RetrievePnrFFPInfo();
		FQTVInfo.setFfpMembershipNumber("1910026122");
		FQTVInfos.add(FQTVInfo);

		RetrievePnrPassengerSegment passengerSegment = new RetrievePnrPassengerSegment();
		passengerSegment.setPassengerId(passengerId);
		List<RetrievePnrEticket> eTickets = new ArrayList<>();
		RetrievePnrEticket retrievePnrEticket = new RetrievePnrEticket();
		retrievePnrEticket.setTicketNumber(eTicket);
		eTickets.add(retrievePnrEticket);
		passengerSegment.setEtickets(eTickets);
		passengerSegment.setFQTVInfos(FQTVInfos);
		passengerSegments.add(passengerSegment);
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setPassengerID(passengerID);
		passenger.setEmails(apEmails);
		passenger.setFamilyName(familyName);
		passenger.setGivenName(givenName);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setPassengerSegments(passengerSegments);
		booking.setOneARloc("MC5IRK");
		ProfilePreference profilePreference = new ProfilePreference();
		List<ProfileTravelDoc> personalTravelDocuments = new ArrayList<>();
		ProfileTravelDoc profileTravelDoc = new ProfileTravelDoc();
		profileTravelDoc.setFamilyName(familyName);
		profileTravelDoc.setGivenName(givenName);
		profileTravelDoc.setType("ST");
		personalTravelDocuments.add(profileTravelDoc);
		List<TravelCompanion> travelCompanions = new ArrayList<>();
		TravelCompanion travelCompanion = new TravelCompanion();
		travelCompanion.setTitle("MR");
		travelCompanion.setGender("F");
		travelCompanions.add(travelCompanion);
		profilePreference.setPersonalTravelDocuments(personalTravelDocuments);
		profilePreference.setTravelCompanions(travelCompanions);
		when(retrieveProfileService.retrievePreference(loginMemberId, null)).thenReturn(profilePreference);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB))
				.thenReturn(constantData);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList())).thenReturn(nameTitleList);

		paxNameIdentificationServiceImpl.primaryPassengerIdentificationByInFo(loginInfo, booking);
		// no exption is fine

	}
	@Test
	public void test_Member_cust_matched() throws BusinessBaseException {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("L Ivan");
		loginInfo.setLoginType("M");
		loginInfo.setMemberId("1910026122");
		loginInfo.setUserType("RU");
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		List<String> nameTitleList = new ArrayList<>();
		List<ConstantData> constantData = new ArrayList<>();
		ConstantData constant = new ConstantData();
		constant.setValue("TITLE");
		constantData.add(constant);

		RetrievePnrBooking booking = new RetrievePnrBooking();
		//sK cust
		List<RetrievePnrDataElements> skList = new ArrayList<>();
		RetrievePnrDataElements skCust = new RetrievePnrDataElements();
		skList.add(skCust);
		skCust.setType("CUST");
		skCust.setFreeText("1910026122:IBE");
		booking.setSkList(skList);

		 
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setPassengerID(passengerID);
		passenger.setEmails(apEmails);
		passenger.setFamilyName(familyName);
		passenger.setGivenName(givenName);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setPassengerSegments(passengerSegments);
		booking.setOneARloc("MC5IRK");
		ProfilePreference profilePreference = new ProfilePreference();
		List<ProfileTravelDoc> personalTravelDocuments = new ArrayList<>();
		ProfileTravelDoc profileTravelDoc = new ProfileTravelDoc();
		profileTravelDoc.setFamilyName(familyName);
		profileTravelDoc.setGivenName(givenName);
		profileTravelDoc.setType("ST");
		personalTravelDocuments.add(profileTravelDoc);
		List<TravelCompanion> travelCompanions = new ArrayList<>();
		TravelCompanion travelCompanion = new TravelCompanion();
		travelCompanion.setTitle("MR");
		travelCompanion.setGender("F");
		travelCompanions.add(travelCompanion);
		profilePreference.setPersonalTravelDocuments(personalTravelDocuments);
		profilePreference.setTravelCompanions(travelCompanions);
		when(retrieveProfileService.retrievePreference(loginMemberId, null)).thenReturn(profilePreference);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB))
				.thenReturn(constantData);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList())).thenReturn(nameTitleList);

		paxNameIdentificationServiceImpl.primaryPassengerIdentificationByInFo(loginInfo, booking);
		// no exption is fine
		assertTrue(booking.getPassengers().get(0).isPrimaryPassenger());
		assertTrue(booking.getPassengers().get(0).isLoginMember());
		
	}
	@Test
	public void test_Member_custname_not_matched() throws BusinessBaseException {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("L Ivan");
		loginInfo.setLoginType("M");
		loginInfo.setMemberId("1910026122");
		loginInfo.setUserType("RU");
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		List<String> nameTitleList = new ArrayList<>();
		List<ConstantData> constantData = new ArrayList<>();
		ConstantData constant = new ConstantData();
		constant.setValue("TITLE");
		constantData.add(constant);

		RetrievePnrBooking booking = new RetrievePnrBooking();
		//sK cust
		List<RetrievePnrDataElements> skList = new ArrayList<>();
		RetrievePnrDataElements skCust = new RetrievePnrDataElements();
		skList.add(skCust);
		skCust.setType("CUST");
		skCust.setFreeText("1910026122:IBE");
		booking.setSkList(skList);

		 
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setPassengerID(passengerID);
		passenger.setEmails(apEmails);
		passenger.setFamilyName(familyName);
		passenger.setGivenName("AAAAA");
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setPassengerSegments(passengerSegments);
		booking.setOneARloc("MC5IRK");
		ProfilePreference profilePreference = new ProfilePreference();
		List<ProfileTravelDoc> personalTravelDocuments = new ArrayList<>();
		ProfileTravelDoc profileTravelDoc = new ProfileTravelDoc();
		profileTravelDoc.setFamilyName(familyName);
		profileTravelDoc.setGivenName("BBBBB");
		profileTravelDoc.setType("ST");
		personalTravelDocuments.add(profileTravelDoc);
		List<TravelCompanion> travelCompanions = new ArrayList<>();
		TravelCompanion travelCompanion = new TravelCompanion();
		travelCompanion.setTitle("MR");
		travelCompanion.setGender("F");
		travelCompanions.add(travelCompanion);
		profilePreference.setPersonalTravelDocuments(personalTravelDocuments);
		profilePreference.setTravelCompanions(travelCompanions);
		when(retrieveProfileService.retrievePreference(loginMemberId, null)).thenReturn(profilePreference);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB))
				.thenReturn(constantData);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList())).thenReturn(nameTitleList);

		paxNameIdentificationServiceImpl.primaryPassengerIdentificationByInFo(loginInfo, booking);
		// no exption is fine
		assertTrue(booking.getPassengers().get(0).isPrimaryPassenger());
		assertTrue(BooleanUtils.isNotTrue(booking.getPassengers().get(0).isLoginMember()));
		
	}
	
	@Test
	public void test_Member_cannot_match() throws BusinessBaseException {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("L Ivan");
		loginInfo.setLoginType("M");
		loginInfo.setMemberId("1910026122");
		loginInfo.setUserType("RU");
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		List<String> nameTitleList = new ArrayList<>();
		List<ConstantData> constantData = new ArrayList<>();
		ConstantData constant = new ConstantData();
		constant.setValue("TITLE");
		constantData.add(constant);

		RetrievePnrBooking booking = new RetrievePnrBooking();
		//sK cust
		List<RetrievePnrDataElements> skList = new ArrayList<>();
		/*RetrievePnrDataElements skCust = new RetrievePnrDataElements();
		skList.add(skCust);
		skCust.setType("CUST");
		skCust.setFreeText("1910026122:IBE");
		booking.setSkList(skList);*/

		 
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setPassengerID(passengerID);
		passenger.setEmails(apEmails);
		passenger.setFamilyName(familyName);
		passenger.setGivenName("AAAAA");
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setPassengerSegments(passengerSegments);
		booking.setOneARloc("MC5IRK");
		ProfilePreference profilePreference = new ProfilePreference();
		List<ProfileTravelDoc> personalTravelDocuments = new ArrayList<>();
		ProfileTravelDoc profileTravelDoc = new ProfileTravelDoc();
		profileTravelDoc.setFamilyName(familyName);
		profileTravelDoc.setGivenName("BBBBB");
		profileTravelDoc.setType("ST");
		personalTravelDocuments.add(profileTravelDoc);
		List<TravelCompanion> travelCompanions = new ArrayList<>();
		TravelCompanion travelCompanion = new TravelCompanion();
		travelCompanion.setTitle("MR");
		travelCompanion.setGender("F");
		travelCompanions.add(travelCompanion);
		profilePreference.setPersonalTravelDocuments(personalTravelDocuments);
		profilePreference.setTravelCompanions(travelCompanions);
		when(linkTempBookingRepository.getLinkedBookings(loginInfo.getMmbToken())).thenReturn(Collections.emptyList());
		when(retrieveProfileService.retrievePreference(loginMemberId, null)).thenReturn(profilePreference);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB))
				.thenReturn(constantData);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList())).thenReturn(nameTitleList);
		try {
			paxNameIdentificationServiceImpl.primaryPassengerIdentificationByInFo(loginInfo, booking);
		} catch (ExpectedException e) { 
			assertEquals(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW.getCode(), e.getErrorInfo().getErrorCode());
		}
			
	}
	
	@Test
	public void test_Member_linked_booking_match() throws BusinessBaseException {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("L Ivan");
		loginInfo.setLoginType("M");
		loginInfo.setMemberId("1910026122");
		loginInfo.setUserType("RU");
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		List<String> nameTitleList = new ArrayList<>();
		List<ConstantData> constantData = new ArrayList<>();
		ConstantData constant = new ConstantData();
		constant.setValue("TITLE");
		constantData.add(constant);

		RetrievePnrBooking booking = new RetrievePnrBooking();
		//sK cust
		List<RetrievePnrDataElements> skList = new ArrayList<>();
/*		RetrievePnrDataElements skCust = new RetrievePnrDataElements();
		skList.add(skCust);
		skCust.setType("CUST");
		skCust.setFreeText("1910026122:IBE");
		booking.setSkList(skList);*/

		 
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setPassengerID(passengerID);
		passenger.setEmails(apEmails);
		passenger.setFamilyName(familyName);
		passenger.setGivenName("AAAAA");
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setPassengerSegments(passengerSegments);
		booking.setOneARloc("MC5IRK");
		ProfilePreference profilePreference = new ProfilePreference();
		List<ProfileTravelDoc> personalTravelDocuments = new ArrayList<>();
		ProfileTravelDoc profileTravelDoc = new ProfileTravelDoc();
		profileTravelDoc.setFamilyName(familyName);
		profileTravelDoc.setGivenName("AAAAA");
		profileTravelDoc.setType("ST");
		personalTravelDocuments.add(profileTravelDoc);
		List<TravelCompanion> travelCompanions = new ArrayList<>();
		TravelCompanion travelCompanion = new TravelCompanion();
		travelCompanion.setTitle("MR");
		travelCompanion.setGender("F");
		travelCompanions.add(travelCompanion);
		profilePreference.setPersonalTravelDocuments(personalTravelDocuments);
		profilePreference.setTravelCompanions(travelCompanions);
		TempLinkedBooking  tempBooking= new TempLinkedBooking();
		tempBooking.setRloc("MC5IRK");
		tempBooking.setPrimaryPassengerId(passengerID);
		
		when(linkTempBookingRepository.getLinkedBookings(loginInfo.getMmbToken())).thenReturn(Arrays.asList(tempBooking));
		when(retrieveProfileService.retrievePreference(loginMemberId, null)).thenReturn(profilePreference);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB))
				.thenReturn(constantData);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList())).thenReturn(nameTitleList);
		paxNameIdentificationServiceImpl.primaryPassengerIdentificationByInFo(loginInfo, booking);
		 
		RetrievePnrPassenger priPax = booking.getPassengers().stream().filter(RetrievePnrPassenger::isPrimaryPassenger).findFirst().orElse(null);
		assertEquals(priPax.getPassengerID(), passengerID);
		assertTrue(priPax.isLoginMember());
		
	}
	
	
	
	@Test
	public void test_Member_Special_character_matched() throws BusinessBaseException {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("L Ivan");
		loginInfo.setLoginType("M");
		loginInfo.setMemberId("1910026122");
		loginInfo.setUserType("RU");
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		List<String> nameTitleList = new ArrayList<>();
		List<ConstantData> constantData = new ArrayList<>();
		ConstantData constant = new ConstantData();
		constant.setValue("TITLE");
		constantData.add(constant);

		RetrievePnrBooking booking = new RetrievePnrBooking();
		//sK cust
		List<RetrievePnrDataElements> skList = new ArrayList<>();
		RetrievePnrDataElements skCust = new RetrievePnrDataElements();
		skList.add(skCust);
		skCust.setType("CUST");
		skCust.setFreeText("1910026122:IBE");
		booking.setSkList(skList);

		 
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setPassengerID(passengerID);
		passenger.setFamilyName("TestFamilyName");
		passenger.setGivenName("TestGivenName");
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setOneARloc("MC5IRK");
		
		ProfilePreference profilePreference = new ProfilePreference();
		List<ProfileTravelDoc> personalTravelDocuments = new ArrayList<>();
		ProfileTravelDoc profileTravelDoc = new ProfileTravelDoc();
		profileTravelDoc.setFamilyName("Test-FamilyName");
		profileTravelDoc.setGivenName("Test-GivenName");
		profileTravelDoc.setType("ST");
		personalTravelDocuments.add(profileTravelDoc);
		
		List<TravelCompanion> travelCompanions = new ArrayList<>();
		TravelCompanion travelCompanion = new TravelCompanion();
		travelCompanion.setTitle("MR");
		travelCompanion.setGender("F");
		travelCompanions.add(travelCompanion);
		profilePreference.setPersonalTravelDocuments(personalTravelDocuments);
		profilePreference.setTravelCompanions(travelCompanions);
		when(retrieveProfileService.retrievePreference(loginMemberId, null)).thenReturn(profilePreference);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB))
				.thenReturn(constantData);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList())).thenReturn(nameTitleList);

		paxNameIdentificationServiceImpl.primaryPassengerIdentificationByInFo(loginInfo, booking);
		// no exption is fine
		assertTrue(booking.getPassengers().get(0).isPrimaryPassenger());
		assertTrue(booking.getPassengers().get(0).isLoginMember());
		
	}
	
	@Test
	public void test_Member_Special_character_not_matched() throws BusinessBaseException {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("L Ivan");
		loginInfo.setLoginType("M");
		loginInfo.setMemberId("1910026122");
		loginInfo.setUserType("RU");
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		List<String> nameTitleList = new ArrayList<>();
		List<ConstantData> constantData = new ArrayList<>();
		ConstantData constant = new ConstantData();
		constant.setValue("TITLE");
		constantData.add(constant);

		RetrievePnrBooking booking = new RetrievePnrBooking();
		//sK cust
		List<RetrievePnrDataElements> skList = new ArrayList<>();
		RetrievePnrDataElements skCust = new RetrievePnrDataElements();
		skList.add(skCust);
		skCust.setType("CUST");
		skCust.setFreeText("1910026122:IBE");
		booking.setSkList(skList);

		 
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setPassengerID(passengerID);
		passenger.setFamilyName("FTestFamilyName");
		passenger.setGivenName("TestGivenName");
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setOneARloc("MC5IRK");
		
		ProfilePreference profilePreference = new ProfilePreference();
		List<ProfileTravelDoc> personalTravelDocuments = new ArrayList<>();
		ProfileTravelDoc profileTravelDoc = new ProfileTravelDoc();
		profileTravelDoc.setFamilyName("Test-FamilyName");
		profileTravelDoc.setGivenName("Test-GivenName");
		profileTravelDoc.setType("ST");
		personalTravelDocuments.add(profileTravelDoc);
		
		List<TravelCompanion> travelCompanions = new ArrayList<>();
		TravelCompanion travelCompanion = new TravelCompanion();
		travelCompanion.setTitle("MR");
		travelCompanion.setGender("F");
		travelCompanions.add(travelCompanion);
		profilePreference.setPersonalTravelDocuments(personalTravelDocuments);
		profilePreference.setTravelCompanions(travelCompanions);
		when(retrieveProfileService.retrievePreference(loginMemberId, null)).thenReturn(profilePreference);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB))
				.thenReturn(constantData);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList())).thenReturn(nameTitleList);

		paxNameIdentificationServiceImpl.primaryPassengerIdentificationByInFo(loginInfo, booking);
		//member always has Primary Passenger
		assertTrue(booking.getPassengers().get(0).isPrimaryPassenger());
		assertTrue(BooleanUtils.isNotTrue(booking.getPassengers().get(0).isLoginMember()));
		
	}
	
	@Test
	public void test_primaryPassengerIdentificationForRlocFlow() throws BusinessBaseException {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("AAAA BBB");
		loginInfo.setLoginType("R");
		
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		RetrievePnrPassenger pnrPassenger1 = new RetrievePnrPassenger();
		pnrPassenger1.setPassengerID("1");
		pnrPassenger1.setFamilyName("TEST");
		pnrPassenger1.setGivenName("AAAABBCC");
		
		RetrievePnrPassenger pnrPassenger2 = new RetrievePnrPassenger();
		pnrPassenger2.setPassengerID("2");
		pnrPassenger2.setFamilyName("TEST");
		pnrPassenger2.setGivenName("A AAABB BC");
		
		List<RetrievePnrPassenger> pnrPassengers = new ArrayList<>();
		pnrPassengers.add(pnrPassenger1);
		pnrPassengers.add(pnrPassenger2);
		pnrBooking.setPassengers(pnrPassengers);
		
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);

		paxNameIdentificationServiceImpl.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		assertTrue(pnrPassenger2.isPrimaryPassenger());
	}
	
	@Test
	public void test_primaryPassengerIdentificationForRuUogradeFlow() throws BusinessBaseException {
		
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("L Ivan");
		loginInfo.setLoginType("M");
		loginInfo.setMemberId("1910026122");
		loginInfo.setOriginalRuMemberId("1910038033");
		loginInfo.setUserType("AM");
 
		
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		RetrievePnrPassenger pnrPassenger1 = new RetrievePnrPassenger();
		pnrPassenger1.setPassengerID("1");
		pnrPassenger1.setFamilyName("TEST");
		pnrPassenger1.setGivenName("AAAABBCC");
		
		List<RetrievePnrPassenger> pnrPassengers = new ArrayList<>();
		pnrPassengers.add(pnrPassenger1);
		pnrBooking.setPassengers(pnrPassengers);
		RetrievePnrDataElements custSk =  new RetrievePnrDataElements();
		custSk.setFreeText("1910038033:IBE");
		custSk.setType("CUST");
		pnrBooking.setSkList(Arrays.asList(custSk));
		List<String> nameTitleList = new ArrayList<>();
		List<ConstantData> constantData = new ArrayList<>();
		ConstantData constant = new ConstantData();
		constant.setValue("TITLE");
		constantData.add(constant);
		
		ReflectionTestUtils.setField(paxNameIdentificationServiceImpl, "shortCompareSize", 4);
		when(retrieveProfileService.retrievePreference(loginMemberId, null)).thenReturn(new ProfilePreference());
		when(retrieveProfileService.retrievePersonInfo(anyString(), anyString())).thenReturn(new ProfilePersonInfo());
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB))
				.thenReturn(constantData);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList())).thenReturn(nameTitleList);
		paxNameIdentificationServiceImpl.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		assertTrue(pnrPassenger1.isPrimaryPassenger());
	}
}
