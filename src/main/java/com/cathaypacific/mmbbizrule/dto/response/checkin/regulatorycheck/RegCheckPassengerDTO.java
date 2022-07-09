package com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class RegCheckPassengerDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 8485051961726128256L;
	
	private String passengerId;
	
	/** Passenger Unique Customer ID, UCI, from CPR */
	private String cprUniqueCustomerId;
	
	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getCprUniqueCustomerId() {
		return cprUniqueCustomerId;
	}

	public void setCprUniqueCustomerId(String cprUniqueCustomerId) {
		this.cprUniqueCustomerId = cprUniqueCustomerId;
	}

}
