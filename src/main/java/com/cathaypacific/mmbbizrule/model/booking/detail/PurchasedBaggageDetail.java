package com.cathaypacific.mmbbizrule.model.booking.detail;

import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPaymentInfo;

public class PurchasedBaggageDetail extends BaggageDetail{

	private RetrievePnrPaymentInfo paymentInfo;

	public RetrievePnrPaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(RetrievePnrPaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	
}
