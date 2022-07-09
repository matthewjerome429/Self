package com.cathaypacific.mmbbizrule.cxservice.novatti.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;

public interface NovattiService {
	
	public String retrieveEntitlementStatus(String entitlementId) throws BusinessBaseException;

	public Future<Map<String, String>> asyncGetUpgradeProgressStatus(Set<String> entitlementIds);

}
