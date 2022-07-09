package com.cathaypacific.mmbbizrule.dto.response.journey;

import java.util.ArrayList;
import java.util.List;

public class JourneySummaryDTO {

	private String journeyId;
	
	private List<JourneySegmentDTO> segments;

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public List<JourneySegmentDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<JourneySegmentDTO> segments) {
		this.segments = segments;
	}
	
	public void addSegment(JourneySegmentDTO segment) {
		if(segment==null){
			return;
		}
		if(this.segments==null){
			this.segments = new ArrayList<>();
		}
		this.segments.add(segment);
	}
	
}
