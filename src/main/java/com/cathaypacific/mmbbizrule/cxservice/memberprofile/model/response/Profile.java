package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile {
    // 01GeneralProfile
    private CustomerRecord customerRecord;

    private CustomerEmailAddressInfo customerEmailAddressInfo;

    private CustomerMobilePhoneInfo customerMobilePhoneInfo;

    // 02ContactInfo
    private AddressDetails addressDetails;

    private CustomerFaxInfo customerFaxInfo;

    // 03TravelInfo
    private AirActivityRecords airActivityRecords;

    // 04NonAirActivity
    private NonAirActivityRecords nonAirActivityRecords;

    // 05Lifestyle
    private MemberLifestyleInfo memberLifestyleInfo;

    // 06LocalLangInfo
    private LocalLanguageCustomerInfo localLanguageCustomerInfo;

    // 07AwardMiles
    private LifeAwardPoints lifeAwardPoints;

    private String lifePointsRedeemed;

    private PeriodToDateAwardPoints periodToDateAwardPoints;

    private String ptdPointsRedeemed;

    private Double milesPurchaseLimit;

    private Double milesReceivedLimit;

    private Integer lifeStatusMiles;

    private Integer lifeStatusSectors;

    private LifeMpoClubPts lifeMpoClubPts;

    private PtdMpoClubPts ptdMpoClubPts;

    private String mpoClubPtsBalance;

    private AwardMilesExpiryInfo awardMilesExpiryInfo;

    // 08AffiliationInfo
    private String programCode;

    private MemberAffiliationInfo memberAffiliationInfo;

    // 09CommunicationLoggingInfo
    private CommunicationLogging communicationLogging;

    // 10RedemptionGroup
    private RedemptionGroup redemptionGroup;

    public CustomerRecord getCustomerRecord() {
        return customerRecord;
    }

    public void setCustomerRecord(final CustomerRecord customerRecord) {
        this.customerRecord = customerRecord;
    }

    public CustomerEmailAddressInfo getCustomerEmailAddressInfo() {
        return customerEmailAddressInfo;
    }

    public void setCustomerEmailAddressInfo(final CustomerEmailAddressInfo customerEmailAddressInfo) {
        this.customerEmailAddressInfo = customerEmailAddressInfo;
    }

    public CustomerMobilePhoneInfo getCustomerMobilePhoneInfo() {
        return customerMobilePhoneInfo;
    }

    public void setCustomerMobilePhoneInfo(final CustomerMobilePhoneInfo customerMobilePhoneInfo) {
        this.customerMobilePhoneInfo = customerMobilePhoneInfo;
    }

    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(final AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    public CustomerFaxInfo getCustomerFaxInfo() {
        return customerFaxInfo;
    }

    public void setCustomerFaxInfo(final CustomerFaxInfo customerFaxInfo) {
        this.customerFaxInfo = customerFaxInfo;
    }

    public AirActivityRecords getAirActivityRecords() {
        return airActivityRecords;
    }

    public void setAirActivityRecords(final AirActivityRecords airActivityRecords) {
        this.airActivityRecords = airActivityRecords;
    }

    public NonAirActivityRecords getNonAirActivityRecords() {
        return nonAirActivityRecords;
    }

    public void setNonAirActivityRecords(final NonAirActivityRecords nonAirActivityRecords) {
        this.nonAirActivityRecords = nonAirActivityRecords;
    }

    public MemberLifestyleInfo getMemberLifestyleInfo() {
        return memberLifestyleInfo;
    }

    public void setMemberLifestyleInfo(final MemberLifestyleInfo memberLifestyleInfo) {
        this.memberLifestyleInfo = memberLifestyleInfo;
    }

    public LocalLanguageCustomerInfo getLocalLanguageCustomerInfo() {
        return localLanguageCustomerInfo;
    }

    public void setLocalLanguageCustomerInfo(final LocalLanguageCustomerInfo localLanguageCustomerInfo) {
        this.localLanguageCustomerInfo = localLanguageCustomerInfo;
    }

    public LifeAwardPoints getLifeAwardPoints() {
        return lifeAwardPoints;
    }

    public void setLifeAwardPoints(final LifeAwardPoints lifeAwardPoints) {
        this.lifeAwardPoints = lifeAwardPoints;
    }

    public String getLifePointsRedeemed() {
        return lifePointsRedeemed;
    }

    public void setLifePointsRedeemed(final String lifePointsRedeemed) {
        this.lifePointsRedeemed = lifePointsRedeemed;
    }

    public PeriodToDateAwardPoints getPeriodToDateAwardPoints() {
        return periodToDateAwardPoints;
    }

    public void setPeriodToDateAwardPoints(final PeriodToDateAwardPoints periodToDateAwardPoints) {
        this.periodToDateAwardPoints = periodToDateAwardPoints;
    }

    public String getPtdPointsRedeemed() {
        return ptdPointsRedeemed;
    }

    public void setPtdPointsRedeemed(final String ptdPointsRedeemed) {
        this.ptdPointsRedeemed = ptdPointsRedeemed;
    }

    public Double getMilesPurchaseLimit() {
        return milesPurchaseLimit;
    }

    public void setMilesPurchaseLimit(final Double milesPurchaseLimit) {
        this.milesPurchaseLimit = milesPurchaseLimit;
    }

    public Double getMilesReceivedLimit() {
        return milesReceivedLimit;
    }

    public void setMilesReceivedLimit(final Double milesReceivedLimit) {
        this.milesReceivedLimit = milesReceivedLimit;
    }

    public Integer getLifeStatusMiles() {
        return lifeStatusMiles;
    }

    public void setLifeStatusMiles(final Integer lifeStatusMiles) {
        this.lifeStatusMiles = lifeStatusMiles;
    }

    public Integer getLifeStatusSectors() {
        return lifeStatusSectors;
    }

    public void setLifeStatusSectors(final Integer lifeStatusSectors) {
        this.lifeStatusSectors = lifeStatusSectors;
    }

    public LifeMpoClubPts getLifeMpoClubPts() {
        return lifeMpoClubPts;
    }

    public void setLifeMpoClubPts(final LifeMpoClubPts lifeMpoClubPts) {
        this.lifeMpoClubPts = lifeMpoClubPts;
    }

    public PtdMpoClubPts getPtdMpoClubPts() {
        return ptdMpoClubPts;
    }

    public void setPtdMpoClubPts(final PtdMpoClubPts ptdMpoClubPts) {
        this.ptdMpoClubPts = ptdMpoClubPts;
    }

    public String getMpoClubPtsBalance() {
        return mpoClubPtsBalance;
    }

    public void setMpoClubPtsBalance(final String mpoClubPtsBalance) {
        this.mpoClubPtsBalance = mpoClubPtsBalance;
    }

    public AwardMilesExpiryInfo getAwardMilesExpiryInfo() {
        return awardMilesExpiryInfo;
    }

    public void setAwardMilesExpiryInfo(final AwardMilesExpiryInfo awardMilesExpiryInfo) {
        this.awardMilesExpiryInfo = awardMilesExpiryInfo;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(final String programCode) {
        this.programCode = programCode;
    }

    public MemberAffiliationInfo getMemberAffiliationInfo() {
        return memberAffiliationInfo;
    }

    public void setMemberAffiliationInfo(final MemberAffiliationInfo memberAffiliationInfo) {
        this.memberAffiliationInfo = memberAffiliationInfo;
    }

    public CommunicationLogging getCommunicationLogging() {
        return communicationLogging;
    }

    public void setCommunicationLogging(final CommunicationLogging communicationLogging) {
        this.communicationLogging = communicationLogging;
    }

    public RedemptionGroup getRedemptionGroup() {
        return redemptionGroup;
    }

    public void setRedemptionGroup(final RedemptionGroup redemptionGroup) {
        this.redemptionGroup = redemptionGroup;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Profile{");
        sb.append("customerRecord=").append(customerRecord);
        sb.append(", customerEmailAddressInfo=").append(customerEmailAddressInfo);
        sb.append(", customerMobilePhoneInfo=").append(customerMobilePhoneInfo);
        sb.append(", addressDetails=").append(addressDetails);
        sb.append(", customerFaxInfo=").append(customerFaxInfo);
        sb.append(", airActivityRecords=").append(airActivityRecords);
        sb.append(", nonAirActivityRecords=").append(nonAirActivityRecords);
        sb.append(", memberLifestyleInfo=").append(memberLifestyleInfo);
        sb.append(", localLanguageCustomerInfo=").append(localLanguageCustomerInfo);
        sb.append(", lifeAwardPoints=").append(lifeAwardPoints);
        sb.append(", lifePointsRedeemed='").append(lifePointsRedeemed).append('\'');
        sb.append(", periodToDateAwardPoints=").append(periodToDateAwardPoints);
        sb.append(", ptdPointsRedeemed='").append(ptdPointsRedeemed).append('\'');
        sb.append(", milesPurchaseLimit='").append(milesPurchaseLimit).append('\'');
        sb.append(", milesReceivedLimit='").append(milesReceivedLimit).append('\'');
        sb.append(", lifeStatusMiles='").append(lifeStatusMiles).append('\'');
        sb.append(", lifeStatusSectors='").append(lifeStatusSectors).append('\'');
        sb.append(", lifeMpoClubPts=").append(lifeMpoClubPts);
        sb.append(", ptdMpoClubPts=").append(ptdMpoClubPts);
        sb.append(", mpoClubPtsBalance='").append(mpoClubPtsBalance).append('\'');
        sb.append(", awardMilesExpiryInfo=").append(awardMilesExpiryInfo);
        sb.append(", programCode='").append(programCode).append('\'');
        sb.append(", memberAffiliationInfo=").append(memberAffiliationInfo);
        sb.append(", communicationLogging=").append(communicationLogging);
        sb.append(", redemptionGroup=").append(redemptionGroup);
        sb.append('}');
        return sb.toString();
    }
}
