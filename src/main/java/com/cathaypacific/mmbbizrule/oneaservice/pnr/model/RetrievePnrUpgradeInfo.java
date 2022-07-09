package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import com.cathaypacific.mbcommon.enums.upgrade.UpgradeType;

public class RetrievePnrUpgradeInfo {
	/**
	 * if upgradeType = BOOKABLEUPGRADE("U"), bookableUpgradeInfo will always be UpgradeProgressStatus.CONFIRMED
	 */
	private  UpgradeType upgradeType;
	
	private String fromSubClass;
	
	private String toSubClass;
	
	private RetrievePnrRedUpgradeInfo redUpgradeInfo;
	
	private RetrievePnrBidUpgradeInfo bidUpgradeInfo;
	
	private RetrievePnrBookableUpgradeInfo bookableUpgradeInfo;

	public UpgradeType getUpgradeType() {
		return upgradeType;
	}

	public void setUpgradeType(UpgradeType upgradeType) {
		this.upgradeType = upgradeType;
	}

	public String getFromSubClass() {
		return fromSubClass;
	}

	public void setFromSubClass(String fromSubClass) {
		this.fromSubClass = fromSubClass;
	}

	public String getToSubClass() {
		return toSubClass;
	}

	public void setToSubClass(String toSubClass) {
		this.toSubClass = toSubClass;
	}

	public RetrievePnrRedUpgradeInfo getRedUpgradeInfo() {
		return redUpgradeInfo;
	}

	public void setRedUpgradeInfo(RetrievePnrRedUpgradeInfo redUpgradeInfo) {
		this.redUpgradeInfo = redUpgradeInfo;
	}

	public RetrievePnrBidUpgradeInfo getBidUpgradeInfo() {
		return bidUpgradeInfo;
	}

	public void setBidUpgradeInfo(RetrievePnrBidUpgradeInfo bidUpgradeInfo) {
		this.bidUpgradeInfo = bidUpgradeInfo;
	}

	public RetrievePnrBookableUpgradeInfo getBookableUpgradeInfo() {
		return bookableUpgradeInfo;
	}

	public void setBookableUpgradeInfo(RetrievePnrBookableUpgradeInfo bookableUpgradeInfo) {
		this.bookableUpgradeInfo = bookableUpgradeInfo;
	}
	
}
