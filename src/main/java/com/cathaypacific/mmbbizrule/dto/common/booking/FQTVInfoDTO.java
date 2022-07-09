package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FQTVInfoDTO implements Serializable{
	
	private static final long serialVersionUID = -7918153024361628145L;

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
	private Boolean isAM;
	
	/** determine whether it's MPO FQTV or not */
	private Boolean isMPO;
	
	/** check member whether in MembershipHoliday */
	private Boolean onHoliday;

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
		FQTVInfoDTO other = (FQTVInfoDTO) obj;
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
	public Boolean isEmpty() {
		
		if(StringUtils.isEmpty(companyId) && StringUtils.isEmpty(membershipNumber) && StringUtils.isEmpty(tierLevel)) {
			
			return true;
		} else {
			return false;
		}
	}

}
