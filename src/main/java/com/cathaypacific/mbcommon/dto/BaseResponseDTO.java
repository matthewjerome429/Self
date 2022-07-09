package com.cathaypacific.mbcommon.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

import io.swagger.annotations.ApiModel;

/**
 * BaseResponseDTO.
 */
@ApiModel
public  class BaseResponseDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7950343844574246670L;

	private List<ErrorInfo> errors;

	/**
	 * getErrors.
	 * 
	 * @return
	 */
	public List<ErrorInfo> getErrors() {
		return errors;
	}

	/**
	 * setErrors.
	 * 
	 * @param errors
	 */
	public void setErrors(List<ErrorInfo> errors) {
		this.errors = errors;
	}

	/**
	 * addErrors.
	 * 
	 * @param error
	 */
	public void addError(ErrorInfo error) {
		if (errors == null) {
			errors = new ArrayList<>();
		}
		errors.add(error);
	}

	/**
	 * add all Errors.
	 * 
	 * @param error
	 */
	public void addAllErrors(List<ErrorInfo> inErrors) {
		if (inErrors == null || inErrors.isEmpty()) {
			return;
		}
		if (errors == null) {
			errors = new ArrayList<>();
		}
		errors.addAll(inErrors);
	}
}
