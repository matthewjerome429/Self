package com.cathaypacific.mmbbizrule.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.db.dao.TBSsrSkMappingDAO;
import com.cathaypacific.mmbbizrule.db.model.TBSsrSkMapping;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.PassengerPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.PassengerSegmentPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.SegmentPropertiesDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrStaffDetail;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

@RunWith(MockitoJUnitRunner.class)
public class BookingPropertiesBuildServiceImplTest {

	private Booking booking;
	
	private SimpleDateFormat dateFormat;
	
	@InjectMocks
	private BookingPropertiesBuildServiceImpl bookingPropertiesBuildServiceImpl;
	
	@Mock
	AEMService aemService;
	
	@Mock
	private TBSsrSkMappingDAO tBSsrSkMappingDao;
	
	@Before
	public void setUp(){
		List<TBSsrSkMapping> tbSsrSkMappings = new ArrayList<>();
		tbSsrSkMappings.add(new TBSsrSkMapping(null, "BLND", null, null, null, null, null, null, "E", null, null));
		tbSsrSkMappings.add(new TBSsrSkMapping(null, "CBBG", null, null, null, null, null, null, "E", null, null));
		tbSsrSkMappings.add(new TBSsrSkMapping(null, "DEAF", null, null, null, null, null, null, "E", null, null));
		tbSsrSkMappings.add(new TBSsrSkMapping(null, "DEPA", null, null, null, null, null, null, "E", null, null));
		tbSsrSkMappings.add(new TBSsrSkMapping(null, "DEPU", null, null, null, null, null, null, "E", null, null));
		tbSsrSkMappings.add(new TBSsrSkMapping(null, "EXST", null, null, null, null, null, null, "E", null, null));
		tbSsrSkMappings.add(new TBSsrSkMapping(null, "MEDA", null, null, null, null, null, null, "E", null, null));
		tbSsrSkMappings.add(new TBSsrSkMapping(null, "PETC", null, null, null, null, null, null, "E", null, null));
		tbSsrSkMappings.add(new TBSsrSkMapping(null, "STCR", null, null, null, null, null, null, "E", null, null));
		tbSsrSkMappings.add(new TBSsrSkMapping(null, "UMNR", null, null, null, null, null, null, "E", null, null));
		tbSsrSkMappings.add(new TBSsrSkMapping(null, "WCHC", null, null, null, null, null, null, "E", null, null));
		tbSsrSkMappings.add(new TBSsrSkMapping(null, "WCHS", null, null, null, null, null, null, "E", null, null));
		when(tBSsrSkMappingDao.findByAppCodeAndUpgradeBid(anyObject(),anyObject())).thenReturn(tbSsrSkMappings);
		
		when(aemService.getCountryCodeByPortCode("HKG")).thenReturn("CN");
		when(aemService.getCountryCodeByPortCode("TPE")).thenReturn("TW");
		when(aemService.getCountryCodeByPortCode("HND")).thenReturn("JP");
		
		dateFormat = new SimpleDateFormat(DepartureArrivalTime.TIME_FORMAT+"Z", Locale.ENGLISH); 
		
		booking = new Booking();
		booking.setPassengers( new ArrayList<>());
		booking.setSegments(new ArrayList<>());
		booking.setPassengerSegments(new ArrayList<>());
		booking.setSkList(new ArrayList<>());
		booking.setNonMiceGroupBooking(false);
		
		RetrievePnrDataElements sk = new RetrievePnrDataElements();
		sk.setType("BAGW");
		sk.setFreeText("10K/AUTHHKGZZCX/EACH/0001AAHKGCX0100");
		sk.setPassengerId("1");
		sk.setSegmentId("1");
		booking.getSkList().add(sk);
		
		
		Passenger passenger1 = new Passenger();
		Passenger passenger2 = new Passenger();
		
		passenger1.setPassengerId("1");
		passenger1.setPassengerType("ADT");
		passenger1.setFamilyName("TEST");
		passenger1.setGivenName("ONE");
		RetrievePnrStaffDetail staff = new RetrievePnrStaffDetail();
		passenger1.setStaffDetail(staff);
		
		passenger2.setPassengerId("1I");
		passenger2.setPassengerType("INF");
		passenger2.setFamilyName("TEST");
		passenger2.setGivenName("TWO");
		
		booking.getPassengers().add(passenger1);
		booking.getPassengers().add(passenger2);
		
		Segment segment1 = new Segment();
		Segment segment2 = new Segment();
		
		segment1.setSegmentID("1");
		segment1.setMarketCompany("CX");
		segment1.setMarketSegmentNumber("456");
		segment1.setOperateCompany("KA");
		segment1.setOperateSegmentNumber("666");
		
		Calendar currentTime = Calendar.getInstance();
		currentTime.setTimeInMillis(System.currentTimeMillis());
		currentTime.add(Calendar.HOUR_OF_DAY, 100);
		String dateStr = dateFormat.format(currentTime.getTime());
		DepartureArrivalTime departureTime1 = new DepartureArrivalTime();
		departureTime1.setTimeZoneOffset(dateStr.substring(dateStr.length()-5,dateStr.length()));
		departureTime1.setRtfsScheduledTime(dateStr.substring(0,dateStr.length()-5));
		segment1.setDepartureTime(departureTime1);
		
		segment2.setSegmentID("2");
		segment2.setMarketCompany("KA");
		segment2.setMarketSegmentNumber("233");
		segment2.setOperateCompany("BA");
		segment2.setOperateSegmentNumber("345");
		
		currentTime.setTimeInMillis(System.currentTimeMillis());
		currentTime.add(Calendar.HOUR_OF_DAY, 120);
		dateStr = dateFormat.format(currentTime.getTime());
		DepartureArrivalTime departureTime2 = new DepartureArrivalTime();
		departureTime2.setTimeZoneOffset(dateStr.substring(dateStr.length()-5,dateStr.length()));
		departureTime2.setRtfsScheduledTime(dateStr.substring(0,dateStr.length()-5));

		segment2.setDepartureTime(departureTime2);
		
		booking.getSegments().add(segment1);
		booking.getSegments().add(segment2);
		
		PassengerSegment passengerSegment1 = new PassengerSegment();
		PassengerSegment passengerSegment2 = new PassengerSegment();
		PassengerSegment passengerSegment3 = new PassengerSegment();
		PassengerSegment passengerSegment4 = new PassengerSegment();
		
		passengerSegment1.setPassengerId("1");
		passengerSegment1.setSegmentId("1");
		passengerSegment1.setEticketNumber("1604550000171");
		
		passengerSegment2.setPassengerId("1");
		passengerSegment2.setSegmentId("2");
		passengerSegment2.setEticketNumber("0434550000172");
		
		passengerSegment3.setPassengerId("1I");
		passengerSegment3.setSegmentId("1");
		passengerSegment3.setEticketNumber("1114550000173");
		
		passengerSegment4.setPassengerId("1I");
		passengerSegment4.setSegmentId("2");

		booking.getPassengerSegments().add(passengerSegment1);
		booking.getPassengerSegments().add(passengerSegment2);
		booking.getPassengerSegments().add(passengerSegment3);
		booking.getPassengerSegments().add(passengerSegment4);
		
	}
	
