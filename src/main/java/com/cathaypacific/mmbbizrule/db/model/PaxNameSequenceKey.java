package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class PaxNameSequenceKey implements Serializable {
	
	
	private static final long serialVersionUID = 218242263594600258L;
	
	@NotNull
    @Column(name="app_code", length = 5)
    private String appCode;

    @NotNull
    @Column(name="locale", length = 2)
    private String locale;

    public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	
	public PaxNameSequenceKey(){

    }
    public PaxNameSequenceKey(String appCode, String locale){
        this.appCode = appCode;
        this.locale = locale;
    }
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
}
