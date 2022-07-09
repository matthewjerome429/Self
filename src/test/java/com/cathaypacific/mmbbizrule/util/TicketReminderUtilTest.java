package com.cathaypacific.mmbbizrule.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.constant.TicketReminderConstant;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.model.booking.detail.TicketIssueInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;

@RunWith(MockitoJUnitRunner.class)
public class TicketReminderUtilTest {
	
	private Booking booking;
	
	
	@Before
	public void setUp() {
		this.booking = new Booking();
		List<Segment> segments = new ArrayList<>();
		Segment segment = new Segment();
		segment.setOperateCompany("CX");
		DepartureArrivalTime departureTime = new DepartureArrivalTime();
		departureTime.setTimeZoneOffset("+0900");
		segment.setDepartureTime(departureTime);
		segment.setSegmentID("1");
		segments.add(segment);
		booking.setSegments(segments);
		TicketIssueInfo ticketIssueInfo=new TicketIssueInfo();
		ticketIssueInfo.setDate("100318");
		ticketIssueInfo.setTime("1400");
		ticketIssueInfo.setTimeZoneOffset("+0900");
		ticketIssueInfo.setIndicator("XL");
		ticketIssueInfo.setOfficeId("hkgcx08mc");
		booking.setTicketIssueInfo(ticketIssueInfo);
		List<RetrievePnrDataElements> ssrList = new ArrayList<>();
		RetrievePnrDataElements ssr = new RetrievePnrDataElements();
		ssr.setSegmentId("1");
		ssr.setFreeText("Free Text");
		ssr.setSegmentName("SegmentName");
		ssrList.add(ssr);
		List<RetrievePnrDataElements> skList = new ArrayList<>();
		RetrievePnrDataElements sk = new RetrievePnrDataElements();
		sk.setSegmentId("2");
		sk.setFreeText("Free Text");
		skList.add(sk);
		sk.setSegmentName("SegmentName");
		
		PassengerSegment ps = new PassengerSegment();
		ps.setPassengerId("1");
		ps.setSegmentId("1");
		booking.getPassengerSegments().add(ps);
		this.booking.setSsrList(ssrList);
		this.booking.setSkList(skList);
		
		
	}
	
	@Test
	public void test_BuildReminderInfosHappyPass() {
		
		TicketReminderUtil.buildReminderInfos(booking);
		assertFalse(booking.isAdtk());
		assertFalse(booking.isPcc());
		assertFalse(booking.isTktl());
		assertTrue(booking.isTkxl());
		
	}
	
	@Test
	public void test_BuildReminderInfosADTKSegmentLevel() {
		booking.getSsrList().get(0).setType(TicketReminderConstant.ADTK);
		TicketReminderUtil.buildReminderInfos(booking);
		assertTrue(booking.getSegments().get(0).isAdtk());
	}
	
	@Test
	public void test_BuildReminderInfosMultipleADTKSegmentLevel() {
		booking.getSsrList().get(0).setType(TicketReminderConstant.ADTK);
		booking.getSkList().get(0).setType(TicketReminderConstant.ADTK);
		Segment segment = new Segment();
		segment.setOperateCompany("CX");
		DepartureArrivalTime departureTime = new DepartureArrivalTime();
		departureTime.setTimeZoneOffset("+0900");
		segment.setDepartureTime(departureTime);
		segment.setSegmentID("2");
		booking.getSegments().add(segment);
		TicketReminderUtil.buildReminderInfos(booking);
		assertTrue(booking.isAdtk());
	}
	
	@Test
	public void test_BuildReminderInfosADTKBookingLevel() {
		booking.getSsrList().get(0).setType(TicketReminderConstant.ADTK);
		booking.getSegments().get(0).setSegmentID("2");
		TicketReminderUtil.buildReminderInfos(booking);
		assertTrue(booking.isAdtk());
	}
	
	@Test
	public void test_BuildReminderInfosPCCSegmentLevel() {
		booking.getSkList().get(0).setType(TicketReminderConstant.CK_TYPE);
		booking.getSkList().get(0).setFreeText(TicketReminderConstant.MUST_VERIFY);
		booking.getSegments().get(0).setSegmentID("2");
		SegmentStatus segmentStatus = new SegmentStatus();
		segmentStatus.setFlown(false);
		booking.getSegments().get(0).setSegmentStatus(segmentStatus);
		TicketReminderUtil.buildReminderInfos(booking);
		assertTrue(booking.getSegments().get(0).isPcc());
	}
	
