package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class AsrEnableCheckKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;
	
	@Id
	@NotNull
	@Column(name = "office_id")
	private String officeId;
	
	@Id
	@NotNull
	@Column(name = "tpos")
	private String tpos;
	
	@NotNull
	@Column(name = "seat_selection")
	private boolean seatSelection;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getTpos() {
		return tpos;
	}

	public void setTpos(String tpos) {
		this.tpos = tpos;
	}

	public boolean isSeatSelection() {
		return seatSelection;
	}

	public void setSeatSelection(boolean seatSelection) {
		this.seatSelection = seatSelection;
	}

}
