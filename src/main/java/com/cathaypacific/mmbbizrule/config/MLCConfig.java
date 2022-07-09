package com.cathaypacific.mmbbizrule.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mlc")
public class MLCConfig {

	private String appCode;

	private String cxDomain;

	private String authenticateUrl;

	private String authenticateAction;

	private String verifyTokenUrl;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getCxDomain() {
		return cxDomain;
	}

	public void setCxDomain(String cxDomain) {
		this.cxDomain = cxDomain;
	}

	public String getAuthenticateUrl() {
		return authenticateUrl;
	}

	public void setAuthenticateUrl(String authenticateUrl) {
		this.authenticateUrl = authenticateUrl;
	}

	public String getAuthenticateAction() {
		return authenticateAction;
	}

	public void setAuthenticateAction(String authenticateAction) {
		this.authenticateAction = authenticateAction;
	}

	public String getVerifyTokenUrl() {
		return verifyTokenUrl;
	}

	public void setVerifyTokenUrl(String verifyTokenUrl) {
		this.verifyTokenUrl = verifyTokenUrl;
	}

}
