package com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.model;

import java.util.List;

public class SsrUpdateModel {
	
	private String segmentId;
	
	private String passengerId;
	
	private List<SsrInfoModel> updateSsrList;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public List<SsrInfoModel> getUpdateSsrList() {
		return updateSsrList;
	}

	public void setUpdateSsrList(List<SsrInfoModel> updateSsrList) {
		this.updateSsrList = updateSsrList;
	} 
}
