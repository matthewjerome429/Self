package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response;

import java.io.Serializable;

public class BaggageAllowanceDTO implements Serializable {

	private static final long serialVersionUID = 880254278606862469L;

	private String memberTier;

	private CabinBaggageDTO cabinBaggage;

	private CheckinBaggageDTO checkinBaggage;

	public String getMemberTier() {
		return memberTier;
	}

	public void setMemberTier(String memberTier) {
		this.memberTier = memberTier;
	}

	public CabinBaggageDTO getCabinBaggage() {
		return cabinBaggage;
	}

	public void setCabinBaggage(CabinBaggageDTO cabinBaggage) {
		this.cabinBaggage = cabinBaggage;
	}

	public CheckinBaggageDTO getCheckinBaggage() {
		return checkinBaggage;
	}

	public void setCheckinBaggage(CheckinBaggageDTO checkinBaggage) {
		this.checkinBaggage = checkinBaggage;
	}

}
