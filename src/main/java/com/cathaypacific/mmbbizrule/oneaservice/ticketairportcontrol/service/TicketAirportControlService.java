package com.cathaypacific.mmbbizrule.oneaservice.ticketairportcontrol.service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.response.tacres_17_1_1a.TicketProcessEDocAirportControlReply;

public interface TicketAirportControlService {

	public TicketProcessEDocAirportControlReply updateCpnstatus(String cpnStatus, String ticketNumber, String cpnNumber, Session session) throws BusinessBaseException;

	public TicketProcessEDocAirportControlReply getAirportControl(String ticketNumber, String cpnNumber, Session session) throws BusinessBaseException;
	
}
