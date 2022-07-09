package com.cathaypacific.mmbbizrule.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.model.common.seatpayment.PaymentInfo;
import com.cathaypacific.mbcommon.model.common.seatpayment.SeatPayment;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatDetail;
import com.cathaypacific.mmbbizrule.service.KeepSeatPaymentService;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

@Service
public class KeepSeatPaymentServiceImpl implements KeepSeatPaymentService{
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Override
	public Booking keepSeatPayment(Booking bookingBefore, Booking bookingAfter) {
		if (bookingBefore == null || bookingAfter == null
				|| CollectionUtils.isEmpty(bookingBefore.getPassengerSegments())
				|| CollectionUtils.isEmpty(bookingAfter.getPassengerSegments())) {
			return bookingAfter == null ? bookingBefore : bookingAfter;
		}
		
		@SuppressWarnings("unchecked")
		List<SeatPayment> seatPayments = mbTokenCacheRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.SEAT_PAYMENT, bookingBefore.getOneARloc(), ArrayList.class); 
		if (CollectionUtils.isEmpty(seatPayments)) {
			seatPayments = new ArrayList<>();
		}
		
		transferSeatPaymentInfo(bookingBefore, bookingAfter, seatPayments);
		
		if (!CollectionUtils.isEmpty(seatPayments)) {
			mbTokenCacheRepository.add(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.SEAT_PAYMENT, bookingAfter.getOneARloc(), seatPayments);
		}
		
		return bookingAfter;
	}

	/**
	 * transfer seat payment info
	 * @param bookingBefore
	 * @param bookingAfter
	 * @param seatPayments
	 */
	private void transferSeatPaymentInfo(Booking bookingBefore, Booking bookingAfter, List<SeatPayment> seatPayments) {
		//populate seat payment info from bookingBefore to bookingAfter
		for(PassengerSegment passengerSegmentAfter : bookingAfter.getPassengerSegments()){
			if(passengerSegmentAfter.getSeat() != null){
				String passengerId = passengerSegmentAfter.getPassengerId();
				String segmentId = passengerSegmentAfter.getSegmentId();
				SeatDetail seatAfter = passengerSegmentAfter.getSeat();
				
				 //find passengerSegment before update by passengerId and segmentId of passengerSegmentAfter
				PassengerSegment passengerSegmentBefore = bookingBefore.getPassengerSegments().stream()
						.filter(ps -> !StringUtils.isEmpty(ps.getPassengerId())
								&& !StringUtils.isEmpty(ps.getSegmentId())
								&& ps.getPassengerId().equals(passengerId)
								&& ps.getSegmentId().equals(segmentId))
						.findFirst().orElse(null);
				
				if(passengerSegmentBefore == null){
					continue;
				}
				SeatDetail seatBefore = passengerSegmentBefore.getSeat();
				//if the seat is not paid, and the same type of seat in passengerSegmentBefore is paid, transfer the payment info
				if ((BooleanUtils.isTrue(seatAfter.isExlSeat()) && !BooleanUtils.isTrue(seatAfter.isPaid()) && isExlSeatPaid(seatBefore))
						|| (!BooleanUtils.isTrue(seatAfter.isExlSeat()) && !BooleanUtils.isTrue(seatAfter.isPaid()) && isRegularSeatPaid(passengerSegmentBefore.getSeat()))) {
					// transfer seat payment info
					transferPayment(seatPayments, passengerId, segmentId, seatAfter, seatBefore);
				}
			}
		}
	}

	/**
	 * transfer seat payment info
	 * @param seatPayments
	 * @param passengerId
	 * @param segmentId
	 * @param seatAfter
	 * @param seatBefore
	 */
	private void transferPayment(List<SeatPayment> seatPayments, String passengerId, String segmentId,
			SeatDetail seatAfter, SeatDetail seatBefore) {
		// transfer the payment info
		seatAfter.setPaid(true);
		seatAfter.setPaymentInfo(seatBefore.getPaymentInfo());
		
		/** since 1A may not create a new payment info for the new seat on time, save the payment info in cache for bookingBuild to transfer the payment manually */
		// save the payment info to cache
		SeatPayment seatPayment = findOrCreateSeatPayment(seatPayments, passengerId, segmentId);
		seatPayment.setSeatNo(seatAfter.getSeatNo());
		seatPayment.setPaid(true);
		if (seatBefore.getPaymentInfo() != null) {
			PaymentInfo paymentInfo = new PaymentInfo();
			paymentInfo.setAmount(seatBefore.getPaymentInfo().getAmount());
			paymentInfo.setCurrency(seatBefore.getPaymentInfo().getCurrency());
			paymentInfo.setDate(seatBefore.getPaymentInfo().getDate());
			paymentInfo.setOfficeId(seatBefore.getPaymentInfo().getOfficeId());
			paymentInfo.setTicket(seatBefore.getPaymentInfo().getTicket());
			seatPayment.setPaymentInfo(paymentInfo);
		}
	}
	
	/**
	 * find or create seat payment
	 * @param seatPayments
	 * @param passengerId
	 * @param segmentId
	 * @return SeatPayment
	 */
	private SeatPayment findOrCreateSeatPayment(List<SeatPayment> seatPayments, String passengerId, String segmentId) {
		SeatPayment seatPayment = seatPayments.stream().filter(sp -> passengerId.equals(sp.getPassengerId()) && segmentId.equals(sp.getSegmentId())).findFirst().orElse(null);
		// if no seatPayment found, create a new one
		if (seatPayment == null) {
			seatPayment = new SeatPayment();
			seatPayment.setPassengerId(passengerId);
			seatPayment.setSegmentId(segmentId);
			seatPayments.add(seatPayment);
		}
		
		return seatPayment;
	}

	/**
	 * 
	* @Description get EXL seat payment info from seat before update
	* @param pnrSeat
	* @param seatNo
	* @return Boolean
	* @author haiwei.jia
	 */
	private Boolean isExlSeatPaid(SeatDetail seatBefore) {
		if(seatBefore == null){
			return false;
		}
		
		if(BooleanUtils.isTrue(seatBefore.isExlSeat())){
			return seatBefore.isPaid();
		}
		
		return false;
	}
	
	/**
	 * 
	* @Description get seat payment info from seat before update
	* @param pnrSeat
	* @param seatNo
	* @return Boolean
	* @author haiwei.jia
	 */
	private Boolean isRegularSeatPaid(SeatDetail seatBefore) {
		if(seatBefore == null){
			return false;
		}
		
		return seatBefore.isPaid();
	}
}
