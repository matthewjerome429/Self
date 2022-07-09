package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.math.BigInteger;

public class SharedWaiverBaggage {
	
	private BaggageDetail totalBaggage;
	
	private BigInteger groupId;

	public BaggageDetail getTotalBaggage() {
		return totalBaggage;
	}
	
	public BaggageDetail findTotalBaggage() {
		if(totalBaggage == null){
			totalBaggage = new BaggageDetail();
		}
		return totalBaggage;
	}

	public void setTotalBaggage(BaggageDetail totalBaggage) {
		this.totalBaggage = totalBaggage;
	}

	public BigInteger getGroupId() {
		return groupId;
	}

	public void setGroupId(BigInteger groupId) {
		this.groupId = groupId;
	}

}
