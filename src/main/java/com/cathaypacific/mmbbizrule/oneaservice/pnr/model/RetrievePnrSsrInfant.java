package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

public class RetrievePnrSsrInfant extends DataElement {
	
	private String familyName;
	
	private String givenName;
	
	private String birthDateYear;
	
	private String birthDateMonth;
	
	private String birthDateDay;
	
	/** INS with seat or INF without seat*/
	private boolean occupySeat;
	
	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getBirthDateYear() {
		return birthDateYear;
	}

	public void setBirthDateYear(String birthDateYear) {
		this.birthDateYear = birthDateYear;
	}

	public String getBirthDateMonth() {
		return birthDateMonth;
	}

	public void setBirthDateMonth(String birthDateMonth) {
		this.birthDateMonth = birthDateMonth;
	}

	public String getBirthDateDay() {
		return birthDateDay;
	}

	public void setBirthDateDay(String birthDateDay) {
		this.birthDateDay = birthDateDay;
	}

	public boolean isOccupySeat() {
		return occupySeat;
	}

	public void setOccupySeat(boolean occupySeat) {
		this.occupySeat = occupySeat;
	}

}
