package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import com.cathaypacific.mbcommon.enums.upgrade.UpgradeBidStatus;
import com.cathaypacific.mbcommon.enums.upgrade.UpgradeProgressStatus;
import com.cathaypacific.mbcommon.enums.upgrade.UpgradeRedStatus;
import com.cathaypacific.mbcommon.enums.upgrade.UpgradeType;

public class UpgradeCustomizedInfo {
	
	private  UpgradeType upgradeType;
	
	private String fromSubClass;
	
	private String fromCabinClass;
	
	private String toSubClass;
	
	private String toCabinClass;
	
	private UpgradeRedStatus upgradeRedStatus;
	
	private UpgradeBidStatus upgradeBidStatus;
	
	private UpgradeProgressStatus bookableUpgradeStatus;

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

	public UpgradeRedStatus getUpgradeRedStatus() {
		return upgradeRedStatus;
	}

	public void setUpgradeRedStatus(UpgradeRedStatus upgradeRedStatus) {
		this.upgradeRedStatus = upgradeRedStatus;
	}

	public UpgradeBidStatus getUpgradeBidStatus() {
		return upgradeBidStatus;
	}

	public void setUpgradeBidStatus(UpgradeBidStatus upgradeBidStatus) {
		this.upgradeBidStatus = upgradeBidStatus;
	}

	public UpgradeProgressStatus getBookableUpgradeStatus() {
		return bookableUpgradeStatus;
	}

	public void setBookableUpgradeStatus(UpgradeProgressStatus bookableUpgradeStatus) {
		this.bookableUpgradeStatus = bookableUpgradeStatus;
	}
 
}
