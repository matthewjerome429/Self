package com.cathaypacific.mmbbizrule.dto.response.retrievepnr;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class RetrieveBookingBaseResponseDTO extends BaseResponseDTO{

	private static final long serialVersionUID = 4831969756810559008L;

	private Boolean retrieveOneASuccess = true;
	
	private Boolean retrieveEODSSuccess = true;
	
	private Boolean retrieveOJSuccess = true;
	
	public Boolean getRetrieveOneASuccess() {
		return retrieveOneASuccess;
	}

	public void setRetrieveOneASuccess(Boolean retrieveOneASuccess) {
		this.retrieveOneASuccess = retrieveOneASuccess;
	}

	public Boolean getRetrieveEODSSuccess() {
		return retrieveEODSSuccess;
	}

	public void setRetrieveEODSSuccess(Boolean retrieveEODSSuccess) {
		this.retrieveEODSSuccess = retrieveEODSSuccess;
	}

	public Boolean getRetrieveOJSuccess() {
		return retrieveOJSuccess;
	}

	public void setRetrieveOJSuccess(Boolean retrieveOJSuccess) {
		this.retrieveOJSuccess = retrieveOJSuccess;
	}
	
}
