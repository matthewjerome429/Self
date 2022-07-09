package com.cathaypacific.mmbbizrule.dto.request.passengerdetails;

import com.cathaypacific.mbcommon.annotation.ValueIsInPair;
import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class UpdateAdultSegmentInfoDTO extends UpdateBasicSegmentInfoDTO{
	
	private static final long serialVersionUID = 3662168414430970033L;
	//FFP info
	@ValueIsInPair(message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private UpdateFFPInfoDTO ffpInfo;

	public UpdateFFPInfoDTO getFfpInfo() {
		return ffpInfo;
	}

	public void setFfpInfo(UpdateFFPInfoDTO ffpInfo) {
		this.ffpInfo = ffpInfo;
	}
	
}