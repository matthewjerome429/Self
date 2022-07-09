package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReferenceDataTimeZoneConfig {
	
	@Value("${endpoint.path.referencedata.timezone}")
	private String referenceDataTimeZoneUrl;

	public String getReferenceDataTimeZoneUrl() {
		return referenceDataTimeZoneUrl;
	}

}
