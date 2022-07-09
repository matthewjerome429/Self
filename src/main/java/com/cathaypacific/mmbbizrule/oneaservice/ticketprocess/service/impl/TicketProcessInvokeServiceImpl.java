package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.TicketProcessRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.TicketProcessResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDocGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessRloc;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.TicketProcessEDoc;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TicketProcessEDocReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Service
public class TicketProcessInvokeServiceImpl implements TicketProcessInvokeService {
	
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	
	@Autowired
	@Qualifier("oneAWSClient") 
	private OneAWSClient oneAWSClient;
	
	@Override
	public String getRlocByEticket(String eticket) throws SoapFaultException {
		TicketProcessRequestBuilder builder = new TicketProcessRequestBuilder();

		TicketProcessEDoc request = builder.buildTicketProcessRequest(OneAConstants.TICKET_PROCESS_EDOC_MESSAGE_FUNCTION_ETICKET, eticket);
		
		TicketProcessEDocReply reply = mmbOneAWSClient.ticketprocess(request, null);
		TicketProcessResponseParser parser = new TicketProcessResponseParser();
	
		TicketProcessInfo ticketProcessInfo = parser.paserResponse(reply);
		
		if(ticketProcessInfo == null || CollectionUtils.isEmpty(ticketProcessInfo.getDocGroups())){
			return null;
		}
		
		for(TicketProcessDocGroup infoGroup : ticketProcessInfo.getDocGroups()){
			if(infoGroup == null 
					|| CollectionUtils.isEmpty(infoGroup.getRlocs())
					|| CollectionUtils.isEmpty(infoGroup.getDetailInfos())){
				continue;
			}
			
			// the number of detailInfo whose e-ticket is same with eticket
			long eticketMatchedCount = infoGroup.getDetailInfos().stream().filter(detailInfo -> detailInfo != null && !StringUtils.isEmpty(detailInfo.getEticket())
					&& detailInfo.getEticket().equals(eticket)).count();
			
			if(eticketMatchedCount > 0){
				List<TicketProcessRloc> rlocs = infoGroup.getRlocs();
				
				// TODO confirm the logic with zilong 
				// return first 1A RLOC, orElse first CX RLOC, orElse first RLOC which is not empty, orElse null
				return rlocs.stream().filter(rloc -> rloc != null && OneAConstants.ONEA_COMPANY.equals(rloc.getCompanyId())).map(TicketProcessRloc :: getControlNumber).findFirst()
						.orElse(rlocs.stream().filter(rloc -> rloc != null && OneAConstants.COMPANY_CX.equals(rloc.getCompanyId())).map(TicketProcessRloc :: getControlNumber).findFirst()
								.orElse(rlocs.stream().filter(rloc -> rloc != null && !StringUtils.isEmpty(rloc.getControlNumber())).map(TicketProcessRloc :: getControlNumber).findFirst()
										.orElse(null)));
			}
		}
		return null;
	}

	@Override
	public TicketProcessInfo getTicketProcessInfo(String messageFunction, List<String> tickets) throws SoapFaultException {
		TicketProcessRequestBuilder builder = new TicketProcessRequestBuilder();
		
		TicketProcessEDoc request = builder.buildTicketProcessRequest(messageFunction, tickets);
		TicketProcessEDocReply reply = mmbOneAWSClient.ticketprocess(request, null);
		
		TicketProcessResponseParser parser = new TicketProcessResponseParser();
		
		return parser.paserResponse(reply);
	}

	@Override
	public TicketProcessInfo getTicketProcessInfoWithoutCache(String messageFunction, List<String> ticketNumbers, Session session) throws BusinessBaseException {
		TicketProcessRequestBuilder builder = new TicketProcessRequestBuilder();
		TicketProcessEDoc request = builder.buildTicketProcessRequest(messageFunction, ticketNumbers);
		
		TicketProcessEDocReply reply = mmbOneAWSClient.ticketprocess(request, session);
		TicketProcessResponseParser.checkTicketProcessEDocReplyErrors(reply);
		
		TicketProcessResponseParser parser = new TicketProcessResponseParser();
		return parser.paserResponse(reply);
	}
	
}
