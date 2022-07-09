package com.cathaypacific.mmbbizrule.dto.request.passengerdetails;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import com.cathaypacific.mbcommon.annotation.IsUpdated;
import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.validator.constraints.PhoneNum;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

public class UpdatePassengerDetailsRequestDTO implements Serializable{

	private static final long serialVersionUID = -7617903965071272073L;

	private String rloc;
	private String passengerId;
	@Valid
	private List<UpdateAdultSegmentInfoDTO> segments;	
	// phone info
	@IsUpdated
	@PhoneNum(groups = {MaskFieldGroup.class}, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private UpdatePhoneInfoDTO phoneInfo;
	// email
	@IsUpdated
	private UpdateEmailDTO email;
	// emergency contact
	@IsUpdated
	@PhoneNum(groups = {MaskFieldGroup.class}, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private UpdateEmergencyContactDTO emergencyContact;
	// destination address
	@IsUpdated
	private UpdateDestinationAddressDTO destinationAddress;
	// KTN
	@IsUpdated
	private UpdateTsDTO ktn;
	// Redress No
	@IsUpdated
	private UpdateTsDTO redress;
	@Valid
	private UpdateInfantDTO infant;

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

	public List<UpdateAdultSegmentInfoDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<UpdateAdultSegmentInfoDTO> segments) {
		this.segments = segments;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public UpdatePhoneInfoDTO getPhoneInfo() {
		return phoneInfo;
	}
	
	public UpdatePhoneInfoDTO findPhoneInfo() {
		if (phoneInfo == null) {
			phoneInfo = new UpdatePhoneInfoDTO();
		}
		return phoneInfo;
	}

	public void setPhoneInfo(UpdatePhoneInfoDTO phoneInfo) {
		this.phoneInfo = phoneInfo;
	}

	public UpdateEmailDTO getEmail() {
		return email;
	}

	public void setEmail(UpdateEmailDTO email) {
		this.email = email;
	}

	public UpdateEmergencyContactDTO getEmergencyContact() {
		return emergencyContact;
	}
	
	public UpdateEmergencyContactDTO findEmergencyContact() {
		if (emergencyContact == null) {
			emergencyContact = new UpdateEmergencyContactDTO();
		}
		return emergencyContact;
	}

	public void setEmergencyContact(UpdateEmergencyContactDTO emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public UpdateDestinationAddressDTO getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(UpdateDestinationAddressDTO destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public UpdateTsDTO getKtn() {
		return ktn;
	}

	public void setKtn(UpdateTsDTO ktn) {
		this.ktn = ktn;
	}

	public UpdateTsDTO getRedress() {
		return redress;
	}

	public void setRedress(UpdateTsDTO redress) {
		this.redress = redress;
	}

	public UpdateInfantDTO getInfant() {
		return infant;
	}

	public void setInfant(UpdateInfantDTO infant) {
		this.infant = infant;
	}
}
