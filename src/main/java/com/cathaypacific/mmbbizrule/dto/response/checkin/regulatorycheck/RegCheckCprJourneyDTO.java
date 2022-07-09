package com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class RegCheckCprJourneyDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 1004369919204121958L;

	/** journey Id */
	private String journeyId;

	//private List<RegCheckPassengerDTO> passengers;

	//private List<RegCheckCprSegmentDTO> segments;

	private List<RegCheckPassengerSegmentDTO> passengerSegments;

	private boolean interActiveError;

	public RegCheckCprJourneyDTO() {
		super();
	}

	public RegCheckCprJourneyDTO(List<ErrorInfo> errorInfos) {
		super();
		this.addAllErrors(errorInfos);
	}

	public RegCheckCprJourneyDTO(List<ErrorInfo> errorInfos, String journeyId) {
		super();
		this.addAllErrors(errorInfos);
		this.journeyId = journeyId;
	}

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public void addPassengerSegments(RegCheckPassengerSegmentDTO regCheckPassengerSegmentDTO) {
		if (regCheckPassengerSegmentDTO == null) {
			return;
		}
		if (passengerSegments == null) {
			passengerSegments = new ArrayList<>();
		}
		passengerSegments.add(regCheckPassengerSegmentDTO);
	}

	public List<RegCheckPassengerSegmentDTO> getPassengerSegments() {
		return passengerSegments;
	}

	public void setPassengerSegments(List<RegCheckPassengerSegmentDTO> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}

	public boolean isInterActiveError() {
		return interActiveError;
	}

	public void setInterActiveError(boolean interActiveError) {
		this.interActiveError = interActiveError;
	}

}
