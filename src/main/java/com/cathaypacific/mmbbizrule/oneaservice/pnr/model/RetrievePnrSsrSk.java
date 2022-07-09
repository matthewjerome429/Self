package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;

public class RetrievePnrSsrSk {
	
	/** SSR/SK type e.g.STFD */
	private String type;
	/** subclass of the SSR/SK */
	private String subclass;
	/** the passenger id */
	private String passengerId;
	/** the segment id */
	private String segmentId;
	/** the segmentName*/
	private String segmentName;
	/** the free text*/
	private String freeText;
	/** the qulifier id*/
	private BigInteger qulifierId;
	
	private String companyId;
	
	private String status;
	
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
	public String getSubclass() {
		return subclass;
	}
	public void setSubclass(String subclass) {
		this.subclass = subclass;
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
	public String getFreeText() {
		return freeText;
	}
	public void setFreeText(String freeText) {
		this.freeText = freeText;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}