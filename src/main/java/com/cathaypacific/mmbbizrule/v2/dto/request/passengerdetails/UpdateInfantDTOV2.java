package com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import com.cathaypacific.mbcommon.annotation.IsUpdated;

public class UpdateInfantDTOV2 implements Serializable {

	private static final long serialVersionUID = 1644482762446924703L;
	
	private String passengerId;
	// destination address
	@Valid
	private UpdateDestinationDTOV2 destination;
	// KTN
	@IsUpdated
	private UpdateTsDTOV2 ktn;
	// redress no
	@IsUpdated
	private UpdateTsDTOV2 redress;
	@Valid
	private List<UpdateBasicSegmentInfoDTOV2> segments;
	
	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public List<UpdateBasicSegmentInfoDTOV2> getSegments() {
		return segments;
	}

	public void setSegments(List<UpdateBasicSegmentInfoDTOV2> segments) {
		this.segments = segments;
	}

	public UpdateDestinationDTOV2 getDestination() {
		return destination;
	}

	public void setDestination(UpdateDestinationDTOV2 destination) {
		this.destination = destination;
	}

	public UpdateTsDTOV2 getKtn() {
		return ktn;
	}

	public void setKtn(UpdateTsDTOV2 ktn) {
		this.ktn = ktn;
	}

	public UpdateTsDTOV2 getRedress() {
		return redress;
	}

	public void setRedress(UpdateTsDTOV2 redress) {
		this.redress = redress;
	}
	
}
