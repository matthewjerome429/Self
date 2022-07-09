package com.cathaypacific.mmbbizrule.handler;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.config.RtfsStatusConfig;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.Flight;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.FlightStatusData;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.SectorDTO;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.service.FlightStatusService;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.service.impl.FlightStatusServiceImpl;
import com.cathaypacific.mmbbizrule.db.dao.BookingStatusDAO;
import com.cathaypacific.mmbbizrule.db.model.BookingStatus;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.RtfsFlightStatusInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;


@RunWith(MockitoJUnitRunner.class)
public class BookingBuildHelperTest {
	@InjectMocks
	private BookingBuildHelper bookingBuildHelper;
	
	@InjectMocks
	FlightStatusServiceImpl flightStatusServiceImpl;
	
	@Mock
	BookingStatusDAO bookingStatusDAO;
	
	@Mock
	RtfsStatusConfig rtfsStatusConfig;
	
	@Mock
	private FlightStatusService flightStatusService;
	
	private Segment segment;
	
	private List<FlightStatusData> flightStatusDataList;
	
	@Before
	public void setUp(){
		segment=new Segment();
		DepartureArrivalTime arrivalTime =new DepartureArrivalTime();
		arrivalTime.setRtfsScheduledTime("2018-03-25 22:00");
		segment.setSegmentID("1");
		segment.setOriginPort("HKG");
		segment.setDestPort("HKG");
		segment.setOperateSegmentNumber("112233");
		segment.setOperateCompany("CX");
		segment.findDepartureTime().setPnrTime("2018-03-25 08:00");
		segment.findDepartureTime().setTimeZoneOffset("+0800");
		segment.setArrivalTime(arrivalTime);
		
	}

	
	@Test
	public void getFirstAvailableStatus_Test() {
		List<String> statusList =new ArrayList<>();
		statusList.add("HK");
		statusList.add("CC");
		
		List<BookingStatus> bookingStatusDtos= new ArrayList<BookingStatus>(); 
		BookingStatus bookingStatusDto=new BookingStatus();
		bookingStatusDto.setAppCode("MMB");
		bookingStatusDto.setAction("ENABLED");
		bookingStatusDto.setStatusCode("HK");
		bookingStatusDto.setDescription("Holding Confirmed");
		bookingStatusDtos.add(bookingStatusDto);
		
		when(bookingStatusDAO.findAvailableStatus(TBConstants.APP_CODE)).thenReturn(bookingStatusDtos);
		BookingStatus bookingStatus=bookingBuildHelper.getFirstAvailableStatus(statusList);
		Assert.assertEquals("HK", bookingStatus.getStatusCode());
	}
	
	
	@Test
	public void populateFlightDetailTime_test() throws ParseException {
		flightStatusDataList = new ArrayList<>();
		FlightStatusData flightStatusData=new FlightStatusData();
		String da="2018-03-25 08:00";
		String inDepartEstimated="2018-03-25 10:00";
		String aDepAct="2018-03-25 12:00";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Flight> flights = new ArrayList<>();
		Flight operatingFlight =new Flight();
		operatingFlight.setCarrierCode("CX");
		operatingFlight.setFlightNumber("112233");
		flights.add(operatingFlight);
		List<SectorDTO> sectors=new ArrayList<>();
		SectorDTO sector=new SectorDTO();
		sector.setFlightStatus("yes");
		sector.setOrigin("HKG");
		sector.setCodeShareFlights(flights);
		sector.setDestination("HKG");
		sector.setDepartScheduled(simpleDateFormat.parse(da));
		sector.setDepartEstimated(simpleDateFormat.parse(inDepartEstimated));
		sector.setDepartActual(simpleDateFormat.parse(aDepAct));
		sectors.add(sector);
		flightStatusData.setSectors(sectors);
		flightStatusData.setOperatingFlight(operatingFlight);
		flightStatusDataList.add(flightStatusData);
		bookingBuildHelper.populateFlightDetailTime(segment,flightStatusDataList);
		
	}

