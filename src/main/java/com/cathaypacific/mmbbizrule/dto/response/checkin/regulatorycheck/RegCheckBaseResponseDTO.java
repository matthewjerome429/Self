package com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;

/**
 * BaseResponseDTO.
 */
@ApiModel
public  class RegCheckBaseResponseDTO implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	
	private List<RegCheckError> errors;

	/**
	 * getErrors.
	 * 
	 * @return
	 */
	public List<RegCheckError> getErrors() {
		return errors;
	}

	/**
	 * setErrors.
	 * 
	 * @param errors
	 */
	public void setErrors(List<RegCheckError> errors) {
		this.errors = errors;
	}

	/**
	 * addErrors.
	 * 
	 * @param error
	 */
	public void addError(RegCheckError error) {
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
	public void addAllErrors(List<RegCheckError> inErrors) {
		if (inErrors == null || inErrors.isEmpty()) {
			return;
		}
		if (errors == null) {
			errors = new ArrayList<>();
		}
		errors.addAll(inErrors);
	}
}
