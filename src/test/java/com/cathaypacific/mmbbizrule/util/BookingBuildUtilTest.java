package com.cathaypacific.mmbbizrule.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.config.BookingStatusConfig;
import com.cathaypacific.mmbbizrule.config.RtfsStatusConfig;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.Flight;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.FlightStatusData;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.SectorDTO;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.repository.FlightStatusRepository;
import com.cathaypacific.mmbbizrule.db.model.BookingStatus;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.model.booking.summary.SegmentSummary;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFFPInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;

@RunWith(MockitoJUnitRunner.class)
public class BookingBuildUtilTest {
	
	@Mock
	FlightStatusRepository flightStatusRepository;
	
	@Mock
	RtfsStatusConfig rtfsStatusConfig;
	
//	@Test
//	public void test() throws ParseException {
//		Segment segment=new Segment();
//		DepartureArrivalTime departureTime=new DepartureArrivalTime();
//		departureTime.setPnrTime("2018-03-22 14:00");
//		departureTime.setTimeZoneOffset("+0800");
//		segment.setDepartureTime(departureTime);
//		segment.setWithinNinetyMins(true);
//		segment.setOpenToCheckIn(true);
//		segment.setWithinTwentyFourHrs(true);
//		long NINETY_MINS_MILLIS = 5400000l;
//		long openWindow=1;
//		java.util.Date etd = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, segment.getDepartureTime().getTime(), segment.getDepartureTime().getTimeZoneOffset());
//		java.util.Date ninetyMinsDate=new java.util.Date();
//		ninetyMinsDate.setTime(System.currentTimeMillis() + NINETY_MINS_MILLIS);
//		long departureRemainingTime = etd.getTime() - ninetyMinsDate.getTime();
//		BookingBuildUtil.buildCheckinWindowInfo(segment, openWindow);
//		boolean str=false;
//		int i=segment.getCheckInRemainingTime().compareTo(String.valueOf(departureRemainingTime));
//		if(i==0||i>0){
//			str=true;
//		}
//		Assert.assertEquals(true,str);
//	}
//	
	
	@Test
	public void test2() {
		List<RetrievePnrDataElements> ssrList=new ArrayList<>();
		RetrievePnrDataElements sk=new RetrievePnrDataElements();
		sk.setType("GRPF");
		sk.setCompanyId("CX");
		ssrList.add(sk);	
		boolean b=BookingBuildUtil.isGroupBooking(ssrList);
		 Assert.assertEquals(true,b);
	}
	
	@Test
	public void test3() {
		List<String> pnrStatusList=new ArrayList<>();
		pnrStatusList.add("1");
		List<BookingStatus> availableBookingStatusList=new ArrayList<>();
		BookingStatus availableBookingStatus=new BookingStatus();
		availableBookingStatus.setStatusCode("1");
		availableBookingStatusList.add(availableBookingStatus);
		BookingStatus bookingStatus=BookingBuildUtil.getFirstAvailableStatus(pnrStatusList, availableBookingStatusList);
		Assert.assertEquals("1",bookingStatus.getStatusCode());
	}

	@Test
	public void test5() throws UnexpectedException {
		DepartureArrivalTime departureTime=new  DepartureArrivalTime();
		List<String> statusList=new ArrayList<>();
		statusList.add("1");
		statusList.add("B");
		BookingStatusConfig bookingStatusConfig=new BookingStatusConfig();
		bookingStatusConfig.setCancelledList("2");
		bookingStatusConfig.setConfirmedList("1");
		bookingStatusConfig.setWaitlistedList("1");
		List<String> pnrStatusList=new ArrayList<>();
		pnrStatusList.add("1");
		List<BookingStatus> availableBookingStatusList=new ArrayList<>();
		BookingStatus availableBookingStatus=new BookingStatus();
		availableBookingStatus.setStatusCode("1");
		availableBookingStatus.setAction("DISABLED");
		availableBookingStatusList.add(availableBookingStatus);
		long flightPassedTime=4;
		SegmentStatus segmentStatus= BookingBuildUtil.generateFlightStatus(departureTime, statusList, availableBookingStatusList, bookingStatusConfig,flightPassedTime);
		Assert.assertEquals(true,segmentStatus.isFlown());
		Assert.assertEquals("CF",segmentStatus.getStatus().getCode());
	}
	
