package com.cathaypacific.mmbbizrule.oneaservice.changepnrelement;

import com.cathaypacific.oneaconsumer.model.request.pnrchg_16_1_1a.LongTextStringTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnrchg_16_1_1a.MessageActionDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnrchg_16_1_1a.MessageFunctionBusinessDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnrchg_16_1_1a.ObjectFactory;
import com.cathaypacific.oneaconsumer.model.request.pnrchg_16_1_1a.PNRChangeElement;

public class ChangePnrElementRequestBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();
	
	public PNRChangeElement buildRequest(String text) {
		PNRChangeElement pnrChangeElement = objFactory.createPNRChangeElement();
		
		MessageActionDetailsTypeI messageAction = objFactory.createMessageActionDetailsTypeI();
		MessageFunctionBusinessDetailsTypeI messageFunction = objFactory.createMessageFunctionBusinessDetailsTypeI();
		messageFunction.setMessageFunction("1");
		messageAction.setMessageFunctionDetails(messageFunction);
		
		LongTextStringTypeI longTextString = objFactory.createLongTextStringTypeI();
		longTextString.setTextStringDetails(text);
		
		pnrChangeElement.setMessageAction(messageAction);
		pnrChangeElement.setLongTextString(longTextString);
		return pnrChangeElement;
	}
	
}
