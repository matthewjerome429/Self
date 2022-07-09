package com.cathaypacific.mmbbizrule.dto.response.checkin.accept;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class CheckInAcceptCprSegmentDTO extends BaseResponseDTO {
	
	private static final long serialVersionUID = -8691044748838444692L;

	private String segmentId;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}
	
}
