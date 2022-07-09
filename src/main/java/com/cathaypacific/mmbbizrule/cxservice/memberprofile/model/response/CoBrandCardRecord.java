package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoBrandCardRecord {

    private String cardIssuerCode;

    private String cardOrgCode;

    private String cardTypeCode;

    private String cardPrefixFrom;

    private String cardPrefixTo;

    private String feeWaiverInd;

    private String branding;

    private String countryCode;

    private String companyCode;

    //@JsonFormat(pattern="yyyy-MM-dd")
    private String startDate;

    //@JsonFormat(pattern="yyyy-MM-dd")
    private String endDate;

    private String milesRenewPacakgeCode;

    private Double milesRenewPackageAmount;

    private String milesRenewDiscountPercentage;

    private String milesTranPackageCode;

    private Double milesTranPackageAmount;

    private String milesTranSerFeePackaCode;

    private Double milesTranSerFeePackaAmount;

    private Double milesTranDiscountPercenta;

    private String giftMilesPackageCode;

    private Double giftMilesPackageAmount;

    private String giftMilesSerFeePackCode;

    private Double giftMilesSerFeePackAmount;

    private Double giftMilesDiscountPercentage;

    public String getCardIssuerCode() {
        return cardIssuerCode;
    }

    public void setCardIssuerCode(final String cardIssuerCode) {
        this.cardIssuerCode = cardIssuerCode;
    }

    public String getCardOrgCode() {
        return cardOrgCode;
    }

    public void setCardOrgCode(final String cardOrgCode) {
        this.cardOrgCode = cardOrgCode;
    }

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(final String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getCardPrefixFrom() {
        return cardPrefixFrom;
    }

    public void setCardPrefixFrom(final String cardPrefixFrom) {
        this.cardPrefixFrom = cardPrefixFrom;
    }

    public String getCardPrefixTo() {
        return cardPrefixTo;
    }

    public void setCardPrefixTo(final String cardPrefixTo) {
        this.cardPrefixTo = cardPrefixTo;
    }

    public String getFeeWaiverInd() {
        return feeWaiverInd;
    }

    public void setFeeWaiverInd(final String feeWaiverInd) {
        this.feeWaiverInd = feeWaiverInd;
    }

    public String getBranding() {
        return branding;
    }

    public void setBranding(final String branding) {
        this.branding = branding;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(final String companyCode) {
        this.companyCode = companyCode;
    }

    public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMilesRenewPacakgeCode() {
        return milesRenewPacakgeCode;
    }

    public void setMilesRenewPacakgeCode(final String milesRenewPacakgeCode) {
        this.milesRenewPacakgeCode = milesRenewPacakgeCode;
    }

    public Double getMilesRenewPackageAmount() {
        return milesRenewPackageAmount;
    }

    public void setMilesRenewPackageAmount(final Double milesRenewPackageAmount) {
        this.milesRenewPackageAmount = milesRenewPackageAmount;
    }

    public String getMilesRenewDiscountPercentage() {
        return milesRenewDiscountPercentage;
    }

    public void setMilesRenewDiscountPercentage(final String milesRenewDiscountPercentage) {
        this.milesRenewDiscountPercentage = milesRenewDiscountPercentage;
    }

    public String getMilesTranPackageCode() {
        return milesTranPackageCode;
    }

    public void setMilesTranPackageCode(final String milesTranPackageCode) {
        this.milesTranPackageCode = milesTranPackageCode;
    }

    public Double getMilesTranPackageAmount() {
        return milesTranPackageAmount;
    }

    public void setMilesTranPackageAmount(final Double milesTranPackageAmount) {
        this.milesTranPackageAmount = milesTranPackageAmount;
    }

    public String getMilesTranSerFeePackaCode() {
        return milesTranSerFeePackaCode;
    }

    public void setMilesTranSerFeePackaCode(final String milesTranSerFeePackaCode) {
        this.milesTranSerFeePackaCode = milesTranSerFeePackaCode;
    }

    public Double getMilesTranSerFeePackaAmount() {
        return milesTranSerFeePackaAmount;
    }

    public void setMilesTranSerFeePackaAmount(final Double milesTranSerFeePackaAmount) {
        this.milesTranSerFeePackaAmount = milesTranSerFeePackaAmount;
    }

    public Double getMilesTranDiscountPercenta() {
        return milesTranDiscountPercenta;
    }

    public void setMilesTranDiscountPercenta(final Double milesTranDiscountPercenta) {
        this.milesTranDiscountPercenta = milesTranDiscountPercenta;
    }

    public String getGiftMilesPackageCode() {
        return giftMilesPackageCode;
    }

    public void setGiftMilesPackageCode(final String giftMilesPackageCode) {
        this.giftMilesPackageCode = giftMilesPackageCode;
    }

    public Double getGiftMilesPackageAmount() {
        return giftMilesPackageAmount;
    }

    public void setGiftMilesPackageAmount(final Double giftMilesPackageAmount) {
        this.giftMilesPackageAmount = giftMilesPackageAmount;
    }

    public String getGiftMilesSerFeePackCode() {
        return giftMilesSerFeePackCode;
    }

    public void setGiftMilesSerFeePackCode(final String giftMilesSerFeePackCode) {
        this.giftMilesSerFeePackCode = giftMilesSerFeePackCode;
    }

    public Double getGiftMilesSerFeePackAmount() {
        return giftMilesSerFeePackAmount;
    }

    public void setGiftMilesSerFeePackAmount(final Double giftMilesSerFeePackAmount) {
        this.giftMilesSerFeePackAmount = giftMilesSerFeePackAmount;
    }

    public Double getGiftMilesDiscountPercentage() {
        return giftMilesDiscountPercentage;
    }

    public void setGiftMilesDiscountPercentage(final Double giftMilesDiscountPercentage) {
        this.giftMilesDiscountPercentage = giftMilesDiscountPercentage;
    }
}
