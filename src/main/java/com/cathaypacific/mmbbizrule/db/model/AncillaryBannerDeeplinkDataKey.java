package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class AncillaryBannerDeeplinkDataKey implements Serializable{
	
	private static final long serialVersionUID = 2636198689978108484L;

    @NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;
    
    @NotNull
    @Column(name = "market")
    private String market;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}
       
}
