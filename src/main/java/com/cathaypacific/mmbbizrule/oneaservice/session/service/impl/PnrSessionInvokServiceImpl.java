package com.cathaypacific.mmbbizrule.oneaservice.session.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.oneaservice.session.service.PnrSessionInvokService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Service
public class PnrSessionInvokServiceImpl implements PnrSessionInvokService {

	private static LogAgent logger = LogAgent.getLogAgent(PnrSessionInvokServiceImpl.class);
	
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	
	@Override
	public Session openSessionOnly(String rloc) throws SoapFaultException {
		logger.info("start open 1A session.");
		return mmbOneAWSClient.openSessionOnly(rloc);
	}
	
	@Override
	public Session closeSessionOnly(Session session) throws SoapFaultException{
		logger.info("end open 1A session.");
		return mmbOneAWSClient.endSessionOnlyWithCxmbRf(session);
	}
	
}
