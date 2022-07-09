package com.cathaypacific.mmbbizrule.cxservice.changeflight.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ChangeFlightEligibleResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ReminderListResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.service.ChangeFlightEligibleService;
import com.cathaypacific.mmbbizrule.util.HttpClientService;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.google.gson.Gson;

@Service
public class ChangeFlightEligibleServiceImpl implements ChangeFlightEligibleService{
	
	private static final Gson GSON = new Gson();
	
	@Value("${endpoint.path.dpeligibility.changeflight}")
	private String changeFlightEligibility;
	
	@Value("${endpoint.path.dpeligibility.reminderList}")
	private String reminderList;
	
	@Autowired
	private HttpClientService httpClientService;
	
	@Override
	public ChangeFlightEligibleResponseDTO changeFlightEligibleByPnr(PNRReply pnrReplay) throws ExpectedException, UnexpectedException {
		
		
		String requestBody = GSON.toJson(pnrReplay);
		try{
		HttpHeaders headers = new HttpHeaders();
		headers.add("AppCode", MMBConstants.APP_CODE);
		// call change flight service retrieve change flight eligibility
		String responseBody =httpClientService.postJson(changeFlightEligibility, requestBody,headers);
		
		return GSON.fromJson(responseBody, ChangeFlightEligibleResponseDTO.class);
		}catch(Exception e) {
			throw new UnexpectedException("Call Change Flight failed", new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM), e);
		}	
	}

	@Override
	public ReminderListResponseDTO reminderListResponseByPnr(PNRReply pnrReplay) throws UnexpectedException {
		String requestBody = GSON.toJson(pnrReplay);
		try{
			// call reminder List service retrieve reminder List
			String responseBody =httpClientService.postJson(reminderList, requestBody);
			return GSON.fromJson(responseBody, ReminderListResponseDTO.class);
		}catch(Exception e) {
			throw new UnexpectedException("Call Reminder List Service Failed", new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM), e);
		}	
	}
}
