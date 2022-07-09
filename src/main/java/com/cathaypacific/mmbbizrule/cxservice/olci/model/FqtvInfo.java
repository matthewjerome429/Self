package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class FqtvInfo implements Serializable {

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = -8836841483859688344L;

	/**
	 * Frequent Flyer Information
	 */
	private FrequentFlyerInfo frequentFlyerInfo;

	/**
	 * FQTV Usage
	 */
	private String fqtvUsage;

	/**
	 * FQTV Carrier
	 */
	private String fqtvCarrier;
	/**
	 * the top tier member flag
	 */
	private boolean topTierMember;

	public FrequentFlyerInfo getFrequentFlyerInfo() {
		return frequentFlyerInfo;
	}

	public void setFrequentFlyerInfo(FrequentFlyerInfo frequentFlyerInfo) {
		this.frequentFlyerInfo = frequentFlyerInfo;
	}

	public String getFqtvUsage() {
		return fqtvUsage;
	}

	public void setFqtvUsage(String fqtvUsage) {
		this.fqtvUsage = fqtvUsage;
	}

	public String getFqtvCarrier() {
		return fqtvCarrier;
	}

	public void setFqtvCarrier(String fqtvCarrier) {
		this.fqtvCarrier = fqtvCarrier;
	}

	public boolean isTopTierMember() {
		return topTierMember;
	}

	public void setTopTierMember(boolean topTierMember) {
		this.topTierMember = topTierMember;
	}
}
