package com.cathaypacific.mmbbizrule.dto.common.booking;

import io.swagger.annotations.ApiModelProperty;

public class SeatPreferenceDTO {
	/** seat preference code */
	private String preferenceCode;
	/** preference status */
	private String status;
	/** if it is a temporary seat preference stored in cache; this is for the OLCI checking in case */
	@ApiModelProperty("if it is a temporary seat preference stored in cache; this is for the OLCI checking in case")
	private boolean tempPreference;
	
	public String getPreferenceCode() {
		return preferenceCode;
	}
	public void setPreferenceCode(String preferenceCode) {
		this.preferenceCode = preferenceCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isTempPreference() {
		return tempPreference;
	}
	public void setTempPreference(boolean tempPreference) {
		this.tempPreference = tempPreference;
	}
	
}
