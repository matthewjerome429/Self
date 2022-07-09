package com.cathaypacific.mmbbizrule.oneaservice.email.service;

import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.cryptic_07_3_1a.CommandCryptic;
import com.cathaypacific.oneaconsumer.model.response.cryptic_07_3_1a.CommandCrypticReply;

public interface EmailInvokeService {
	
	public CommandCrypticReply sendTicketEmail(CommandCryptic request, Session session) throws Exception;

}
