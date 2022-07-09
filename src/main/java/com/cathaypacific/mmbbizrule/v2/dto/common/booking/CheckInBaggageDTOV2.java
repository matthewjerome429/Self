package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckInBaggageDTOV2 implements Serializable {

	private static final long serialVersionUID = -6022795570924143904L;

	/** standard baggage */
	private BaggageDetailDTOV2 standardBaggage;
	
	/** waiver baggage */
	private BaggageDetailDTOV2 waiverBaggage;

	/** purchased baggage */
	private PurchasedBaggageDetailDTOV2 purchasedBaggage;

	/** share baggage */
	private List<SharedWaiverBaggageDTOV2> sharedWaiverBaggages;
	
	/** member baggage */
	private BaggageDetailDTOV2 memberBaggage;
	
	/** baggage total */
	private BaggageDetailDTOV2 total;
	
	/** baggage limit */
	private BaggageDetailDTOV2 limit;

	public BaggageDetailDTOV2 getStandardBaggage() {
		return standardBaggage;
	}
	
	public BaggageDetailDTOV2 findStandardBaggage() {
		if(standardBaggage == null){
			standardBaggage = new BaggageDetailDTOV2();
		}
		return standardBaggage;
	}

	public void setStandardBaggage(BaggageDetailDTOV2 standardBaggage) {
		this.standardBaggage = standardBaggage;
	}

	public BaggageDetailDTOV2 getWaiverBaggage() {
		return waiverBaggage;
	}
	
	public BaggageDetailDTOV2 findWaiverBaggage() {
		if(waiverBaggage == null){
			waiverBaggage = new BaggageDetailDTOV2();
		}
		return waiverBaggage;
	}

	public void setWaiverBaggage(BaggageDetailDTOV2 waiverBaggage) {
		this.waiverBaggage = waiverBaggage;
	}

	public BaggageDetailDTOV2 getPurchasedBaggage() {
		return purchasedBaggage;
	}
	
	public PurchasedBaggageDetailDTOV2 findPurchasedBaggage() {
		if(purchasedBaggage == null){
			purchasedBaggage = new PurchasedBaggageDetailDTOV2();
		}
		return purchasedBaggage;
	}

	public void setPurchasedBaggage(PurchasedBaggageDetailDTOV2 purchasedBaggage) {
		this.purchasedBaggage = purchasedBaggage;
	}

	public List<SharedWaiverBaggageDTOV2> getSharedWaiverBaggages() {
		return sharedWaiverBaggages;
	}
	
	public List<SharedWaiverBaggageDTOV2> findSharedWaiverBaggages() {
		if(sharedWaiverBaggages == null){
			sharedWaiverBaggages = new ArrayList<>();
		}
		return sharedWaiverBaggages;
	}

	public void setSharedWaiverBaggages(List<SharedWaiverBaggageDTOV2> sharedWaiverBaggages) {
		this.sharedWaiverBaggages = sharedWaiverBaggages;
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

}