	@Test
	public void test6() {
//		List<Summary> segmentSummaryList=new ArrayList<>();
//		SegmentSummary segmentSummary=new SegmentSummary();
//		DepartureArrivalTime departureTime=new DepartureArrivalTime();
//		departureTime.setPnrTime("2018-04-22 14:00");
//		departureTime.setTimeZoneOffset("+0800");
//		segmentSummary.setDepartureTime(departureTime);
//		int flightFlownLimithours=100;
//		segmentSummaryList.add(segmentSummary);
//		boolean bookingStatus=BookingBuildUtil.departureTimeLimitCheck(segmentSummaryList, flightFlownLimithours);
//		Assert.assertEquals(false,bookingStatus);
	}
	
	@Test
	public void test7() {
		String officeId="112CX0334";
		boolean bookingStatus=BookingBuildUtil.isAROBooking(officeId);
		Assert.assertEquals(true,bookingStatus);
	}
	
	@Test
	public void test8() {
		String pnrPaxType="INF";
		boolean bookingStatus=BookingBuildUtil.isInfant(pnrPaxType);
		Assert.assertEquals(true,bookingStatus);
	}
	
	@Test
	public void test9() {
		String officeId="112CA3334";
		boolean bookingStatus=BookingBuildUtil.isGDSBooking(officeId);
		Assert.assertEquals(true,bookingStatus);
	}
	
	@Test
	public void test10() {
		String officeId="112CX0334";
		String trainCase=BookingBuildUtil.getTrainCaseByOfficeId(officeId);
		Assert.assertEquals("TRAIN_CASE_ARO",trainCase);
	}
	
	@Test
	public void test11() {
		String status="12";
		BookingStatusConfig bookingStatusConfig=new BookingStatusConfig();	
		bookingStatusConfig.setCancelledList("12");
		bookingStatusConfig.setConfirmedList("12");
		bookingStatusConfig.setWaitlistedList("12");
		FlightStatusEnum trainCase=BookingBuildUtil.getFlightStatusEnum(status, bookingStatusConfig);
		Assert.assertEquals("CC",trainCase.getCode());
	}
	
	@Test
	public void test12() {
		String originPortOffset = "+0800";
		String destPortOffset = "+8000";
		SegmentSummary segmentSummary=new SegmentSummary();
		segmentSummary.setDestPort("CX");
		segmentSummary.setOriginPort("KA");;
		
		try {
			BookingBuildUtil.setTimeZone(originPortOffset, destPortOffset, segmentSummary);
		} catch (Exception e) {
			// TODO: handle exception
		}
		Assert.assertEquals("+8000",segmentSummary.getArrivalTime().getTimeZoneOffset());
		Assert.assertEquals("+0800",segmentSummary.getDepartureTime().getTimeZoneOffset());
	}
	
