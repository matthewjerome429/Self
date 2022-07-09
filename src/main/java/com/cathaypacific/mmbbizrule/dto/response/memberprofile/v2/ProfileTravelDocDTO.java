package com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2;

import java.util.List;

public class ProfileTravelDocDTO {

	private TravelerInformationDTO customerTravelerInformation;
	
	private List<CompanionTravelerInformationDTO> companionTravelerInformation;

	public TravelerInformationDTO getCustomerTravelerInformation() {
		return customerTravelerInformation;
	}

	public void setCustomerTravelerInformation(TravelerInformationDTO customerTravelerInformation) {
		this.customerTravelerInformation = customerTravelerInformation;
	}

	public List<CompanionTravelerInformationDTO> getCompanionTravelerInformation() {
		return companionTravelerInformation;
	}

	public void setCompanionTravelerInformation(List<CompanionTravelerInformationDTO> companionTravelerInformation) {
		this.companionTravelerInformation = companionTravelerInformation;
	}
	
}
