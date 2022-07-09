package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

public class CabinBaggageDTOV2 implements Serializable {

	private static final long serialVersionUID = 4398502091208026433L;

	/** standard baggage */
	private BaggageDetailDTOV2 standardBaggage;

	/** member baggage */
	private BaggageDetailDTOV2 memberBaggage;
	
	/** baggage total */
	private BaggageDetailDTOV2 total;

	/** baggage limit */
	private BaggageDetailDTOV2 limit;
	
	/** small item */
	private Integer smallItem;

	public BaggageDetailDTOV2 getStandardBaggage() {
		return standardBaggage;
	}

	public BaggageDetailDTOV2 findStandardBaggage() {
		if (standardBaggage == null) {
			standardBaggage = new BaggageDetailDTOV2();
		}
		return standardBaggage;
	}

	public void setStandardBaggage(BaggageDetailDTOV2 standardBaggage) {
		this.standardBaggage = standardBaggage;
	}

	public BaggageDetailDTOV2 getMemberBaggage() {
		return memberBaggage;
	}

	public BaggageDetailDTOV2 findMemberBaggage() {
		if (memberBaggage == null) {
			memberBaggage = new BaggageDetailDTOV2();
		}
		return memberBaggage;
	}

	public void setMemberBaggage(BaggageDetailDTOV2 memberBaggage) {
		this.memberBaggage = memberBaggage;
	}

	public BaggageDetailDTOV2 getTotal() {
		return total;
	}
	
	public BaggageDetailDTOV2 findTotal() {
		if (total == null) {
			total = new BaggageDetailDTOV2();
		}
		return total;
	}

	public void setTotal(BaggageDetailDTOV2 total) {
		this.total = total;
	}

	public BaggageDetailDTOV2 getLimit() {
		return limit;
	}

	public BaggageDetailDTOV2 findLimit() {
		if (limit == null) {
			limit = new BaggageDetailDTOV2();
		}
		return limit;
	}

	public void setLimit(BaggageDetailDTOV2 limit) {
		this.limit = limit;
	}

	public Integer getSmallItem() {
		return smallItem;
	}

	public Integer findSmallItem() {
		if (smallItem == null) {
			smallItem = 0;
		}
		return smallItem;
	}

	public void setSmallItem(Integer smallItem) {
		this.smallItem = smallItem;
	}

}
