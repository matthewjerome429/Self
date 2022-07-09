package com.cathaypacific.mmbbizrule.dto.request.umnrform;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class UmnrFormUpdateRequestDTO {
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@NotBlank(message= ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String passengerId;
	
	@NotBlank(message= ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String age;
	
	@NotBlank(message= ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@Pattern(regexp = "^(M|F)$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String gender;

	@Valid
	private UmnrFormGuardianInfoRemarkDTO parentInfo;
	
	@Valid
	private UmnrFormAddressRemarkDTO address;
	
	@Valid
	private List<UmnrFormSegmentRemarkDTO> segments;
	
	private boolean parentalLock = false;

	public String getRloc() {
		return rloc;
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

	public String getAge() {
		return age;
	}
	
	/**
	 * Return double digit age
	 * @return
	 */
	public String getDoubleDigitAge() {
		return String.format("%02d", Integer.parseInt(age));
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public UmnrFormAddressRemarkDTO getAddress() {
		return address;
	}

	public void setAddress(UmnrFormAddressRemarkDTO address) {
		this.address = address;
	}

	public List<UmnrFormSegmentRemarkDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<UmnrFormSegmentRemarkDTO> segments) {
		this.segments = segments;
	}

	public UmnrFormGuardianInfoRemarkDTO getParentInfo() {
		return parentInfo;
	}

	public void setParentInfo(UmnrFormGuardianInfoRemarkDTO parentInfo) {
		this.parentInfo = parentInfo;
	}

	public boolean isParentalLock() {
		return parentalLock;
	}

	public void setParentalLock(boolean parentalLock) {
		this.parentalLock = parentalLock;
	}

}
