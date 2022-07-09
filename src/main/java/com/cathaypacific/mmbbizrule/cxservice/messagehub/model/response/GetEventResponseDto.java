package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class GetEventResponseDto extends BaseResponseDTO{

	private String jobId;
	
	private List<EventsDto> events;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public List<EventsDto> getEvents() {
		return events;
	}

	public void setEvents(List<EventsDto> events) {
		this.events = events;
	}
	
	public List<EventsDto> findEvents() {
		if(CollectionUtils.isEmpty(events)) {
			this.events = new ArrayList<>();
		}
		return this.events;
	}
}
