package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;
import java.util.List;

public class EventBookingDTO implements Serializable{

	private static final long serialVersionUID = 8723814355324391407L;

	private List<EventDTO> details;

	public List<EventDTO> getDetails() {
		return details;
	}

	public void setDetails(List<EventDTO> details) {
		this.details = details;
	}

}
