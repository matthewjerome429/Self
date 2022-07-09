package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import java.io.Serializable;

public class FQTVCustomizedInfoDTO implements Serializable{
	
	private static final long serialVersionUID = -5393702540137455562L;

	/** FQTV company ID */
	private String companyId;
	
	/** FQTV membership number */
	private String membershipNumber;
	
	/** FQTV membership tier level */
	private String tierLevel;
	
	/** determine whether tier is top or not*/
	private Boolean isTopTier;
	
	/** determine whether it's AM FQTV or not */
	private Boolean isAM;
	
	/** determine whether it's MPO FQTV or not */
	private Boolean isMPO;
	
	/**indicate whether member on holiday */
	private Boolean onHoliday;
	
	/** member holiday startDate */
	private String holidayStartDate;
	
	/** member holiday endDate */
	private String holidayEndDate;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getMembershipNumber() {
		return membershipNumber;
	}

	public void setMembershipNumber(String membershipNumber) {
		this.membershipNumber = membershipNumber;
	}

	public String getTierLevel() {
		return tierLevel;
	}

	public void setTierLevel(String tierLevel) {
		this.tierLevel = tierLevel;
	}

	public Boolean isTopTier() {
		return isTopTier;
	}

	public void setTopTier(Boolean isTopTier) {
		this.isTopTier = isTopTier;
	}
	
	public Boolean isAM() {
		return isAM;
	}

	public void setIsAM(Boolean isAM) {
		this.isAM = isAM;
	}

	public Boolean isMPO() {
		return isMPO;
	}

	public void setIsMPO(Boolean isMPO) {
		this.isMPO = isMPO;
	}

	public Boolean getOnHoliday() {
		return onHoliday;
	}

	public void setOnHoliday(Boolean onHoliday) {
		this.onHoliday = onHoliday;
	}

	public String getHolidayStartDate() {
		return holidayStartDate;
	}

	public void setHolidayStartDate(String holidayStartDate) {
		this.holidayStartDate = holidayStartDate;
	}

	public String getHolidayEndDate() {
		return holidayEndDate;
	}

	public void setHolidayEndDate(String holidayEndDate) {
		this.holidayEndDate = holidayEndDate;
	}
	
}
