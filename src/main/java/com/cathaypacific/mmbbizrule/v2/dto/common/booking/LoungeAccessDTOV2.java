package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import com.cathaypacific.mbcommon.enums.booking.LoungeClass;

public class LoungeAccessDTOV2 {

	private LoungeClass type;
	
	private String tier;
	
	private boolean purchasedLounge;
	
	private PaymentInfoDTOV2 paymentInfo;

	public LoungeClass getType() {
		return type;
	}

	public void setType(LoungeClass type) {
		this.type = type;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public boolean getPurchasedLounge() {
		return purchasedLounge;
	}

	public void isPurchasedLounge(boolean isPurchasedLounge) {
		this.purchasedLounge = isPurchasedLounge;
	}

	public PaymentInfoDTOV2 getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(PaymentInfoDTOV2 paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
}

