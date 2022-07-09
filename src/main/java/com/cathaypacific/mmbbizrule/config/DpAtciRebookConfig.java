package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DpAtciRebookConfig {
	
	@Value("${endpoint.path.dpatcirebook.landing}")
	private String landing;

	public String getLanding() {
		return landing;
	}

	public void setLanding(String landing) {
		this.landing = landing;
	}
}
