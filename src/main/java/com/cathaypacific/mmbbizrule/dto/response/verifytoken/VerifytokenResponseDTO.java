package com.cathaypacific.mmbbizrule.dto.response.verifytoken;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class VerifytokenResponseDTO extends BaseResponseDTO{
	
	private static final long serialVersionUID = -8141124264073072540L;

	private boolean loginSuccess;
	
	private String mmbToken;
	
	private boolean requireLocalConsentPage = false;

	public boolean isRequireLocalConsentPage() {
		return requireLocalConsentPage;
	}

	public void setRequireLocalConsentPage(boolean requireLocalConsentPage) {
		this.requireLocalConsentPage = requireLocalConsentPage;
	}

	public boolean isLoginSuccess() {
		return loginSuccess;
	}

	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}

	public String getMmbToken() {
		return mmbToken;
	}

	public void setMmbToken(String mmbToken) {
		this.mmbToken = mmbToken;
	}
	
}
