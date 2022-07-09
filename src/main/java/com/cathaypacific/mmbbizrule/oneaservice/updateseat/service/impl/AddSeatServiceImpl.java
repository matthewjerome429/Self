package com.cathaypacific.mmbbizrule.oneaservice.updateseat.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.constants.OneAErrorConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.AswrInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.RemarkInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.XlwrInfo;
import com.cathaypacific.mmbbizrule.oneaservice.handler.OneAErrorHandler;
import com.cathaypacific.mmbbizrule.oneaservice.model.common.OneAError;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.updateseat.UpdateSeatRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.updateseat.service.AddSeatService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.response.OneaResponse;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;
import com.google.gson.Gson;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to update passenger info
 * @author fengfeng.jiang
 * @date Jan 29, 2018 5:42:46 PM
 * @version V1.0
 */
@Service
public class AddSeatServiceImpl implements AddSeatService{
	private static LogAgent logger = LogAgent.getLogAgent(AddSeatServiceImpl.class);
	
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	@Autowired
	AddSeatService updateSeatService;
	@Autowired
	private OneAErrorHandler oneAErrorHandler;
	@Autowired
	private PnrResponseParser pnrResponseParser;
	
	@Override
	public RetrievePnrBooking addSeat(UpdateSeatRequestDTO requestDTO, Session session, List<XlwrInfo> xlwrInfos, List<RemarkInfo> remarkInfos, List<AswrInfo> aswrInfos) throws BusinessBaseException{
		logger.debug(String.format("request json:{%s}",new Gson().toJson(requestDTO)));
		
		UpdateSeatRequestBuilder builder = new UpdateSeatRequestBuilder();	
		PNRAddMultiElements request = builder.buildRequest(requestDTO, session, xlwrInfos, remarkInfos, aswrInfos);
		OneaResponse<PNRReply> pnrReplay = mmbOneAWSClient.addMultiElements(request, session);
		
		//check onea error for update
		checkOneAResponseError(pnrReplay.getBody(), pnrReplay.getOneAAction().getInterfaceCode());
		return pnrResponseParser.paserResponse(pnrReplay.getBody());
	}
	
	private void checkOneAResponseError(PNRReply pnrReply,String oneAWsCallCode) throws BusinessBaseException{
		List<OneAError> oneAErrorCodeList = PnrResponseParser.getAllErrors(pnrReply);
		oneAErrorHandler.parseOneAErrorCode(oneAErrorCodeList, MMBConstants.APP_CODE,
				OneAErrorConstants.MMB_ACTION_NO_SPECIFIC, oneAWsCallCode);
	}
}
