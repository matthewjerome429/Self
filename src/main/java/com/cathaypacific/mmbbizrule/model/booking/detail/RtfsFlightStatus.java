package com.cathaypacific.mmbbizrule.model.booking.detail;

import com.cathaypacific.mbcommon.enums.flightstatus.RtfsStatusEnum;

public class RtfsFlightStatus {
	
	private Integer sequenceId;
	
	/** The detailed departure time. */
	private RtfsDepartureArrivalTime departureTime;
	
	/** The detailed arrival time. */
	private RtfsDepartureArrivalTime arrivalTime;
	
	/** The origin. */
	private String originPort;
	
	/** The destination. */
	private String destPort;
	
	 /** flight status*/
	private RtfsStatusEnum status;
	
	/** The scenario ID */
	private Integer scenarioId;
	
	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public RtfsDepartureArrivalTime getDepartureTime() {
		return departureTime;
	}
	
	public void setDepartureTime(RtfsDepartureArrivalTime departureTime) {
		this.departureTime = departureTime;
	}

	public RtfsDepartureArrivalTime findDepartureTime() {
		if (this.departureTime == null) {
			this.departureTime = new RtfsDepartureArrivalTime();
		}
		return this.departureTime;
	}
	
	public RtfsDepartureArrivalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(RtfsDepartureArrivalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public RtfsDepartureArrivalTime findArrivalTime() {
		if (this.arrivalTime == null) {
			this.arrivalTime = new RtfsDepartureArrivalTime();
		}
		return this.arrivalTime;
	}
	
	public String getOriginPort() {
		return originPort;
	}

	public void setOriginPort(String originPort) {
		this.originPort = originPort;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public RtfsStatusEnum getStatus() {
		return status;
	}

	public void setStatus(RtfsStatusEnum status) {
		this.status = status;
	}

	public Integer getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(Integer scenarioId) {
		this.scenarioId = scenarioId;
	}
	
}
