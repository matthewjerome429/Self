package com.cathaypacific.mmbbizrule.model.journey;

import java.util.ArrayList;
import java.util.List;

public class JourneySummary {

	private String journeyId;
	
	private List<JourneySegment> segments;

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public List<JourneySegment> getSegments() {
		return segments;
	}

	public void setSegments(List<JourneySegment> segments) {
		this.segments = segments;
	}
	
	public void addSegment(JourneySegment segment) {
		if(segment==null){
			return;
		}
		if(this.segments==null){
			this.segments = new ArrayList<>();
		}
		this.segments.add(segment);
	}
	
}
