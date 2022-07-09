package com.cathaypacific.mmbbizrule.cxservice.rtfs.service;

import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.FlightStatusData;

public interface FlightStatusService {
	
	public List<FlightStatusData> retrieveFlightStatus(String company, String flightNumber, String travelTime);
	
}
