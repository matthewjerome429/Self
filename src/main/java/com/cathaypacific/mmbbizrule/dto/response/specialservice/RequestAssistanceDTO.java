package com.cathaypacific.mmbbizrule.dto.response.specialservice;

import java.io.Serializable;
import java.util.List;

public class RequestAssistanceDTO implements Serializable {

	private static final long serialVersionUID = 7931674748964666834L;

	private String rloc;

	private List<RequestAssistancePassengerDTO> passengers;
	private List<RequestAssistanceSegmentDTO> segments;
	private List<RequestAssistancePassengerSegmentDTO> passengerSegments;
	
	public String getRloc() {
		return rloc;
	}
	public void setRloc(String rloc) {
		this.rloc = rloc;
	}
	public List<RequestAssistancePassengerDTO> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<RequestAssistancePassengerDTO> passengers) {
		this.passengers = passengers;
	}
	public List<RequestAssistanceSegmentDTO> getSegments() {
		return segments;
	}
	public void setSegments(List<RequestAssistanceSegmentDTO> segments) {
		this.segments = segments;
	}
	public List<RequestAssistancePassengerSegmentDTO> getPassengerSegments() {
		return passengerSegments;
	}
	public void setPassengerSegments(List<RequestAssistancePassengerSegmentDTO> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}

}
