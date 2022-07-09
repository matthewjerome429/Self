package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.util.CollectionUtils;

public class MealDescriptionRequest {

    @NotNull
    private String jobId;

    @Valid
    @NotNull
    private List<MealDescriptionRequestInfo> infos;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<MealDescriptionRequestInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<MealDescriptionRequestInfo> infos) {
        this.infos = infos;
    }

    public void addInfo(MealDescriptionRequestInfo info) {
        if (CollectionUtils.isEmpty(this.infos)) {
            this.infos = new ArrayList<>();
        }
        this.infos.add(info);
    }

}
