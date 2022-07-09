package com.cathaypacific.mmbbizrule.service.impl;

import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

import java.text.ParseException;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.config.BookingStatusConfig;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.AirportTimeZoneService;
import com.cathaypacific.mmbbizrule.db.dao.BookingStatusDAO;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.model.BookingStatus;
import com.cathaypacific.mmbbizrule.db.model.ConstantData;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormJourneyDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormPassengerDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormSegmentDTO;
import com.cathaypacific.mmbbizrule.handler.BookingBuildHelper;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormAddressRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormGuardianInfoRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormPortInfoRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormSegmentRemark;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.model.AirFlightInfoBean;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.service.AirFlightInfoService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElementsOtherData;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.service.UMNREFormRemarkService;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class UMNREFormBuildServiceImplTest  {
	
	@InjectMocks
	UMNREFormBuildServiceImpl umnreFormBuildService;
	
	@Mock
	private UMNREFormRemarkService umnrEFormRemarkService;
	
	@Mock
	private BookingStatusDAO bookingStatusDAO;
	
	@Mock
	private BookingStatusConfig bookingStatusConfig;
	
	@Mock
	private ConstantDataDAO constantDataDAO;
	
	@Mock
	private BizRuleConfig bizRuleConfig;
	
	@Mock
	private AEMService aemService;
	
	@Mock
	private AirFlightInfoService airFlightInfoService;
	
	@Mock
	private AirportTimeZoneService airportTimeZoneService;
	
	@Mock
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Mock
	private BookingBuildHelper bookingBuildHelper;

	@Before
	public void setUp() {
		// Mock AirFlightInfoBean
		AirFlightInfoBean airFlightInfoBean = new AirFlightInfoBean();
		airFlightInfoBean.setCarrierCode("CX");
		airFlightInfoBean.setFlightNumber("250");
		when(airFlightInfoService.getAirFlightInfo(any(), any(), any(), any(), any(), any())).thenReturn(airFlightInfoBean);
		
		// Mock BookingStatus
		List<BookingStatus> bookingStatusList = Lists.newArrayList();
		BookingStatus bookingStatus = new BookingStatus();
		bookingStatusList.add(bookingStatus);
		bookingStatus.setStatusCode("HK");
		when(bookingStatusDAO.findAvailableStatus(any())).thenReturn(bookingStatusList);
		
		// Mock bookingStatusConfig
		when(bookingStatusConfig.getConfirmedList()).thenReturn(Lists.newArrayList("HK"));
		
		// Mock ConstantData
		List<ConstantData> constantDatas = Lists.newArrayList();
		ConstantData constantData = new ConstantData();
		constantData.setType("TITLE");
		constantData.setValue("ms");
		constantDatas.add(constantData);
		
		ConstantData constantData2 = new ConstantData();
		constantData2.setType("TITLE");
		constantData2.setValue("mr");
		constantDatas.add(constantData2);
		when(constantDataDAO.findByAppCodeAndType(any(), any())).thenReturn(constantDatas);
		
		// Mock bizRuleConfig
		when(bizRuleConfig.getFemaleNameTitle()).thenReturn(Lists.newArrayList("ms"));
		when(bizRuleConfig.getMaleNameTitle()).thenReturn(Lists.newArrayList("mr"));
		
		// Mock aemService
		when(aemService.getCityCodeByPortCode("HKG")).thenReturn("HKG");
		when(aemService.getCityCodeByPortCode("NRT")).thenReturn("TYO");
		
		// Mock airportTimeZoneService
		when(airportTimeZoneService.getAirPortTimeZoneOffset("HKG")).thenReturn("+0800");
		when(airportTimeZoneService.getAirPortTimeZoneOffset("NRT")).thenReturn("+0900");
	}
	
	/**
	 * Test case: Prefill info from remark / SSR / OSI; Parse multi-segments to multi-journeys with default hours interval config;
	 * Country code to search OSI; 
	 * @throws UnexpectedException
	 * @throws ParseException
	 */
	@Test
	public void multiUMNREFormJourneys() throws UnexpectedException, ParseException {
		// Mock UMNREFormRemark
		List<UMNREFormRemark> umnreFormRemarks = Lists.newArrayList();
		when(umnrEFormRemarkService.buildUMNREFormRemark(any())).thenReturn(umnreFormRemarks);
		UMNREFormRemark umnreFormRemark = new UMNREFormRemark();
		umnreFormRemarks.add(umnreFormRemark);
		umnreFormRemark.setAddress(new UMNREFormAddressRemark());
		umnreFormRemark.getAddress().setBuilding("Building #1");
		umnreFormRemark.getAddress().setCity("City #1");
		umnreFormRemark.getAddress().setStreet("Street #1");
		umnreFormRemark.getAddress().setCountryCode("HKG");
		umnreFormRemark.setAge("07");
		umnreFormRemark.setGender("F");
		umnreFormRemark.setPassengerId("P1");
		
		UMNREFormGuardianInfoRemark parentInfo = new UMNREFormGuardianInfoRemark();
		parentInfo.setName("Parent name");
		parentInfo.setPhoneNumber("85265006500");
		parentInfo.setAddress(new UMNREFormAddressRemark());
		parentInfo.getAddress().setBuilding("Building #1");
		parentInfo.getAddress().setCity("City #1");
		parentInfo.getAddress().setStreet("Street #1");
		parentInfo.getAddress().setCountryCode("HKG");
		umnreFormRemark.setParentInfo(parentInfo);
		
		// Mock UMNREFormSegmentRemark
		umnreFormRemark.setSegments(Lists.newArrayList());
		UMNREFormSegmentRemark umnreFormSegmentRemark = new UMNREFormSegmentRemark();
		umnreFormSegmentRemark.setFlightDate("01122025");
		umnreFormSegmentRemark.setFlightNumber("KA333");
		umnreFormSegmentRemark.setPortInfo(Lists.newArrayList());
		umnreFormRemark.getSegments().add(umnreFormSegmentRemark);
		
		// Mock UMNREFormPortInfoRemark #1
		UMNREFormPortInfoRemark umnreFormPortInfoRemark = new UMNREFormPortInfoRemark();
		umnreFormSegmentRemark.getPortInfo().add(umnreFormPortInfoRemark);
		umnreFormPortInfoRemark.setAirportCode("HKG");
		// Mock UMNREFormGuardianInfoRemark
		umnreFormPortInfoRemark.setGuardianInfo(new UMNREFormGuardianInfoRemark());
		umnreFormPortInfoRemark.getGuardianInfo().setName("Guardion Name #1");
		umnreFormPortInfoRemark.getGuardianInfo().setPhoneNumber("85269698677");
		// Mock Guardion's UMNREFormAddressRemark
		umnreFormPortInfoRemark.getGuardianInfo().setAddress(new UMNREFormAddressRemark());
		umnreFormPortInfoRemark.getGuardianInfo().getAddress().setBuilding("Building #2");
		umnreFormPortInfoRemark.getGuardianInfo().getAddress().setCity("City #2");
		umnreFormPortInfoRemark.getGuardianInfo().getAddress().setStreet("Street #2");
		
		// Mock LoginInfo
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginGivenName("GIVEN NAME");
		loginInfo.setLoginFamilyName("FAMILY NAME");
		loginInfo.setLoginRloc("ABC123");
		loginInfo.setUserType("nonmember");
		
		// Mock RetrievePnrBooking
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		pnrBooking.setPassengers(Lists.newArrayList());
		pnrBooking.setSegments(Lists.newArrayList());
		
		// Mock UMNR SSR
		pnrBooking.setSsrList(Lists.newArrayList());
		RetrievePnrDataElements pnrDataElements = new RetrievePnrDataElements();
		pnrBooking.getSsrList().add(pnrDataElements);
		pnrDataElements.setType("UMNR");
		pnrDataElements.setFreeText("UM07");
		
		// Mock UMNR OSI
		pnrBooking.setOsiList(Lists.newArrayList());
		pnrDataElements = new RetrievePnrDataElements();
		pnrBooking.getOsiList().add(pnrDataElements);
		pnrDataElements.setType("OS");
		pnrDataElements.setOtherDataList(Lists.newArrayList());
		RetrievePnrDataElementsOtherData pnrDataElementsOtherData = new RetrievePnrDataElementsOtherData();
		pnrDataElementsOtherData.setFreeText("UMNR/HKG/NELSON WONG/85269698677");
		pnrDataElements.getOtherDataList().add(pnrDataElementsOtherData);
		
		pnrDataElements = new RetrievePnrDataElements();
		pnrBooking.getOsiList().add(pnrDataElements);
		pnrDataElements.setType("OS");
		pnrDataElements.setOtherDataList(Lists.newArrayList());
		pnrDataElementsOtherData = new RetrievePnrDataElementsOtherData();
		pnrDataElementsOtherData.setFreeText("UMNR/HKG/Kent WONG/85269698677");
		pnrDataElements.getOtherDataList().add(pnrDataElementsOtherData);
		
		// Mock RetrievePnrPassenger
		RetrievePnrPassenger pnrPassenger = new RetrievePnrPassenger();
		pnrBooking.getPassengers().add(pnrPassenger);
		pnrPassenger.setFamilyName("FAMILY NAME");
		pnrPassenger.setGivenName("GIVEN NAME MS");
		pnrPassenger.setPrimaryPassenger(true);
		pnrPassenger.setPassengerID("1");

		// Mock RetrievePnrSegment #1
		RetrievePnrSegment pnrSegment = new RetrievePnrSegment();
		pnrBooking.getSegments().add(pnrSegment);
		pnrSegment.setSegmentID("1");
		pnrSegment.setPnrOperateCompany("CX");
		pnrSegment.setPnrOperateSegmentNumber("250");
		pnrSegment.setMarketCompany("KA");
		pnrSegment.setMarketSegmentNumber("333");
		pnrSegment.setOriginPort("HKG");
		pnrSegment.setDestPort("NRT");
		pnrSegment.setDepartureTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getDepartureTime().setPnrTime("2025-12-01 11:00");
		pnrSegment.getDepartureTime().setTimeZoneOffset("+0800");
		pnrSegment.setArrivalTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getArrivalTime().setPnrTime("2025-12-01 15:00");
		pnrSegment.getArrivalTime().setTimeZoneOffset("+0900");
		
		// Mock RetrievePnrSegment #2
		pnrSegment = new RetrievePnrSegment();
		pnrBooking.getSegments().add(pnrSegment);
		pnrSegment.setPnrOperateCompany("CX");
		pnrSegment.setPnrOperateSegmentNumber("450");
		pnrSegment.setMarketCompany("CX");
		pnrSegment.setMarketSegmentNumber("450");
		pnrSegment.setSegmentID("2");
		pnrSegment.setOriginPort("NRT");
		pnrSegment.setDestPort("TPE");
		pnrSegment.setDepartureTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getDepartureTime().setPnrTime("2025-12-01 18:00");
		pnrSegment.getDepartureTime().setTimeZoneOffset("+0800");
		pnrSegment.setArrivalTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getArrivalTime().setPnrTime("2025-12-01 21:00");
		pnrSegment.getArrivalTime().setTimeZoneOffset("+0900");
		
		// Mock RetrievePnrSegment #3
		pnrSegment = new RetrievePnrSegment();
		pnrBooking.getSegments().add(pnrSegment);
		pnrSegment.setPnrOperateCompany("CX");
		pnrSegment.setPnrOperateSegmentNumber("650");
		pnrSegment.setMarketCompany("CX");
		pnrSegment.setMarketSegmentNumber("650");
		pnrSegment.setSegmentID("3");
		pnrSegment.setOriginPort("HKG");
		pnrSegment.setDestPort("LHR");
		pnrSegment.setDepartureTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getDepartureTime().setPnrTime("2025-12-02 18:00");
		pnrSegment.getDepartureTime().setTimeZoneOffset("+0800");
		pnrSegment.setArrivalTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getArrivalTime().setPnrTime("2025-12-02 21:00");
		pnrSegment.getArrivalTime().setTimeZoneOffset("+0900");
		
		when(bookingBuildHelper.isValidPhoneNumber(any(), any(), any())).thenReturn(true);
		// actaul
		UMNREFormResponseDTO umnreFormResponseDTO = umnreFormBuildService.buildUMNREForm(pnrBooking);
		
		Assert.assertEquals(1, umnreFormResponseDTO.getPassengers().size());
		Assert.assertEquals(3, umnreFormResponseDTO.getSegments().size());
		Assert.assertEquals(2, umnreFormResponseDTO.getPassengers().get(0).getUmnrEFormJourneys().size());
		
		// First segment
		UMNREFormSegmentDTO umnreFormSegmentDTO = umnreFormResponseDTO.getSegments().get(0);
		Assert.assertEquals("1", umnreFormSegmentDTO.getSegmentId());
		Assert.assertEquals("2025-12-01 11:00", umnreFormSegmentDTO.getDepartureTime());
		Assert.assertEquals("2025-12-01 15:00", umnreFormSegmentDTO.getArrivalTime());
		Assert.assertEquals("HKG", umnreFormSegmentDTO.getOriginPort());
		Assert.assertEquals("NRT", umnreFormSegmentDTO.getDestPort());
		Assert.assertEquals("CX", umnreFormSegmentDTO.getOperatingCompany());
		Assert.assertEquals("250", umnreFormSegmentDTO.getOperatingSegmentNumber());
		Assert.assertEquals("KA", umnreFormSegmentDTO.getMarketingCompany());
		Assert.assertEquals("333", umnreFormSegmentDTO.getMarketingSegmentNumber());
		
		// Second segment
		umnreFormSegmentDTO = umnreFormResponseDTO.getSegments().get(1);
		Assert.assertEquals("2", umnreFormSegmentDTO.getSegmentId());
		Assert.assertEquals("2025-12-01 18:00", umnreFormSegmentDTO.getDepartureTime());
		Assert.assertEquals("2025-12-01 21:00", umnreFormSegmentDTO.getArrivalTime());
		Assert.assertEquals("NRT", umnreFormSegmentDTO.getOriginPort());
		Assert.assertEquals("TPE", umnreFormSegmentDTO.getDestPort());
		Assert.assertEquals("CX", umnreFormSegmentDTO.getOperatingCompany());
		Assert.assertEquals("450", umnreFormSegmentDTO.getOperatingSegmentNumber());
		Assert.assertEquals("CX", umnreFormSegmentDTO.getMarketingCompany());
		Assert.assertEquals("450", umnreFormSegmentDTO.getMarketingSegmentNumber());
		
		// Third segment
		umnreFormSegmentDTO = umnreFormResponseDTO.getSegments().get(2);
		Assert.assertEquals("3", umnreFormSegmentDTO.getSegmentId());
		Assert.assertEquals("2025-12-02 18:00", umnreFormSegmentDTO.getDepartureTime());
		Assert.assertEquals("2025-12-02 21:00", umnreFormSegmentDTO.getArrivalTime());
		Assert.assertEquals("HKG", umnreFormSegmentDTO.getOriginPort());
		Assert.assertEquals("LHR", umnreFormSegmentDTO.getDestPort());
		Assert.assertEquals("CX", umnreFormSegmentDTO.getOperatingCompany());
		Assert.assertEquals("650", umnreFormSegmentDTO.getOperatingSegmentNumber());
		Assert.assertEquals("CX", umnreFormSegmentDTO.getMarketingCompany());
		Assert.assertEquals("650", umnreFormSegmentDTO.getMarketingSegmentNumber());
		
		// First passenger
		UMNREFormPassengerDTO umnreFormPassengerDTO = umnreFormResponseDTO.getPassengers().get(0);
		Assert.assertEquals("7", umnreFormPassengerDTO.getAge());
		Assert.assertEquals("FAMILY NAME", umnreFormPassengerDTO.getFamilyName());
		Assert.assertEquals("F", umnreFormPassengerDTO.getGender());
		Assert.assertEquals("GIVEN NAME", umnreFormPassengerDTO.getGivenName());
		Assert.assertEquals("1", umnreFormPassengerDTO.getPassengerId());
		Assert.assertEquals("Building #1", umnreFormPassengerDTO.getPermanentAddress().getBuilding());
		Assert.assertEquals("City #1", umnreFormPassengerDTO.getPermanentAddress().getCity());
		Assert.assertEquals("Street #1", umnreFormPassengerDTO.getPermanentAddress().getStreet());
		Assert.assertEquals("MS", umnreFormPassengerDTO.getTitle());
		
		// First journeys
		UMNREFormJourneyDTO umnreFormJourneyDTO = umnreFormPassengerDTO.getUmnrEFormJourneys().get(0);
		Assert.assertEquals("1", umnreFormJourneyDTO.getJourneyId());
		Assert.assertEquals(null, umnreFormJourneyDTO.getPersonMeetingArrival());
		Assert.assertEquals("Building #2", umnreFormJourneyDTO.getPersonSeeingOffDeparture().getAddress().getBuilding());
		Assert.assertEquals("City #2", umnreFormJourneyDTO.getPersonSeeingOffDeparture().getAddress().getCity());
		Assert.assertEquals("Street #2", umnreFormJourneyDTO.getPersonSeeingOffDeparture().getAddress().getStreet());
		Assert.assertEquals("HKG", umnreFormJourneyDTO.getPersonSeeingOffDeparture().getCountryCode());
		Assert.assertEquals("Guardion Name #1", umnreFormJourneyDTO.getPersonSeeingOffDeparture().getName());
		Assert.assertEquals("69698677", umnreFormJourneyDTO.getPersonSeeingOffDeparture().getPhoneNumber());
		Assert.assertEquals("1", umnreFormJourneyDTO.getSegmentIds().get(0));
		Assert.assertEquals("2", umnreFormJourneyDTO.getSegmentIds().get(1));
		
		// Second journeys
		umnreFormJourneyDTO = umnreFormPassengerDTO.getUmnrEFormJourneys().get(1);
		Assert.assertEquals("2", umnreFormJourneyDTO.getJourneyId());
		Assert.assertEquals(null, umnreFormJourneyDTO.getPersonMeetingArrival());
		Assert.assertEquals("HKG", umnreFormJourneyDTO.getPersonSeeingOffDeparture().getCountryCode());
		Assert.assertEquals("Kent WONG", umnreFormJourneyDTO.getPersonSeeingOffDeparture().getName());
		Assert.assertEquals("69698677", umnreFormJourneyDTO.getPersonSeeingOffDeparture().getPhoneNumber());
		Assert.assertEquals("3", umnreFormJourneyDTO.getSegmentIds().get(0));
	}
	
	
	/**
	 * Test case: Prefill gender and age
	 * @throws UnexpectedException
	 * @throws ParseException
	 */
	@Test
	public void multiUMNREFormJourneysWithoutRemark() throws UnexpectedException, ParseException {
		// Mock UMNREFormRemark
		List<UMNREFormRemark> umnreFormRemarks = Lists.newArrayList();
		when(umnrEFormRemarkService.buildUMNREFormRemark(any())).thenReturn(Lists.newArrayList());

		// Mock LoginInfo
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginGivenName("GIVEN NAME MR");
		loginInfo.setLoginFamilyName("FAMILY NAME");
		loginInfo.setLoginRloc("ABC123");
		loginInfo.setUserType("nonmember");
		
		// Mock RetrievePnrBooking
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		pnrBooking.setPassengers(Lists.newArrayList());
		pnrBooking.setSegments(Lists.newArrayList());
		
		// Mock UMNR SSR
		pnrBooking.setSsrList(Lists.newArrayList());
		RetrievePnrDataElements pnrDataElements = new RetrievePnrDataElements();
		pnrBooking.getSsrList().add(pnrDataElements);
		pnrDataElements.setType("UMNR");
		pnrDataElements.setFreeText("UM11");
		
		// Mock UMNR OSI
		pnrBooking.setOsiList(Lists.newArrayList());
		pnrDataElements = new RetrievePnrDataElements();
		pnrBooking.getOsiList().add(pnrDataElements);
		pnrDataElements.setType("OS");
		pnrDataElements.setOtherDataList(Lists.newArrayList());
		RetrievePnrDataElementsOtherData pnrDataElementsOtherData = new RetrievePnrDataElementsOtherData();
		pnrDataElementsOtherData.setFreeText("UMNR/HKG/NELSON WONG/852-10000002");
		pnrDataElements.getOtherDataList().add(pnrDataElementsOtherData);
		
		// Mock RetrievePnrPassenger
		RetrievePnrPassenger pnrPassenger = new RetrievePnrPassenger();
		pnrBooking.getPassengers().add(pnrPassenger);
		pnrPassenger.setFamilyName("FAMILY NAME");
		pnrPassenger.setGivenName("GIVEN NAME MR");
		pnrPassenger.setPrimaryPassenger(true);
		pnrPassenger.setPassengerID("1");

		// Mock RetrievePnrSegment #1
		RetrievePnrSegment pnrSegment = new RetrievePnrSegment();
		pnrBooking.getSegments().add(pnrSegment);
		pnrSegment.setSegmentID("1");
		pnrSegment.setPnrOperateCompany("CX");
		pnrSegment.setPnrOperateSegmentNumber("250");
		pnrSegment.setOriginPort("HKG");
		pnrSegment.setDestPort("NRT");
		pnrSegment.setDepartureTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getDepartureTime().setPnrTime("2025-12-01 11:00");
		pnrSegment.getDepartureTime().setTimeZoneOffset("+0800");
		pnrSegment.setArrivalTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getArrivalTime().setPnrTime("2025-12-01 15:00");
		pnrSegment.getArrivalTime().setTimeZoneOffset("+0900");
		
		// Mock RetrievePnrSegment #2
		pnrSegment = new RetrievePnrSegment();
		pnrBooking.getSegments().add(pnrSegment);
		pnrSegment.setPnrOperateCompany("CX");
		pnrSegment.setPnrOperateSegmentNumber("450");
		pnrSegment.setSegmentID("2");
		pnrSegment.setOriginPort("NRT");
		pnrSegment.setDestPort("TPE");
		pnrSegment.setDepartureTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getDepartureTime().setPnrTime("2025-12-01 18:00");
		pnrSegment.getDepartureTime().setTimeZoneOffset("+0800");
		pnrSegment.setArrivalTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getArrivalTime().setPnrTime("2025-12-01 21:00");
		pnrSegment.getArrivalTime().setTimeZoneOffset("+0900");
		
		// Mock RetrievePnrSegment #3
		pnrSegment = new RetrievePnrSegment();
		pnrBooking.getSegments().add(pnrSegment);
		pnrSegment.setPnrOperateCompany("CX");
		pnrSegment.setPnrOperateSegmentNumber("650");
		pnrSegment.setSegmentID("3");
		pnrSegment.setOriginPort("HKG");
		pnrSegment.setDestPort("LHR");
		pnrSegment.setDepartureTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getDepartureTime().setPnrTime("2025-12-02 18:00");
		pnrSegment.getDepartureTime().setTimeZoneOffset("+0800");
		pnrSegment.setArrivalTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getArrivalTime().setPnrTime("2025-12-02 21:00");
		pnrSegment.getArrivalTime().setTimeZoneOffset("+0900");
		
		// actaul
		UMNREFormResponseDTO umnreFormResponseDTO = umnreFormBuildService.buildUMNREForm(pnrBooking);

		// First passenger
		UMNREFormPassengerDTO umnreFormPassengerDTO = umnreFormResponseDTO.getPassengers().get(0);
		Assert.assertEquals("11", umnreFormPassengerDTO.getAge());
		Assert.assertEquals("FAMILY NAME", umnreFormPassengerDTO.getFamilyName());
		Assert.assertEquals("M", umnreFormPassengerDTO.getGender());
		Assert.assertEquals("GIVEN NAME", umnreFormPassengerDTO.getGivenName());
		Assert.assertEquals("1", umnreFormPassengerDTO.getPassengerId());
		Assert.assertEquals("MR", umnreFormPassengerDTO.getTitle());
	}
	
	
	/**
	 * Test case: Passenger has a fullly completed EForm remark
	 * Country code to search OSI; 
	 * @throws UnexpectedException
	 * @throws ParseException
	 */
	@Test
	public void paxHasUMNREFormRemark() throws UnexpectedException, ParseException {
		// Mock UMNREFormRemark
		List<UMNREFormRemark> umnreFormRemarks = Lists.newArrayList();
		when(umnrEFormRemarkService.buildUMNREFormRemark(any())).thenReturn(umnreFormRemarks);
		UMNREFormRemark umnreFormRemark = new UMNREFormRemark();
		umnreFormRemarks.add(umnreFormRemark);
		umnreFormRemark.setAddress(new UMNREFormAddressRemark());
		umnreFormRemark.getAddress().setBuilding("Building #1");
		umnreFormRemark.getAddress().setCity("City #1");
		umnreFormRemark.getAddress().setStreet("Street #1");
		umnreFormRemark.setAge("07");
		umnreFormRemark.setGender("F");
		umnreFormRemark.setPassengerId("P1");
		
		// Mock UMNREFormSegmentRemark
		umnreFormRemark.setSegments(Lists.newArrayList());
		UMNREFormSegmentRemark umnreFormSegmentRemark = new UMNREFormSegmentRemark();
		umnreFormSegmentRemark.setFlightDate("01122025");
		umnreFormSegmentRemark.setFlightNumber("CX250");
		umnreFormSegmentRemark.setPortInfo(Lists.newArrayList());
		umnreFormRemark.getSegments().add(umnreFormSegmentRemark);
		
		// Mock UMNREFormPortInfoRemark #1
		UMNREFormPortInfoRemark umnreFormPortInfoRemark = new UMNREFormPortInfoRemark();
		umnreFormSegmentRemark.getPortInfo().add(umnreFormPortInfoRemark);
		umnreFormPortInfoRemark.setAirportCode("HKG");
		// Mock UMNREFormGuardianInfoRemark
		umnreFormPortInfoRemark.setGuardianInfo(new UMNREFormGuardianInfoRemark());
		umnreFormPortInfoRemark.getGuardianInfo().setName("Guardion Name #1");
		umnreFormPortInfoRemark.getGuardianInfo().setPhoneNumber("852-00000001");
		// Mock Guardion's UMNREFormAddressRemark
		umnreFormPortInfoRemark.getGuardianInfo().setAddress(new UMNREFormAddressRemark());
		umnreFormPortInfoRemark.getGuardianInfo().getAddress().setBuilding("Building #2");
		umnreFormPortInfoRemark.getGuardianInfo().getAddress().setCity("City #2");
		umnreFormPortInfoRemark.getGuardianInfo().getAddress().setStreet("Street #2");
		
		// Mock UMNREFormPortInfoRemark #2
		umnreFormPortInfoRemark = new UMNREFormPortInfoRemark();
		umnreFormSegmentRemark.getPortInfo().add(umnreFormPortInfoRemark);
		umnreFormPortInfoRemark.setAirportCode("NRT");
		// Mock UMNREFormGuardianInfoRemark
		umnreFormPortInfoRemark.setGuardianInfo(new UMNREFormGuardianInfoRemark());
		umnreFormPortInfoRemark.getGuardianInfo().setName("Guardion Name #1");
		umnreFormPortInfoRemark.getGuardianInfo().setPhoneNumber("852-00000001");
		// Mock Guardion's UMNREFormAddressRemark
		umnreFormPortInfoRemark.getGuardianInfo().setAddress(new UMNREFormAddressRemark());
		umnreFormPortInfoRemark.getGuardianInfo().getAddress().setBuilding("Building #2");
		umnreFormPortInfoRemark.getGuardianInfo().getAddress().setCity("City #2");
		umnreFormPortInfoRemark.getGuardianInfo().getAddress().setStreet("Street #2");
		
		// Mock RetrievePnrBooking
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		pnrBooking.setPassengers(Lists.newArrayList());
		pnrBooking.setSegments(Lists.newArrayList());
		
		// Mock UMNR SSR
		pnrBooking.setSsrList(Lists.newArrayList());
		RetrievePnrDataElements pnrDataElements = new RetrievePnrDataElements();
		pnrBooking.getSsrList().add(pnrDataElements);
		pnrDataElements.setType("UMNR");
		pnrDataElements.setFreeText("UM07");
		
		// Mock UMNR OSI
		pnrBooking.setOsiList(Lists.newArrayList());
		pnrDataElements = new RetrievePnrDataElements();
		pnrBooking.getOsiList().add(pnrDataElements);
		pnrDataElements.setType("OS");
		pnrDataElements.setOtherDataList(Lists.newArrayList());
		RetrievePnrDataElementsOtherData pnrDataElementsOtherData = new RetrievePnrDataElementsOtherData();
		pnrDataElementsOtherData.setFreeText("UMNR/HKG/NELSON WONG/852-10000002");
		pnrDataElements.getOtherDataList().add(pnrDataElementsOtherData);
		
		// Mock RetrievePnrPassenger
		RetrievePnrPassenger pnrPassenger = new RetrievePnrPassenger();
		pnrBooking.getPassengers().add(pnrPassenger);
		pnrPassenger.setFamilyName("FAMILY NAME");
		pnrPassenger.setGivenName("GIVEN NAME MS");
		pnrPassenger.setPrimaryPassenger(true);
		pnrPassenger.setPassengerID("1");

		// Mock RetrievePnrSegment #1
		RetrievePnrSegment pnrSegment = new RetrievePnrSegment();
		pnrBooking.getSegments().add(pnrSegment);
		pnrSegment.setSegmentID("1");
		pnrSegment.setPnrOperateCompany("CX");
		pnrSegment.setPnrOperateSegmentNumber("250");
		pnrSegment.setOriginPort("HKG");
		pnrSegment.setDestPort("NRT");
		pnrSegment.setDepartureTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getDepartureTime().setPnrTime("2025-12-01 11:00");
		pnrSegment.getDepartureTime().setTimeZoneOffset("+0800");
		pnrSegment.setArrivalTime(new RetrievePnrDepartureArrivalTime());
		pnrSegment.getArrivalTime().setPnrTime("2025-12-01 15:00");
		pnrSegment.getArrivalTime().setTimeZoneOffset("+0900");
		
		// actaul
		Assert.assertEquals(true, umnreFormBuildService.hasEFormRemark("1", pnrBooking));

	}
}
