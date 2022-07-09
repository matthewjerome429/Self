package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.util.ArrayList;
import java.util.List;

public class CheckInBaggageDTO {

	/** standard baggage */
	private BaggageDetailDTO standardBaggage;
	
	/** waiver baggage */
	private BaggageDetailDTO waiverBaggage;

	/** purchased baggage */
	private PurchasedBaggageDetailDTO purchasedBaggage;

	/** share baggage */
	private List<SharedWaiverBaggageDTO> sharedWaiverBaggages;
	
	/** member baggage */
	private BaggageDetailDTO memberBaggage;
	
	/** baggage total */
	private BaggageDetailDTO total;
	
	/** baggage limit */
	private BaggageDetailDTO limit;

	public BaggageDetailDTO getStandardBaggage() {
		return standardBaggage;
	}
	
	public BaggageDetailDTO findStandardBaggage() {
		if(standardBaggage == null){
			standardBaggage = new BaggageDetailDTO();
		}
		return standardBaggage;
	}

	public void setStandardBaggage(BaggageDetailDTO standardBaggage) {
		this.standardBaggage = standardBaggage;
	}

	public BaggageDetailDTO getWaiverBaggage() {
		return waiverBaggage;
	}
	
	public BaggageDetailDTO findWaiverBaggage() {
		if(waiverBaggage == null){
			waiverBaggage = new BaggageDetailDTO();
		}
		return waiverBaggage;
	}

	public void setWaiverBaggage(BaggageDetailDTO waiverBaggage) {
		this.waiverBaggage = waiverBaggage;
	}

	public BaggageDetailDTO getPurchasedBaggage() {
		return purchasedBaggage;
	}
	
	public PurchasedBaggageDetailDTO findPurchasedBaggage() {
		if(purchasedBaggage == null){
			purchasedBaggage = new PurchasedBaggageDetailDTO();
		}
		return purchasedBaggage;
	}

	public void setPurchasedBaggage(PurchasedBaggageDetailDTO purchasedBaggage) {
		this.purchasedBaggage = purchasedBaggage;
	}

	public List<SharedWaiverBaggageDTO> getSharedWaiverBaggages() {
		return sharedWaiverBaggages;
	}
	
	public List<SharedWaiverBaggageDTO> findSharedWaiverBaggages() {
		if(sharedWaiverBaggages == null){
			sharedWaiverBaggages = new ArrayList<>();
		}
		return sharedWaiverBaggages;
	}

	public void setSharedWaiverBaggages(List<SharedWaiverBaggageDTO> sharedWaiverBaggages) {
		this.sharedWaiverBaggages = sharedWaiverBaggages;
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

}
