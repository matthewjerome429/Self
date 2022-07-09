package com.cathaypacific.mmbbizrule.model.booking.detail;

public class SeatPreference {
	/** seat preference */
	private String preferenceCode;
	/** preference status */
	private String status;
	/** speicalPreference **/
	private boolean speicalPreference;
	/** if it is a temporary seat preference stored in cache; this is for the OLCI checking in case */
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
	public boolean isSpeicalPreference() {
		return speicalPreference;
	}
	public void setSpeicalPreference(boolean speicalPreference) {
		this.speicalPreference = speicalPreference;
	}
	public boolean isTempPreference() {
		return tempPreference;
	}
	public void setTempPreference(boolean tempPreference) {
		this.tempPreference = tempPreference;
	}
}
