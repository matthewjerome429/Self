package com.cathaypacific.mmbbizrule.model.profile.v2;

import java.util.List;

public class ProfilePreferenceV2 {
	
	private String dateOfBirth;
	
	private TravelerInformation customerTravelerInformation;
	
	private List<CompanionTravelerInformation> companionTravelerInformation;

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public TravelerInformation getCustomerTravelerInformation() {
		return customerTravelerInformation;
	}

	public void setCustomerTravelerInformation(TravelerInformation customerTravelerInformation) {
		this.customerTravelerInformation = customerTravelerInformation;
	}

	public List<CompanionTravelerInformation> getCompanionTravelerInformation() {
		return companionTravelerInformation;
	}

	public void setCompanionTravelerInformation(List<CompanionTravelerInformation> companionTravelerInformation) {
		this.companionTravelerInformation = companionTravelerInformation;
	}
	
}
