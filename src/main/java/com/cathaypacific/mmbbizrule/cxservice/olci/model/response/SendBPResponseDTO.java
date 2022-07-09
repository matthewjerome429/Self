package com.cathaypacific.mmbbizrule.cxservice.olci.model.response;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class SendBPResponseDTO extends BaseResponseDTO{
		
		private static final long serialVersionUID = 4711380746866629970L;
		
		private boolean success ;

		public SendBPResponseDTO() {
			super();
		}
		
		public SendBPResponseDTO(List<ErrorInfo> errorInfos, boolean success) {
			super();
			this.addAllErrors(errorInfos);
			this.success = success;
		}
		
		public SendBPResponseDTO(ErrorInfo errorInfo, boolean success) {
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
