package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class MealDetailsRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7177431124637410112L;

    private String jobId;
    private List<MealDetailRequestInfo> infos;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<MealDetailRequestInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<MealDetailRequestInfo> infos) {
        this.infos = infos;
    }

    public void addInfo(MealDetailRequestInfo info) {
        if (CollectionUtils.isEmpty(infos)) {
            infos = new ArrayList<MealDetailRequestInfo>();
        }
        infos.add(info);
    }

}
