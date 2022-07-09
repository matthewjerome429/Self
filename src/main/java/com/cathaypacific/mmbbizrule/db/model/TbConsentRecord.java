package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "tb_consent_record")
@DynamicInsert
@DynamicUpdate
public class TbConsentRecord implements Serializable {
	private static final long serialVersionUID = -213512105943728915L;
	@Id
	@NotNull
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private int id;
	
	@Column(name = "APP_CODE")
	private String appCode;

	@Column(name = "CONSENT_DATE")
	private Date consentDate;

	@Column(name = "LOCALE")
	private String locale;

	@Column(name = "LANGUAGE")
	private String language;

	@Column(name = "MARKETING_CARRIER")
	private String marketingcarrier;

	@Column(name = "OPERATING_CARRIER")
	private String operatingCarrier;

	@Column(name = "FLIGHT_NO")
	private String flightNo;
	
	@Column(name = "FLIGHT_DEPARTURE_TIME")
	private Date flightDepartureTime;

	@Column(name = "SSR_PAX_LAST_NAME")
	private String ssrPaxLastName;

	@Column(name = "SSR_PAX_FIRST_NAME")
	private String ssrPaxFirstName;

	@Column(name = "LOGIN_PAX_LAST_NAME")
	private String loginPaxLastName;

	@Column(name = "LOGIN_PAX_FIRST_NAME")
	private String loginPaxFirstName;

	@Column(name = "RLOC")
	private String rloc;

	@Column(name = "E_TICKET")
	private String eTicket;

	@Column(name = "LOGIN_SOURCE")
	private String loginSource;

	@Column(name = "SSR")
	private String ssr;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public Date getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getMarketingcarrier() {
		return marketingcarrier;
	}

	public void setMarketingcarrier(String marketingcarrier) {
		this.marketingcarrier = marketingcarrier;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public String getSsrPaxLastName() {
		return ssrPaxLastName;
	}

	public void setSsrPaxLastName(String ssrPaxLastName) {
		this.ssrPaxLastName = ssrPaxLastName;
	}

	public String getSsrPaxFirstName() {
		return ssrPaxFirstName;
	}

	public void setSsrPaxFirstName(String ssrPaxFirstName) {
		this.ssrPaxFirstName = ssrPaxFirstName;
	}

	public String getLoginPaxLastName() {
		return loginPaxLastName;
	}

	public void setLoginPaxLastName(String loginPaxLastName) {
		this.loginPaxLastName = loginPaxLastName;
	}

	public String getLoginPaxFirstName() {
		return loginPaxFirstName;
	}

	public void setLoginPaxFirstName(String loginPaxFirstName) {
		this.loginPaxFirstName = loginPaxFirstName;
	}

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String geteTicket() {
		return eTicket;
	}

	public void seteTicket(String eTicket) {
		this.eTicket = eTicket;
	}

	public String getLoginSource() {
		return loginSource;
	}

	public void setLoginSource(String loginSource) {
		this.loginSource = loginSource;
	}

	public String getSsr() {
		return ssr;
	}

	public void setSsr(String ssr) {
		this.ssr = ssr;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getFlightDepartureTime() {
		return flightDepartureTime;
	}

	public void setFlightDepartureTime(Date flightDepartureTime) {
		this.flightDepartureTime = flightDepartureTime;
	}

	public String getOperatingCarrier() {
		return operatingCarrier;
	}

	public void setOperatingCarrier(String operatingCarrier) {
		this.operatingCarrier = operatingCarrier;
	}


	
}