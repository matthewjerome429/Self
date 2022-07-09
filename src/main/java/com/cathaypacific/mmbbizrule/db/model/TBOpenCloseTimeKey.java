package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class TBOpenCloseTimeKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "app_code")
	private String appCode;

	@NotNull
	@Column(name = "airline_code")
	private String airlineCode;

	@NotNull
	@Column(name = "origin")
	private String origin;

	@NotNull
	@Column(name = "pax_type")
	private String paxType;

	@NotNull
	@Column(name = "start_date")
	private Date startDate;

	public TBOpenCloseTimeKey() {
		// Empty constructor.
	}

	public TBOpenCloseTimeKey(String appCode, String airlineCode, String origin, String paxType, Date startDate) {
		this.appCode = appCode;
		this.airlineCode = airlineCode;
		this.origin = origin;
		this.paxType = paxType;
		this.startDate = startDate;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getPaxType() {
		return paxType;
	}

	public void setPaxType(String paxType) {
		this.paxType = paxType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
