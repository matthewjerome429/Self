package com.cathaypacific.mmbbizrule.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatDetail;
@RunWith(MockitoJUnitRunner.class)
public class KeepExlSeatPaymentServiceImplTest {
	@InjectMocks
	private KeepSeatPaymentServiceImpl keepSeatPaymentServiceImpl;
	
	@Mock
	private MbTokenCacheRepository mbTokenCacheRepository;

	@Test
	public void test() {
		Booking bookingBefore =new Booking();
		Booking bookingAfter=new Booking();
		SeatDetail seatDetail=new SeatDetail();
		seatDetail.setExlSeat(true);
		seatDetail.setPaid(false);
		BigInteger b=new BigInteger("123456");
		seatDetail.setQulifierId(b);
		List<PassengerSegment> passengerSegments=new ArrayList<>();
		PassengerSegment passengerSegment=new PassengerSegment();
		passengerSegment.setSeat(seatDetail);
		passengerSegment.setPassengerId("1");
		passengerSegment.setSegmentId("1");
		passengerSegments.add(passengerSegment);
		bookingBefore.setPassengerSegments(passengerSegments);
		bookingAfter.setPassengerSegments(passengerSegments);
		when(mbTokenCacheRepository.get(any(), any(), any(), any())).thenReturn(new ArrayList<>());
		doNothing().when(mbTokenCacheRepository).add(any(), any(), any(), any());
		doNothing().when(mbTokenCacheRepository).delete(any(), any(), any());
		
		Booking booking=keepSeatPaymentServiceImpl.keepSeatPayment(bookingBefore, bookingAfter);
		Assert.assertEquals("1", booking.getPassengerSegments().get(0).getPassengerId());
		Assert.assertEquals("1", booking.getPassengerSegments().get(0).getSegmentId());
		Assert.assertEquals("123456", booking.getPassengerSegments().get(0).getSeat().getQulifierId().toString());
	}

}
