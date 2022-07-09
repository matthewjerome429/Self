package com.cathaypacific.mmbbizrule.db.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name="tb_reminder_code")
@IdClass(SpecialServiceKey.class)
public class SpecialServiceModel{
	
	@Id
    @NotNull
    @Column(name = "app_code", length = 5)
    private String appCode;
	
    @Id
    @NotNull
    @Column(name="reminder_code", length = 5)
    private String reminderCode;

    @NotNull
    @Column(name = "type", length = 3)
    private String type;

    @Column(name = "point")
    private Integer point;

    @Id
    @Column(name = "action")
    private String action;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "last_update_timestamp")
    private Date lastUpdateTimeStamp;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastUpdateTimeStamp() {
        return lastUpdateTimeStamp;
    }

    public void setLastUpdateTimeStamp(Date lastUpdateTimeStamp) {
        this.lastUpdateTimeStamp = lastUpdateTimeStamp;
    }
}
