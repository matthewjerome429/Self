package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class TbPortFlightKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "APP_CODE")
	private String appCode;

	@NotNull
	@Column(name = "AIRLINE_CODE")
	private String airlineCode;

	@NotNull
	@Column(name = "FLT_NO_FROM")
	private int flightNumFrom;

	@NotNull
	@Column(name = "FLT_NO_TO")
	private int flightNumTo;
	
	@NotNull
	@Column(name = "ORIGIN")
	private String origin;
	
	@NotNull
	@Column(name = "DESTINATION")
	private String destination;

	@NotNull
	@Column(name = "SEG_TYPE")
	private String segType;

	@NotNull
	@Column(name = "START_DATE")
	private Date startDate;

	public TbPortFlightKey() {
		// Empty constructor.
	}

	public TbPortFlightKey(String appCode, String airlineCode, int flightNumFrom, int flightNumTo, String origin, String destination, String segType, Date startDate) {
		this.appCode = appCode;
		this.airlineCode = airlineCode;
		this.flightNumFrom = flightNumFrom;
		this.flightNumTo = flightNumTo;
		this.origin = origin;
		this.destination = destination;
		this.segType = segType;
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

	public int getFlightNumFrom() {
		return flightNumFrom;
	}

	public void setFlightNumFrom(int flightNumFrom) {
		this.flightNumFrom = flightNumFrom;
	}

	public int getFlightNumTo() {
		return flightNumTo;
	}

	public void setFlightNumTo(int flightNumTo) {
		this.flightNumTo = flightNumTo;
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

	public String getSegType() {
		return segType;
	}

	public void setSegType(String segType) {
		this.segType = segType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
