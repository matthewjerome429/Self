package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "tb_open_close_time")
@IdClass(TBOpenCloseTimeKey.class)
@NamedNativeQueries({
	@NamedNativeQuery(name = "getOpenCloseWindow", 
				query = "select * from tb_open_close_time where airline_code=:airlineCode"
						+ " and app_code in (:appCode,'*')"   //OLSS-4260 add default record when try to get info by appCode not OLCI
						+ " and origin in (:origin,'***')"
						+ " and pax_type=:paxType"
						+ " and :departureTime between start_date and end_date "
						+ " order by app_code desc, origin desc, last_update_timestamp desc limit 0,1", resultClass= TBOpenCloseTime.class) })
public class TBOpenCloseTime {

	@Id
	@NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;

	@Id
	@NotNull
	@Column(name = "airline_code", length = 2)
	private String airlineCode;

	@Id
	@NotNull
	@Column(name = "origin", length = 3)
	private String origin;

	@NotNull
	@Column(name = "open_time_limit")
	private int openTimeLimit;

	@NotNull
	@Column(name = "close_time_limit")
	private int closeTimeLimit;
	
	@NotNull
	@Column(name = "flight_closed_time")
	private int flightClosedTime;
	
	@Id
	@NotNull
	@Column(name = "pax_type", length = 3)
	private String paxType;

	@Id
	@NotNull
	@Column(name = "start_date")
	private Date startDate;

	@NotNull
	@Column(name = "end_date")
	private Date endDate;

	@CreatedBy
	@NotNull
	@Column(name = "last_update_source")
	private String lastUpdateSource;

	@CreatedDate
	@NotNull
	@Column(name = "last_update_timestamp")
	private Timestamp lastUpdateTimestamp;

	// @EmbeddedId (primary key for db)
	// private TBOpenCloseTimeKey tbOpenCloseTimeKey; (primary key for db)

	public TBOpenCloseTime() {
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

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public int getOpenTimeLimit() {
		return openTimeLimit;
	}

	public void setOpenTimeLimit(int openTimeLimit) {
		this.openTimeLimit = openTimeLimit;
	}

	public int getCloseTimeLimit() {
		return closeTimeLimit;
	}

	public void setCloseTimeLimit(int closeTimeLimit) {
		this.closeTimeLimit = closeTimeLimit;
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

	public int getFlightClosedTime() {
		return flightClosedTime;
	}

	public void setFlightClosedTime(int flightClosedTime) {
		this.flightClosedTime = flightClosedTime;
	}

}
