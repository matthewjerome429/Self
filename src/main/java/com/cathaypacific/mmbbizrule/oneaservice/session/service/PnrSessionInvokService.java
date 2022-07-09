package com.cathaypacific.mmbbizrule.oneaservice.session.service;

import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.oneaconsumer.model.header.Session;

public interface PnrSessionInvokService {
	
	public Session openSessionOnly(String rloc) throws SoapFaultException;
	
	public Session closeSessionOnly(Session session) throws SoapFaultException;

}
