package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.math.BigInteger;

public class SharedWaiverBaggageDTO {
	
	private BaggageDetailDTO totalBaggage;
	
	private BigInteger groupId;

	public BaggageDetailDTO getTotalBaggage() {
		return totalBaggage;
	}
	
	public BaggageDetailDTO findTotalBaggage() {
		if(totalBaggage == null){
			totalBaggage = new BaggageDetailDTO();
		}
		return totalBaggage;
	}

	public void setTotalBaggage(BaggageDetailDTO totalBaggage) {
		this.totalBaggage = totalBaggage;
	}

	public BigInteger getGroupId() {
		return groupId;
	}

	public void setGroupId(BigInteger groupId) {
		this.groupId = groupId;
	}

}
