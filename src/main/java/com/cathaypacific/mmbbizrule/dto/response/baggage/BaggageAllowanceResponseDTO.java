package com.cathaypacific.mmbbizrule.dto.response.baggage;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class BaggageAllowanceResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 4117707031221810365L;
	
	private List<BaggageAllowancePassengerSegmentDTO> passengerSegments;

	public List<BaggageAllowancePassengerSegmentDTO> getPassengerSegments() {
		return passengerSegments;
	}

	public void setPassengerSegments(List<BaggageAllowancePassengerSegmentDTO> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}

}