	@Test
	public void test13() throws ParseException {
		String timeFormat=DepartureArrivalTime.TIME_FORMAT; 
		int flightFlownLimithours=10; 
		java.util.Date date = new java.util.Date();
		date.setTime(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String depDateTime = simpleDateFormat.format(date);
		boolean bookingStatus=BookingBuildUtil.checkFlightInFlownLimitTimeWithoutTz(timeFormat, flightFlownLimithours, depDateTime);
		Assert.assertEquals(true,bookingStatus);
	}
	
	@Test
	public void test14() {
		String status="12";
		BookingStatusConfig bookingStatusConfig=new BookingStatusConfig();	
		bookingStatusConfig.setCancelledList("121");
		bookingStatusConfig.setConfirmedList("12");
		bookingStatusConfig.setWaitlistedList("12");
		FlightStatusEnum trainCase=BookingBuildUtil.getFlightStatusEnum(status, bookingStatusConfig);
		Assert.assertEquals("WL",trainCase.getCode());
	}
	
	@Test
	public void test15() {
		String status="12";
		BookingStatusConfig bookingStatusConfig=new BookingStatusConfig();	
		bookingStatusConfig.setCancelledList("121");
		bookingStatusConfig.setConfirmedList("12");
		bookingStatusConfig.setWaitlistedList("121");
		FlightStatusEnum trainCase=BookingBuildUtil.getFlightStatusEnum(status, bookingStatusConfig);
		Assert.assertEquals("CF",trainCase.getCode());
	}
	
	@Test
	public void test16() {
		String originPortOffset = "";
		String destPortOffset = "+8000";
		SegmentSummary segmentSummary=new SegmentSummary();
		segmentSummary.setDestPort("CX");
		segmentSummary.setOriginPort("KA");
		 Throwable t = null; 
		try{
			BookingBuildUtil.setTimeZone(originPortOffset, destPortOffset, segmentSummary);
		}catch(Exception ex){
			t=ex;
			assertNotNull(t);  
		    assertTrue(t instanceof UnexpectedException);  
		    assertTrue(t.getMessage().contains("Canot find time zone offset for airport:")); 
		}
	}
	
	@Test
	public void test17() throws UnexpectedException {
		DepartureArrivalTime departureTime=new  DepartureArrivalTime();
		List<String> statusList=new ArrayList<>();
		statusList.add("1");
		statusList.add("B");
		BookingStatusConfig bookingStatusConfig=new BookingStatusConfig();
		bookingStatusConfig.setCancelledList("2");
		bookingStatusConfig.setConfirmedList("1");
		bookingStatusConfig.setWaitlistedList("2");
		List<String> pnrStatusList=new ArrayList<>();
		pnrStatusList.add("1");
		List<BookingStatus> availableBookingStatusList=new ArrayList<>();
		BookingStatus availableBookingStatus=new BookingStatus();
		availableBookingStatus.setStatusCode("1");
		availableBookingStatus.setAction("DISABLED");
		availableBookingStatusList.add(availableBookingStatus);
		long flightPassedTime=4;
		SegmentStatus segmentStatus= BookingBuildUtil.generateFlightStatus(departureTime, statusList, availableBookingStatusList, bookingStatusConfig,flightPassedTime);
		Assert.assertEquals(true,segmentStatus.isFlown());
		Assert.assertEquals("CF",segmentStatus.getStatus().getCode());
	}
	
	@Test
	public void test18() throws UnexpectedException {
		DepartureArrivalTime departureTime=new  DepartureArrivalTime();
		List<String> statusList=new ArrayList<>();
		statusList.add("1");
		statusList.add("B");
		BookingStatusConfig bookingStatusConfig=new BookingStatusConfig();
		bookingStatusConfig.setCancelledList("1");
		bookingStatusConfig.setConfirmedList("2");
		bookingStatusConfig.setWaitlistedList("2");
		List<String> pnrStatusList=new ArrayList<>();
		pnrStatusList.add("1");
		List<BookingStatus> availableBookingStatusList=new ArrayList<>();
		BookingStatus availableBookingStatus=new BookingStatus();
		availableBookingStatus.setStatusCode("1");
		availableBookingStatus.setAction("DISABLED");
		availableBookingStatusList.add(availableBookingStatus);
		long flightPassedTime=4;
		SegmentStatus segmentStatus= BookingBuildUtil.generateFlightStatus(departureTime, statusList, availableBookingStatusList, bookingStatusConfig,flightPassedTime);
		Assert.assertEquals(false,segmentStatus.isFlown());
		Assert.assertEquals("CC",segmentStatus.getStatus().getCode());
	}
	
	@Test
	public void test20() {
		long compareResult=1;
		int trainCase=BookingBuildUtil.convertCompareResultToInt(compareResult);
		Assert.assertEquals(1,trainCase);
	}
	
	@Test
	public void test21() {
		long compareResult=-1;
		int trainCase=BookingBuildUtil.convertCompareResultToInt(compareResult);
		Assert.assertEquals(-1,trainCase);
	}
	
	@Test
	public void test22() {
		long compareResult=0;
		int trainCase=BookingBuildUtil.convertCompareResultToInt(compareResult);
		Assert.assertEquals(0,trainCase);
	}
	
	@Test
	public void isNonMiceGroupBooking_test() {
		List<RetrievePnrDataElements> ssrList=new ArrayList<>();
		RetrievePnrDataElements sk=new RetrievePnrDataElements();
		sk.setType("GRPF");
		ssrList.add(sk);	
		Boolean boolean1=BookingBuildUtil.isNonMiceGroupBooking(ssrList,ssrList);
		Assert.assertEquals(true,boolean1);
	}
	
	@Test
	public void incompleteRedemptionBookingDiabilityCheck_test() throws ExpectedException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginType("M");
		Booking booking=new Booking();
		List<RetrievePnrDataElements> skList=new ArrayList<>();
		/*RetrievePnrSsrSk sk=new RetrievePnrSsrSk();
		sk.setType("TPOS");
		skList.add(sk);*/
		booking.setSkList(skList);
		booking.setHasIssuedTicket(false);
		booking.setRedemptionBooking(true);
		RetrievePnrBooking pnrBooking=new RetrievePnrBooking();
		List<RetrievePnrPassengerSegment> passengerSegments=new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment=new RetrievePnrPassengerSegment();
		List<RetrievePnrFFPInfo> fQTRInfos=new ArrayList<>();
		RetrievePnrFFPInfo fQTRInfo=new RetrievePnrFFPInfo();
		fQTRInfo.setCompanyId("CX");
		fQTRInfo.setFfpCompany("CX");
		fQTRInfos.add(fQTRInfo);
		passengerSegment.setFQTRInfos(fQTRInfos);
		passengerSegments.add(passengerSegment);
		pnrBooking.setPassengerSegments(passengerSegments);
		boolean cxIncompleteRedemptionBooking = BookingBuildUtil.isCxIncompleteRedemptionBooking(booking, pnrBooking);
		Assert.assertEquals(true,cxIncompleteRedemptionBooking);
	}
	
