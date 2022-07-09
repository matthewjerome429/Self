package com.cathaypacific.mmbbizrule.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BizRuleConfig {
	
	@Value("${fqtv.member.cxka}")
	private String cxkaTierLevel;
	
	@Value("${fqtv.member.oneworld}")
	private String oneworldTierLevel;
	
	@Value("${fqtv.member.toptier}")
	private String topTier;
	
	@Value("${traveldoc.gender}")
	private String tdGenders;
	
	@Value("${traveldoc.infant.gender}")
	private String tdInfantGenders;
	
	@Value("${traveldoc.primary.type}")
	private String tdPrimaryTypes;
	
	@Value("${traveldoc.secondary.type}")
	private String tdSecondaryTypes;
	
	@Value("${fqtv.member.am}")
	private String amTierLevel;

	@Value("${seatSelection.eligible-fare-type}")
	private String seatSelectionEligibeFareTypes;

	@Value("${seatSelection.ineligibleSeatPreferenceAirlineCodes}")
	private String ineligibleSeatPreferenceAirlineCodes;
	
	@Value("${redemption.ticket.issuing.office}")
	private String redemptionIssuingOffice;
	
	@Value("${redemption.segment.subclass}")
	private String redemptionSegmentSubclass;

	@Value("${nametitle.male}")
	private String maleNameTitle;
	
	@Value("${nametitle.female}")
	private String femaleNameTitle;
	
	@Value("${onea.error.ignore.errorcategory}")
	private String ignoreCategoryFromOneAError;
	
	@Value("${officialreceipt.fp.freetext.prefix}")
	private String officialReceiptFpFreetextPrefix;
	
	@Value("${officialreceipt.eligible.iata.officenumbers}")
	private String officialReceiptIataOfficeNumbers;
	
	@Value("${mb.req.assistance.cutoff.timeInMillionSeconds}")
	private long cutOffTimeForRequestAssistance;
	
	@Value("${consent.page.required.locale}")
	private String consentPageRequiredLocale;
	
	@Value("#{'${booking.refund.feeWaiverDwCodes}'.split(',')}")
	private List<String> bookingRefundFeeWaiverDwCodes;
	
	public List<String> getCxkaTierLevel() {
		return cxkaTierLevel != null ? Arrays.asList(cxkaTierLevel.split(",")) : new ArrayList<>();
	}

	public List<String> getOneworldTierLevel() {
		return oneworldTierLevel != null ? Arrays.asList(oneworldTierLevel.split(",")) : new ArrayList<>();
	}

	public List<String> getTopTier() {
		return topTier != null ? Arrays.asList(topTier.split(",")) : new ArrayList<>();
	}

	public List<String> getTdGenders() {
		return tdGenders != null ? Arrays.asList(tdGenders.split(",")) : new ArrayList<>();
	}

	public List<String> getTdInfantGenders() {
		return tdInfantGenders != null ? Arrays.asList(tdInfantGenders.split(",")) : new ArrayList<>();
	}

	public List<String> getAmTierLevel() {
		return amTierLevel != null ? Arrays.asList(amTierLevel.split(",")) : new ArrayList<>();
	}

	public List<String> getTdPrimaryTypes() {
		return tdPrimaryTypes != null ? Arrays.asList(tdPrimaryTypes.split(",")) : new ArrayList<>();
	}

	public List<String> getTdSecondaryTypes() {
		return tdSecondaryTypes != null ? Arrays.asList(tdSecondaryTypes.split(",")) : new ArrayList<>();
	}
	
	public List<String> getSeatSelectionEligibeFareTypes() {
		return seatSelectionEligibeFareTypes != null ? Arrays.asList(seatSelectionEligibeFareTypes.split(",")) : new ArrayList<>();
	}

	public List<String> getIneligibleSeatPreferenceAirlineCodes() {
		return ineligibleSeatPreferenceAirlineCodes != null ? Arrays.asList(ineligibleSeatPreferenceAirlineCodes.split(",")) : new ArrayList<>();
	}
	
	public List<String> getRedemptionIssuingOffice() {
		return redemptionIssuingOffice != null ? Arrays.asList(redemptionIssuingOffice.split(",")) : new ArrayList<>();
	}
	
	public List<String> getRedemptionSegmentSubclass() {
		return redemptionSegmentSubclass != null ? Arrays.asList(redemptionSegmentSubclass.split(",")) : new ArrayList<>();
	}
	
	public List<String> getMaleNameTitle() {
		return maleNameTitle != null ? Arrays.asList(maleNameTitle.split(",")) : new ArrayList<>();
	}
	
	public List<String> getFemaleNameTitle() {
		return femaleNameTitle != null ? Arrays.asList(femaleNameTitle.split(",")) : new ArrayList<>();
	}
	
	public List<String> getIgnoreCategoryFromOneAError() {
		return ignoreCategoryFromOneAError != null ? Arrays.asList(ignoreCategoryFromOneAError.split(",")) : new ArrayList<>();
	}

	public List<String> getOfficialReceiptFpFreetextPrefix() {
		return officialReceiptFpFreetextPrefix != null ? Arrays.asList(officialReceiptFpFreetextPrefix.split(",")) : new ArrayList<>();
	}

	public List<String> getOfficialReceiptIataOfficeNumbers() {
		return officialReceiptIataOfficeNumbers != null ? Arrays.asList(officialReceiptIataOfficeNumbers.split(",")) : new ArrayList<>();
	}

    public long getCutOffTimeForRequestAssistance() {
        return cutOffTimeForRequestAssistance;
    }

    public void setCutOffTimeForRequestAssistance(long cutOffTimeForRequestAssistance) {
        this.cutOffTimeForRequestAssistance = cutOffTimeForRequestAssistance;
    }

	public List<String> getConsentPageRequiredLocale() {
		return consentPageRequiredLocale != null ? Arrays.asList(consentPageRequiredLocale.split(",")) : new ArrayList<>();
	}

	public List<String> getBookingRefundFeeWaiverDwCodes() {
		return bookingRefundFeeWaiverDwCodes != null ? bookingRefundFeeWaiverDwCodes : new ArrayList<>();
	}
	
}
