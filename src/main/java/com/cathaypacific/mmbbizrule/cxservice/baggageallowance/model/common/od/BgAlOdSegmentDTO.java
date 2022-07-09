package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.common.od;

import java.io.Serializable;

public class BgAlOdSegmentDTO implements Serializable {

	private static final long serialVersionUID = 4133201350467229126L;

	private String boardPoint;

	private String offPoint;

	private String operatingCarrier;

	private String marketingCarrier;

	private String cabinClass;

	private String departureDate;

	private String departureTime;

	private String arrivalDate;

	private String arrivalTime;

	public String getBoardPoint() {
		return boardPoint;
	}

	public void setBoardPoint(String boardPoint) {
		this.boardPoint = boardPoint;
	}

	public String getOffPoint() {
		return offPoint;
	}

	public void setOffPoint(String offPoint) {
		this.offPoint = offPoint;
	}

	public String getOperatingCarrier() {
		return operatingCarrier;
	}

	public void setOperatingCarrier(String operatingCarrier) {
		this.operatingCarrier = operatingCarrier;
	}

	public String getMarketingCarrier() {
		return marketingCarrier;
	}

	public void setMarketingCarrier(String marketingCarrier) {
		this.marketingCarrier = marketingCarrier;
	}

	public String getCabinClass() {
		return cabinClass;
	}

	public void setCabinClass(String cabinClass) {
		this.cabinClass = cabinClass;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

}
