package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class EcommServiceConfig {
	
	@Value("${endpoint.path.ecommService.cacheProducts}")
	private String ecommCacheProductsUrl;
	
	@Value("${endpoint.path.ecommService.eligibleProducts}")
	private String ecommEligibleProductsUrl;
	
	@Value("${endpoint.path.ecommService.baggageProducts}")
	private String ecommBaggageProductsUrl;
	
	@Value("${endpoint.path.ecommService.seatProducts}")
	private String ecommSeatProductsUrl;

	public String getEcommCacheProductsUrl() {
		return ecommCacheProductsUrl;
	}

	public String getEcommEligibleProductsUrl() {
		return ecommEligibleProductsUrl;
	}

	public String getEcommBaggageProductsUrl() {
		return ecommBaggageProductsUrl;
	}

	public String getEcommSeatProductsUrl() {
		return ecommSeatProductsUrl;
	}
	
}
