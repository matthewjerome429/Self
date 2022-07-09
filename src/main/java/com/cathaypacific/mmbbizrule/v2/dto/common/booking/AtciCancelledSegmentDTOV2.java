package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

import com.cathaypacific.mmbbizrule.model.booking.detail.AdditionalOperatorInfo;

public class AtciCancelledSegmentDTOV2 implements Serializable {
	
	private static final long serialVersionUID = -5465694316294994356L;
	// since the segment has been removed from PNR and is builded by RM item, this is a mocked id, e.g. "1M"
	private String segmentId;
	// format: "YYYY-MM-DD"
	private String departureDate;
	
	private String operateCompany;
	
	private String operateSegmentNumber;
	
	private String originPort;
	
	private String destPort;
	
	private RebookInfoDTOV2 rebookInfo;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getOperateCompany() {
		return operateCompany;
	}

	public void setOperateCompany(String operateCompany) {
		this.operateCompany = operateCompany;
	}

	public String getOperateSegmentNumber() {
		return operateSegmentNumber;
	}

	public void setOperateSegmentNumber(String operateSegmentNumber) {
		this.operateSegmentNumber = operateSegmentNumber;
	}

	public String getOriginPort() {
		return originPort;
	}

	public void setOriginPort(String originPort) {
		this.originPort = originPort;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public RebookInfoDTOV2 getRebookInfo() {
		return rebookInfo;
	}

	public void setRebookInfo(RebookInfoDTOV2 rebookInfo) {
		this.rebookInfo = rebookInfo;
	}

}
