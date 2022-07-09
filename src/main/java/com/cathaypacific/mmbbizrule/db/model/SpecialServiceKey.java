package com.cathaypacific.mmbbizrule.db.model;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SpecialServiceKey implements Serializable {

    private static final long serialVersionUID = 2636198689978108484L;

    @NotNull
    @Column(name = "app_code", length = 5)
    private String appCode;
    
    @NotNull
    @Column(name="reminder_code", length = 5)
    private String reminderCode;

    @NotNull
    @Column(name = "action")
    private String action;
   
    public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getReminderCode() {
        return reminderCode;
    }

    public void setReminderCode(String reminderCode) {
        this.reminderCode = reminderCode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