	@Test
	public void populateFlightDetailTime_test2() {
		List<FlightStatusData> flightStatusDataList = new ArrayList<>();
		FlightStatusData flightStatusData=new FlightStatusData();
		Flight operatingFlight =new Flight();
		operatingFlight.setCarrierCode("CX");
		operatingFlight.setFlightNumber("112233");
		List<SectorDTO> sectors=new ArrayList<>();
		SectorDTO sector=new SectorDTO();
		sector.setFlightStatus("yes");
		sector.setOrigin("HKG");
		sectors.add(sector);
		flightStatusData.setSectors(sectors);
		flightStatusData.setOperatingFlight(operatingFlight);
		flightStatusDataList.add(flightStatusData);
		bookingBuildHelper.populateFlightDetailTime(segment,flightStatusDataList);
	}
	
	
	@Test
	public void populateFlightDetailTime_test3() throws ParseException {
		List<FlightStatusData> flightStatusDataList = new ArrayList<>();
		FlightStatusData flightStatusData=new FlightStatusData();
		String da="2018-03-25 08:00";
		String inDepartEstimated="2018-03-25 10:00";
		String aDepAct="2018-03-25 12:00";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Flight> flights = new ArrayList<>();
		Flight operatingFlight =new Flight();
		operatingFlight.setCarrierCode("CX");
		operatingFlight.setFlightNumber("112233");
		flights.add(operatingFlight);
		List<SectorDTO> sectors=new ArrayList<>();
		SectorDTO sector=new SectorDTO();
		sector.setFlightStatus("yes");
		sector.setOrigin("HKG");
		sector.setCodeShareFlights(flights);
		sector.setDestination("HKG");
		sector.setDepartScheduled(simpleDateFormat.parse(da));
		sector.setDepartEstimated(simpleDateFormat.parse(inDepartEstimated));
		sector.setDepartActual(simpleDateFormat.parse(aDepAct));
		sectors.add(sector);
		flightStatusData.setSectors(sectors);
		flightStatusData.setOperatingFlight(operatingFlight);
		flightStatusDataList.add(flightStatusData);
		segment.setDestPort("SYD");
		when(flightStatusService.retrieveFlightStatus(anyObject(), anyObject(), anyObject())).thenReturn(flightStatusDataList);
		bookingBuildHelper.populateFlightDetailTime(segment,flightStatusDataList);
	}	
	
