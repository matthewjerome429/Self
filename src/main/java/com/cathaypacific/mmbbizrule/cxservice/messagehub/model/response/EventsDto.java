package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

import java.util.List;

public class EventsDto {

	private EventDto event;
	
	private List<CollectionDto> collections;

	public EventDto getEvent() {
		return event;
	}

	public void setEvent(EventDto event) {
		this.event = event;
	}

	public List<CollectionDto> getCollections() {
		return collections;
	}

	public void setCollections(List<CollectionDto> collections) {
		this.collections = collections;
	}
	
}