	@Test
	public void test_BuildReminderInfosMultiplePCCSegmentLevel() {
		booking.getSkList().get(0).setType(TicketReminderConstant.CK_TYPE);
		booking.getSkList().get(0).setFreeText(TicketReminderConstant.MUST_VERIFY);
		
		booking.getSsrList().get(0).setType(TicketReminderConstant.CK_TYPE);
		booking.getSsrList().get(0).setFreeText(TicketReminderConstant.MUST_VERIFY);
		
		booking.getSegments().get(0).setSegmentID("2");
		SegmentStatus segmentStatus = new SegmentStatus();
		segmentStatus.setFlown(false);
		booking.getSegments().get(0).setSegmentStatus(segmentStatus);
		
		Segment segment = new Segment();
		segment.setOperateCompany("CX");
		DepartureArrivalTime departureTime = new DepartureArrivalTime();
		departureTime.setTimeZoneOffset("+0900");
		segment.setDepartureTime(departureTime);
		segment.setSegmentID("1");
		booking.getSegments().get(0).setSegmentStatus(segmentStatus);
		booking.getSegments().add(segment);
		TicketReminderUtil.buildReminderInfos(booking);
		assertTrue(booking.isPcc());
	}
	
	@Test
	public void test_BuildReminderInfosPCCBookingLevel() {
		booking.getSsrList().get(0).setType(TicketReminderConstant.CK_TYPE);
		booking.getSsrList().get(0).setFreeText(TicketReminderConstant.MUST_VERIFY);
		booking.getSegments().get(0).setSegmentID("2");
		SegmentStatus segmentStatus = new SegmentStatus();
		segmentStatus.setFlown(false);
		booking.getSegments().get(0).setSegmentStatus(segmentStatus);
		TicketReminderUtil.buildReminderInfos(booking);
		assertTrue(booking.isPcc());
	}
	
	@Test
	public void test_BuildReminderInfosTKTLNoETickets() {
		booking.getPassengerSegments().get(0).setEticketNumber("RTICKET");
		TicketReminderUtil.buildReminderInfos(booking);
		assertFalse(booking.isTktl());
	}
	
	@Test
	public void test_BuildReminderInfosTKTL() {
		TicketReminderUtil.buildReminderInfos(booking);
		assertFalse(booking.isTktl());
	}
	
	@Test
	public void test_BuildReminderInfosTKXL() {
		booking.getTicketIssueInfo().setIndicator("XL");
		TicketReminderUtil.buildReminderInfos(booking);
		assertTrue(booking.isTkxl());
	}
	
	@Test
	public void test_BuildReminderInfosTKXLDifferentTimeZone() {
		booking.getTicketIssueInfo().setIndicator("XL");
		booking.getTicketIssueInfo().setTimeZoneOffset("+0800");
		TicketReminderUtil.buildReminderInfos(booking);
		assertTrue(booking.isTkxl());
		assertEquals("2018-03-10 14:00", booking.getTicketIssueInfo().getRpLocalDeadLineTime());
	}
	
	@Test
	public void test_BuildReminderInfosTKXLMultipleTicketIssueInfos() {
		
		TicketReminderUtil.buildReminderInfos(booking);
		assertTrue(booking.isTkxl());
		assertEquals("2018-03-10 14:00", booking.getTicketIssueInfo().getRpLocalDeadLineTime());
	}
	
	@Test
	public void test_ConvertTimeToRPTimezone() {
		booking.setOfficeTimezone("-0100");
		TicketReminderUtil.buildReminderInfos(booking);
		
		assertEquals("100318", booking.getTicketIssueInfo().getDate());
		assertEquals("XL", booking.getTicketIssueInfo().getIndicator());
		assertEquals("hkgcx08mc", booking.getTicketIssueInfo().getOfficeId());
		assertEquals("2018-03-10 04:00", booking.getTicketIssueInfo().getRpLocalDeadLineTime());
		assertEquals("1400", booking.getTicketIssueInfo().getTime());
		assertEquals("+0900", booking.getTicketIssueInfo().getTimeZoneOffset());
	}
}
