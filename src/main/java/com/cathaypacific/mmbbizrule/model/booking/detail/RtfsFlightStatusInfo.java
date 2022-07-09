package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.enums.flightstatus.RtfsStatusEnum;
import com.google.common.collect.Lists;

public class RtfsFlightStatusInfo {
	/** rtfs flight status*/
	private RtfsStatusEnum flightStatus;
	
	/** rtfs flight status*/
	private List<RtfsFlightStatus> rtfsLegsStatus;
	
	/** rtfs passengers advice **/
	private List<AdviceToPassengerInfo> adviceToPassengers;
	
	public RtfsStatusEnum getFlightStatus() {
		return flightStatus;
	}

	public void setFlightStatus(RtfsStatusEnum flightStatus) {
		this.flightStatus = flightStatus;
	}

	public List<RtfsFlightStatus> getRtfsLegsStatus() {
		return rtfsLegsStatus;
	}
	
	public List<RtfsFlightStatus> findRtfsLegsStatus() {
		if(CollectionUtils.isEmpty(this.rtfsLegsStatus)) {
			this.rtfsLegsStatus = Lists.newArrayList();
		}
		return this.rtfsLegsStatus;
	}
	
	public void setRtfsLegsStatus(List<RtfsFlightStatus> rtfsLegsStatus) {
		this.rtfsLegsStatus = rtfsLegsStatus;
	}

	public List<AdviceToPassengerInfo> getAdviceToPassengers() {
		return adviceToPassengers;
	}

	public void setAdviceToPassengers(List<AdviceToPassengerInfo> adviceToPassengers) {
		this.adviceToPassengers = adviceToPassengers;
	}
	public List<AdviceToPassengerInfo> findAdviceToPassengers() {
		if(CollectionUtils.isEmpty(this.adviceToPassengers)) {
			this.adviceToPassengers = Lists.newArrayList();
		}
		return this.adviceToPassengers;
	}
}
