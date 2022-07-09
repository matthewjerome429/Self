package com.cathaypacific.mmbbizrule.model.profile;

import java.util.List;

public class ProfilePreference {
	
	/** Travel documents in personal information of member profile */
	private List<ProfileTravelDoc> personalTravelDocuments;

	/** Travel documents in travel companion of member profile */
	private List<TravelCompanion> travelCompanions;
	
	/** The original member id of the member, in case member upgrade to AM/MPO from RU,</br>
	 *  The member will has a new member id and this field will record the original member id*/
	private String originalMemberId;
	
	public List<ProfileTravelDoc> getPersonalTravelDocuments() {
		return personalTravelDocuments;
	}

	public void setPersonalTravelDocuments(List<ProfileTravelDoc> personalTravelDocuments) {
		this.personalTravelDocuments = personalTravelDocuments;
	}

	public List<TravelCompanion> getTravelCompanions() {
		return travelCompanions;
	}

	public void setTravelCompanions(List<TravelCompanion> travelCompanions) {
		this.travelCompanions = travelCompanions;
	}

	public String getOriginalMemberId() {
		return originalMemberId;
	}

	public void setOriginalMemberId(String originalMemberId) {
		this.originalMemberId = originalMemberId;
	}
	
}
