package com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.journey;

import java.util.List;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.DpEligibilityErrorInfo;

public class DpEligibilityJourneyResponse {
	private DpEligibilityErrorInfo error;
	private List<DpEligibilityJourney> journeys;
	private boolean success;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public DpEligibilityErrorInfo getError() {
		return error;
	}
	public void setError(DpEligibilityErrorInfo error) {
		this.error = error;
	}
	public List<DpEligibilityJourney> getJourneys() {
		return journeys;
	}
	public void setJourneys(List<DpEligibilityJourney> journeys) {
		this.journeys = journeys;
	}
}
