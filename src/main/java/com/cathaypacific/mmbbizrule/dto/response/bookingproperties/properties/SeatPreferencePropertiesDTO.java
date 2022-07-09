package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties;

import java.io.Serializable;

public class SeatPreferencePropertiesDTO implements Serializable {

	private static final long serialVersionUID = -5112533469243997556L;

	private String preferenceCode;

	private String status;

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

}
