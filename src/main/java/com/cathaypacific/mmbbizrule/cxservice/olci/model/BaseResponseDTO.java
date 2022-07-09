package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseResponseDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7950343844574246670L;

	private List<ErrorInfo> errors;

	public List<ErrorInfo> getErrors() {
		return errors;
	}

	public void addErrors(ErrorInfo error) {
		if (error != null) {
			if (errors == null) {
				errors = new ArrayList<>();
			}
			errors.add(error);
		}
	}

	public void addAllErrors(List<ErrorInfo> errorInfoList) {
		if (errorInfoList != null && !errorInfoList.isEmpty()) {
			if (errors == null) {
				errors = new ArrayList<>();
			}
			errors.addAll(errorInfoList);
		}
	}
}
