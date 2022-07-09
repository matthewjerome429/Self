package com.cathaypacific.mmbbizrule.dto.response.journey;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mmbbizrule.model.journey.JourneySummary;

public class JoruneyResponseDTO extends BaseResponseDTO {

	private String oneARloc;

	/**
	 * Disruption (DP) Eligibility journeys are retrieved from DPEligibility endpoint
	 * It is a complete journey based on journey concept
	 */
	private List<JourneySummaryDTO> journeys;

	public String getOneARloc() {
		return oneARloc;
	}

	public void setOneARloc(String oneARloc) {
		this.oneARloc = oneARloc;
	}

	public List<JourneySummaryDTO> getJourneys() {
		return journeys;
	}

	public void setJourneys(List<JourneySummaryDTO> journeys) {
		this.journeys = journeys;
	}

	public void addJourney(JourneySummaryDTO journey) {
		if (journey == null) {
			return;
		}
		if (this.journeys == null) {
			this.journeys = new ArrayList<>();
		}
		journeys.add(journey);
	}

}
