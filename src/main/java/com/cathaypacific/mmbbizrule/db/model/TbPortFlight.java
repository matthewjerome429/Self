package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "TB_PORT_FLIGHT")
@IdClass(TbPortFlightKey.class)
public class TbPortFlight {

	@Id
	@NotNull
	@Column(name = "APP_CODE", length = 5)
	private String appCode;

	@Id
	@NotNull
	@Column(name = "AIRLINE_CODE", length = 2)
	private String airlineCode;

	@Id
	@NotNull
	@Column(name = "FLT_NO_FROM")
	private int flightNumFrom;

	@Id
	@NotNull
	@Column(name = "FLT_NO_TO")
	private int flightNumTo;
	
	@Id
	@NotNull
	@Column(name = "ORIGIN", length = 3)
	private String origin;
	
	@Id
	@NotNull
	@Column(name = "DESTINATION", length = 3)
	private String destination;

	@Id
	@NotNull
	@Column(name = "SEG_TYPE", length = 3)
	private String segType;

	@Id
	@NotNull
	@Column(name = "VALUE", length = 1)
	private String value;

	@Id
	@NotNull
	@Column(name = "ERROR_CODE", length = 50)
	private String errorCode;
	
	@Id
	@NotNull
	@Column(name = "START_DATE")
	private Date startDate;

	@NotNull
	@Column(name = "END_DATE")
	private Date endDate;

	@CreatedBy
	@NotNull
	@Column(name = "LAST_UPDATE_SOURCE", length = 8)
	private String lastUpdateSource;

	@CreatedDate
	@NotNull
	@Column(name = "LAST_UPDATE_TIMESTAMP")
	private Timestamp lastUpdateTimestamp;

	// @EmbeddedId (primary key for db)
	// private TBPortFlightKey tbPortFlightKey;
	// (primary key for db)

	public TbPortFlight() {
		// Empty constructor.
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLastUpdateSource() {
		return lastUpdateSource;
	}

	public void setLastUpdateSource(String lastUpdateSource) {
		this.lastUpdateSource = lastUpdateSource;
	}

	public Timestamp getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

}
