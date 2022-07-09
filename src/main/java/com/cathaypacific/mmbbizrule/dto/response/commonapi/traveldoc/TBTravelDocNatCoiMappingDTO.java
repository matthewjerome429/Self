package com.cathaypacific.mmbbizrule.dto.response.commonapi.traveldoc;

import java.sql.Timestamp;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class TBTravelDocNatCoiMappingDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 7749963612929866679L;

	private String appCode;
	private String docType;
	private String nat;
	private String coi;
	private String reminder;
	private String lastUpdateSource;
	private Timestamp lastUpdateTimestamp;
	
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getNat() {
		return nat;
	}
	public void setNat(String nat) {
		this.nat = nat;
	}
	public String getCoi() {
		return coi;
	}
	public void setCoi(String coi) {
		this.coi = coi;
	}
	public String getReminder() {
		return reminder;
	}
	public void setReminder(String reminder) {
		this.reminder = reminder;
	}
	public String getLastUpdateSource() {
		return lastUpdateSource;
	}
	public void setLastUpdateSource(String lastUpdateSource) {
		this.lastUpdateSource = lastUpdateSource;
	}
	public Timestamp getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}
	public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}
	
}
