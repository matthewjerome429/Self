package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TB_FLIGHT_HAUL")
@IdClass(TbFlightHaulKey.class)
public class TbFlightHaul {

	@Id
	@NotNull
	@Column(name = "APP_CODE", length = 10)
	private String appCode;

	@Id
	@NotNull
	@Column(name = "APT_CODE_ONE", length = 3)
	private String aptCodeOne;

	@Id
	@NotNull
	@Column(name = "APT_CODE_TWO", length = 3)
	private String aptCodeTwo;

	@NotNull
	@Column(name = "DESTINATION_CITY", length = 30)
	private String destinationCity;
	
	@NotNull
	@Column(name = "DESTINATION_COUNTRY", length = 30)
	private String destinationCountry;
	
	@NotNull
	@Column(name = "HAUL_TYPE", length = 2)
	private String haulType;
	
	@NotNull
	@Column(name = "MILES", length = 6)
	private int miles;
	
	@Id
	@NotNull
	@Column(name = "OPT_CX", length =1)
	private String optCx;
	
	@Id
	@NotNull
	@Column(name = "OPT_KA", length =1)
	private String optKa;
	
	@NotNull
	@Column(name = "ORIGIN_CITY", length =30)
	private String originCity;
	
	@NotNull
	@Column(name = "ORIGIN_COUNTRY", length =30)
	private String originCountry;
	
	@NotNull
	@Column(name = "last_update_source", length = 8)
	private String lastUpdateSource;
	
	@NotNull
	@Column(name = "last_update_timestamp")
	private Timestamp lastUpdateTimestamp;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAptCodeOne() {
		return aptCodeOne;
	}

	public void setAptCodeOne(String aptCodeOne) {
		this.aptCodeOne = aptCodeOne;
	}

	public String getAptCodeTwo() {
		return aptCodeTwo;
	}

	public void setAptCodeTwo(String aptCodeTwo) {
		this.aptCodeTwo = aptCodeTwo;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getHaulType() {
		return haulType;
	}

	public void setHaulType(String haulType) {
		this.haulType = haulType;
	}

	public int getMiles() {
		return miles;
	}

	public void setMiles(int miles) {
		this.miles = miles;
	}

	public String getOptCx() {
		return optCx;
	}

	public void setOptCx(String optCx) {
		this.optCx = optCx;
	}

	public String getOptKa() {
		return optKa;
	}

	public void setOptKa(String optKa) {
		this.optKa = optKa;
	}

	public String getOriginCity() {
		return originCity;
	}

	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
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
