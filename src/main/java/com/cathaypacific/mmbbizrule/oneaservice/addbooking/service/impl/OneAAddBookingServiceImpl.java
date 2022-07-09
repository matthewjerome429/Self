package com.cathaypacific.mmbbizrule.oneaservice.addbooking.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.constants.OneAErrorConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.oneaservice.addbooking.AddBookingBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.addbooking.service.OneAAddBookingService;
import com.cathaypacific.mmbbizrule.oneaservice.handler.OneAErrorHandler;
import com.cathaypacific.mmbbizrule.oneaservice.model.common.OneAError;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.response.OneaResponse;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Service
public class OneAAddBookingServiceImpl implements OneAAddBookingService{
	
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
		
	@Autowired
	private OneAErrorHandler oneAErrorHandler;

	@Override
	public void addBookingByFQTV(String rloc, String passengerId, List<String> segmentIds, String memberId, RetrievePnrBooking booking) throws BusinessBaseException {
		AddBookingBuilder builder = new AddBookingBuilder();
		PNRAddMultiElements request = builder.buildFQTVRequest(rloc, passengerId, segmentIds, memberId, booking);
		OneaResponse<PNRReply> pnrReplay = mmbOneAWSClient.addMultiElements(request, null);
		
		//check onea error for update
		checkOneAResponseError(pnrReplay.getBody(), pnrReplay.getOneAAction().getInterfaceCode());
		
	}

	@Override
	public void addBookingBySK(String rloc, String memberId, List<String> companyIds) throws BusinessBaseException {
		AddBookingBuilder builder = new AddBookingBuilder();
		PNRAddMultiElements request = builder.buildSkCustRequest(rloc, memberId, companyIds);
		// confirm if could use session as null
		OneaResponse<PNRReply> pnrReplay = mmbOneAWSClient.addMultiElements(request, null);
		
		//check onea error for update
		checkOneAResponseError(pnrReplay.getBody(), pnrReplay.getOneAAction().getInterfaceCode());
	}
	
	/**
	 * check 1A error
	 * @param parser
	 * @param pnrReply
	 * @param oneAWsCallCode
	 * @throws BusinessBaseException
	 */
	private void checkOneAResponseError(PNRReply pnrReply,String oneAWsCallCode) throws BusinessBaseException{
		List<OneAError> oneAErrorCodeList = PnrResponseParser.getAllErrors(pnrReply);
		oneAErrorHandler.parseOneAErrorCode(oneAErrorCodeList, MMBConstants.APP_CODE,
				OneAErrorConstants.MMB_ACTION_NO_SPECIFIC, oneAWsCallCode);
	}

}
