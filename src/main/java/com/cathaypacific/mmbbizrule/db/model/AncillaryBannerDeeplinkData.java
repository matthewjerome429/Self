package com.cathaypacific.mmbbizrule.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="tb_ancillary_banner_deeplink_data")
@IdClass(AncillaryBannerDeeplinkDataKey.class)
public class AncillaryBannerDeeplinkData {

	@Id
	@NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;
	
    @Id
    @NotNull
    @Column(name = "market")
    private String market;

    @Column(name = "_DTA_DESTINATION_")
    private String dtaDestination;

    @Column(name = "_DTA_COVER_FOR_")
    private String dtaCoverFor;
    
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

	public String getDtaDestination() {
		return dtaDestination;
	}

	public void setDtaDestination(String dtaDestination) {
		this.dtaDestination = dtaDestination;
	}

	public String getDtaCoverFor() {
		return dtaCoverFor;
	}

	public void setDtaCoverFor(String dtaCoverFor) {
		this.dtaCoverFor = dtaCoverFor;
	}
}
