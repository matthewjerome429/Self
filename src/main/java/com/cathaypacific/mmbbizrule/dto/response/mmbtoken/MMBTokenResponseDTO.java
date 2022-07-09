package com.cathaypacific.mmbbizrule.dto.response.mmbtoken;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class MMBTokenResponseDTO extends BaseResponseDTO{

	private static final long serialVersionUID = 6690643704676145930L;
	
	private String mmbToken;

	public String getMmbToken() {
		return mmbToken;
	}

	public void setMmbToken(String mmbToken) {
		this.mmbToken = mmbToken;
	}
}
