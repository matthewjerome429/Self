package com.cathaypacific.mmbbizrule.db.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by shane.tian.xia on 12/14/2017.
 */
@Embeddable
public class BookingStatusKey implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 218242263594600258L;

	@NotNull
    @Column(name="app_code", length = 5)
    private String appCode;

    @NotNull
    @Column(name="STATUS_CODE", length = 5)
    private String statusCode;

    public BookingStatusKey(){

    }

    public BookingStatusKey(String appCode, String statusCode){
        this.appCode = appCode;
        this.statusCode = statusCode;
    }

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
}
