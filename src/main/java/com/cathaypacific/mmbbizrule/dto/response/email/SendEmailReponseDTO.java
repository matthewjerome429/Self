package com.cathaypacific.mmbbizrule.dto.response.email;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class SendEmailReponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = -6447389576986005134L;
	
	private String rloc;
	
	private List<SendEmailInfoDTO> passengerInfos;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<SendEmailInfoDTO> getPassengerInfos() {
		return passengerInfos;
	}

	public void setPassengerInfos(List<SendEmailInfoDTO> passengerInfos) {
		this.passengerInfos = passengerInfos;
	}

}
