package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response;

import java.io.Serializable;

public class BaggageAllowanceResponseBaseDTO implements Serializable {

	private static final long serialVersionUID = 2867998550580473376L;

	private ErrorInfoDTO error;

	public ErrorInfoDTO getError() {
		return error;
	}

	public void setError(ErrorInfoDTO error) {
		this.error = error;
	}

}
