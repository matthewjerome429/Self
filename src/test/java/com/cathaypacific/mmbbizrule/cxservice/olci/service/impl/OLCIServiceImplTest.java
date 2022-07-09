package com.cathaypacific.mmbbizrule.cxservice.olci.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.olci.OLCIConnector;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.Flight;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.FlightStatusInfo;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.Journey;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.Passenger;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.PassengerCheckInInfo;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.RetrieveJourneyResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.RevenueIntegrityStatus;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.Seat;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.model.ConstantData;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.util.HttpResponse;

@RunWith(MockitoJUnitRunner.class)
public class OLCIServiceImplTest {
	
	@Mock
	private OLCIConnector olciConnector;
	
	@InjectMocks
	private OLCIServiceImpl olciServiceImpl;
	
	@Mock
	private ConstantDataDAO constantDataDAO;
	
	private HttpResponse<RetrieveJourneyResponseDTO> journeyResponse;
	
	private RetrieveJourneyResponseDTO retrieveJourneyResponseDTO;
	
	private List<Journey> journeys;
	
	private List<Passenger> passengers;
	
	private List<Flight> flights;
	
	private Booking booking;
	
	private LoginInfo loginInfo;
	
	@Before
	public void setUp() {
		
		List<ConstantData> constantDatas = new ArrayList<>();
		ConstantData constantData = new ConstantData();
		constantData.setValue("MR");
		constantDatas.add(constantData);
		when(constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE,OneAConstants.NAME_TITLE_TYPE_IN_DB)).thenReturn(constantDatas);
		
		ReflectionTestUtils.setField(
				olciServiceImpl, "shortCompareSize", 4);
		
		loginInfo = new LoginInfo();
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("TT");
		loginInfo.setLoginRloc("TESTRLOC");
		journeyResponse = new HttpResponse<RetrieveJourneyResponseDTO>();
		retrieveJourneyResponseDTO = new RetrieveJourneyResponseDTO();
		journeys = new ArrayList<>();
		passengers = new ArrayList<>();
		flights = new ArrayList<>();
		Journey journey = new Journey();
		Passenger passenger = new Passenger();
		Flight flight = new Flight();
		DepartureArrivalTime departureArrivalTime = new DepartureArrivalTime();
		departureArrivalTime.setRtfsScheduledTime("2018-03-22 14:00");
		flight.setDepartureTime(departureArrivalTime);
		flight.setOriginPort("HKG");
		flight.setDestPort("LON");
		flight.setOperateCompany("CX");
		flight.setOperateFlightNumber("520");
		flight.setMarketingCompany("CX");
		flight.setMarketFlightNumber("520");
		flight.setAcceptanceStatus("CAC");
		flight.setPortDisplayOnly(false);
		flight.setAssocETicketNumber("ETICKET");
		List<FlightStatusInfo> flightStatusList = new ArrayList<>();
		FlightStatusInfo flightStatusInfo = new FlightStatusInfo();
		flightStatusInfo.setIndicator("AC");
		flightStatusInfo.setAction("Action");
		
