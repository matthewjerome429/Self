package com.cathaypacific.mbcommon.exception;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class OneAErrorsException extends BusinessBaseException {

	/**
		 * 
		 */
	private static final long serialVersionUID = 6939827978906880724L;
	private final List<ErrorInfo> oneAErrors;

	public OneAErrorsException(String message, List<ErrorInfo> errorList) {
		super(message);
		oneAErrors = new ArrayList<>();
		if (errorList != null && !errorList.isEmpty()) {
			oneAErrors.addAll(errorList);
		}

	}

	public List<ErrorInfo> getOneAErrors() {
		return oneAErrors;
	}

	public String getOneAErrorCodes(List<ErrorInfo> errorList) {
		List<String> list = new ArrayList<>();
		if (errorList != null && !errorList.isEmpty()) {
			for (ErrorInfo errorInfo : errorList) {
				list.add(errorInfo.getErrorCode());
			}
		}
		return String.join(",", list);
	}
}
