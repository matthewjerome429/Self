package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class SSRSKMappingRule implements Serializable {

	private static final long serialVersionUID = 5936159575552158343L;

	private String checkInValue;

	private String checkInMsgCode;

	private String bpValue;

	private String bpMsgCode;

	private String seatValue;

	public String getCheckInValue() {
		return checkInValue;
	}

	public void setCheckInValue(String checkInValue) {
		this.checkInValue = checkInValue;
	}

	public String getCheckInMsgCode() {
		return checkInMsgCode;
	}

	public void setCheckInMsgCode(String checkInMsgCode) {
		this.checkInMsgCode = checkInMsgCode;
	}

	public String getBpValue() {
		return bpValue;
	}

	public void setBpValue(String bpValue) {
		this.bpValue = bpValue;
	}

	public String getBpMsgCode() {
		return bpMsgCode;
	}

	public void setBpMsgCode(String bpMsgCode) {
		this.bpMsgCode = bpMsgCode;
	}

	public String getSeatValue() {
		return seatValue;
	}

	public void setSeatValue(String seatValue) {
		this.seatValue = seatValue;
	}
}
