package com.cathaypacific.mmbbizrule.model.profile;

public class TravelCompanion {
	private String title;
	
	private String gender;
	// format:"yyyy-MM-dd"
	private String dateOfBirth;
	
	private ProfileTravelDoc travelDocument;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public ProfileTravelDoc getTravelDocument() {
		return travelDocument;
	}

	public void setTravelDocument(ProfileTravelDoc travelDocument) {
		this.travelDocument = travelDocument;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

 

}
