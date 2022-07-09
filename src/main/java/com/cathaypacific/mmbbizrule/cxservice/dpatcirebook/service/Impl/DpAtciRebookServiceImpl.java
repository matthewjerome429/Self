package com.cathaypacific.mmbbizrule.cxservice.dpatcirebook.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.config.DpAtciRebookConfig;
import com.cathaypacific.mmbbizrule.cxservice.dpatcirebook.model.request.RescheduleEligibleRequest;
import com.cathaypacific.mmbbizrule.cxservice.dpatcirebook.model.response.RescheduleEligibleResponse;
import com.cathaypacific.mmbbizrule.cxservice.dpatcirebook.service.DpAtciRebookService;
import com.google.gson.Gson;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to dpAtciRebook
 * @author fangfang.zhang
 * @date Feb 19, 2019 5:42:46 PM
 * @version V1.0
 */
@Service
public class DpAtciRebookServiceImpl implements DpAtciRebookService{
	
	private static LogAgent logger = LogAgent.getLogAgent(DpAtciRebookServiceImpl.class);
	
	private static final Gson GSON = new Gson();
	
	@Autowired
	DpAtciRebookConfig dpAtciRebookConfig;
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * get booking Reschedule availability
	 * @param requestDTO
	 * @return
	 */
	public RescheduleEligibleResponse getBookingReschedule(RescheduleEligibleRequest requestDTO) {
		try {
			HttpHeaders requestHeaders = new HttpHeaders();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
	        requestHeaders.setContentType(type);
	        HttpEntity<String> requestEntity = new HttpEntity<>(GSON.toJson(requestDTO), requestHeaders);
	        
			return restTemplate.postForObject(dpAtciRebookConfig.getLanding(), requestEntity, RescheduleEligibleResponse.class);
		} catch (Exception e) {
			logger.error(String.format("Get reschedule deeplink by rloc [%s] failed",requestDTO.getRloc()), e);
		}
		return null;
	}
	

}
