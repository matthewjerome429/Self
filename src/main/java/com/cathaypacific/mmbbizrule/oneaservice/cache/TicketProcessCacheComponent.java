package com.cathaypacific.mmbbizrule.oneaservice.cache;

import org.springframework.stereotype.Component;

import com.cathaypacific.mbcommon.aop.tokenlevelcache.TokenLevelCacheable;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.TicketProcessEDoc;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TicketProcessEDocReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Component
public class TicketProcessCacheComponent extends OneAWSClientDecorator {

	@Override
	@TokenLevelCacheable(name=TokenCacheKeyEnum.TICKET_PROCESS)
	public TicketProcessEDocReply ticketprocess(TicketProcessEDoc request, Session session) throws SoapFaultException {
		TicketProcessEDocReply response = oneAWSClient.ticketprocess(request, session);
		if (response.getDocGroup() == null) {
			return null;
		}
		return response;
	}
	@Override
	public void setOneAWSClient(OneAWSClient oneAWSClient) {
		super.setOneAWSClient(oneAWSClient);
	}
}
