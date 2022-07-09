package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_official_receipt_download_history")
public class TbOfficialReceiptDownloadHistory implements Serializable {

	private static final long serialVersionUID = -6741964963988322116L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "reference_prefix")
	private String referencePrefix;
	
	@Column(name = "app_code")
	private String appCode;
	
	@Column(name = "login_type")
	private String loginType;
	
	@Column(name = "member_id")
	private String memberId;
	
	@Column(name = "rloc")
	private String rloc;
	
	@Column(name = "login_passenger_id")
	private String loginPassengerId;
	
	@Column(name = "request_passenger_id")
	private String requestPassengerId;
	
	@Column(name = "eticket")
	private String eticket;
	
	@Column(name = "fp_code")
	private String fpCode;
	
	@Column(name = "amount")
	private String amount;
	
	@Column(name = "currency")
	private String currency;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReferencePrefix() {
		return referencePrefix;
	}

	public void setReferencePrefix(String referencePrefix) {
		this.referencePrefix = referencePrefix;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getLoginPassengerId() {
		return loginPassengerId;
	}

	public void setLoginPassengerId(String loginPassengerId) {
		this.loginPassengerId = loginPassengerId;
	}

	public String getRequestPassengerId() {
		return requestPassengerId;
	}

	public void setRequestPassengerId(String requestPassengerId) {
		this.requestPassengerId = requestPassengerId;
	}

	public String getEticket() {
		return eticket;
	}

	public void setEticket(String eticket) {
		this.eticket = eticket;
	}

	public String getFpCode() {
		return fpCode;
	}

	public void setFpCode(String fpCode) {
		this.fpCode = fpCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
}
