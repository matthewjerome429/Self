package com.cathaypacific.mmbbizrule.cxservice.oj.model.build;

import java.util.List;

public class OJHotelRoomOption {
	
	private String selectedBedType;
	
	private String code;
	
	private String name;
	
	private String description;
	
	private String smokingPreference;
	
	private List<OJRoomGuestDetail> guestDetails;

	public String getSelectedBedType() {
		return selectedBedType;
	}

	public void setSelectedBedType(String selectedBedType) {
		this.selectedBedType = selectedBedType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSmokingPreference() {
		return smokingPreference;
	}

	public void setSmokingPreference(String smokingPreference) {
		this.smokingPreference = smokingPreference;
	}

	public List<OJRoomGuestDetail> getGuestDetails() {
		return guestDetails;
	}

	public void setGuestDetails(List<OJRoomGuestDetail> guestDetails) {
		this.guestDetails = guestDetails;
	}

}
