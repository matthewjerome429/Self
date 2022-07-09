package com.cathaypacific.mmbbizrule.dto.request.oneaoperation;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class AddSkDetail {
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String ssrType;
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String companyId;
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String freeText;
	
	private List<String> passegnerIds;
	
	private List<String> segmentIds;

	public String getSsrType() {
		return ssrType;
	}

	public void setSsrType(String ssrType) {
		this.ssrType = ssrType;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getFreeText() {
		return freeText;
	}

	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}

	public List<String> getPassegnerIds() {
		return passegnerIds;
	}

	public void setPassegnerIds(List<String> passegnerIds) {
		this.passegnerIds = passegnerIds;
	}

	public List<String> getSegmentIds() {
		return segmentIds;
	}

	public void setSegmentIds(List<String> segmentIds) {
		this.segmentIds = segmentIds;
	}
	
}
