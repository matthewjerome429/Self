package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TbCardPaymentMappingPK implements Serializable {
	
	private static final long serialVersionUID = 534252401070251295L;

	@Column(name = "app_code")
	private String appCode;
	
	@Column(name = "card_type")
	private String cardType;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
}
