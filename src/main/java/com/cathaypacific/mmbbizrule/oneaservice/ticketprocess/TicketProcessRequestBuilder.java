package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.MessageActionDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.MessageFunctionBusinessDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.ObjectFactory;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.TicketNumberDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.TicketNumberTypeI;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.TicketProcessEDoc;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.TicketProcessEDoc.InfoGroup;
 
public class TicketProcessRequestBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();

	public TicketProcessEDoc buildTicketProcessRequest(String messageFunction, List<String> etickets){
		
		TicketProcessEDoc ticketProcessEDoc = objFactory.createTicketProcessEDoc();
		MessageActionDetailsTypeI msgActionDetail = objFactory.createMessageActionDetailsTypeI();
		ticketProcessEDoc.setMsgActionDetails(msgActionDetail);
		MessageFunctionBusinessDetailsTypeI messageFunctionDetails = objFactory.createMessageFunctionBusinessDetailsTypeI();
		msgActionDetail.setMessageFunctionDetails(messageFunctionDetails);
		messageFunctionDetails.setMessageFunction(messageFunction);
		//info group
		for(String eticket : etickets){
			InfoGroup infoGroup = objFactory.createTicketProcessEDocInfoGroup(); 
			TicketNumberTypeI docInfo = objFactory.createTicketNumberTypeI();
			infoGroup.setDocInfo(docInfo);
			TicketNumberDetailsTypeI documentDetails = objFactory.createTicketNumberDetailsTypeI();
			docInfo.setDocumentDetails(documentDetails);
			documentDetails.setNumber(eticket);
			
			ticketProcessEDoc.getInfoGroup().add(infoGroup);
		}
		
		return ticketProcessEDoc;
	}
	
	public TicketProcessEDoc buildTicketProcessRequest(String messageFunction, String eticket){
		List<String> etickets = new ArrayList<>();
		etickets.add(eticket);
		return buildTicketProcessRequest(messageFunction, etickets);
	}
}
