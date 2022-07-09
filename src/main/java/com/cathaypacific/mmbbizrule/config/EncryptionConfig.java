package com.cathaypacific.mmbbizrule.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig {
	@Value("${alternative.encryption.mode}")
	private String encryptionMode;
	
	@Value("${alternative.encryption.appcode}")
	private String appCode;

	public String getEncryptionMode() {
		return encryptionMode;
	}

	public List<String> getAppCode() {
		return appCode != null ? Arrays.asList(appCode.split(",")) : new ArrayList<>();
	}
	
	
}