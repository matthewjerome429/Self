package com.cathaypacific.mmbbizrule.v2.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cathaypacific.mbcommon.model.common.AdcMessage;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.olciconsumer.model.response.ErrorInfo;

@Component
public class CacheHelper {

	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@SuppressWarnings("unchecked")
	public List<AdcMessage> getAdcMessageFromCache(String rloc) {
		return mbTokenCacheRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.ADC_MESSAGE, rloc, ArrayList.class);
	}

	@SuppressWarnings("unchecked")
	public Map<String, List<ErrorInfo>> getInteractiveErrorFromCache(String rloc) {
		return mbTokenCacheRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.INTER_ACTIVE_ERROR, rloc, HashMap.class);
	}
}
