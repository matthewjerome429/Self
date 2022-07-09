package com.cathaypacific.mmbbizrule.model.umnreform;

public class UMNREFormPortInfoRemark {
	
	private String airportCode;
	
	private UMNREFormGuardianInfoRemark guardianInfo;

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public UMNREFormGuardianInfoRemark getGuardianInfo() {
		if (guardianInfo == null) {
			guardianInfo = new UMNREFormGuardianInfoRemark();
		}
		return guardianInfo;
	}

	public void setGuardianInfo(UMNREFormGuardianInfoRemark guardianInfo) {
		this.guardianInfo = guardianInfo;
	}
	
	
}
