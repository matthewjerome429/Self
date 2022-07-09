package com.cathaypacific.mmbbizrule.oneaservice.email;

import com.cathaypacific.oneaconsumer.model.request.cryptic_07_3_1a.CommandCryptic;
import com.cathaypacific.oneaconsumer.model.request.cryptic_07_3_1a.CommandCryptic.LongTextString;
import com.cathaypacific.oneaconsumer.model.request.cryptic_07_3_1a.CommandCryptic.MessageAction;
import com.cathaypacific.oneaconsumer.model.request.cryptic_07_3_1a.CommandCryptic.MessageAction.MessageFunctionDetails;
import com.cathaypacific.oneaconsumer.model.request.cryptic_07_3_1a.ObjectFactory;

public class EmailRequestBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();
	
	public CommandCryptic buildRequest(String email,String lineNumber) {
		CommandCryptic commandCryptic = objFactory.createCommandCryptic();
		
		MessageAction messageAction = objFactory.createCommandCrypticMessageAction();
		MessageFunctionDetails messageFunctionDetails = objFactory.createCommandCrypticMessageActionMessageFunctionDetails();
		messageFunctionDetails.setMessageFunction("1");
		messageAction.setMessageFunctionDetails(messageFunctionDetails);
		
		LongTextString longTextString = objFactory.createCommandCrypticLongTextString();
		longTextString.setTextStringDetails("ITR-EML-" + email + "/L" + lineNumber);
		
		commandCryptic.setMessageAction(messageAction);
		commandCryptic.setLongTextString(longTextString);
		return commandCryptic;
	}
	
}
