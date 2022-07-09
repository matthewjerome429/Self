package com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import com.cathaypacific.mbcommon.annotation.IsUpdated;
import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.validator.constraints.PhoneNum;
import com.cathaypacific.mmbbizrule.validator.constraints.ValidWithGroup;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

public class UpdatePassengerDetailsRequestDTOV2 implements Serializable{

	private static final long serialVersionUID = -7617903965071272073L;

	private String rloc;
	private String passengerId;
	private String journeyId;
	@Valid
	private List<UpdateAdultSegmentInfoDTOV2> segments;	
	// phone info
	@IsUpdated
	@PhoneNum(groups = {MaskFieldGroup.class}, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private UpdatePhoneInfoDTOV2 phoneInfo;
	// email
	@IsUpdated
	@ValidWithGroup(groups = { MaskFieldGroup.class })
	private UpdateEmailDTOV2 email;
	// emergency contact
	@IsUpdated
	@PhoneNum(groups = {MaskFieldGroup.class}, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private UpdateEmergencyContactDTOV2 emergencyContact;
	// destination address
	@Valid
	private UpdateDestinationDTOV2 destination;
	// KTN
	@IsUpdated
	private UpdateTsDTOV2 ktn;
	// Redress No
	@IsUpdated
	private UpdateTsDTOV2 redress;
	@Valid
	private UpdateInfantDTOV2 infant;

	private String updateType;
	
	public String getRloc() {
		return rloc;
	}

	public String getUpdateType() {
		return this.updateType;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public List<UpdateAdultSegmentInfoDTOV2> getSegments() {
		return segments;
	}

	public void setSegments(List<UpdateAdultSegmentInfoDTOV2> segments) {
		this.segments = segments;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public UpdatePhoneInfoDTOV2 getPhoneInfo() {
		return phoneInfo;
	}
	
	public UpdatePhoneInfoDTOV2 findPhoneInfo() {
		if (phoneInfo == null) {
			phoneInfo = new UpdatePhoneInfoDTOV2();
		}
		return phoneInfo;
	}

	public void setPhoneInfo(UpdatePhoneInfoDTOV2 phoneInfo) {
		this.phoneInfo = phoneInfo;
	}

	public UpdateEmailDTOV2 getEmail() {
		return email;
	}

	public void setEmail(UpdateEmailDTOV2 email) {
		this.email = email;
	}

	public UpdateEmergencyContactDTOV2 getEmergencyContact() {
		return emergencyContact;
	}
	
	public UpdateEmergencyContactDTOV2 findEmergencyContact() {
		if (emergencyContact == null) {
			emergencyContact = new UpdateEmergencyContactDTOV2();
		}
		return emergencyContact;
	}

	public void setEmergencyContact(UpdateEmergencyContactDTOV2 emergencyContact) {
		this.emergencyContact = emergencyContact;
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

	public UpdateInfantDTOV2 getInfant() {
		return infant;
	}

	public void setInfant(UpdateInfantDTOV2 infant) {
		this.infant = infant;
	}

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}
}
