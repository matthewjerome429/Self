package com.cathaypacific.mmbbizrule.dto.request.unlock;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class UnlockPaxInfoDTO {
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@Valid
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<PaxInfoDTO> passengerInfos;

	public UnlockPaxInfoDTO() {
		super();
	}

	public UnlockPaxInfoDTO(String rloc, List<PaxInfoDTO> paxInfoDTOs) {
		super();
		this.rloc = rloc;
		this.passengerInfos = paxInfoDTOs;
	}

	public UnlockPaxInfoDTO(String rloc, PaxInfoDTO paxInfoDTO) {
		super();
		this.rloc = rloc;
		List<PaxInfoDTO> list = new ArrayList<>();
		list.add(paxInfoDTO);
		this.passengerInfos = list;
	}

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<PaxInfoDTO> getPassengerInfos() {
		return passengerInfos;
	}

	public void setPassengerInfos(List<PaxInfoDTO> passengerInfos) {
		this.passengerInfos = passengerInfos;
	}
	
}
