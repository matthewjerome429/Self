package com.cathaypacific.mmbbizrule.cxservice.olci.model.response;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class SendSMSResponseDTO extends BaseResponseDTO{
		
		private static final long serialVersionUID = -4783586759519801472L;
		private boolean success ;

		public SendSMSResponseDTO() {
			super();
		}
		
		public SendSMSResponseDTO(List<ErrorInfo> errorInfos, boolean success) {
			super();
			this.addAllErrors(errorInfos);
			this.success = success;
		}
		
		public SendSMSResponseDTO(ErrorInfo errorInfo, boolean success) {
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
