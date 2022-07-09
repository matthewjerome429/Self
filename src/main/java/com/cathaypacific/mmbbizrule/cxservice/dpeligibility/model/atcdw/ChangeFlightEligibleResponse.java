package com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.atcdw;

import java.util.List;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.DpEligibilityErrorInfo;

public class ChangeFlightEligibleResponse {
	
	private boolean atcBooking;
	
	private boolean canChangeFlight ;
	
	private String dwCode ;
	
	private List<String> dwCodeList;
	
	private DpEligibilityErrorInfo error;
	
	private List<FlightSegment> flownSegments ;
	
	private String retrieveOfficeId ;
	
	private String timeStamp ;
	
	private WarningInfo warningInfo;
	
	public boolean isAtcBooking() {
		return atcBooking;
	}
	public void setAtcBooking(boolean atcBooking) {
		this.atcBooking = atcBooking;
	}
	public boolean isCanChangeFlight() {
		return canChangeFlight;
	}
	public void setCanChangeFlight(boolean canChangeFlight) {
		this.canChangeFlight = canChangeFlight;
	}
	public String getDwCode() {
		return dwCode;
	}
	public void setDwCode(String dwCode) {
		this.dwCode = dwCode;
	}
	public List<String> getDwCodeList() {
		return dwCodeList;
	}
	public void setDwCodeList(List<String> dwCodeList) {
		this.dwCodeList = dwCodeList;
	}
	public DpEligibilityErrorInfo getError() {
		return error;
	}
	public void setError(DpEligibilityErrorInfo error) {
		this.error = error;
	}
	 
	public List<FlightSegment> getFlownSegments() {
		return flownSegments;
	}
	public void setFlownSegments(List<FlightSegment> flownSegments) {
		this.flownSegments = flownSegments;
	}
	public String getRetrieveOfficeId() {
		return retrieveOfficeId;
	}
	public void setRetrieveOfficeId(String retrieveOfficeId) {
		this.retrieveOfficeId = retrieveOfficeId;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public WarningInfo getWarningInfo() {
		return warningInfo;
	}
	public void setWarningInfo(WarningInfo warningInfo) {
		this.warningInfo = warningInfo;
	}
	
	
}
