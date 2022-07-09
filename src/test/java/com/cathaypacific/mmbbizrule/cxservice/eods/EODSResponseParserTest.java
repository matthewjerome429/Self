package com.cathaypacific.mmbbizrule.cxservice.eods;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.cxservice.eods.model.EODSBooking;
import com.cathaypacific.eodsconsumer.model.response.bookingsummary_v1.BookingSummary;
import com.cathaypacific.eodsconsumer.model.response.bookingsummary_v1.BookingSummaryInfo;
import com.cathaypacific.eodsconsumer.model.response.bookingsummary_v1.BookingSummaryRS;
import com.cathaypacific.eodsconsumer.model.response.bookingsummary_v1.Passenger;
import com.cathaypacific.eodsconsumer.model.response.bookingsummary_v1.Segment;


@RunWith(MockitoJUnitRunner.class)
public class EODSResponseParserTest {
	
	private EODSResponseParser eodsResponseParser;
	
	@Mock
	private BookingSummaryRS bookingSummaryRS;
	
	@Mock
	private BookingSummary bookingSummary;
	
	@Before
	public void setUp () throws DatatypeConfigurationException {
		eodsResponseParser = new EODSResponseParser();
		List<BookingSummaryInfo> bookingSummaryInfos = new ArrayList<>();
		BookingSummaryInfo bookingSummaryInfo = new BookingSummaryInfo();
		
		bookingSummaryInfo.setRLOC("TEST");
		
		Passenger passenger = new Passenger();
		passenger.setPassengerFirstName("FisrtName");
		passenger.setPassengerLastName("LastName");
		bookingSummaryInfo.setPassenger(passenger);
		
		Segment segment = new Segment();
		segment.setSegmentNo(1);
		segment.setBookingType("TestBT");
		segment.setCarrierCode("XC");
		segment.setOrigin("KIX");
		segment.setDestination("HKG");
		segment.setFlightNo("567");
		segment.setStatus("HK");
		Date time = new Date();
		time.setTime(1517903165118l);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(time);
		XMLGregorianCalendar departureDatetime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		segment.setScheduleArrivalDatetime(departureDatetime);
		segment.setScheduleDepartureDatetime(departureDatetime);
		bookingSummaryInfo.setSegment(segment);
		
		BookingSummaryInfo bookingSummaryInfo2 = new BookingSummaryInfo();
		bookingSummaryInfo2.setRLOC("TEST");
		
		Passenger passenger2 = new Passenger();
		passenger2.setPassengerFirstName("FisrtName");
		passenger2.setPassengerLastName("LastName");
		bookingSummaryInfo2.setPassenger(passenger);
		
		Segment segment2 = new Segment();
		segment2.setSegmentNo(2);
		segment2.setBookingType("TestBT");
		segment2.setCarrierCode("XC");
		segment2.setOrigin("KIX");
		segment2.setDestination("HKG");
		segment2.setFlightNo("567");
		segment2.setStatus("HK");
		segment2.setScheduleArrivalDatetime(departureDatetime);
		segment2.setScheduleDepartureDatetime(departureDatetime);
		bookingSummaryInfo2.setSegment(segment2);
		
		bookingSummaryInfos.add(bookingSummaryInfo);
		bookingSummaryInfos.add(bookingSummaryInfo2);
		
		when(bookingSummaryRS.getBookingSummary()).thenReturn(bookingSummary);
		when(bookingSummary.getBookingSummaryInfo()).thenReturn(bookingSummaryInfos);
	}
	
	@Test
	public void testParseResponseAndBuildToBookingList () {
		
		List<EODSBooking> eodsBookings = eodsResponseParser.parseResponseAndBuildToBookingList(bookingSummaryRS);
		assertEquals(1, eodsBookings.size());
		EODSBooking booking = eodsBookings.get(0);
		assertEquals("TestBT", booking.getBookingType());
		assertEquals("TEST", booking.getRloc());
		
		assertEquals(1, booking.getPassengers().size());
		assertEquals("LastName", booking.getPassengers().get(0).getFamilyName());
		assertEquals("FisrtName", booking.getPassengers().get(0).getGivenName());
		
		assertEquals(2, booking.getSegments().size());
		assertEquals("XC", booking.getSegments().get(0).getCompany());
		assertEquals("2018-02-06 15:46", booking.getSegments().get(0).getArrDateTime());
		assertEquals("2018-02-06 15:46", booking.getSegments().get(0).getDepDateTime());
		assertEquals("KIX", booking.getSegments().get(0).getOriginPort());
		assertEquals("HKG", booking.getSegments().get(0).getDestPort());
		assertEquals("567", booking.getSegments().get(0).getNumber());
		assertEquals("HK", booking.getSegments().get(0).getStatus());
		
		
	}

}
