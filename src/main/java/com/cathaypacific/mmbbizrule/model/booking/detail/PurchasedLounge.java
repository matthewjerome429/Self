package com.cathaypacific.mmbbizrule.model.booking.detail;

import com.cathaypacific.mbcommon.enums.booking.LoungeClass;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPaymentInfo;

public class PurchasedLounge {

	/** Lounge type = LGAB (Business lounge pass) or LGAF (First lounge pass) */
	private LoungeClass type;
	/** Tier = Membership tier of the requester: Technically could be AM/GR/SL/GO/DM/IN, but AM is not eligible for BLAC in reality) */
	private String tier;
	/** GUID = M + requesting member ID */
	private String guid;
	/** Entitlement ID = ID of the claim */
	private String entitlementId;
	/** Pax type = Passenger using the entitlement: Technically could be "M" (Member self) or "G" (Travelling companion) */
	private String passengerType;
	/** Payment info of FA / FHD */
	private RetrievePnrPaymentInfo paymentInfo;

	public LoungeClass getType() {
		return type;
	}

	public void setType(LoungeClass type) {
		this.type = type;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getEntitlementId() {
		return entitlementId;
	}

	public void setEntitlementId(String entitlementId) {
		this.entitlementId = entitlementId;
	}

	public String getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}

	public RetrievePnrPaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(RetrievePnrPaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	
}
