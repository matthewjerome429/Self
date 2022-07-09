package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.math.BigInteger;

public class SharedWaiverBaggageDTOV2 {
	
	private BaggageDetailDTOV2 totalBaggage;
	
	private BigInteger groupId;

	public BaggageDetailDTOV2 getTotalBaggage() {
		return totalBaggage;
	}
	
	public BaggageDetailDTOV2 findTotalBaggage() {
		if(totalBaggage == null){
			totalBaggage = new BaggageDetailDTOV2();
		}
		return totalBaggage;
	}

	public void setTotalBaggage(BaggageDetailDTOV2 totalBaggage) {
		this.totalBaggage = totalBaggage;
	}

	public BigInteger getGroupId() {
		return groupId;
	}

	public void setGroupId(BigInteger groupId) {
		this.groupId = groupId;
	}

}
