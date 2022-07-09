package com.cathaypacific.mmbbizrule.cxservice.novatti.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.cxservice.novatti.NovattiEntitlementBuilder;
import com.cathaypacific.mmbbizrule.cxservice.novatti.service.NovattiService;
import com.cathaypacific.novatticonsumer.model.entitlement.SoapEntitlementInfoRequest;
import com.cathaypacific.novatticonsumer.model.entitlement.SoapEntitlementInfoResponse;
import com.cathaypacific.novatticonsumer.webservice.service.client.NovattiWSClient;

@Service
public class NovattiServiceImpl implements NovattiService {
	
	private static final LogAgent LOGGER = LogAgent.getLogAgent(NovattiServiceImpl.class);
	
	@Value("${novatti.int031.lockcuccesscode}")
	private String lockSuccessCode;
	
	@Autowired
	private NovattiWSClient novattiWSClient;

	@Override
	public String retrieveEntitlementStatus(String entitlementId) throws BusinessBaseException {
		NovattiEntitlementBuilder entitlementBuilder = new NovattiEntitlementBuilder();
		SoapEntitlementInfoRequest entitlementInfoRequest = entitlementBuilder.buildEntitlementInfoRequest(entitlementId);
		SoapEntitlementInfoResponse entitlementInfoResponse = novattiWSClient.retrieveEntitlement(entitlementInfoRequest);
		if(!lockSuccessCode.equals(entitlementInfoResponse.getHeader().getResultCode())) {
			throw new UnexpectedException("NovattiService retrieve EntitlementId:" + entitlementId + ", failure! resuleCode:" + entitlementInfoResponse.getHeader().getResultCode(), new ErrorInfo(ErrorCodeEnum.ERR_NOVATTI_RETRIEVE_ENTITLEMENT));
		}
		return entitlementInfoResponse.getEntitlements().getEntitlement().get(0).getStatus().value();
	}

	@Async
	@Override
	public Future<Map<String, String>> asyncGetUpgradeProgressStatus(Set<String> entitlementIds) {
		Map<String, String> map = new HashMap<>();
		for(String entitlementId: entitlementIds){
			try {
				map.put(entitlementId, this.retrieveEntitlementStatus(entitlementId));
			} catch (BusinessBaseException e) {
				LOGGER.error(e.getMessage());
			}
		}
		return new AsyncResult<>(map);
	}

}
