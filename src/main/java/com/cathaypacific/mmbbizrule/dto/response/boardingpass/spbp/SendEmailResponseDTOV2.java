package com.cathaypacific.mmbbizrule.dto.response.boardingpass.spbp;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class SendEmailResponseDTOV2 extends BaseResponseDTO {

	private static final long serialVersionUID = 5012990847344160513L;
	
	private boolean success = false;
	
	private List<String> uniqueCustomerIds;
	
	public SendEmailResponseDTOV2() {
		super();
	}
	
	public SendEmailResponseDTOV2(List<ErrorInfo> errorInfos, boolean success) {
		super();
		this.addAllErrors(errorInfos);
		this.success = success;
	}
	
	public SendEmailResponseDTOV2(ErrorInfo errorInfo, boolean success) {
		super();
		this.addError(errorInfo);
		this.success = success;
	}
	
	public SendEmailResponseDTOV2(ErrorInfo errorInfo) {
		super();
		this.addError(errorInfo);
	}
	
	public SendEmailResponseDTOV2(List<ErrorInfo> errorInfos) {
		super();
		this.addAllErrors(errorInfos);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<String> getUniqueCustomerIds() {
		return uniqueCustomerIds;
	}

	public void setUniqueCustomerIds(List<String> uniqueCustomerIds) {
		this.uniqueCustomerIds = uniqueCustomerIds;
	}

	
}
