package com.cathaypacific.mmbbizrule.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.business.RetrieveMiniRuleBusiness;
import com.cathaypacific.mmbbizrule.oneaservice.minirule.MiniruleRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.session.service.PnrSessionInvokService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.tmrxrq_17_2_1a.MiniRuleGetFromRec;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.model.response.tmrxrq_17_2_1a.MiniRuleGetFromRecReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Service
public class RetrieveMiniRuleBusinessImpl implements RetrieveMiniRuleBusiness {
	
	@Autowired
	private PnrSessionInvokService pnrSessionInvokService;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;

	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	
	public MiniRuleGetFromRecReply retrieveMiniRuleFromRec(String rloc) throws BusinessBaseException {
		
		Session session = pnrSessionInvokService.openSessionOnly(rloc);
		//PNRReply pnrReply = pnrInvokeService.retrievePnrReplyByOneARloc(rloc, session);
		session.setStatus(SessionStatus.END.getStatus());
		MiniruleRequestBuilder builder = new MiniruleRequestBuilder();
		MiniRuleGetFromRec request = builder.buildMiniruleRequest(rloc);
		return mmbOneAWSClient.getMiniRuleGetFromRec(request, session);
		
	}
}