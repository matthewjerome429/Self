package com.cathaypacific.mmbbizrule.dto.common.booking;

public class CabinBaggageDTO {

	/** standard baggage */
	private BaggageDetailDTO standardBaggage;

	/** member baggage */
	private BaggageDetailDTO memberBaggage;
	
	/** baggage total */
	private BaggageDetailDTO total;

	/** baggage limit */
	private BaggageDetailDTO limit;
	
	/** small item */
	private Integer smallItem;

	public BaggageDetailDTO getStandardBaggage() {
		return standardBaggage;
	}

	public BaggageDetailDTO findStandardBaggage() {
		if (standardBaggage == null) {
			standardBaggage = new BaggageDetailDTO();
		}
		return standardBaggage;
	}

	public void setStandardBaggage(BaggageDetailDTO standardBaggage) {
		this.standardBaggage = standardBaggage;
	}

	public BaggageDetailDTO getMemberBaggage() {
		return memberBaggage;
	}

	public BaggageDetailDTO findMemberBaggage() {
		if (memberBaggage == null) {
			memberBaggage = new BaggageDetailDTO();
		}
		return memberBaggage;
	}

	public void setMemberBaggage(BaggageDetailDTO memberBaggage) {
		this.memberBaggage = memberBaggage;
	}

	public BaggageDetailDTO getTotal() {
		return total;
	}
	
	public BaggageDetailDTO findTotal() {
		if (total == null) {
			total = new BaggageDetailDTO();
		}
		return total;
	}

	public void setTotal(BaggageDetailDTO total) {
		this.total = total;
	}

	public BaggageDetailDTO getLimit() {
		return limit;
	}

	public BaggageDetailDTO findLimit() {
		if (limit == null) {
			limit = new BaggageDetailDTO();
		}
		return limit;
	}

	public void setLimit(BaggageDetailDTO limit) {
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
