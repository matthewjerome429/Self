package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.RedemptionRow;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.TargetRow;

public class PromotionInfo extends BaseResponse {

    public List<RedemptionRow> redemptionRow;

    public List<TargetRow> targetRow;

    public List<RedemptionRow> getRedemptionRow() {
        return redemptionRow;
    }

    public void setRedemptionRow(final List<RedemptionRow> redemptionRow) {
        this.redemptionRow = redemptionRow;
    }

    public List<TargetRow> getTargetRow() {
        return targetRow;
    }

    public void setTargetRow(final List<TargetRow> targetRow) {
        this.targetRow = targetRow;
    }
}
