package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.io.Serializable;

public class Priority implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1648904394842573545L;
    // name (mealPreselect_F, extraLegRm)
    private String name;
    // priority (0…10)
    private Integer priority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

}
