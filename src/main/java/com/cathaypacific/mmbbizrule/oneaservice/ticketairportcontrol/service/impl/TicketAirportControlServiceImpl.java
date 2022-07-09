package com.cathaypacific.mmbbizrule.oneaservice.ticketairportcontrol.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mmbbizrule.oneaservice.ticketairportcontrol.TicketAirportControlRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.ticketairportcontrol.service.TicketAirportControlService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.TicketProcessEDocAirportControl;
import com.cathaypacific.oneaconsumer.model.response.tacres_17_1_1a.TicketProcessEDocAirportControlReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Service
public class TicketAirportControlServiceImpl implements TicketAirportControlService {

	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	
	@Override
	public TicketProcessEDocAirportControlReply updateCpnstatus(String cpnStatus, String ticketNumber, String cpnNumber, Session session) throws BusinessBaseException {
		TicketAirportControlRequestBuilder requestBuilder = new TicketAirportControlRequestBuilder();
		TicketProcessEDocAirportControl request = requestBuilder.buildUpdateStatusRequest(cpnStatus, ticketNumber, cpnNumber);
		TicketProcessEDocAirportControlReply reply = mmbOneAWSClient.ticketprocessControl(request, session);
		checkTicketprocessControlErrors(reply);
		return reply;
	}
	
	@Override
	public TicketProcessEDocAirportControlReply getAirportControl(String ticketNumber, String cpnNumber, Session session) throws BusinessBaseException {
		TicketAirportControlRequestBuilder requestBuilder = new TicketAirportControlRequestBuilder();
		TicketProcessEDocAirportControl request = requestBuilder.buildGetAirportControlRequest(ticketNumber, cpnNumber);
		TicketProcessEDocAirportControlReply reply = mmbOneAWSClient.ticketprocessControl(request, session);
		checkTicketprocessControlErrors(reply);
		return null;
	}
	
	/**
	 * Check TicketprocessControl reply error 
	 * 
	 * @param reply
	 * @throws UnexpectedException
	 */
	private void checkTicketprocessControlErrors(TicketProcessEDocAirportControlReply reply) throws UnexpectedException {
		if(reply.getError() == null) {
			return;
		}
		
		String errorCode = StringUtils.EMPTY;
		if(reply.getError().getErrorDetails() != null) {
			errorCode = reply.getError().getErrorDetails().getErrorCode();
		}
		
		String textInfo = reply.getTextInfo().stream().filter(t -> CollectionUtils.isNotEmpty(t.getFreeText())).map(t -> t.getFreeText().get(0)).findFirst().orElse(StringUtils.EMPTY);
		
		throw new UnexpectedException(String.format("1A TicketProcessEDocAirportControlReply error found, errorCode:[%s], textInfo:[%s]", errorCode, textInfo),
				new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
	}

}
