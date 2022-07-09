package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.math.BigInteger;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FQTVInfo {
	
	/** FQTV company ID */
	private String companyId;
	
	/** FQTV membership number */
	private String membershipNumber;
	
	/** FQTV membership tier level */
	private String tierLevel;
	
	/** determine whether tier is top or not*/
	private Boolean isTopTier;
	
	/** determine whether this is product level or not */
	private Boolean productLevel;
	
	/** determine whether it's AM FQTV or not */
	private Boolean am;
	
	/** determine whether it's MPO FQTV or not */
	private Boolean mpo;
	
	/** determine whether it's one world FQTV or not */
	private Boolean oneWorld;
	
	/**indicate whether member on holiday */
	private Boolean onHoliday;
	
	/** member holiday startDate */
	private String holidayStartDate;
	
	/** member holiday endDate */
	private String holidayEndDate;
	
	/** OT number in pnr */
	private BigInteger qualifierId;
	
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

	public Boolean isProductLevel() {
		return productLevel;
	}

	public void setProductLevel(Boolean productLevel) {
		this.productLevel = productLevel;
	}
	
	public Boolean getAm() {
		return am;
	}

	public void setAm(Boolean am) {
		this.am = am;
	}

	public Boolean getMpo() {
		return mpo;
	}

	public void setMpo(Boolean mpo) {
		this.mpo = mpo;
	}
	
	public Boolean getOneWorld() {
		return oneWorld;
	}

	public void setOneWorld(Boolean oneWorld) {
		this.oneWorld = oneWorld;
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

	public BigInteger getQualifierId() {
		return qualifierId;
	}

	public void setQualifierId(BigInteger qualifierId) {
		this.qualifierId = qualifierId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((membershipNumber == null) ? 0 : membershipNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FQTVInfo other = (FQTVInfo) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (membershipNumber == null) {
			if (other.membershipNumber != null)
				return false;
		} else if (!membershipNumber.equals(other.membershipNumber))
			return false;
		return true;
	}
	@JsonIgnore
	public boolean isBlank() {
		return StringUtils.isEmpty(companyId) && StringUtils.isEmpty(membershipNumber);
	}
}
