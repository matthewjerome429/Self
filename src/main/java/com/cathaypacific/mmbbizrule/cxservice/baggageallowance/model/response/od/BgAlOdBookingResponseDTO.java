package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od;

import java.io.Serializable;
import java.util.List;

public class BgAlOdBookingResponseDTO implements Serializable {

	private static final long serialVersionUID = 8636639009454180261L;

	private String bookingId;

	private List<BgAlOdJourneyDTO> journeys;

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public List<BgAlOdJourneyDTO> getJourneys() {
		return journeys;
	}

	public void setJourneys(List<BgAlOdJourneyDTO> journeys) {
		this.journeys = journeys;
	}

}
