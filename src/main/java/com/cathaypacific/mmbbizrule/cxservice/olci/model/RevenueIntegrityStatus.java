package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class RevenueIntegrityStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String statusCode;

	/** The status value of revenue integrity stopover, indicator is STC */
	private String stopoverStatus;

	/** The status value of revenue integrity sequence, indicator is SCS */
	private String sequenceStatus;

	public String getStopoverStatus() {
		return stopoverStatus;
	}

	public void setStopoverStatus(String stopoverStatus) {
		this.stopoverStatus = stopoverStatus;
	}

	public String getSequenceStatus() {
		return sequenceStatus;
	}

	public void setSequenceStatus(String sequenceStatus) {
		this.sequenceStatus = sequenceStatus;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
}
