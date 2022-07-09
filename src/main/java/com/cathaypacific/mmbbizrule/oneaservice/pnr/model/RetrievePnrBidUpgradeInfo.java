package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import com.cathaypacific.mbcommon.enums.upgrade.UpgradeBidStatus;

public class RetrievePnrBidUpgradeInfo {

	private UpgradeBidStatus status;
	
	public RetrievePnrBidUpgradeInfo() {
		super();
	}

	public RetrievePnrBidUpgradeInfo(UpgradeBidStatus status) {
		super();
		this.status = status;
	}

	public UpgradeBidStatus getStatus() {
		return status;
	}

	public void setStatus(UpgradeBidStatus status) {
		this.status = status;
	}

}
