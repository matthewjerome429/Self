package com.cathaypacific.mmbbizrule.dto.common;

public class CustomizedRequiredInfoDTO {

	private boolean memberHolidayCheck = false;
	
	private boolean cprCheck = false;
	
	private boolean rtfsTime = false;
	
	private boolean bookableUpgradeStatusCheck = false;
	
	private boolean seatInfo = false;

	public boolean getMemberHolidayCheck() {
		return memberHolidayCheck;
	}

	public void setMemberHolidayCheck(boolean memberHolidayCheck) {
		this.memberHolidayCheck = memberHolidayCheck;
	}

	public boolean getCprCheck() {
		return cprCheck;
	}

	public void setCprCheck(boolean cprCheck) {
		this.cprCheck = cprCheck;
	}

	public boolean getRtfsTime() {
		return rtfsTime;
	}

	public void setRtfsTime(boolean rtfsTime) {
		this.rtfsTime = rtfsTime;
	}

	public boolean isBookableUpgradeStatusCheck() {
		return bookableUpgradeStatusCheck;
	}

	public void setBookableUpgradeStatusCheck(boolean bookableUpgradeStatusCheck) {
		this.bookableUpgradeStatusCheck = bookableUpgradeStatusCheck;
	}

	public boolean getSeatInfo() {
		return seatInfo;
	}

	public void setSeatInfo(boolean seatInfo) {
		this.seatInfo = seatInfo;
	}
}
