package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.MessageActionDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.MessageFunctionBusinessDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.ObjectFactory;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.TicketNumberDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.TicketNumberTypeI;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.TicketProcessEDoc;
import com.cathaypacific.oneaconsumer.model.request.tatreq_15_2_1a.TicketProcessEDoc.InfoGroup;

@RunWith(MockitoJUnitRunner.class)
public class TicketProcessRequestBuilderTest {
	@InjectMocks
	TicketProcessRequestBuilder ticketProcessRequestBuilder;
	@Mock
	ObjectFactory objFactory;
	@Test
	public void test() {
		List<String> etickets =new ArrayList<>();
		etickets.add("123456");
		TicketProcessEDoc ticketProcessEDoc=new TicketProcessEDoc();
		MessageActionDetailsTypeI msgActionDetail = new MessageActionDetailsTypeI();
		MessageFunctionBusinessDetailsTypeI messageFunctionDetails=new MessageFunctionBusinessDetailsTypeI();
		InfoGroup infoGroup=new InfoGroup();
		TicketNumberTypeI docInfo=new TicketNumberTypeI();
		TicketNumberDetailsTypeI documentDetails =new TicketNumberDetailsTypeI();
		when(objFactory.createTicketProcessEDoc()).thenReturn(ticketProcessEDoc);	
		when(objFactory.createMessageActionDetailsTypeI()).thenReturn(msgActionDetail);
		when(objFactory.createMessageFunctionBusinessDetailsTypeI()).thenReturn(messageFunctionDetails);
		when(objFactory.createTicketProcessEDocInfoGroup()).thenReturn(infoGroup);
		when(objFactory.createTicketNumberTypeI()).thenReturn(docInfo);
		when(objFactory.createTicketNumberDetailsTypeI()).thenReturn(documentDetails);
		TicketProcessEDoc ticketProcessEDoc1=ticketProcessRequestBuilder.buildTicketProcessRequest(OneAConstants.TICKET_PROCESS_EDOC_MESSAGE_FUNCTION_ETICKET, etickets);
		Assert.assertEquals("123456", ticketProcessEDoc1.getInfoGroup().get(0).getDocInfo().getDocumentDetails().getNumber());
	}
	@Test
	public void test1() {
		String eticket ="123456";
		TicketProcessEDoc ticketProcessEDoc=new TicketProcessEDoc();
		MessageActionDetailsTypeI msgActionDetail = new MessageActionDetailsTypeI();
		MessageFunctionBusinessDetailsTypeI messageFunctionDetails=new MessageFunctionBusinessDetailsTypeI();
		InfoGroup infoGroup=new InfoGroup();
		TicketNumberTypeI docInfo=new TicketNumberTypeI();
		TicketNumberDetailsTypeI documentDetails =new TicketNumberDetailsTypeI();
		when(objFactory.createTicketProcessEDoc()).thenReturn(ticketProcessEDoc);	
		when(objFactory.createMessageActionDetailsTypeI()).thenReturn(msgActionDetail);
		when(objFactory.createMessageFunctionBusinessDetailsTypeI()).thenReturn(messageFunctionDetails);
		when(objFactory.createTicketProcessEDocInfoGroup()).thenReturn(infoGroup);
		when(objFactory.createTicketNumberTypeI()).thenReturn(docInfo);
		when(objFactory.createTicketNumberDetailsTypeI()).thenReturn(documentDetails);
		TicketProcessEDoc ticketProcessEDoc1=ticketProcessRequestBuilder.buildTicketProcessRequest(OneAConstants.TICKET_PROCESS_EDOC_MESSAGE_FUNCTION_ETICKET, eticket);
		Assert.assertEquals("123456", ticketProcessEDoc1.getInfoGroup().get(0).getDocInfo().getDocumentDetails().getNumber());
	}

}