		flightStatusList.add(flightStatusInfo);
		flight.setFlightStatusList(flightStatusList);
		Seat seat = new Seat();
		seat.setSeatNum("045A");
		flight.setSeat(seat);
		booking = new Booking();
		List<Segment> segments = new ArrayList<>();
		Segment segment = new Segment();
		com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime departureTime = new com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime();
		departureTime.setPnrTime("2018-03-22 14:00");
		segment.setDepartureTime(departureTime);
		segment.setOriginPort("HKG");
		segment.setDestPort("LON");
		segment.setOperateCompany("CX");
		segment.setOperateSegmentNumber("520");
		segment.setMarketCompany("CX");
		segment.setMarketSegmentNumber("520");
		segments.add(segment);
		booking.setSegments(segments);
		flights.add(flight);
		passenger.setFlights(flights);
		passenger.setFamilyName("Test");
		passenger.setGivenName("TT");
		passenger.setDateOfBirth("1983-12-02");
		passenger.setUniqueCustomerId("customId");
		passengers.add(passenger);
		journey.setPassengers(passengers);
		journeys.add(journey);
		retrieveJourneyResponseDTO.setJourneys(journeys);
		journeyResponse.setValue(retrieveJourneyResponseDTO);
	}
	
	@Test
	public void test_getBRLPaxDetailsHappyPass() {
		booking.setOneARloc("TESTRLOC");
		com.cathaypacific.mmbbizrule.model.booking.detail.Passenger passenger = new com.cathaypacific.mmbbizrule.model.booking.detail.Passenger();
		passenger.setFamilyName("TEST");
		passenger.setGivenName("TT");
		passenger.setPrimaryPassenger(true);
		booking.getPassengers().add(passenger);
		when(olciConnector.getJourneyResponseByRLOC("TESTRLOC", "TT", "TEST")).thenReturn(journeyResponse);
		journeys.get(0).getPassengers().get(0).getFlights().get(0).setAcceptanceStatus("SSS");
		List<PassengerCheckInInfo> result = olciServiceImpl.getBRLPaxDetails(loginInfo, booking);
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getPassengerDetails().size());
		assertEquals("N", result.get(0).getPassengerDetails().get(0).getCheckInIndicator());
	}
	
	@Test
	public void test_getBRLPaxDetailsHappyPassACFlightStatus() {
		FlightStatusInfo gnFlightStatusInfo = new FlightStatusInfo();
		gnFlightStatusInfo.setIndicator("GN");
		gnFlightStatusInfo.setAction("Action");
		booking.setOneARloc("TESTRLOC");
		com.cathaypacific.mmbbizrule.model.booking.detail.Passenger passenger = new com.cathaypacific.mmbbizrule.model.booking.detail.Passenger();
		passenger.setFamilyName("TEST");
		passenger.setGivenName("TT");
		passenger.setPrimaryPassenger(true);
		booking.getPassengers().add(passenger);
		journeys.get(0).getPassengers().get(0).getFlights().get(0).getFlightStatusList().clear();
		journeys.get(0).getPassengers().get(0).getFlights().get(0).getFlightStatusList().add(gnFlightStatusInfo);
		when(olciConnector.getJourneyResponseByRLOC("TESTRLOC", "TT", "TEST")).thenReturn(journeyResponse);
		List<PassengerCheckInInfo> result = olciServiceImpl.getBRLPaxDetails(loginInfo, booking);
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getPassengerDetails().size());
		assertEquals("Y", result.get(0).getPassengerDetails().get(0).getCheckInIndicator());
	}
	
	@Test
	public void test_getBRLPaxDetailsMemberLoginHappyPass() {
		loginInfo.setLoginType(LoginInfo.LOGINTYPE_MEMBER);
		booking.setOneARloc("TESTRLOC");
		com.cathaypacific.mmbbizrule.model.booking.detail.Passenger passenger = new com.cathaypacific.mmbbizrule.model.booking.detail.Passenger();
		passenger.setFamilyName("TEST");
		passenger.setGivenName("TT");
		passenger.setPrimaryPassenger(true);
		booking.getPassengers().add(passenger);
		when(olciConnector.getJourneyResponseByRLOC("TESTRLOC", "TT", "TEST")).thenReturn(journeyResponse);
		journeys.get(0).getPassengers().get(0).getFlights().get(0).setAcceptanceStatus("SSS");
		List<PassengerCheckInInfo> result = olciServiceImpl.getBRLPaxDetails(loginInfo, booking);
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getPassengerDetails().size());
		assertEquals("N", result.get(0).getPassengerDetails().get(0).getCheckInIndicator());
	}
	
	@Test
	public void test_getBRLPaxDetailsDateFormatError() {
		loginInfo.setLoginType(LoginInfo.LOGINTYPE_MEMBER);
		booking.setOneARloc("TESTRLOC");
		com.cathaypacific.mmbbizrule.model.booking.detail.Passenger passenger = new com.cathaypacific.mmbbizrule.model.booking.detail.Passenger();
		passenger.setFamilyName("TEST");
		passenger.setGivenName("TT");
		passenger.setPrimaryPassenger(true);
		booking.getPassengers().add(passenger);
		when(olciConnector.getJourneyResponseByRLOC("TESTRLOC", "TT", "TEST")).thenReturn(journeyResponse);
		journeys.get(0).getPassengers().get(0).setDateOfBirth("2018-05-12");
		List<PassengerCheckInInfo> result = olciServiceImpl.getBRLPaxDetails(loginInfo, booking);
		assertEquals(1, result.size());
	}
	
	@Test
	public void test_getBRLPaxDetailsDisplayOnly() {
		loginInfo.setLoginType(LoginInfo.LOGINTYPE_MEMBER);
		booking.setOneARloc("TESTRLOC");
		com.cathaypacific.mmbbizrule.model.booking.detail.Passenger passenger = new com.cathaypacific.mmbbizrule.model.booking.detail.Passenger();
		passenger.setFamilyName("TEST");
		passenger.setGivenName("TT");
		passenger.setPrimaryPassenger(true);
		booking.getPassengers().add(passenger);
		RevenueIntegrityStatus revenueIntegrityStatus = new RevenueIntegrityStatus();
		revenueIntegrityStatus.setStatusCode("F");
		journeys.get(0).getPassengers().get(0).getFlights().get(0).setRevenueIntegrityStatus(revenueIntegrityStatus);
		when(olciConnector.getJourneyResponseByRLOC("TESTRLOC", "TT", "TEST")).thenReturn(journeyResponse);
		List<PassengerCheckInInfo> result = olciServiceImpl.getBRLPaxDetails(loginInfo, booking);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getPassengerDetails().get(0).isDisplayOnly());
	}

}
