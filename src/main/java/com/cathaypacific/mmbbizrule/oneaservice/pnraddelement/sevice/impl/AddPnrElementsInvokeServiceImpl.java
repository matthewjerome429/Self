package com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.sevice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.constants.OneAErrorConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.oneaservice.handler.OneAErrorHandler;
import com.cathaypacific.mmbbizrule.oneaservice.model.common.OneAError;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.sevice.AddPnrElementsInvokeService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.response.OneaResponse;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Service
public class AddPnrElementsInvokeServiceImpl implements AddPnrElementsInvokeService {
	
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	
	@Autowired
	private OneAErrorHandler oneAErrorHandler;

	@Override
	public PNRReply addMutiElements(PNRAddMultiElements request, Session session) throws BusinessBaseException {
		OneaResponse<PNRReply> pnrReplay = mmbOneAWSClient.addMultiElements(request, session);
		//check onea error for update
		checkOneAResponseError(pnrReplay.getBody(), pnrReplay.getOneAAction().getInterfaceCode());
		return pnrReplay.getBody();
	}
	
	private void checkOneAResponseError(PNRReply pnrReply, String oneAWsCallCode) throws BusinessBaseException {
		List<OneAError> oneAErrorCodeList = PnrResponseParser.getAllErrors(pnrReply);
		oneAErrorHandler.parseOneAErrorCode(oneAErrorCodeList, MMBConstants.APP_CODE, 
				OneAErrorConstants.MMB_ACTION_NO_SPECIFIC, oneAWsCallCode);
	}

}