	@Test
	public void testBookingPorpertiesBuild() {
		BookingPropertiesDTO bookingProperties = bookingPropertiesBuildServiceImpl.buildBookingProperties(booking);
		
		assertNotNull(bookingProperties);
		assertFalse(bookingProperties.getGroupBooking());
		assertTrue(bookingProperties.getStaffBooking());
		
		List<PassengerPropertiesDTO> passengerProperties = bookingProperties.getPassengerProperties();
		List<SegmentPropertiesDTO> segmentProperties = bookingProperties.getSegmentProperties();
		List<PassengerSegmentPropertiesDTO> passengerSegmentProperties = bookingProperties.getPassengerSegmentProperties();
		assertEquals(2, passengerProperties.size());
		assertEquals(2, segmentProperties.size());
		assertEquals(4, passengerSegmentProperties.size());
		
		for(int i = 0 ; i < passengerProperties.size() ; i++){
			if(i == 0){
				assertEquals("1", passengerProperties.get(i).getPassengerId());
				assertFalse(passengerProperties.get(i).isInfant());
			} else if(i == 1){
				assertEquals("1I", passengerProperties.get(i).getPassengerId());
				assertTrue(passengerProperties.get(i).isInfant());
			}
		}
		
		for(int i = 0 ; i < segmentProperties.size() ; i++){
			if(i == 1){
				assertEquals("2", segmentProperties.get(i).getSegmentId());
				assertTrue(segmentProperties.get(i).isMarketedByCxKa());
				assertFalse(segmentProperties.get(i).isOperatedByCxKa());
			} else if(i == 0){
				assertEquals("1", segmentProperties.get(i).getSegmentId());
				assertTrue(segmentProperties.get(i).isMarketedByCxKa());
				assertTrue(segmentProperties.get(i).isOperatedByCxKa());
			}
		}
		
		for(int i = 0 ; i < passengerSegmentProperties.size() ; i++){
			if(i == 0){
				assertEquals("1", passengerSegmentProperties.get(i).getPassengerId());
				assertEquals("1", passengerSegmentProperties.get(i).getSegmentId());
				assertTrue(BooleanUtils.isTrue(passengerSegmentProperties.get(i).isEticketIssued()));
				assertTrue(BooleanUtils.isTrue(passengerSegmentProperties.get(i).isCxKaET()));
			} else if(i == 1){
				assertEquals("1", passengerSegmentProperties.get(i).getPassengerId());
				assertEquals("2", passengerSegmentProperties.get(i).getSegmentId());
				assertTrue(BooleanUtils.isTrue(passengerSegmentProperties.get(i).isEticketIssued()));
				assertTrue(BooleanUtils.isTrue(passengerSegmentProperties.get(i).isCxKaET()));
			} else if(i == 2){
				assertEquals("1I", passengerSegmentProperties.get(i).getPassengerId());
				assertEquals("1", passengerSegmentProperties.get(i).getSegmentId());
				assertTrue(BooleanUtils.isTrue(passengerSegmentProperties.get(i).isEticketIssued()));
				assertFalse(BooleanUtils.isTrue(passengerSegmentProperties.get(i).isCxKaET()));
			} else if(i == 3){
				assertEquals("1I", passengerSegmentProperties.get(i).getPassengerId());
				assertEquals("2", passengerSegmentProperties.get(i).getSegmentId());
				assertFalse(BooleanUtils.isTrue(passengerSegmentProperties.get(i).isEticketIssued()));
				assertFalse(BooleanUtils.isTrue(passengerSegmentProperties.get(i).isCxKaET()));
			}
		}
		
	}
	
}
