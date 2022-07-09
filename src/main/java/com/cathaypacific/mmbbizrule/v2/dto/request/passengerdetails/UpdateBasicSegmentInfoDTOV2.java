package com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails;

import java.io.Serializable;

import com.cathaypacific.mbcommon.annotation.IsUpdated;
import com.cathaypacific.mmbbizrule.validator.constraints.TravelDoc;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

public class UpdateBasicSegmentInfoDTOV2 implements Serializable{

	private static final long serialVersionUID = -4221895341352652674L;

	private String segmentId;
    
	/** The countryOfResidence */
	@IsUpdated
	private UpdateCountryOfResidenceDTOV2 countryOfResidence;
	
	@TravelDoc(groups = {MaskFieldGroup.class})
	private UpdateTravelDocDTOV2 primaryTravelDoc;

	@TravelDoc(groups = {MaskFieldGroup.class}, level = "secondary")
	private UpdateTravelDocDTOV2 secondaryTravelDoc;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public UpdateCountryOfResidenceDTOV2 getCountryOfResidence() {
		return countryOfResidence;
	}

	public void setCountryOfResidence(UpdateCountryOfResidenceDTOV2 countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}

	public UpdateTravelDocDTOV2 getPrimaryTravelDoc() {
		return primaryTravelDoc;
	}

	public void setPrimaryTravelDoc(UpdateTravelDocDTOV2 primaryTravelDoc) {
		this.primaryTravelDoc = primaryTravelDoc;
	}

	public UpdateTravelDocDTOV2 getSecondaryTravelDoc() {
		return secondaryTravelDoc;
	}

	public void setSecondaryTravelDoc(UpdateTravelDocDTOV2 secondaryTravelDoc) {
		this.secondaryTravelDoc = secondaryTravelDoc;
	}	

}
