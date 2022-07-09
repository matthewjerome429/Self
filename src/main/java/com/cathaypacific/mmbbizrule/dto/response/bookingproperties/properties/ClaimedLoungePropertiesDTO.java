package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties;

import java.io.Serializable;

public class ClaimedLoungePropertiesDTO implements Serializable{

	private static final long serialVersionUID = 569998667076668234L;

	private String type;
	
	private String tier;
	
	private String guid;
	
	private String entitlementId;
	
	private String passengerType;

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
	
}
