package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReferenceDataNationalityConfig {
	
	@Value("${endpoint.path.referencedata.nationality}")
	private String referenceDataNationalityUrl;

	public String getReferenceDataNationalityUrl() {
		return referenceDataNationalityUrl;
	}

}
