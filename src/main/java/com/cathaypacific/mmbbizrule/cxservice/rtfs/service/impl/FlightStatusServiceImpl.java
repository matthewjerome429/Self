package com.cathaypacific.mmbbizrule.cxservice.rtfs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.FlightStatusData;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.repository.FlightStatusRepository;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.service.FlightStatusService;


/**
 * get flight status for RTFS.
 */
@Service
public class FlightStatusServiceImpl implements FlightStatusService {

	@Autowired
	private FlightStatusRepository flightStatusRepository;
	
	@Override
	public List<FlightStatusData> retrieveFlightStatus(String company, String flightNumber, String travelTime) {
		List<FlightStatusData> flightStatusDatas = new ArrayList<>();
		if (!StringUtils.isEmpty(company) && !StringUtils.isEmpty(flightNumber) 
				&& !StringUtils.isEmpty(travelTime)) {
			flightStatusDatas = flightStatusRepository.findByFlightNumber(company,
					flightNumber, travelTime);
		}
		return flightStatusDatas;
	}

}
