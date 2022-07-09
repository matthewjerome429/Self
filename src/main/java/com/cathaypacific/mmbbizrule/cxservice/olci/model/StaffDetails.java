package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class StaffDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1023609389953908799L;

	/**
	 * The company code, e.g. CX
	 */
	private String company;

	/**
	 * The staff number, e.g. 111111H
	 */
	private String staffNumber;

	/**
	 * Staff joining date in yyyy-MM-dd
	 */
	private String joiningDate;

	/**
	 * The staff attribute type, possible values are: NA | SBY | BKB
	 */
	private String staffAttributeType;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getStaffNumber() {
		return staffNumber;
	}

	public void setStaffNumber(String staffNumber) {
		this.staffNumber = staffNumber;
	}

	public String getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getStaffAttributeType() {
		return staffAttributeType;
	}

	public void setStaffAttributeType(String staffAttributeType) {
		this.staffAttributeType = staffAttributeType;
	}
}