	@Test
	public void incompleteRedemptionBookingDiabilityCheck_test1() throws ExpectedException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginType("M");
		Booking booking=new Booking();
		List<RetrievePnrDataElements> skList=new ArrayList<>();
		RetrievePnrDataElements sk=new RetrievePnrDataElements();
		sk.setType("TPOS");
		skList.add(sk);
		booking.setSkList(skList);
		booking.setHasIssuedTicket(true);
		booking.setRedemptionBooking(true);
		RetrievePnrBooking pnrBooking=new RetrievePnrBooking();
		List<RetrievePnrPassengerSegment> passengerSegments=new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment=new RetrievePnrPassengerSegment();
		List<RetrievePnrFFPInfo> fQTRInfos=new ArrayList<>();
		RetrievePnrFFPInfo fQTRInfo=new RetrievePnrFFPInfo();
		fQTRInfo.setCompanyId("CX");
		fQTRInfos.add(fQTRInfo);
		passengerSegment.setFQTRInfos(fQTRInfos);
		passengerSegments.add(passengerSegment);
		pnrBooking.setPassengerSegments(passengerSegments);
		boolean cxIncompleteRedemptionBooking = BookingBuildUtil.isCxIncompleteRedemptionBooking(booking, pnrBooking);
		Assert.assertEquals(false,cxIncompleteRedemptionBooking);
	}
	
	@Test
	public void hasMemberBooking_test1() throws ExpectedException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginType("R");
		Booking booking=new Booking();
		List<RetrievePnrDataElements> skList=new ArrayList<>();
		RetrievePnrDataElements sk=new RetrievePnrDataElements();
		sk.setType("TPOS");
		skList.add(sk);
		booking.setSkList(skList);
		booking.setHasIssuedTicket(true);
		booking.setRedemptionBooking(true);
		List<Passenger> passengers=new ArrayList<>();
		Passenger passenger1=new Passenger();
		passenger1.setLoginMember(false);
		passengers.add(passenger1);
		Passenger passenger2=new Passenger();
		passenger2.setLoginMember(false);
		passengers.add(passenger2);
		booking.setPassengers(passengers);
		Assert.assertEquals(false,BookingBuildUtil.hasMemberBooking(booking,loginInfo));
	}
	
	@Test
	public void hasMemberBooking_test2() throws ExpectedException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginType("M");
		Booking booking=new Booking();
		List<RetrievePnrDataElements> skList=new ArrayList<>();
		RetrievePnrDataElements sk=new RetrievePnrDataElements();
		sk.setType("TPOS");
		skList.add(sk);
		booking.setSkList(skList);
		booking.setHasIssuedTicket(true);
		booking.setRedemptionBooking(true);
		List<Passenger> passengers=new ArrayList<>();
		Passenger passenger1=new Passenger();
		passenger1.setLoginMember(true);
		passengers.add(passenger1);
		Passenger passenger2=new Passenger();
		passenger2.setLoginMember(false);
		passengers.add(passenger2);
		booking.setPassengers(passengers);
		Assert.assertEquals(true,BookingBuildUtil.hasMemberBooking(booking,loginInfo));
	}
	
	@Test
	public void hasSpecificFlight_test1() throws ExpectedException {
		List<FlightStatusData> flightStatusDataList = new ArrayList<>();
		FlightStatusData flightStatusData=new FlightStatusData();
		List<Flight> flights = new ArrayList<>();
		Flight operatingFlight =new Flight();
		operatingFlight.setCarrierCode("CX");
		operatingFlight.setFlightNumber("564");
		flights.add(operatingFlight);
		
		List<SectorDTO> sectors=new ArrayList<>();
		SectorDTO sector1=new SectorDTO();
		sector1.setSequenceId(1);
		sector1.setFlightStatus("delayed");
		sector1.setScenarioID(14);
		sector1.setOrigin("HKG");
		sector1.setCodeShareFlights(flights);
		sector1.setDestination("TPE");
		sectors.add(sector1);
		
		
		SectorDTO sector2=new SectorDTO();
		sector2.setSequenceId(2);
		sector2.setFlightStatus("delayed");
		sector2.setScenarioID(14);
		sector2.setOrigin("TPE");
		sector2.setCodeShareFlights(flights);
		sector2.setDestination("KIX");
		sectors.add(sector2);
		flightStatusData.setSectors(sectors);
		
		flightStatusData.setOperatingFlight(operatingFlight);
		flightStatusDataList.add(flightStatusData);
		when(rtfsStatusConfig.getCancelledList()).thenReturn(Arrays.asList("2","3"));
		when(rtfsStatusConfig.getDelayedList()).thenReturn(Arrays.asList("9","11","13","15","17","19"));
		when(rtfsStatusConfig.getReroutedList()).thenReturn(Arrays.asList("1"));
		Assert.assertEquals(false,BookingBuildUtil.hasSpecificFlight(sectors, rtfsStatusConfig));
	}
	
	@Test
	public void hasSpecificFlight_test2() throws ExpectedException {
		List<FlightStatusData> flightStatusDataList = new ArrayList<>();
		FlightStatusData flightStatusData=new FlightStatusData();
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
		sectors.add(sector1);
		
		
		SectorDTO sector2=new SectorDTO();
		sector2.setSequenceId(2);
		sector2.setFlightStatus("delayed");
		sector2.setScenarioID(11);
		sector2.setOrigin("TPE");
		sector2.setCodeShareFlights(flights);
		sector2.setDestination("KIX");
		sectors.add(sector2);
		flightStatusData.setSectors(sectors);
		
		flightStatusData.setOperatingFlight(operatingFlight);
		flightStatusDataList.add(flightStatusData);
		when(rtfsStatusConfig.getCancelledList()).thenReturn(Arrays.asList("2","3"));
		when(rtfsStatusConfig.getDelayedList()).thenReturn(Arrays.asList("9","11","13","15","17","19"));
		when(rtfsStatusConfig.getReroutedList()).thenReturn(Arrays.asList("1"));
		Assert.assertEquals(true,BookingBuildUtil.hasSpecificFlight(sectors, rtfsStatusConfig));
	}
}
