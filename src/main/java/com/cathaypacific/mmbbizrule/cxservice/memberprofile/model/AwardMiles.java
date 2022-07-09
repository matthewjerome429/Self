package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.AwardMilesExpiryInfo;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.LifeAwardPoints;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.LifeMpoClubPts;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.PeriodToDateAwardPoints;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.PtdMpoClubPts;

public class AwardMiles extends BaseResponse {

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

}
