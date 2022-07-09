package com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.constants.OneAErrorConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.oneaservice.handler.OneAErrorHandler;
import com.cathaypacific.mmbbizrule.oneaservice.model.common.OneAError;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.DeletePnrRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.pnrxcl_16_1_1a.PNRCancel;
import com.cathaypacific.oneaconsumer.model.response.OneaResponse;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to update passenger info
 * @author fengfeng.jiang
 * @date Jan 8, 2018 5:42:46 PM
 * @version V1.0
 */
@Service
public class DeletePnrServiceImpl implements DeletePnrService {
	
	private static LogAgent logger = LogAgent.getLogAgent(DeletePnrServiceImpl.class);
	
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	
	@Autowired
	private OneAErrorHandler oneAErrorHandler;
	
	@Autowired
	private PnrResponseParser pnrResponseParser;
	
	@Override
	public RetrievePnrBooking deletePnr(String rloc, Map<String, List<String>> map, Session session) throws BusinessBaseException{
		logger.debug(String.format("request json:{%s}", rloc));
		
		DeletePnrRequestBuilder builder = new DeletePnrRequestBuilder();
		PNRCancel request = builder.buildRequest(rloc, map, session);
		OneaResponse<PNRReply> pnrReplay	= mmbOneAWSClient.deleteMultiElements(request, session);
		
		if(pnrReplay == null){
			return null;
		}
		
		RetrievePnrBooking booking = pnrResponseParser.paserResponse(pnrReplay.getBody());
		booking.setSession(pnrReplay.getHeader().getSession());
		return booking;
	}
	
	@Async
	@Override
	public void asyncDeletePnrWithoutParser(String rloc, Map<String, List<String>> map, Session session) {
		try {
			this.deletePnrWithoutParser(rloc, map, session);
		} catch (BusinessBaseException e) {
			logger.error(String.format("deletePnrWithoutParser failure with rloc:[%s], OT numbners: %s", rloc, map.get(OneAConstants.OT_QUALIFIER)), e);
		}
	}
	
	@Override
	public void deletePnrWithoutParser(String rloc, Map<String, List<String>> map, Session session) throws BusinessBaseException {
		logger.debug(String.format("request json:{%s}", rloc));
		
		DeletePnrRequestBuilder builder = new DeletePnrRequestBuilder();
		PNRCancel request = builder.buildRequest(rloc, map, session);
		OneaResponse<PNRReply> pnrReplay = mmbOneAWSClient.deleteMultiElements(request, session);
		
		checkOneAResponseError(pnrReplay.getBody(), pnrReplay.getOneAAction().getInterfaceCode());
	}
	
	private void checkOneAResponseError(PNRReply pnrReply,String oneAWsCallCode) throws BusinessBaseException{
		List<OneAError> oneAErrorCodeList = PnrResponseParser.getAllErrors(pnrReply);
		oneAErrorHandler.parseOneAErrorCode(oneAErrorCodeList, MMBConstants.APP_CODE,
				OneAErrorConstants.MMB_ACTION_NO_SPECIFIC, oneAWsCallCode);
	}

	@Override
	public PNRReply cancelBooking(String rloc, Session session) throws BusinessBaseException {
		DeletePnrRequestBuilder builder = new DeletePnrRequestBuilder();
		PNRCancel request = builder.buildCancelBookingRequest(rloc, session);
		OneaResponse<PNRReply> pnrReply = mmbOneAWSClient.deleteMultiElements(request, session);
		checkOneAResponseError(pnrReply.getBody(), pnrReply.getOneAAction().getInterfaceCode());
		return pnrReply.getBody();
		
	}
	
}
