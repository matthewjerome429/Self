package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class CprJourneySegmentDTOV2 extends BaseResponseDTO {
	
	private static final long serialVersionUID = 8560472744636224679L;

	private String segmentId;
	
	private Boolean canCheckIn;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public Boolean getCanCheckIn() {
		return canCheckIn;
	}

	public void setCanCheckIn(Boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
	}
	
}
