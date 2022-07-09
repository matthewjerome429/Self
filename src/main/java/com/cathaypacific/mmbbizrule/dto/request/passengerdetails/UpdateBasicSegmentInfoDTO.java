package com.cathaypacific.mmbbizrule.dto.request.passengerdetails;

import java.io.Serializable;

import com.cathaypacific.mmbbizrule.validator.constraints.TravelDoc;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

public class UpdateBasicSegmentInfoDTO implements Serializable{

	private static final long serialVersionUID = -4221895341352652674L;

	private String segmentId;
	
	@TravelDoc(groups = {MaskFieldGroup.class})
	private UpdateTravelDocDTO primaryTravelDoc;

	@TravelDoc(groups = {MaskFieldGroup.class}, level = "secondary")
	private UpdateTravelDocDTO secondaryTravelDoc;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public UpdateTravelDocDTO getPrimaryTravelDoc() {
		return primaryTravelDoc;
	}

	public void setPrimaryTravelDoc(UpdateTravelDocDTO primaryTravelDoc) {
		this.primaryTravelDoc = primaryTravelDoc;
	}

	public UpdateTravelDocDTO getSecondaryTravelDoc() {
		return secondaryTravelDoc;
	}

	public void setSecondaryTravelDoc(UpdateTravelDocDTO secondaryTravelDoc) {
		this.secondaryTravelDoc = secondaryTravelDoc;
	}	

}
