package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberAwardConfig {
	@Value("${memberaward.endpoint}")
	private String memberAwardUrl;
	
	@Value("${memberaward.endpoint.appname}")
	private String appName;
	
	@Value("${member.services.api.key}")
	private String apiKey;

	public String getMemberAwardUrl() {
		return memberAwardUrl;
	}

	public String getAppName() {
		return appName;
	}

	public String getApiKey() {
		return apiKey;
	}
	
}
