package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DpEligibilityConfig {

	@Value("${endpoint.path.dpeligibility.journey}")
	private String journeyUrl;
	
	@Value("${endpoint.path.dpeligibility.atcdwpnr}")
	private String atcDwPnrUrl;
	
	public String getJourneyUrl() {
		return journeyUrl;
	}

	public void setJourneyUrl(String journeyUrl) {
		this.journeyUrl = journeyUrl;
	}

	public String getAtcDwPnrUrl() {
		return atcDwPnrUrl;
	}

	public void setAtcDwPnrUrl(String atcDwPnrUrl) {
		this.atcDwPnrUrl = atcDwPnrUrl;
	}
	
}
