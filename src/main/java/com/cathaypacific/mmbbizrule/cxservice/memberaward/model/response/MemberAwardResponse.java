package com.cathaypacific.mmbbizrule.cxservice.memberaward.model.response;

import java.util.ArrayList;
import java.util.List;

public class MemberAwardResponse {
	private List<MemberAwardError> errors;
	
	private String memberNumber;
	
	private long totalBasicAwardMiles;

	private long totalClubPoints;
	
	private long totalBonusClubPoints;
	
	private long totalTierBonusMiles;
	
	private long totalBonusMiles;
	
	private String lvoIndicator;
	
	private String statusCode;
	
	private List<SectorDetailRecordInResponse> sectorDetailRecord;

	public List<MemberAwardError> getErrors() {
		return errors;
	}

	public void setErrors(List<MemberAwardError> errors) {
		this.errors = errors;
	}

	public String getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber;
	}

	public long getTotalBasicAwardMiles() {
		return totalBasicAwardMiles;
	}

	public void setTotalBasicAwardMiles(long totalBasicAwardMiles) {
		this.totalBasicAwardMiles = totalBasicAwardMiles;
	}

	public long getTotalClubPoints() {
		return totalClubPoints;
	}

	public void setTotalClubPoints(long totalClubPoints) {
		this.totalClubPoints = totalClubPoints;
	}

	public long getTotalBonusClubPoints() {
		return totalBonusClubPoints;
	}

	public void setTotalBonusClubPoints(long totalBonusClubPoints) {
		this.totalBonusClubPoints = totalBonusClubPoints;
	}

	public long getTotalTierBonusMiles() {
		return totalTierBonusMiles;
	}

	public void setTotalTierBonusMiles(long totalTierBonusMiles) {
		this.totalTierBonusMiles = totalTierBonusMiles;
	}

	public long getTotalBonusMiles() {
		return totalBonusMiles;
	}

	public void setTotalBonusMiles(long totalBonusMiles) {
		this.totalBonusMiles = totalBonusMiles;
	}

	public String getLvoIndicator() {
		return lvoIndicator;
	}

	public void setLvoIndicator(String lvoIndicator) {
		this.lvoIndicator = lvoIndicator;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public List<SectorDetailRecordInResponse> getSectorDetailRecord() {
		if(sectorDetailRecord == null){
			sectorDetailRecord = new ArrayList<>();
		}
		return sectorDetailRecord;
	}

	public void setSectorDetailRecord(List<SectorDetailRecordInResponse> sectorDetailRecord) {
		this.sectorDetailRecord = sectorDetailRecord;
	}

}
