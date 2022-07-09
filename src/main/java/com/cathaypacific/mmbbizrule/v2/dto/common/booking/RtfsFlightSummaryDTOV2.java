package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;
import java.util.List;

public class RtfsFlightSummaryDTOV2 implements Serializable {

	private static final long serialVersionUID = -1851203491511936681L;
	
	private List<RtfsLegSummaryDTOV2> legs;

	public List<RtfsLegSummaryDTOV2> getLegs() {
		return legs;
	}

	public void setLegs(List<RtfsLegSummaryDTOV2> legs) {
		this.legs = legs;
	}

}
