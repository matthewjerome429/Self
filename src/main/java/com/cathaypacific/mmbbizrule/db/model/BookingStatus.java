package com.cathaypacific.mmbbizrule.db.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

/**
 * Created by shane.tian.xia on 12/14/2017.
 */
@Entity
@Table(name="tb_booking_status")
@IdClass(BookingStatusKey.class)
public class BookingStatus {

    @Id
    @NotNull
    @Column(name = "app_code", length = 5)
    private String appCode;

    @Id
    @NotNull
    @Column(name = "status_code")
    private String statusCode;

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "action")
    private String action;

    @NotNull
    @Column(name = "last_update_source")
    private String lastUpdateSource;

    @NotNull
    @Column(name = "last_update_timestamp")
    private Date lastUpdateTimeStamp;

    public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLastUpdateSource() {
        return lastUpdateSource;
    }

    public void setLastUpdateSource(String lastUpdateSource) {
        this.lastUpdateSource = lastUpdateSource;
    }

    public Date getLastUpdateTimeStamp() {
        return lastUpdateTimeStamp;
    }

    public void setLastUpdateTimeStamp(Date lastUpdateTimeStamp) {
        this.lastUpdateTimeStamp = lastUpdateTimeStamp;
    }
}
