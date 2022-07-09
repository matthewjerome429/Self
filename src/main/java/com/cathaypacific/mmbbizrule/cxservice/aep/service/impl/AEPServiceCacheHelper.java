package com.cathaypacific.mmbbizrule.cxservice.aep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mmbbizrule.config.AEPConfig;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProductsResponse;
import com.cathaypacific.mmbbizrule.util.HttpClientService;

@Component
public class AEPServiceCacheHelper {

	private static final String PARAM_BOOKING_REF = "bookingRef";

	private static final String PARAM_POS = "pos";

	private static final String PARAM_CHANNEL = "channel";

	private static final String CHANNEL_MMB = "MMB";

	@Autowired
	private AEPConfig aepConfig;

	@Autowired
	private HttpClientService httpClientService;

	@Cacheable(cacheNames = CacheNamesConstants.AEP_BAGGAGE, keyGenerator = "shareKeyGenerator")
	@LogPerformance(message = "Time required to call aep for baggage allowances.")
	public AEPProductsResponse getBookingProducts(String bookingRef, String pos) throws BusinessBaseException {

		String url = UriComponentsBuilder.fromHttpUrl(aepConfig.getProductsUrl())
				.queryParam(PARAM_BOOKING_REF, bookingRef).queryParam(PARAM_POS, pos)
				.queryParam(PARAM_CHANNEL, CHANNEL_MMB).build().toString();

		AEPProductsResponse productsResponse;

		try {
			productsResponse = httpClientService.get(url, AEPProductsResponse.class);
		} catch (Exception e) {
			throw new ExpectedException("Cannot connect aes service." + e.getMessage(),
					new ErrorInfo(ErrorCodeEnum.ERR_AEP_CONNECTION), e);
		}

		return productsResponse;
	}
}
