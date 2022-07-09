package com.cathaypacific.mmbbizrule.dto.response.rebooking;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class ProtectFlightInfoResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = -5573472036317445804L;
	
	private List<ProtectInfoDTO> protectFlightInfos;

	public List<ProtectInfoDTO> getProtectFlightInfos() {
		return protectFlightInfos;
	}

	public void setProtectFlightInfos(List<ProtectInfoDTO> protectFlightInfos) {
		this.protectFlightInfos = protectFlightInfos;
	}
	
}
