package com.cathaypacific.mmbbizrule.model.booking.detail;

import com.cathaypacific.mbcommon.enums.upgrade.UpgradeType;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBidUpgradeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookableUpgradeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRedUpgradeInfo;

public class UpgradeInfo {
	
	private  UpgradeType upgradeType;
	
	private String fromSubClass;
	
	private String fromCabinClass;
	
	private String toSubClass;
	
	private String toCabinClass;
	
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

	public String getFromCabinClass() {
		return fromCabinClass;
	}

	public void setFromCabinClass(String fromCabinClass) {
		this.fromCabinClass = fromCabinClass;
	}

	public String getToCabinClass() {
		return toCabinClass;
	}

	public void setToCabinClass(String toCabinClass) {
		this.toCabinClass = toCabinClass;
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
