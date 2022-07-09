package com.cathaypacific.mmbbizrule.cxservice.messagehub.service;

import java.io.IOException;
import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response.GetEventResponseDto;

public interface MessageHubService {
	
	/**
	 * get event response from message hub
	 * @param loginInfo
	 * @param requestDTO
	 * @return
	 * @throws IOException 
	 */
	public GetEventResponseDto getEventResponse(String rloc, List<String> eventTypes) throws Exception;
	

}
