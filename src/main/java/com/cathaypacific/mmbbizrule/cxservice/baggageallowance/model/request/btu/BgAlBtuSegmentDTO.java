package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu;

import java.io.Serializable;

public class BgAlBtuSegmentDTO implements Serializable {

	private static final long serialVersionUID = -5412298949492020517L;

	private String boardPoint;

	private String offPoint;

	private String operatingCarrier;

	private String cabinClass;

	/**
	 * GMT of departure date.
	 */
	private String departureDate;

	/**
	 * GMT of departure time.
	 */
	private String departureTime;

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

}
