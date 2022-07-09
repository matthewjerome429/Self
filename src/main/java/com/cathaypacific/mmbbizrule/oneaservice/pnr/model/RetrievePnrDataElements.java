package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RetrievePnrDataElements {
	
	/** the segmentName*/
	private String segmentName;
	
	/** SSR/SK type e.g.STFD */
	private String type;
	
	/** the qulifier id*/
	private BigInteger qulifierId;
	
	private String companyId;
	
	/** the free text*/
	private String freeText;
	
	/** the passenger id */
	private String passengerId;
	
	/** the segment id */
	private String segmentId;
	
	private String status;
	
	private List<RetrievePnrDataElementsOtherData> otherDataList;

	public String getSegmentName() {
		return segmentName;
	}

	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigInteger getQulifierId() {
		return qulifierId;
	}

	public void setQulifierId(BigInteger qulifierId) {
		this.qulifierId = qulifierId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getFreeText() {
		return freeText;
	}

	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<RetrievePnrDataElementsOtherData> getOtherDataList() {
		return otherDataList;
	}
	
	public List<RetrievePnrDataElementsOtherData> findOtherDataList() {
		if(otherDataList == null) {
			otherDataList = new ArrayList<>();
		}
		return otherDataList;
	}

	public void setOtherDataList(List<RetrievePnrDataElementsOtherData> otherDataList) {
		this.otherDataList = otherDataList;
	}
	
}