package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaggageAllowanceConfig {

	@Value("${endpoint.path.baggageAllowance.btu}")
	private String baggageAllowanceByBtuUrl;

	@Value("${endpoint.path.baggageAllowance.od}")
	private String baggageAllowanceByOdUrl;

	public String getBaggageAllowanceByBtuUrl() {
		return baggageAllowanceByBtuUrl;
	}

	public String getBaggageAllowanceByOdUrl() {
		return baggageAllowanceByOdUrl;
	}

}
