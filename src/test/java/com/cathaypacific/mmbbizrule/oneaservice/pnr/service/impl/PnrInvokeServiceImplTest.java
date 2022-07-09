package com.cathaypacific.mmbbizrule.oneaservice.pnr.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.cathaypacific.mmbbizrule.BaseTest;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.dao.StatusManagementDAO;
import com.cathaypacific.mmbbizrule.db.dao.TbSsrTypeDAO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.service.AirFlightInfoService;
import com.cathaypacific.mmbbizrule.oneaservice.convertoarloc.service.ConvertOARlocService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.eodsconsumer.util.MarshallerFactory;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;
/**
 * 
 * OLSS-MMB
 * 
 * @Desc: GDS RLOC Login test
 * @author: fengfeng.jiang
 * @date: Dec 8, 2017 12:50:12 PM
 * @version: V1.0
 * 
 */

@RunWith(MockitoJUnitRunner.class)
public class PnrInvokeServiceImplTest extends BaseTest{
	private Booking booking = new Booking();
	
	@Mock
	private OneAWSClient oneAWSClient;
	@Mock
	private BizRuleConfig bizRuleConfig;
	@Mock
	private ConvertOARlocService convertOARlocService;
	@Mock
	private ConstantDataDAO constantDataDAO;
	@Mock
	private TbSsrTypeDAO tbSsrTypeDAO;
	@InjectMocks
	private PnrInvokeServiceImpl pnrInvokeService;
	@Mock
	private AirFlightInfoService airFlightInfoService;
	@Mock
	private StatusManagementDAO statusManagementDAO;
	@Mock
	private PnrResponseParser pnrResponseParser;
	
	@Before
	public void setUp() throws Exception{
		booking.setOneARloc("JU6AKW");
		booking.setCanCheckIn(true);
		
		List<Passenger> passengers = new ArrayList<Passenger>();
		Passenger passenger = new Passenger();
		passenger.setPassengerId("1");
		passenger.setFamilyName("RUSH");
		passenger.setGivenName("SIMONMR");
		passenger.setPrimaryPassenger(true);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		
		List<Segment> segments = new ArrayList<Segment>();
		Segment segment = new Segment();
		segment.setSegmentID("8");
		segment.setOriginPort("SYD");
		segment.setDestPort("HKG");
		segment.setSegmentID("8");
		segments.add(segment);
		booking.setSegments(segments);
		
		List<PassengerSegment> passengerSegments = new ArrayList<PassengerSegment>();
		PassengerSegment passengerSegment = new PassengerSegment();
		passengerSegment.setPassengerId("1");
		passengerSegment.setSegmentId("1");
		passengerSegments.add(passengerSegment);
		booking.setPassengerSegments(passengerSegments);
		
		when(bizRuleConfig.getCxkaTierLevel()).thenReturn(Arrays.asList("IN,DM,DMP,GO,SL,GR".split(",")));
		when(bizRuleConfig.getOneworldTierLevel()).thenReturn(Arrays.asList("RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTopTier()).thenReturn(Arrays.asList("IN,DM,GO,SL,RUBY,SAPH,EMER".split(",")));
		//when(airFlightInfoService.getAirFlightInfo(any(), any(), any(), any(), any(), any())).thenReturn(null);
	}
	  
	@Test
	public void test_retrievePnrByRloc_withrloc() throws Exception {
		Resource resource = new ClassPathResource("xml/PNRReply.xml");
		InputStream is = resource.getInputStream();
		Jaxb2Marshaller marshaller =MarshallerFactory.getMarshaller(PNRReply.class);
		PNRReply pnrReply = (PNRReply) marshaller.unmarshal(new StreamSource(is));
		when(oneAWSClient.retrievePnr(anyObject())).thenReturn(pnrReply);
		
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		pnrBooking.setOneARloc("JU6AKW");
		RetrievePnrPassenger pnrPassenger = new RetrievePnrPassenger();
		pnrPassenger.setPassengerID("1");
		pnrPassenger.setFamilyName("RUSH");
		pnrPassenger.setGivenName("SIMONMR");
		List<RetrievePnrPassenger> pnrPassengers = new ArrayList<>();
		pnrPassengers.add(pnrPassenger);
		pnrBooking.setPassengers(pnrPassengers);
		
		when(pnrResponseParser.paserResponse(pnrReply)).thenReturn(pnrBooking);
		
		RetrievePnrBooking booking1 = pnrInvokeService.retrievePnrByRloc("JU6AKW");
		
		assertNotNull(booking1);
		assertEquals("JU6AKW",booking1.getOneARloc());
		
		RetrievePnrPassenger passenger = booking1.getPassengers().get(0);
		assertEquals("RUSH",passenger.getFamilyName());
		assertEquals("SIMONMR",passenger.getGivenName());
		assertEquals("1",passenger.getPassengerID());
	}
	
	@Test
	public void test_retrievePnrByRloc_withIllegalRloc() throws Exception {
		when(oneAWSClient.retrievePnr(anyObject())).thenReturn(null);
		
		RetrievePnrBooking booking1 = pnrInvokeService.retrievePnrByRloc("111111");
		
		assertNull(booking1);
	}
	
	@Test
	public void test_retrievePnrByRloc_withgdsRloc() throws Exception {
		Resource resource = new ClassPathResource("xml/PNRReply.xml");
		InputStream is = resource.getInputStream();
		Jaxb2Marshaller marshaller = MarshallerFactory.getMarshaller(PNRReply.class);
		PNRReply pnrReply = (PNRReply) marshaller.unmarshal(new StreamSource(is));
		when(oneAWSClient.retrievePnr(anyObject())).thenReturn(null).thenReturn(pnrReply);
		when(convertOARlocService.getRlocByGDSRloc("6RCLXK")).thenReturn("JU6AKW");
		
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		pnrBooking.setOneARloc("JU6AKW");
		RetrievePnrPassenger pnrPassenger = new RetrievePnrPassenger();
		pnrPassenger.setPassengerID("1");
		pnrPassenger.setFamilyName("RUSH");
		pnrPassenger.setGivenName("SIMONMR");
		List<RetrievePnrPassenger> pnrPassengers = new ArrayList<>();
		pnrPassengers.add(pnrPassenger);
		pnrBooking.setPassengers(pnrPassengers);
		
		when(pnrResponseParser.paserResponse(anyObject())).thenReturn(pnrBooking);
		
		RetrievePnrBooking booking1 = pnrInvokeService.retrievePnrByRloc("6RCLXK");
		assertNotNull(booking1);
		assertEquals("JU6AKW",booking1.getOneARloc());
		
		RetrievePnrPassenger passenger = booking1.getPassengers().get(0);
		assertEquals("RUSH",passenger.getFamilyName());
		assertEquals("SIMONMR",passenger.getGivenName());
		assertEquals("1",passenger.getPassengerID());
	}
	
	@Test
	public void test_retrievePnrByRloc_withIllegalGdsRloc() throws Exception {
		when(oneAWSClient.retrievePnr(anyObject())).thenReturn(null);
		when(convertOARlocService.getRlocByGDSRloc("111111")).thenReturn(null);
		
		RetrievePnrBooking booking1 = pnrInvokeService.retrievePnrByRloc("111111");
		assertNull(booking1);
	}
}