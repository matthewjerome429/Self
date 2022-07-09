package com.cathaypacific.mmbbizrule.dto.response.consent;

import java.io.Serializable;

public class ConsentInfoRecordDTO implements Serializable {
	
	private static final long serialVersionUID = -2757033297151032682L;
	
	private Integer id;
	private boolean consentInfoRecord;
	private String paxId;
	private String segmentId;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isConsentInfoRecord() {
		return consentInfoRecord;
	}

	public void setConsentInfoRecord(boolean consentInfoRecord) {
		this.consentInfoRecord = consentInfoRecord;
	}

	public String getPaxId() {
		return paxId;
	}

	public void setPaxId(String paxId) {
		this.paxId = paxId;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

}
