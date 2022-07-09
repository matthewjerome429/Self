package com.cathaypacific.mmbbizrule.oneaservice.ticketairportcontrol;

import org.apache.commons.collections.CollectionUtils;

import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.CouponInformationDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.CouponInformationTypeI;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.FreeTextQualificationTypeI;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.InteractiveFreeTextTypeI;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.MessageActionDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.MessageFunctionBusinessDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.ObjectFactory;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.TicketNumberDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.TicketNumberTypeI;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.TicketProcessEDocAirportControl;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.TicketProcessEDocAirportControl.DocGroup;
import com.cathaypacific.oneaconsumer.model.request.tacreq_17_1_1a.TicketProcessEDocAirportControl.DocGroup.CouponGroup;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

public class TicketAirportControlRequestBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();
	
	/**
	 * Build request for updating ticket cpnStatus by ticketNumber & cpnNumber
	 * 
	 * @param cpnStatus
	 * @param ticketNumber
	 * @param cpnNumber
	 * @return
	 */
	public TicketProcessEDocAirportControl buildUpdateStatusRequest(String cpnStatus, String ticketNumber, String cpnNumber) {
		TicketProcessEDocAirportControl request = objFactory.createTicketProcessEDocAirportControl();

		request.setMsgActionDetails(createMsgActionDetails("FCS", "ATO"));
		request.getTextInfo().add(createTextInfo("4", "23", "By AML US DOT refund"));
		request.getDocGroup().add(createDocGroup(cpnStatus, ticketNumber, cpnNumber));
		
		return request;
	}
	
	/**
	 * Build request for getting airportControl
	 * 
	 * @param ticketNumber
	 * @param cpnNumber
	 * @return
	 */
	public TicketProcessEDocAirportControl buildGetAirportControlRequest(String ticketNumber, String cpnNumber) {
		TicketProcessEDocAirportControl request = objFactory.createTicketProcessEDocAirportControl();
		request.setMsgActionDetails(createMsgActionDetails("751", null));
		request.getDocGroup().add(createDocGroup(OneAConstants.TICKET_COUPON_STATUS_I, ticketNumber, cpnNumber));
		return request;
	}
	
	/**
	 * Create DocGroup
	 * 
	 * @param cpnStatus
	 * @param ticketNumber
	 * @param cpnNumber
	 * @return
	 */
	private DocGroup createDocGroup(String cpnStatus, String ticketNumber, String cpnNumber) {
		DocGroup docGroup = objFactory.createTicketProcessEDocAirportControlDocGroup();
		
		docGroup.setDocInfo(createDocInfo(ticketNumber));
		docGroup.getCouponGroup().add(createCouponGroup(cpnStatus, cpnNumber));		
		
		return docGroup;
	}
	
	/**
	 * Create CouponGroup
	 * 
	 * @param cpnStatus
	 * @param cpnNumber
	 * @return
	 */
	private CouponGroup createCouponGroup(String cpnStatus, String cpnNumber) {
		CouponGroup couponGroup = objFactory.createTicketProcessEDocAirportControlDocGroupCouponGroup();
		couponGroup.setCouponInfo(createCouponInfo(cpnStatus, cpnNumber));
		return couponGroup;
	}

	/**
	 * Create CouponInfo 
	 * 
	 * @param cpnStatus
	 * @param cpnNumber
	 * @return
	 */
	private CouponInformationTypeI createCouponInfo(String cpnStatus, String cpnNumber) {
		CouponInformationTypeI couponInfo = objFactory.createCouponInformationTypeI();
		couponInfo.setCouponDetails(createCouponDetails(cpnStatus, cpnNumber));
		return couponInfo;
	}

	/**
	 * Create CouponDetails
	 * 
	 * @param cpnStatus
	 * @param cpnNumber
	 * @return
	 */
	private CouponInformationDetailsTypeI createCouponDetails(String cpnStatus, String cpnNumber) {
		CouponInformationDetailsTypeI couponDetails = objFactory.createCouponInformationDetailsTypeI();
		couponDetails.setCpnNumber(cpnNumber);
		couponDetails.setCpnStatus(cpnStatus);
		return couponDetails;
	}

	/**
	 * Create DocInfo
	 * 
	 * @param number
	 * @return
	 */
	private TicketNumberTypeI createDocInfo(String number) {
		TicketNumberTypeI docInfo = objFactory.createTicketNumberTypeI();
		
		TicketNumberDetailsTypeI documentDetails = objFactory.createTicketNumberDetailsTypeI();
		documentDetails.setNumber(number);
		docInfo.setDocumentDetails(documentDetails);
		
		return docInfo;
	}

	/**
	 * Create TextInfo
	 * 
	 * @param textSubjectQualifier
	 * @param informationType
	 * @param freeText
	 * @return
	 */
	private InteractiveFreeTextTypeI createTextInfo(String textSubjectQualifier, String informationType, String freeText) {
		InteractiveFreeTextTypeI textInfo = objFactory.createInteractiveFreeTextTypeI();
		
		FreeTextQualificationTypeI freeTextQualification = objFactory.createFreeTextQualificationTypeI();
		freeTextQualification.setTextSubjectQualifier(textSubjectQualifier);
		freeTextQualification.setInformationType(informationType);
		textInfo.setFreeTextQualification(freeTextQualification);
		
		textInfo.getFreeText().add(freeText);
		
		return textInfo;
	}

	/**
	 * Create MsgActionDetails
	 * 
	 * @param messageFunction
	 * @param additionalMessageFunction
	 * @return
	 */
	private MessageActionDetailsTypeI createMsgActionDetails(String messageFunction, String additionalMessageFunction) {
		MessageActionDetailsTypeI msgActionDetails = objFactory.createMessageActionDetailsTypeI();
		
		MessageFunctionBusinessDetailsTypeI messageFunctionDetails = objFactory.createMessageFunctionBusinessDetailsTypeI();
		messageFunctionDetails.setMessageFunction(messageFunction);
		if(StringUtils.isNotEmpty(additionalMessageFunction)) {
			messageFunctionDetails.getAdditionalMessageFunction().add(additionalMessageFunction);			
		}
		msgActionDetails.setMessageFunctionDetails(messageFunctionDetails);
		
		return msgActionDetails;
	}

}
