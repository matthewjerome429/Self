package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

public class RetrievePnrSeatPreference {
	/** seat preference codes */
	private String preferenceCode;
	/** preference status */
	private String status;
	/** speicalPreference **/
	private boolean speicalPreference;
	
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
}
