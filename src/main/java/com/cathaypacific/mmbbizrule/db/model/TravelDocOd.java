package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@IdClass(TravelDocOdKey.class)
@Table(name="tb_travel_doc_od")
public class TravelDocOd implements Serializable {
	
	private static final long serialVersionUID = 218352463594600279L;
	
	@Id
	@NotNull
    @Column(name="app_code", length = 5)
	private String appCode;

	@Id
	@NotNull
	@Column(name = "carrier_code", length = 2)
	private String carrierCode;
	
	@Id
	@NotNull
    @Column(name="origin", length = 2)
	private String origin;
	
	@Id
	@NotNull
    @Column(name="destination", length = 2)
	private String destination;
	
	@Id
	@NotNull
    @Column(name="travel_doc_version", length =3)
	private int travelDocVersion;
	
	@Id
	@NotNull
    @Column(name="start_date")
	private Date startDate;
	
	@NotNull
	@Column(name="end_date")
	private Date endDate;
	
	@NotNull
	@Column(name="last_update_source", length = 8)
	private String lastUpdateSource;
	
	@Column(name="last_update_timestamp")
	private Timestamp lastUpdateTimeStamp;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
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

	public int getTravelDocVersion() {
		return travelDocVersion;
	}

	public void setTravelDocVersion(int travelDocVersion) {
		this.travelDocVersion = travelDocVersion;
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

	public Timestamp getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}

	public void setLastUpdateTimeStamp(Timestamp lastUpdateTimeStamp) {
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
}
