package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service;

import java.util.List;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessInfo;
import com.cathaypacific.oneaconsumer.model.header.Session;

public interface TicketProcessInvokeService {

	public String getRlocByEticket(String ticket) throws SoapFaultException;
	
	public TicketProcessInfo getTicketProcessInfo(String messageFunction, List<String> ticket) throws SoapFaultException;
	
	/**
	 * get TicketProcessInfo from 1A directly(not from cache)
	 * 
	 * @param messageFunction
	 * @param ticketNumbers
	 * @param session
	 * @return
	 * @throws BusinessBaseException
	 */
	public TicketProcessInfo getTicketProcessInfoWithoutCache(String messageFunction, List<String> ticketNumbers, Session session)
			 throws BusinessBaseException;
}
