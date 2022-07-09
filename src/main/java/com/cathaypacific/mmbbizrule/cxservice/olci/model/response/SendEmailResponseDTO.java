package com.cathaypacific.mmbbizrule.cxservice.olci.model.response;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class SendEmailResponseDTO extends BaseResponseDTO{
	
		private static final long serialVersionUID = 4263410112872073513L;
	
		private boolean success ;
		
		public SendEmailResponseDTO() {
			super();
		}
		
		public SendEmailResponseDTO(List<ErrorInfo> errorInfos, boolean success) {
			super();
			this.addAllErrors(errorInfos);
			this.success = success;
		}
		
		public SendEmailResponseDTO(ErrorInfo errorInfo, boolean success) {
			super();
			this.addError(errorInfo);
			this.success = success;
		}
		
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
