package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlightStatusConfig {

	@Value("${endpoint.path.rtfs.flight-number}")
	private String flightStatusByFlightNumberUrl;

	public String getFlightStatusByFlightNumberUrl() {
		return flightStatusByFlightNumberUrl;
	}

	public void setFlightStatusByFlightNumberUrl(String flightStatusByFlightNumberUrl) {
		this.flightStatusByFlightNumberUrl = flightStatusByFlightNumberUrl;
	}

}
