package com.cathaypacific.mmbbizrule.dto.response.rebooking;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class ProtectFlightInfoDTO implements Serializable {

	private static final long serialVersionUID = -5903630534091861774L;

	private String segmentId;
	
	@ApiModelProperty("market company id")
	private String marketCarrierCode;
	
	private String marketFlightNumber;
	
	@ApiModelProperty(value="departure schedule time, format: yyyy-MM-dd HH:mm", example="2018-09-14 02:45")
	private String scheduledDepartureTime;
	
	@ApiModelProperty(value="arrival schedule time, format: yyyy-MM-dd HH:mm", example="2018-09-14 02:45")
	private String scheduledArrivalTime;

	private String operatingCarrierCode;
	
	private String originAirportCode;
	
	private String destAirportCode;
	
	private String cabinCode;
	
	private String subclass;
	
	private String status;
	
	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getMarketCarrierCode() {
		return marketCarrierCode;
	}

	public void setMarketCarrierCode(String marketCarrierCode) {
		this.marketCarrierCode = marketCarrierCode;
	}

	public String getMarketFlightNumber() {
		return marketFlightNumber;
	}

	public void setMarketFlightNumber(String marketFlightNumber) {
		this.marketFlightNumber = marketFlightNumber;
	}

	public String getScheduledDepartureTime() {
		return scheduledDepartureTime;
	}

	public void setScheduledDepartureTime(String scheduledDepartureTime) {
		this.scheduledDepartureTime = scheduledDepartureTime;
	}

	public String getScheduledArrivalTime() {
		return scheduledArrivalTime;
	}

	public void setScheduledArrivalTime(String scheduledArrivalTime) {
		this.scheduledArrivalTime = scheduledArrivalTime;
	}

	public String getOperatingCarrierCode() {
		return operatingCarrierCode;
	}

	public void setOperatingCarrierCode(String operatingCarrierCode) {
		this.operatingCarrierCode = operatingCarrierCode;
	}

	public String getOriginAirportCode() {
		return originAirportCode;
	}

	public void setOriginAirportCode(String originAirportCode) {
		this.originAirportCode = originAirportCode;
	}

	public String getDestAirportCode() {
		return destAirportCode;
	}

	public void setDestAirportCode(String destAirportCode) {
		this.destAirportCode = destAirportCode;
	}

	public String getCabinCode() {
		return cabinCode;
	}

	public void setCabinCode(String cabinCode) {
		this.cabinCode = cabinCode;
	}

	public String getSubclass() {
		return subclass;
	}

	public void setSubclass(String subclass) {
		this.subclass = subclass;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
