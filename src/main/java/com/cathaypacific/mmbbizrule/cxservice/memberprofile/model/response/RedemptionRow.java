package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RedemptionRow {

    private String promotionCode;

    private String targetCode;

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(final String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(final String targetCode) {
        this.targetCode = targetCode;
    }
}
