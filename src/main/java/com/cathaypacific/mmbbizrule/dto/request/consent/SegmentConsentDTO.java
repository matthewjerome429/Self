package com.cathaypacific.mmbbizrule.dto.request.consent;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.validator.constraints.SingleMandatorySet;

import io.swagger.annotations.ApiModel;

@ApiModel("Use \"ssr\" to save single SSR, use \"ssrList\" to save multiple SSR. One and only one of them should be present.")
@SingleMandatorySet(fieldNames = { "ssr", "ssrList" },
		message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
public class SegmentConsentDTO {

	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String segmentId;
	
	private String ssr;
	
	private List<String> ssrList;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getSsr() {
		return ssr;
	}

	public void setSsr(String ssr) {
		this.ssr = ssr;
	}

	public List<String> getSsrList() {
		return ssrList;
	}

	public void setSsrList(List<String> ssrList) {
		this.ssrList = ssrList;
	}

}
