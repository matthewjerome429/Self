package com.cathaypacific.mmbbizrule.cxservice.seatmap.service;

import com.cathaypacific.mmbbizrule.dto.request.seatmap.SeatMapRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.RetrieveSeatMapDTO;

public interface SeatMapService {
	
	/**
	 * retrieve seatMap from seatMapService by mmbToken & request
	 * 
	 * @param mmbToken
	 * @param request
	 * @return RetrieveSeatMapDTO
	 */
	public RetrieveSeatMapDTO retrieveSeatMap(String mmbToken, SeatMapRequestDTO request);
	
}
