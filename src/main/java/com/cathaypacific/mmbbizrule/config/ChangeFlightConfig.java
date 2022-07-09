package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;

public class ChangeFlightConfig {
	
	@Value("${changflight.eligibility.url}")
	private String checkChangFlight;

	public String getCheckChangFlight() {
		return checkChangFlight;
	}

	public void setCheckChangFlight(String checkChangFlight) {
		this.checkChangFlight = checkChangFlight;
	}

	
}
