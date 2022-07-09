package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import com.cathaypacific.mbcommon.enums.booking.LoungeClass;
import com.cathaypacific.mmbbizrule.dto.common.booking.PaymentInfoDTO;

public class LoungeAccessDTO {

	private LoungeClass type;
	
	private String tier;
	
	private boolean purchasedLounge;
	
	private PaymentInfoDTO paymentInfo;

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

	public PaymentInfoDTO getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(PaymentInfoDTO paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
}

