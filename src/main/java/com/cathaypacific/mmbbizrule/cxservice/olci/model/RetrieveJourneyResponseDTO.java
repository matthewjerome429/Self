package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RetrieveJourneyResponseDTO extends BaseResponseDTO implements Serializable {

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = -3268959444143634290L;

	/** Rloc */
	private String rloc;

	/** passengers */
	private List<Journey> journeys;

	public String getOaRloc() {
		return rloc;
	}

	public void setOaRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<Journey> getJourneys() {
		return journeys;
	}

	public void setJourneys(List<Journey> journeys) {
		this.journeys = journeys;
	}

	public void addJoruney(Journey joruney) {
		if (journeys == null) {
			journeys = new ArrayList<>();
		}
		journeys.add(joruney);
	}
}
