package com.cathaypacific.mmbbizrule.dto.request.consent;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class ConsentsDeleteRequestDTO {

	@NotEmpty(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<Integer> ids;

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

}
