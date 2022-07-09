package com.cathaypacific.mmbbizrule.dto.request.email;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class FIFBelowReponseDTO extends BaseResponseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8579872738068576208L;
	
	/** sent email results */
	private boolean success = false;

	private List<ErrorInfo> blockReason;
	

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<ErrorInfo> getBlockReason() {
		return blockReason;
	}

	public void setBlockReason(List<ErrorInfo> blockReason) {
		this.blockReason = blockReason;
	}
	
	public void addBlocReason(ErrorInfo errorInfo){
		if(blockReason == null){
			blockReason = new ArrayList<>();
		}
		blockReason.add(errorInfo);
	}
	
}
