package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "tb_card_payment_mapping")
@IdClass(TbCardPaymentMappingPK.class)
public class TbCardPaymentMapping {
	
	@Id
	@Column(name = "app_code")
	private String appCode;
	
	@Id
	@Column(name = "card_type")
	private String cardType;
	
	@Column(name = "payment_type")
	private String paymentType;
	
	@Column(name = "last_update_source")
	private String lastUpdateSource;
	
	@Column(name = "last_update_datetime")
	private Date lastUpdateDatetime;

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

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getLastUpdateSource() {
		return lastUpdateSource;
	}

	public void setLastUpdateSource(String lastUpdateSource) {
		this.lastUpdateSource = lastUpdateSource;
	}

	public Date getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Date lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}
	
}
