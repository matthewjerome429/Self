package com.cathaypacific.mmbbizrule.oneaservice.minirule.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mmbbizrule.oneaservice.minirule.MiniruleRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.minirule.service.MiniruleService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.tmrxrq_17_2_1a.MiniRuleGetFromRec;
import com.cathaypacific.oneaconsumer.model.response.tmrxrq_17_2_1a.MiniRuleGetFromRecReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

public class MiniruleServiceImpl implements MiniruleService {
	
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	
	@Override
	public MiniRuleGetFromRecReply retrieveMiniRuleByRec(String rloc, Session session) throws SoapFaultException{
		MiniruleRequestBuilder builder = new MiniruleRequestBuilder();
		MiniRuleGetFromRec request = builder.buildMiniruleRequest(rloc);
		return mmbOneAWSClient.getMiniRuleGetFromRec(request, session);
	}
	
	
}