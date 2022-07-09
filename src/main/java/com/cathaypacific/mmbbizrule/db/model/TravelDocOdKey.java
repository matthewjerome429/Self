package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class TravelDocOdKey implements Serializable {

	private static final long serialVersionUID = 218342263594600268L;
	
	@NotNull
    @Column(name="app_code", length = 5)
	private String appCode;

	@NotNull
	@Column(name = "carrier_code", length = 2)
	private String carrierCode;
	
	@NotNull
    @Column(name="origin", length = 2)
	private String origin;
	
	@NotNull
    @Column(name="destination", length = 2)
	private String destination;
	
	@NotNull
    @Column(name="travel_doc_version", length =3)
	private int travelDocVersion;
	
	@NotNull
    @Column(name="start_date")
	private Date startDate;

	public TravelDocOdKey(){}
	
	public TravelDocOdKey(String appCode, String carrierCode, String destination, String origin, int travelDocVersion, Date startDate) {
		this.appCode = appCode;
		this.carrierCode = carrierCode;
		this.origin = origin;
		this.destination = destination;
		this.travelDocVersion = travelDocVersion;
		this.startDate = startDate;
	}
	
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

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
}
