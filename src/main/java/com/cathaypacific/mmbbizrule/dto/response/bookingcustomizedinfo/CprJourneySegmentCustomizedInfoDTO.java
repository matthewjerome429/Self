package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class CprJourneySegmentCustomizedInfoDTO extends BaseResponseDTO{

	private static final long serialVersionUID = 1471106288877436875L;

	private String segmentId;
	
	private boolean  canCheckIn;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public boolean isCanCheckIn() {
		return canCheckIn;
	}

	public void setCanCheckIn(boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
	}
}
