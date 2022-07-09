package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AEPConfig {

	@Value("${endpoint.path.aep.products}")
	private String productsUrl;

	public String getProductsUrl() {
		return productsUrl;
	}

}
