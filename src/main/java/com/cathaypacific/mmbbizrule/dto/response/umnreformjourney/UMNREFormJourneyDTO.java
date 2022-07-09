package com.cathaypacific.mmbbizrule.dto.response.umnreformjourney;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class UMNREFormJourneyDTO {

	private String journeyId;
	
	private List<String> segmentIds;
	
	private UMNREFormGuardianInfoDTO personSeeingOffDeparture;
	
	private UMNREFormGuardianInfoDTO personMeetingArrival;

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public List<String> getSegmentIds() {
		if (segmentIds == null) {
			segmentIds = Lists.newArrayList();
		}
		return segmentIds;
	}

	public void setSegmentIds(List<String> segmentIds) {
		this.segmentIds = segmentIds;
	}

	public UMNREFormGuardianInfoDTO getPersonSeeingOffDeparture() {
		return personSeeingOffDeparture;
	}

	public void setPersonSeeingOffDeparture(UMNREFormGuardianInfoDTO personSeeingOffDeparture) {
		this.personSeeingOffDeparture = personSeeingOffDeparture;
	}

	public UMNREFormGuardianInfoDTO getPersonMeetingArrival() {
		return personMeetingArrival;
	}

	public void setPersonMeetingArrival(UMNREFormGuardianInfoDTO personMeetingArrival) {
		this.personMeetingArrival = personMeetingArrival;
	}
	
	
}
