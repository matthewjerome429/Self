package com.cathaypacific.mmbbizrule.business.impl;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.StringUtils;
import com.cathaypacific.mbcommon.enums.booking.LoungeClass;
import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.enums.staff.StaffBookingType;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.dto.common.CustomizedRequiredInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.bookingcustomizedinfo.BookingCustomizedInfoRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.BookingCustomizedInfoResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.ClaimedLoungeDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.DepartureArrivalTimeCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.FQTVCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.PassengeCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.PassengerSegmentCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.SegmentCustomizedInfoDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.ClaimedLounge;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrStaffDetail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;

@RunWith(MockitoJUnitRunner.class)
public class BookingPropertiesBusinessImplTest {
	@InjectMocks
	private BookingPropertiesBusinessImpl bookingPropertiesBusinessImpl;
	
	@Mock
	private TicketProcessInvokeService ticketProcessInvokeService;
	
	@Mock
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Mock
	private PnrInvokeService pnrInvokeService;
	
	@Mock
	private BookingBuildService bookingBuildService;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetBookingCustomizedInfo() throws BusinessBaseException {
		BookingCustomizedInfoRequestDTO request = new BookingCustomizedInfoRequestDTO();
		
		request.setEticket("1602369890489");
		request.setFamilyName("TEST");
		request.setGivenName("ALAN");
		request.setMemberId("1910026016");
		CustomizedRequiredInfoDTO requiredInfo = new CustomizedRequiredInfoDTO();
		requiredInfo.setCprCheck(true);
		requiredInfo.setMemberHolidayCheck(true);
		requiredInfo.setRtfsTime(true);
		request.setRequiredInfo(requiredInfo);
		
		String rloc = "JHFWJP";
		
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		
		Booking booking = new Booking();
		
		List<Passenger> passengers = new ArrayList<>();
		Passenger passenger = new Passenger();
		passenger.setFamilyName("TEST");
		passenger.setGivenName("ALAN");
		passenger.setPassengerId("1");
		passenger.setPassengerType("ADT");
		passenger.setTitle("mr");
		passenger.setLoginFFPMatched(true);
		RetrievePnrStaffDetail staffDetail = new RetrievePnrStaffDetail();
		staffDetail.setType(StaffBookingType.INDUSTRY_DISCOUNT);
		passenger.setStaffDetail(staffDetail);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		
		List<PassengerSegment> passengerSegments = new ArrayList<>();
		PassengerSegment passengerSegment = new PassengerSegment();
		passengerSegment.setPassengerId("1");
		passengerSegment.setSegmentId("1");
		passengerSegment.setEticketNumber("1602369890489");
		
		FQTVInfo fqtvInfo = new FQTVInfo();		
		fqtvInfo.setCompanyId("CX");
		fqtvInfo.setAm(false);
		fqtvInfo.setMpo(true);
		fqtvInfo.setMembershipNumber("1910026016");
		fqtvInfo.setOnHoliday(true);
		fqtvInfo.setTierLevel("GR");
		fqtvInfo.setTopTier(false);
		passengerSegment.setFqtvInfo(fqtvInfo);
		
		ClaimedLounge claimedLounge = new ClaimedLounge();
		claimedLounge.setType(LoungeClass.BUSINESS);
		passengerSegment.setClaimedLounge(claimedLounge);
		
		passengerSegments.add(passengerSegment);
		booking.setPassengerSegments(passengerSegments);
		
		List<Segment> segments = new ArrayList<>();
		Segment segment = new Segment();
		segment.setCabinClass("F");
		segment.setSubClass("F");
		segment.setOriginPort("HKG");
		segment.setDestPort("TPE");
		segment.setMarketCompany("CX");
		segment.setMarketSegmentNumber("999");
		segment.setOperateCompany("BA");
		segment.setOperateSegmentNumber("888");
		segment.setSegmentID("1");
		
		DepartureArrivalTime departureTime = new DepartureArrivalTime();
		departureTime.setPnrTime("2019-02-20 08:30");
		departureTime.setTimeZoneOffset("+0800");
		segment.setDepartureTime(departureTime);
		
		DepartureArrivalTime arrivalTime = new DepartureArrivalTime();
		arrivalTime.setPnrTime("2019-02-21 08:30");
		arrivalTime.setTimeZoneOffset("+0900");
		segment.setArrivalTime(arrivalTime);
		
		SegmentStatus segmentStatus = new SegmentStatus();
		segmentStatus.setStatus(FlightStatusEnum.CONFIRMED);
		segmentStatus.setFlown(true);
		segment.setSegmentStatus(segmentStatus);
		segments.add(segment);
		booking.setSegments(segments);
		
		booking.setOneARloc(rloc);
		booking.setMiceBooking(true);
		booking.setNonMiceGroupBooking(true);
		
		when(ticketProcessInvokeService.getRlocByEticket(request.getEticket())).thenReturn(rloc);
		pnrBooking.setPassengers(Arrays.asList(new RetrievePnrPassenger()));
		when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(pnrBooking);
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(anyObject(), anyObject());
		when(bookingBuildService.buildBooking(anyObject(), anyObject(), anyObject())).thenReturn(booking);
		
		BookingCustomizedInfoResponseDTO response = bookingPropertiesBusinessImpl.getBookingCustomizedInfo(request);
		
		Assert.assertTrue(response.getStaffBooking());
		Assert.assertTrue(response.getGroupBooking());
		Assert.assertEquals("JHFWJP", response.getOneARloc());
		
		List<PassengeCustomizedInfoDTO> passengerCustomizedInfos = response.getPassengers();
		Assert.assertEquals(1, passengerCustomizedInfos.size());
		PassengeCustomizedInfoDTO passengerCustomizedInfo = passengerCustomizedInfos.get(0);
		Assert.assertEquals("1", passengerCustomizedInfo.getPassengerId());
		Assert.assertEquals("mr", passengerCustomizedInfo.getTitle());
		Assert.assertEquals("TEST", passengerCustomizedInfo.getFamilyName());
		Assert.assertEquals("ALAN", passengerCustomizedInfo.getGivenName());
		Assert.assertNull(passengerCustomizedInfo.getParentId());
		Assert.assertEquals("ADT", passengerCustomizedInfo.getPassengerType());
		Assert.assertTrue(BooleanUtils.isTrue(passengerCustomizedInfo.getLoginFFPMatched()));
		
		List<SegmentCustomizedInfoDTO> segmentCustomizedInfos = response.getSegments();
		Assert.assertEquals(1, segmentCustomizedInfos.size());
		SegmentCustomizedInfoDTO segmentCustomizedInfo = segmentCustomizedInfos.get(0);
		Assert.assertEquals("1", segmentCustomizedInfo.getSegmentId());
		Assert.assertEquals("F", segmentCustomizedInfo.getCabinClass());
		Assert.assertEquals("F", segmentCustomizedInfo.getSubClass());
		Assert.assertEquals("HKG", segmentCustomizedInfo.getOriginPort());
		Assert.assertEquals("TPE", segmentCustomizedInfo.getDestPort());
		Assert.assertEquals("CX", segmentCustomizedInfo.getMarketCompany());
		Assert.assertEquals("999", segmentCustomizedInfo.getMarketSegmentNumber());
		Assert.assertEquals("BA", segmentCustomizedInfo.getOperateCompany());
		Assert.assertEquals("888", segmentCustomizedInfo.getOperateSegmentNumber());
		Assert.assertTrue(BooleanUtils.isTrue(segmentCustomizedInfo.getFlown()));
		Assert.assertEquals(FlightStatusEnum.CONFIRMED, segmentCustomizedInfo.getStatus());
		Assert.assertNotNull(segmentCustomizedInfo.getArrivalTime());
		Assert.assertNotNull(segmentCustomizedInfo.getDepartureTime());
		
		DepartureArrivalTimeCustomizedInfoDTO arrivalTimeCustomizedInfo = segmentCustomizedInfo.getArrivalTime();
		Assert.assertEquals("2019-02-21 08:30", arrivalTimeCustomizedInfo.getPnrTime());
		Assert.assertEquals("+0900", arrivalTimeCustomizedInfo.getTimeZoneOffset());	
		
		DepartureArrivalTimeCustomizedInfoDTO departureTimeCustomizedInfo = segmentCustomizedInfo.getDepartureTime();
		Assert.assertEquals("2019-02-20 08:30", departureTimeCustomizedInfo.getPnrTime());
		Assert.assertEquals("+0800", departureTimeCustomizedInfo.getTimeZoneOffset());	

		List<PassengerSegmentCustomizedInfoDTO> passengerSegmentCustomizedInfoDTOs = response.getPassengerSegments();
		Assert.assertEquals(1, passengerSegmentCustomizedInfoDTOs.size());
		PassengerSegmentCustomizedInfoDTO passengerSegmentCustomizedInfoDTO = passengerSegmentCustomizedInfoDTOs.get(0);
		Assert.assertEquals("1", passengerSegmentCustomizedInfoDTO.getPassengerId());
		Assert.assertEquals("1", passengerSegmentCustomizedInfoDTO.getSegmentId());
		Assert.assertTrue(!StringUtils.isEmpty(passengerSegmentCustomizedInfoDTO.getEticketNumber()));
		Assert.assertNotNull(passengerSegmentCustomizedInfoDTO.getFqtvInfo());
		Assert.assertNotNull(passengerSegmentCustomizedInfoDTO.getClaimedLounge());
		
		FQTVCustomizedInfoDTO fqtvCustomizedInfo = passengerSegmentCustomizedInfoDTO.getFqtvInfo();
		Assert.assertEquals("CX", fqtvCustomizedInfo.getCompanyId());
		Assert.assertEquals("1910026016", fqtvCustomizedInfo.getMembershipNumber());
		Assert.assertEquals("GR", fqtvCustomizedInfo.getTierLevel());
		Assert.assertTrue(BooleanUtils.isTrue(fqtvCustomizedInfo.isMPO()));
		Assert.assertTrue(!BooleanUtils.isTrue(fqtvCustomizedInfo.isAM()));
		Assert.assertTrue(BooleanUtils.isTrue(fqtvCustomizedInfo.getOnHoliday()));
		Assert.assertTrue(!BooleanUtils.isTrue(fqtvCustomizedInfo.isTopTier()));

		ClaimedLoungeDTO claimedLoungeDTO = passengerSegmentCustomizedInfoDTO.getClaimedLounge();
		Assert.assertEquals(LoungeClass.BUSINESS, claimedLoungeDTO.getType());
				
	}
}
