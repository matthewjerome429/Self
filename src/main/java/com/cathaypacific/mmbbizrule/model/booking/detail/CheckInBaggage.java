package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.util.ArrayList;
import java.util.List;

public class CheckInBaggage {
	
	/** standard baggage */
	private BaggageDetail standardBaggage;
	
	/** waiver baggage */
	private BaggageDetail waiverBaggage;
	
	/** purchased baggage */
	private List<PurchasedBaggageDetail> purchasedBaggages;
	
	/** member baggage */
	private BaggageDetail memberBaggage;
	
	/** baggage limit */
	private BaggageDetail limit;
	
	/** shared baggage */
	private List<SharedWaiverBaggage> sharedWaiverBaggages;

	public BaggageDetail getStandardBaggage() {
		return standardBaggage;
	}
	
	public BaggageDetail findStandardBaggage() {
		if(standardBaggage == null){
			standardBaggage = new BaggageDetail();
		}
		return standardBaggage;
	}

	public void setStandardBaggage(BaggageDetail standardBaggage) {
		this.standardBaggage = standardBaggage;
	}

	public BaggageDetail getWaiverBaggage() {
		return waiverBaggage;
	}
	
	public BaggageDetail findWaiverBaggage() {
		if(waiverBaggage == null){
			waiverBaggage = new BaggageDetail();
		}
		return waiverBaggage;
	}

	public void setWaiverBaggage(BaggageDetail waiverBaggage) {
		this.waiverBaggage = waiverBaggage;
	}

 
	public List<PurchasedBaggageDetail> getPurchasedBaggages() {
		return purchasedBaggages;
	}

	public void setPurchasedBaggages(List<PurchasedBaggageDetail> purchasedBaggages) {
		this.purchasedBaggages = purchasedBaggages;
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

	public List<SharedWaiverBaggage> getSharedWaiverBaggages() {
		return sharedWaiverBaggages;
	}

	public List<SharedWaiverBaggage> findSharedWaiverBaggages() {
		if(sharedWaiverBaggages == null){
			sharedWaiverBaggages = new ArrayList<>();
		}
		return sharedWaiverBaggages;
	}
	
	public void setSharedWaiverBaggages(List<SharedWaiverBaggage> sharedWaiverBaggages) {
		this.sharedWaiverBaggages = sharedWaiverBaggages;
	}
	
}
