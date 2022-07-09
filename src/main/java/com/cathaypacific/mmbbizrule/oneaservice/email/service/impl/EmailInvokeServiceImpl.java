package com.cathaypacific.mmbbizrule.oneaservice.email.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cathaypacific.mmbbizrule.oneaservice.email.service.EmailInvokeService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.cryptic_07_3_1a.CommandCryptic;
import com.cathaypacific.oneaconsumer.model.response.cryptic_07_3_1a.CommandCrypticReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Service
public class EmailInvokeServiceImpl implements EmailInvokeService{
	
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;

	@Override
	public CommandCrypticReply sendTicketEmail(CommandCryptic request, Session session) throws Exception {
		return mmbOneAWSClient.sendTicketEmail(request, session);
	}
	
}
