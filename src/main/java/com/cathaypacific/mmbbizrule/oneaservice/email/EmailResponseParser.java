package com.cathaypacific.mmbbizrule.oneaservice.email;

import com.cathaypacific.mmbbizrule.oneaservice.email.model.EmailResponse;
import com.cathaypacific.oneaconsumer.model.response.cryptic_07_3_1a.CommandCrypticReply;

public class EmailResponseParser {
	
	public EmailResponse parser(CommandCrypticReply reply) {
		EmailResponse response = new EmailResponse();
		response.setMessage(parserMessage(reply));
		return response;
	}

	private String parserMessage(CommandCrypticReply reply) {
		if(reply == null) {
			return null;
		}
		return reply.getLongTextString().getTextStringDetails();
	}
	
}
