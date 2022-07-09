package com.cathaypacific.mmbbizrule.model.booking.detail;

public class CabinBaggage {

	/** standard baggage */
	private BaggageDetail standardBaggage;

	/** member baggage */
	private BaggageDetail memberBaggage;
	
	/** total baggage */
	private BaggageDetail total;
	
	/** baggage limit */
	private BaggageDetail limit;
	
	/** small item */
	private Integer smallItem;

	public BaggageDetail getStandardBaggage() {
		return standardBaggage;
	}
	
	public BaggageDetail findStandardBaggage() {
		if (standardBaggage == null) {
			standardBaggage = new BaggageDetail();
		}
		return standardBaggage;
	}

	public void setStandardBaggage(BaggageDetail standardBaggage) {
		this.standardBaggage = standardBaggage;
	}

	public BaggageDetail getMemberBaggage() {
		return memberBaggage;
	}
	
	public BaggageDetail findMemberBaggage() {
		if (memberBaggage == null) {
			memberBaggage = new BaggageDetail();
		}
		return memberBaggage;
	}

	public void setMemberBaggage(BaggageDetail memberBaggage) {
		this.memberBaggage = memberBaggage;
	}

	public BaggageDetail getTotal() {
		return total;
	}

	public BaggageDetail findTotal() {
		if (total == null) {
			total = new BaggageDetail();
		}
		return total;
	}

	public void setTotal(BaggageDetail total) {
		this.total = total;
	}

	public BaggageDetail getLimit() {
		return limit;
	}

	public BaggageDetail findLimit() {
		if (limit == null) {
			limit = new BaggageDetail();
		}
		return limit;
	}

	public void setLimit(BaggageDetail limit) {
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