	@Test
	public void buildRtfsStatusInfo_test1() throws ParseException {
		List<FlightStatusData> flightStatusDataList = new ArrayList<>();
		FlightStatusData flightStatusData=new FlightStatusData();
		String da=DateUtil.getCurrentCal2Str("yyyy-MM-dd HH:mm");;
		String inDepartEstimated="2018-03-25 10:00";
		String aDepAct="2018-03-25 12:00";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		
		List<Flight> flights = new ArrayList<>();
		Flight operatingFlight =new Flight();
		operatingFlight.setCarrierCode("CX");
		operatingFlight.setFlightNumber("564");
		flights.add(operatingFlight);
		
		List<SectorDTO> sectors=new ArrayList<>();
		SectorDTO sector1=new SectorDTO();
		sector1.setSequenceId(1);
		sector1.setFlightStatus("delayed");
		sector1.setScenarioID(9);
		sector1.setOrigin("HKG");
		sector1.setCodeShareFlights(flights);
		sector1.setDestination("TPE");
		sector1.setDepartScheduled(simpleDateFormat.parse(da));
		sector1.setDepartEstimated(simpleDateFormat.parse(inDepartEstimated));
		sector1.setDepartActual(simpleDateFormat.parse(aDepAct));
		sectors.add(sector1);
		
		
		SectorDTO sector2=new SectorDTO();
		sector2.setSequenceId(2);
		sector2.setFlightStatus("delayed");
		sector2.setScenarioID(11);
		sector2.setOrigin("TPE");
		sector2.setCodeShareFlights(flights);
		sector2.setDestination("KIX");
		sector2.setDepartScheduled(simpleDateFormat.parse(da));
		sector2.setDepartEstimated(simpleDateFormat.parse(inDepartEstimated));
		sector2.setDepartActual(simpleDateFormat.parse(aDepAct));
		sectors.add(sector2);
		flightStatusData.setSectors(sectors);
		
		flightStatusData.setOperatingFlight(operatingFlight);
		flightStatusDataList.add(flightStatusData);
		segment.setOriginPort("HKG");
		segment.setDestPort("KIX");
		segment.setOperateCompany("CX");
		segment.setOperateSegmentNumber("564");
		segment.findDepartureTime().setPnrTime(DateUtil.getCurrentCal2Str("yyyy-MM-dd HH:mm"));
		when(rtfsStatusConfig.getCancelledList()).thenReturn(Arrays.asList("2","3"));
		when(rtfsStatusConfig.getDelayedList()).thenReturn(Arrays.asList("9","11","13","15","17","19"));
		when(rtfsStatusConfig.getReroutedList()).thenReturn(Arrays.asList("1"));
		when(flightStatusService.retrieveFlightStatus(anyObject(), anyObject(), anyObject())).thenReturn(flightStatusDataList);
		Assert.assertEquals(2,flightStatusDataList.get(0).getSectors().size());
		bookingBuildHelper.populateRTFSStatus(segment,flightStatusDataList);
		
		RtfsFlightStatusInfo flightStatusInfo= segment.getRtfsFlightStatusInfo();
		Assert.assertEquals("DL",flightStatusInfo.getFlightStatus().getCode());
	}
	
	
	@Test
	public void buildRtfsStatusInfo_test2() throws ParseException {
		List<FlightStatusData> flightStatusDataList = new ArrayList<>();
		FlightStatusData flightStatusData=new FlightStatusData();
		String da=DateUtil.getCurrentCal2Str("yyyy-MM-dd HH:mm");;
		String inDepartEstimated="2018-03-25 10:00";
		String aDepAct="2018-03-25 12:00";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		
		List<Flight> flights = new ArrayList<>();
		Flight operatingFlight =new Flight();
		operatingFlight.setCarrierCode("CX");
		operatingFlight.setFlightNumber("564");
		flights.add(operatingFlight);
		
		List<SectorDTO> sectors=new ArrayList<>();
		SectorDTO sector1=new SectorDTO();
		sector1.setSequenceId(1);
		sector1.setFlightStatus("delayed");
		sector1.setScenarioID(1);
		sector1.setOrigin("HKG");
		sector1.setCodeShareFlights(flights);
		sector1.setDestination("KHH");
		sector1.setDepartScheduled(simpleDateFormat.parse(da));
		sector1.setDepartEstimated(simpleDateFormat.parse(inDepartEstimated));
		sector1.setDepartActual(simpleDateFormat.parse(aDepAct));
		sectors.add(sector1);
		
		SectorDTO sector2=new SectorDTO();
		sector2.setSequenceId(2);
		sector2.setFlightStatus("delayed");
		sector2.setScenarioID(6);
		sector2.setOrigin("TPE");
		sector2.setCodeShareFlights(flights);
		sector2.setDestination("HKG");
		sector2.setDepartScheduled(simpleDateFormat.parse(da));
		sector2.setDepartEstimated(simpleDateFormat.parse(inDepartEstimated));
		sector2.setDepartActual(simpleDateFormat.parse(aDepAct));
		sectors.add(sector2);
		
		SectorDTO sector3=new SectorDTO();
		sector3.setSequenceId(1);
		sector3.setFlightStatus("delayed");
		sector3.setScenarioID(11);
		sector3.setOrigin("HKG");
		sector3.setCodeShareFlights(flights);
		sector3.setDestination("TPE");
		sector3.setDepartScheduled(simpleDateFormat.parse(da));
		sector3.setDepartEstimated(simpleDateFormat.parse(inDepartEstimated));
		sector3.setDepartActual(simpleDateFormat.parse(aDepAct));
		sectors.add(sector3);
		flightStatusData.setSectors(sectors);
		
		flightStatusData.setOperatingFlight(operatingFlight);
		flightStatusDataList.add(flightStatusData);
		segment.setOriginPort("HKG");
		segment.setDestPort("KHH");
		segment.setOperateCompany("CX");
		segment.setOperateSegmentNumber("564");
		segment.findDepartureTime().setPnrTime(DateUtil.getCurrentCal2Str("yyyy-MM-dd HH:mm"));
		when(rtfsStatusConfig.getCancelledList()).thenReturn(Arrays.asList("2","3"));
		when(rtfsStatusConfig.getDelayedList()).thenReturn(Arrays.asList("9","11","13","15","17","19"));
		when(rtfsStatusConfig.getReroutedList()).thenReturn(Arrays.asList("1"));
		Assert.assertEquals(3,flightStatusDataList.get(0).getSectors().size());
		
		bookingBuildHelper.populateRTFSStatus(segment,flightStatusDataList);
		RtfsFlightStatusInfo flightStatusInfo= segment.getRtfsFlightStatusInfo();
		Assert.assertEquals(3,flightStatusDataList.get(0).getSectors().size());
		
		Assert.assertEquals("RR",flightStatusInfo.getFlightStatus().getCode());
	}
}
