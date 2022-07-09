package com.cathaypacific.mmbbizrule.cxservice.rtfs.repository;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.cathaypacific.mmbbizrule.util.RTFSHttpClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.config.FlightStatusConfig;
import com.cathaypacific.mmbbizrule.constant.FlightStatusConstants;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.FlightStatusData;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.FlightStatusRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.FlightStatusResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Provide methods of retrieving data from flight status.
 */
@Repository
public class FlightStatusRepository {
	
	private static LogAgent logger = LogAgent.getLogAgent(FlightStatusRepository.class);
	
	private static final Gson REQUEST_GSON;

	private static final Gson RESPONSE_GSON;

	@Autowired
	private FlightStatusConfig flightStatusConfig;

	@Autowired
	private RTFSHttpClientService rtfsHttpClientService;

	static {
		REQUEST_GSON = new GsonBuilder().registerTypeAdapter(Date.class, new RequestDateSerializer()).create();
		RESPONSE_GSON = new GsonBuilder().registerTypeAdapter(Date.class, new ResponseDateDeserializer()).create();
	}


	/**
	 * Retrieve flight status by flight number.
	 * 
	 * @param company
	 *            company code.
	 * @param flightNumber
	 *            flight number.
	 * @param travelTime
	 *            travel time.
	 * @return response of flight status JSON service.
	 */
	@LogPerformance(message = "Time required to get flight status")
	@Cacheable(cacheNames=CacheNamesConstants.FLIGHTSTATUS, keyGenerator = "shareKeyGenerator")
	public List<FlightStatusData> findByFlightNumber(String company, String flightNumber, String travelTime) {
		FlightStatusRequestDTO flightStatusRequest = new FlightStatusRequestDTO();
		flightStatusRequest.setCarrierCode(company);
		flightStatusRequest.setFlightNumber(flightNumber);

		Date travelDate = null;
		try {
			travelDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, travelTime);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Flight time doesn't match format.", e);
		}
		flightStatusRequest.setTravelDate(travelDate);
		flightStatusRequest.setDepartureArrival(FlightStatusConstants.DEPARTURE);

		String json = null;
		try {
			json = rtfsHttpClientService.postJson(flightStatusConfig.getFlightStatusByFlightNumberUrl(),
					REQUEST_GSON.toJson(flightStatusRequest));
		} catch (Exception e) {
			logger.error("Get flight status failed.", e);
			return null;
		}
		FlightStatusResponseDTO flightStatusResponse = null;
		try {
			flightStatusResponse = RESPONSE_GSON.fromJson(json, FlightStatusResponseDTO.class);
		} catch (JsonSyntaxException e) {
			logger.error("Parse flight status response failed.", e);
			return null;
		}
		
		if (!isValid(flightStatusResponse)) {
			logger.error("Flight Status Respons is not valid");
			return null;
		}

		return flightStatusResponse.getResult();
	}

	/**
	 * Determine whether flight status data is valid.
	 * @param flightStatusResponse
	 * response of flight status.
	 * @return
	 * true if valid, otherwise false.
	 */
	private boolean isValid(FlightStatusResponseDTO flightStatusResponse) {
		if (flightStatusResponse == null) {
			logger.error("No available flight status response.");
			return false;
		}
		
		if (flightStatusResponse.getErrors() != null && !flightStatusResponse.getErrors().isEmpty()) {
			for (ErrorInfo errorInfo : flightStatusResponse.getErrors()) {
				logger.error("Error in flight status response", errorInfo.getErrorCode());
			}
			return false;
		}

		if (flightStatusResponse.getResult() == null || flightStatusResponse.getResult().isEmpty()) {
			logger.error("No flight status in response.");
			return false;
		}
		return true;
	}

}
