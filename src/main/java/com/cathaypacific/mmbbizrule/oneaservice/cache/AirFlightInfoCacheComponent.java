package com.cathaypacific.mmbbizrule.oneaservice.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.oneaconsumer.model.request.flireq_07_1_1a.AirFlightInfo;
import com.cathaypacific.oneaconsumer.model.response.flires_07_1_1a.AirFlightInfoReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Component
public class AirFlightInfoCacheComponent extends OneAWSClientDecorator {

	@Override
	@Cacheable(cacheNames = CacheNamesConstants.AIR_FLIGHT_INFO)
	public AirFlightInfoReply findFlightInfo(AirFlightInfo request) throws SoapFaultException {
		AirFlightInfoReply response = oneAWSClient.findFlightInfo(request);

		if (response.getFlightScheduleDetails() == null) {
			return null;
		}
		return response;
	}
	@Override
	public void setOneAWSClient(OneAWSClient oneAWSClient) {
		super.setOneAWSClient(oneAWSClient);
	}

}
