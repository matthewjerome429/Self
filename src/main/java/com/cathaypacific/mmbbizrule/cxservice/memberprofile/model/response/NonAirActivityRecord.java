package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NonAirActivityRecord {

    private String source;

    //@JsonFormat(pattern="yyyy-MM-dd")
    private String activityDate;

   // @JsonFormat(pattern="yyyy-MM-dd")
    private String creditingDate;

    private String promotionCode;

    private String partnerCode;

    private String partnerEstCode;

    private String activityDescription;

    private String referenceCode;

    private String activityDebitCredit;

    private String awardTotalPoints;

    private String awardBasicPoints;

    private String awardBonusPoints;

    private String suspenseStatus;

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}

	public String getCreditingDate() {
		return creditingDate;
	}

	public void setCreditingDate(String creditingDate) {
		this.creditingDate = creditingDate;
	}

	public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(final String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(final String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getPartnerEstCode() {
        return partnerEstCode;
    }

    public void setPartnerEstCode(final String partnerEstCode) {
        this.partnerEstCode = partnerEstCode;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(final String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(final String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getActivityDebitCredit() {
        return activityDebitCredit;
    }

    public void setActivityDebitCredit(final String activityDebitCredit) {
        this.activityDebitCredit = activityDebitCredit;
    }

    public String getAwardTotalPoints() {
        return awardTotalPoints;
    }

    public void setAwardTotalPoints(final String awardTotalPoints) {
        this.awardTotalPoints = awardTotalPoints;
    }

    public String getAwardBasicPoints() {
        return awardBasicPoints;
    }

    public void setAwardBasicPoints(final String awardBasicPoints) {
        this.awardBasicPoints = awardBasicPoints;
    }

    public String getAwardBonusPoints() {
        return awardBonusPoints;
    }

    public void setAwardBonusPoints(final String awardBonusPoints) {
        this.awardBonusPoints = awardBonusPoints;
    }

    public String getSuspenseStatus() {
        return suspenseStatus;
    }

    public void setSuspenseStatus(final String suspenseStatus) {
        this.suspenseStatus = suspenseStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NonAirActivityRecord{");
        sb.append("source='").append(source).append('\'');
        sb.append(", activityDate='").append(activityDate).append('\'');
        sb.append(", creditingDate='").append(creditingDate).append('\'');
        sb.append(", promotionCode='").append(promotionCode).append('\'');
        sb.append(", partnerCode='").append(partnerCode).append('\'');
        sb.append(", partnerEstCode='").append(partnerEstCode).append('\'');
        sb.append(", activityDescription='").append(activityDescription).append('\'');
        sb.append(", referenceCode='").append(referenceCode).append('\'');
        sb.append(", activityDebitCredit='").append(activityDebitCredit).append('\'');
        sb.append(", awardTotalPoints='").append(awardTotalPoints).append('\'');
        sb.append(", awardBasicPoints='").append(awardBasicPoints).append('\'');
        sb.append(", awardBonusPoints='").append(awardBonusPoints).append('\'');
        sb.append(", suspenseStatus='").append(suspenseStatus).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
