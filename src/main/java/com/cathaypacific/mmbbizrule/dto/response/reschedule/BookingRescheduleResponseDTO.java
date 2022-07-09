package com.cathaypacific.mmbbizrule.dto.response.reschedule;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class BookingRescheduleResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 5673534604409707282L;
	
	private Boolean canGetEmail = true;
	
	private Boolean canReschedule = false;

	private String deeplinkUrl;

	
	public Boolean getCanGetEmail() {
		return canGetEmail;
	}

	public void setCanGetEmail(Boolean canGetEmail) {
		this.canGetEmail = canGetEmail;
	}

	public Boolean getCanReschedule() {
		return canReschedule;
	}

	public void setCanReschedule(Boolean canReschedule) {
		this.canReschedule = canReschedule;
	}

	public String getDeeplinkUrl() {
		return deeplinkUrl;
	}

	public void setDeeplinkUrl(String deeplinkUrl) {
		this.deeplinkUrl = deeplinkUrl;
	}

}
