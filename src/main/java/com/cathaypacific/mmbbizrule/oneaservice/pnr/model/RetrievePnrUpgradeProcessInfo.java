package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

public class RetrievePnrUpgradeProcessInfo extends DataElement{
	
	private String entitlementId;
	
	private String fromSubclass;
	
	private Boolean confirmed;

	public String getEntitlementId() {
		return entitlementId;
	}

	public void setEntitlementId(String entitlementId) {
		this.entitlementId = entitlementId;
	}

	public String getFromSubclass() {
		return fromSubclass;
	}

	public void setFromSubclass(String fromSubclass) {
		this.fromSubclass = fromSubclass;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}
	
}
