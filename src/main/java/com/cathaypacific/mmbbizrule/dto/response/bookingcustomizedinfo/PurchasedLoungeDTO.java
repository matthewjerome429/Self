package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import java.io.Serializable;

import com.cathaypacific.mbcommon.enums.booking.LoungeClass;

public class PurchasedLoungeDTO implements Serializable {
	
	private static final long serialVersionUID = -3823993756445506741L;

	private LoungeClass type;
	
	private String tier;
	
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
	
}
