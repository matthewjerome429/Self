package com.cathaypacific.mmbbizrule.dto.response.umnreformjourney;

import java.util.List;

public class UMNREFormPassengerDTO {

	private String passengerId;
	
	private boolean primaryPassenger;
	
	private String title;
	
	private String familyName;
	
	private String givenName;
	
	private String age;
	
	private String gender;
	
	private UMNREFormGuardianInfoDTO parentInfo;
	
	private UMNREFormAddressDTO permanentAddress;
	
	private List<UMNREFormJourneyDTO> umnrEFormJourneys;
	
	private boolean parentalLock;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public boolean isPrimaryPassenger() {
		return primaryPassenger;
	}

	public void setPrimaryPassenger(boolean primaryPassenger) {
		this.primaryPassenger = primaryPassenger;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public UMNREFormAddressDTO getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(UMNREFormAddressDTO permanetAddress) {
		this.permanentAddress = permanetAddress;
	}

	public List<UMNREFormJourneyDTO> getUmnrEFormJourneys() {
		return umnrEFormJourneys;
	}

	public void setUmnrEFormJourneys(List<UMNREFormJourneyDTO> umnrEFormJourneys) {
		this.umnrEFormJourneys = umnrEFormJourneys;
	}

	public UMNREFormGuardianInfoDTO getParentInfo() {
		return parentInfo;
	}

	public void setParentInfo(UMNREFormGuardianInfoDTO parentInfo) {
		this.parentInfo = parentInfo;
	}

	public boolean isParentalLock() {
		return parentalLock;
	}

	public void setParentalLock(boolean parentalLock) {
		this.parentalLock = parentalLock;
	}
	
	public boolean isUMEFormRmExist() {
		return parentInfo != null;
	}
	
}
