package com.cathaypacific.mmbbizrule.dto.common.booking;

public class PurchasedBaggageDetailDTO extends BaggageDetailDTO{

	private PaymentInfoDTO paymentInfo;

	public PaymentInfoDTO getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(PaymentInfoDTO paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	
}
