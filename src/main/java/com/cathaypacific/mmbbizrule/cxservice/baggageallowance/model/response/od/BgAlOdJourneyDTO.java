package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od;

import java.io.Serializable;
import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.common.od.BgAlOdSegmentDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.BaggageAllowanceDTO;

public class BgAlOdJourneyDTO implements Serializable {

	private static final long serialVersionUID = 8764665002427980227L;

	private String journeyId;

	private String baggageAllowanceType;

	private List<BgAlOdSegmentDTO> segments;

	private List<BaggageAllowanceDTO> baggageAllowance;

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public String getBaggageAllowanceType() {
		return baggageAllowanceType;
	}

	public void setBaggageAllowanceType(String baggageAllowanceType) {
		this.baggageAllowanceType = baggageAllowanceType;
	}

	public List<BgAlOdSegmentDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<BgAlOdSegmentDTO> segments) {
		this.segments = segments;
	}

	public List<BaggageAllowanceDTO> getBaggageAllowance() {
		return baggageAllowance;
	}

	public void setBaggageAllowance(List<BaggageAllowanceDTO> baggageAllowance) {
		this.baggageAllowance = baggageAllowance;
	}

}
