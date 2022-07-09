package com.cathaypacific.mmbbizrule.cxservice.memberaward.model.request;

import java.util.ArrayList;
import java.util.List;

public class MemberAwardRequest {
	private String applicationName;
	
	private String correlationId;
	
	private String memberNumber;
	
	private List<SectorDetailRecordInRequest> sectorDetailRecord;
	
	private String source;
	
	private UserInformation userInformation;
	
	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber;
	}

	public List<SectorDetailRecordInRequest> getSectorDetailRecord() {
		if(sectorDetailRecord == null){
			sectorDetailRecord = new ArrayList<>();
		}
		return sectorDetailRecord;
	}

	public void setSectorDetailRecord(List<SectorDetailRecordInRequest> sectorDetailRecord) {
		this.sectorDetailRecord = sectorDetailRecord;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public UserInformation getUserInformation() {
		if(userInformation == null){
			userInformation = new UserInformation();
		}
		return userInformation;
	}

	public void setUserInformation(UserInformation userInformation) {
		this.userInformation = userInformation;
	}

}
