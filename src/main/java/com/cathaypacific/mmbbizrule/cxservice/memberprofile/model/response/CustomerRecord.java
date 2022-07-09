package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRecord {

    private AmexCard amexCard;

    private String tierCode;

    private MemberName memberName;

    private String currentMembershipStatus;

   // @JsonFormat(pattern="yyyy-MM-dd")
    private String birthDate;

    private String promoMailInd;

    private String gender;

    private String preferredSpokenLanguage;

    private String lifetimeIndicator;

    private CurrentTierPeriod currentTierPeriod;

    //@JsonFormat(pattern="yyyy-MM-dd")
    private String joinDate;

    private String countryCode;

    private Account account;

    private String preferredWrittenLanguage;

    private String preferredCommunicationChannel;

    private String telemarketingPreference;

    private String previousTierCode;

    private String esummaryInd;

    private String disallowInternetAddrUpdate;

    private String disallowRedemptionInd;

    private String top100Status;

    private String custUsername;

    private String statusCode;

    private String youngMpoIndicator;

    private MembHoliday membHoliday;

    private String hvmIndicator;

    private String lifestyleNationality;

   // @JsonFormat(pattern="yyyy-MM-dd")
    private String enrollmentDate;

    private String cobrandCardHoldIef;

    private String memberNumber;

    public AmexCard getAmexCard() {
        return amexCard;
    }

    public void setAmexCard(final AmexCard amexCard) {
        this.amexCard = amexCard;
    }

    public String getTierCode() {
        return tierCode;
    }

    public void setTierCode(final String tierCode) {
        this.tierCode = tierCode;
    }

    public MemberName getMemberName() {
        return memberName;
    }

    public void setMemberName(final MemberName memberName) {
        this.memberName = memberName;
    }

    public String getCurrentMembershipStatus() {
        return currentMembershipStatus;
    }

    public void setCurrentMembershipStatus(final String currentMembershipStatus) {
        this.currentMembershipStatus = currentMembershipStatus;
    }


    public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getPromoMailInd() {
        return promoMailInd;
    }

    public void setPromoMailInd(final String promoMailInd) {
        this.promoMailInd = promoMailInd;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public String getPreferredSpokenLanguage() {
        return preferredSpokenLanguage;
    }

    public void setPreferredSpokenLanguage(final String preferredSpokenLanguage) {
        this.preferredSpokenLanguage = preferredSpokenLanguage;
    }

    public String getLifetimeIndicator() {
        return lifetimeIndicator;
    }

    public void setLifetimeIndicator(final String lifetimeIndicator) {
        this.lifetimeIndicator = lifetimeIndicator;
    }

    public CurrentTierPeriod getCurrentTierPeriod() {
        return currentTierPeriod;
    }

    public void setCurrentTierPeriod(final CurrentTierPeriod currentTierPeriod) {
        this.currentTierPeriod = currentTierPeriod;
    }

 

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    public String getPreferredWrittenLanguage() {
        return preferredWrittenLanguage;
    }

    public void setPreferredWrittenLanguage(final String preferredWrittenLanguage) {
        this.preferredWrittenLanguage = preferredWrittenLanguage;
    }

    public String getPreferredCommunicationChannel() {
        return preferredCommunicationChannel;
    }

    public void setPreferredCommunicationChannel(final String preferredCommunicationChannel) {
        this.preferredCommunicationChannel = preferredCommunicationChannel;
    }

    public String getTelemarketingPreference() {
        return telemarketingPreference;
    }

    public void setTelemarketingPreference(final String telemarketingPreference) {
        this.telemarketingPreference = telemarketingPreference;
    }

    public String getPreviousTierCode() {
        return previousTierCode;
    }

    public void setPreviousTierCode(final String previousTierCode) {
        this.previousTierCode = previousTierCode;
    }

    public String getEsummaryInd() {
        return esummaryInd;
    }

    public void setEsummaryInd(final String esummaryInd) {
        this.esummaryInd = esummaryInd;
    }

    public String getDisallowInternetAddrUpdate() {
        return disallowInternetAddrUpdate;
    }

    public void setDisallowInternetAddrUpdate(final String disallowInternetAddrUpdate) {
        this.disallowInternetAddrUpdate = disallowInternetAddrUpdate;
    }

    public String getDisallowRedemptionInd() {
        return disallowRedemptionInd;
    }

    public void setDisallowRedemptionInd(final String disallowRedemptionInd) {
        this.disallowRedemptionInd = disallowRedemptionInd;
    }

    public String getTop100Status() {
        return top100Status;
    }

    public void setTop100Status(final String top100Status) {
        this.top100Status = top100Status;
    }

    public String getCustUsername() {
        return custUsername;
    }

    public void setCustUsername(final String custUsername) {
        this.custUsername = custUsername;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    public String getYoungMpoIndicator() {
        return youngMpoIndicator;
    }

    public void setYoungMpoIndicator(final String youngMpoIndicator) {
        this.youngMpoIndicator = youngMpoIndicator;
    }

    public MembHoliday getMembHoliday() {
        return membHoliday;
    }

    public void setMembHoliday(final MembHoliday membHoliday) {
        this.membHoliday = membHoliday;
    }

    public String getHvmIndicator() {
        return hvmIndicator;
    }

    public void setHvmIndicator(final String hvmIndicator) {
        this.hvmIndicator = hvmIndicator;
    }

    public String getLifestyleNationality() {
        return lifestyleNationality;
    }

    public void setLifestyleNationality(final String lifestyleNationality) {
        this.lifestyleNationality = lifestyleNationality;
    }


    public String getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(String enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public String getCobrandCardHoldIef() {
        return cobrandCardHoldIef;
    }

    public void setCobrandCardHoldIef(final String cobrandCardHoldIef) {
        this.cobrandCardHoldIef = cobrandCardHoldIef;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(final String memberNumber) {
        this.memberNumber = memberNumber;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerRecord{");
        sb.append("amexCard=").append(amexCard);
        sb.append(", tierCode='").append(tierCode).append('\'');
        sb.append(", memberName=").append(memberName);
        sb.append(", currentMembershipStatus='").append(currentMembershipStatus).append('\'');
        sb.append(", birthDate='").append(birthDate).append('\'');
        sb.append(", promoMailInd='").append(promoMailInd).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", preferredSpokenLanguage='").append(preferredSpokenLanguage).append('\'');
        sb.append(", lifetimeIndicator='").append(lifetimeIndicator).append('\'');
        sb.append(", currentTierPeriod=").append(currentTierPeriod);
        sb.append(", joinDate='").append(joinDate).append('\'');
        sb.append(", countryCode='").append(countryCode).append('\'');
        sb.append(", account=").append(account);
        sb.append(", preferredWrittenLanguage='").append(preferredWrittenLanguage).append('\'');
        sb.append(", preferredCommunicationChannel='").append(preferredCommunicationChannel).append('\'');
        sb.append(", telemarketingPreference='").append(telemarketingPreference).append('\'');
        sb.append(", previousTierCode='").append(previousTierCode).append('\'');
        sb.append(", esummaryInd='").append(esummaryInd).append('\'');
        sb.append(", disallowInternetAddrUpdate='").append(disallowInternetAddrUpdate).append('\'');
        sb.append(", disallowRedemptionInd='").append(disallowRedemptionInd).append('\'');
        sb.append(", top100Status='").append(top100Status).append('\'');
        sb.append(", custUsername='").append(custUsername).append('\'');
        sb.append(", statusCode='").append(statusCode).append('\'');
        sb.append(", youngMpoIndicator='").append(youngMpoIndicator).append('\'');
        sb.append(", membHoliday=").append(membHoliday);
        sb.append(", hvmIndicator='").append(hvmIndicator).append('\'');
        sb.append(", lifestyleNationality='").append(lifestyleNationality).append('\'');
        sb.append(", enrollmentDate='").append(enrollmentDate).append('\'');
        sb.append(", cobrandCardHoldIef='").append(cobrandCardHoldIef).append('\'');
        sb.append(", memberNumber='").append(memberNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
