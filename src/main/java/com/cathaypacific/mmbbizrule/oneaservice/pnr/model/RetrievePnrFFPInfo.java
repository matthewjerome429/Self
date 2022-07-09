package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;

public class RetrievePnrFFPInfo extends DataElement{
	
	/** FFP company ID */
	private String companyId;
	
	/** FFP company */
	private String ffpCompany;
	
	/** FFP membership number */
	private String ffpMembershipNumber;
	
	/** FFP membership tier level */
	private String tierLevel;
	
	/** FFP membership tier level */
	//private String oneWordTierLevel;
	
	/** determine whether tier is top or not*/
	private Boolean topTier;
	
	/** OT number in pnr */
	private BigInteger qualifierId;
	
	/** FFP status */
	private String status;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getFfpCompany() {
		return ffpCompany;
	}

	public void setFfpCompany(String ffpCompany) {
		this.ffpCompany = ffpCompany;
	}

	public String getFfpMembershipNumber() {
		return ffpMembershipNumber;
	}

	public void setFfpMembershipNumber(String ffpMembershipNumber) {
		this.ffpMembershipNumber = ffpMembershipNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTierLevel() {
		return tierLevel;
	}

	public void setTierLevel(String tierLevel) {
		this.tierLevel = tierLevel;
	}

	public Boolean isTopTier() {
		return topTier;
	}

	public void setTopTier(Boolean topTier) {
		this.topTier = topTier;
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
		result = prime * result + ((ffpCompany == null) ? 0 : ffpCompany.hashCode());
		result = prime * result + ((ffpMembershipNumber == null) ? 0 : ffpMembershipNumber.hashCode());
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
		RetrievePnrFFPInfo other = (RetrievePnrFFPInfo) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId)) {
			return false;
		}
		if (ffpCompany == null) {
			if (other.ffpCompany != null) {
				return false;
			}
		} else if (!ffpCompany.equals(other.ffpCompany)) {
			return false;
		}

		if (ffpMembershipNumber == null) {
			if (other.ffpMembershipNumber != null) {
				return false;
			}
		} else if (!ffpMembershipNumber.equals(other.ffpMembershipNumber)) {
			return false;
		}

		return true;
	}
}
