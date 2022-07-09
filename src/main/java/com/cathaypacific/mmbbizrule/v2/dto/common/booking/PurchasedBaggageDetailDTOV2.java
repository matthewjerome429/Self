package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

public class PurchasedBaggageDetailDTOV2 extends BaggageDetailDTOV2{

	private PaymentInfoDTOV2 paymentInfo;

	public PaymentInfoDTOV2 getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(PaymentInfoDTOV2 paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	
}
