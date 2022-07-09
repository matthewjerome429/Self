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
@Table(name = "tb_application_consent_info")
@DynamicInsert
@DynamicUpdate
public class TbConsentLogInfo implements Serializable {
	private static final long serialVersionUID = -213512105943728915L;
	@Id
	@NotNull
	@Column(name = "LOG_ID")
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private int logId;
	
	@Column(name = "LOG_APPCODE", length = 40, nullable = false)
	private String appCode;

	@Column(name = "LOG_VERSIONNO", length = 8, nullable = false)
	private String versionNo;

	@Column(name = "LOG_CONSENTDATE", nullable = false)
	private Date consentDate;

	@Column(name = "LOG_LASTNAME", length = 32)
	private String lastName;

	@Column(name = "LOG_FIRSTNAME", length = 32)
	private String firstName;

	@Column(name = "LOG_RLOC", length = 6)
	private String recordLocator;

	@Column(name = "LOG_ETICKETNUMBER", length = 13)
	private String eTicketNo;

	@Column(name = "LOG_MEMBERID", length = 25)
	private String memberId;

	@Column(name = "LOG_SITE", length = 2)
	private String site;

	@Column(name = "LOG_COUNTRY", length = 2)
	private String country;

	@Column(name = "LOG_LANGUAGE", length = 2)
	private String language;

	@Column(name = "LOG_CARRIERCODE", length = 2)
	private String carrierCode;

	@Column(name = "LOG_FLIGHTNO", length = 4)
	private String flightNo;

	@Column(name = "LOG_FLIGHTDATE")
	private Date flightDate;

	@Column(name = "LOG_ALERTTYPE", length = 5)
	private String alertType;

	@Column(name = "LOG_ORIGIN", length = 3)
	private String origin;

	@Column(name = "LOG_DESTINATION", length = 3)
	private String destination;

	@Column(name = "LOG_EMAILADDRESS", length = 200)
	private String emailAddress;

	@Column(name = "LOG_MOBILENO", length = 33)
	private String mobileNo;

	@Column(name = "LOG_SOURCE", length = 8)
	private String source;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public Date getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getRecordLocator() {
		return recordLocator;
	}

	public void setRecordLocator(String recordLocator) {
		this.recordLocator = recordLocator;
	}

	public String geteTicketNo() {
		return eTicketNo;
	}

	public void seteTicketNo(String eTicketNo) {
		this.eTicketNo = eTicketNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public Date getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(Date flightDate) {
		this.flightDate = flightDate;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}