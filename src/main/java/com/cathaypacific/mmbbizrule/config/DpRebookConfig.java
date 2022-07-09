package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DpRebookConfig {

	@Value("${endpoint.path.dprebook.encrypt}")
	private String encrypt;

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

	
}
