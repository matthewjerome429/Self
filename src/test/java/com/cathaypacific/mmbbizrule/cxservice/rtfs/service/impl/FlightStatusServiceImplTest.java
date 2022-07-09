package com.cathaypacific.mmbbizrule.cxservice.rtfs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.cxservice.rtfs.repository.FlightStatusRepository;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;

@RunWith(MockitoJUnitRunner.class)
public class FlightStatusServiceImplTest {
	
	@InjectMocks
	FlightStatusServiceImpl flightStatusServiceImpl;
	
	@Mock
	private FlightStatusRepository flightStatusRepository;
	
	private Booking booking;
	
	@Before
	public void setUp(){
		List<Segment> segments=new ArrayList<>();
		booking = new Booking();
		Segment segment=new Segment();
		DepartureArrivalTime arrivalTime =new DepartureArrivalTime();
		arrivalTime.setRtfsScheduledTime("2018-03-25 22:00");
		segment.setSegmentID("8");
		segment.setOriginPort("HKG");
		segment.setDestPort("HKG");
		segment.setOperateSegmentNumber("112233");
		segment.setOperateCompany("CX");
		segment.findDepartureTime().setPnrTime("2018-03-25 08:00");
		segment.findDepartureTime().setTimeZoneOffset("+0800");
		segment.setArrivalTime(arrivalTime);
		segments.add(segment);
		booking.setSegments(segments);
	}

//	@Test
//	public void test() throws ParseException {
//		List<FlightStatusData> flightStatusDataList = new ArrayList<>();
//		FlightStatusData flightStatusData=new FlightStatusData();
//		String da="2018-03-25 08:00";
//		String inDepartEstimated="2018-03-25 10:00";
//		String aDepAct="2018-03-25 12:00";
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		List<Flight> flights = new ArrayList<>();
//		Flight operatingFlight =new Flight();
//		operatingFlight.setCarrierCode("CX");
//		operatingFlight.setFlightNumber("112233");
//		flights.add(operatingFlight);
//		List<SectorDTO> sectors=new ArrayList<>();
//		SectorDTO sector=new SectorDTO();
//		sector.setFlightStatus("yes");
//		sector.setOrigin("HKG");
//		sector.setCodeShareFlights(flights);
//		sector.setDestination("HKG");
//		sector.setDepartScheduled(simpleDateFormat.parse(da));
//		sector.setDepartEstimated(simpleDateFormat.parse(inDepartEstimated));
//		sector.setDepartActual(simpleDateFormat.parse(aDepAct));
//		sectors.add(sector);
//		flightStatusData.setSectors(sectors);
//		flightStatusData.setOperatingFlight(operatingFlight);
//		flightStatusDataList.add(flightStatusData);
//		when(flightStatusRepository.findByFlightNumber(Matchers.anyString(),
//				Matchers.anyString(),Matchers.anyString())).thenReturn(flightStatusDataList);
//		flightStatusServiceImpl.populateFlightDetailTime(booking);
//	}
//	
	@Test
	public void test1() {
		String company = booking.getSegments().get(0).getOperateCompany();
		String travelTime = booking.getSegments().get(0).getDepartureTime().getPnrTime();
		String flightNumber = booking.getSegments().get(0).getOperateSegmentNumber();
		flightStatusServiceImpl.retrieveFlightStatus(company, flightNumber, travelTime);
	}
//	
//	@Test
//	public void test4() {
//		booking.getSegments().get(0).findDepartureTime().setPnrTime(null);
//		flightStatusServiceImpl.populateFlightDetailTime(booking);
//	}
//	
//	@Test
//	public void test2() {
//		List<FlightStatusData> flightStatusDataList = new ArrayList<>();
//		FlightStatusData flightStatusData=new FlightStatusData();
//		Flight operatingFlight =new Flight();
//		operatingFlight.setCarrierCode("CX");
//		operatingFlight.setFlightNumber("112233");
//		List<SectorDTO> sectors=new ArrayList<>();
//		SectorDTO sector=new SectorDTO();
//		sector.setFlightStatus("yes");
//		sector.setOrigin("HKG");
//		sectors.add(sector);
//		flightStatusData.setSectors(sectors);
//		flightStatusData.setOperatingFlight(operatingFlight);
//		flightStatusDataList.add(flightStatusData);
//		when(flightStatusRepository.findByFlightNumber(Matchers.anyString(),
//				Matchers.anyString(),Matchers.anyString())).thenReturn(flightStatusDataList);
//		flightStatusServiceImpl.populateFlightDetailTime(booking);
//	}
//	
//	@Test
//	public void test3() throws ParseException {
//		List<FlightStatusData> flightStatusDataList = new ArrayList<>();
//		FlightStatusData flightStatusData=new FlightStatusData();
//		String da="2018-03-25 08:00";
//		String inDepartEstimated="2018-03-25 10:00";
//		String aDepAct="2018-03-25 12:00";
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		List<Flight> flights = new ArrayList<>();
//		Flight operatingFlight =new Flight();
//		operatingFlight.setCarrierCode("CX");
//		operatingFlight.setFlightNumber("112233");
//		flights.add(operatingFlight);
//		List<SectorDTO> sectors=new ArrayList<>();
//		SectorDTO sector=new SectorDTO();
//		sector.setFlightStatus("yes");
//		sector.setOrigin("HKG");
//		sector.setCodeShareFlights(flights);
//		sector.setDestination("HKG");
//		sector.setDepartScheduled(simpleDateFormat.parse(da));
//		sector.setDepartEstimated(simpleDateFormat.parse(inDepartEstimated));
//		sector.setDepartActual(simpleDateFormat.parse(aDepAct));
//		sectors.add(sector);
//		flightStatusData.setSectors(sectors);
//		flightStatusData.setOperatingFlight(operatingFlight);
//		flightStatusDataList.add(flightStatusData);
//		booking.getSegments().get(0).setDestPort("SYD");
//		when(flightStatusRepository.findByFlightNumber(Matchers.anyString(),
//				Matchers.anyString(),Matchers.anyString())).thenReturn(flightStatusDataList);
//		flightStatusServiceImpl.populateFlightDetailTime(booking);
//	}
}
