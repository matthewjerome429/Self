package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import com.cathaypacific.mbcommon.enums.upgrade.UpgradeProgressStatus;

public class RetrievePnrBookableUpgradeInfo {

	private UpgradeProgressStatus status;

	public UpgradeProgressStatus getStatus() {
		return status;
	}

	public void setStatus(UpgradeProgressStatus status) {
		this.status = status;
	}
	
}
