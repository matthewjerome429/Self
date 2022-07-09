package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;

public class RetrievePnrLoungeInfo extends DataElement {
	/** Lounge type = BLAC (Business lounge pass) or FLAC (First lounge pass) */
	private String type;
	
	/** Tier = Membership tier of the requester: Technically could be AM/GR/SL/GO/DM/IN, but AM is not eligible for BLAC in reality */
	private String tier;
	
	/** GUID = M + requesting member ID */
	private String guid;
	
	/** Entitlement ID = ID of the claim */
	private String entitlementId;
	
	/** Pax type = Passenger using the entitlement: Technically could be "M" (Member self) or "G" (Travelling companion) */
	private String passengerType;
	
	private String segmentId;
	
	private String passengerId;
	
	private BigInteger qulifierId;
	
	private RetrievePnrPaymentInfo paymentInfo;

	public String getType() {
		return type;
	}

	public void setType(String type) {
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

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public BigInteger getQulifierId() {
		return qulifierId;
	}

	public void setQulifierId(BigInteger qulifierId) {
		this.qulifierId = qulifierId;
	}

	public RetrievePnrPaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(RetrievePnrPaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	
}
