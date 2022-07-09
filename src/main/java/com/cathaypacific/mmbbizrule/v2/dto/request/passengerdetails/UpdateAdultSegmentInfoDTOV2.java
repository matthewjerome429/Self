package com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails;

import com.cathaypacific.mbcommon.annotation.ValueIsInPair;
import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class UpdateAdultSegmentInfoDTOV2 extends UpdateBasicSegmentInfoDTOV2{
	
	private static final long serialVersionUID = 3662168414430970033L;
	//FFP info
	@ValueIsInPair(message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private UpdateFFPInfoDTOV2 ffpInfo;

	public UpdateFFPInfoDTOV2 getFfpInfo() {
		return ffpInfo;
	}

	public void setFfpInfo(UpdateFFPInfoDTOV2 ffpInfo) {
		this.ffpInfo = ffpInfo;
	}
	
}