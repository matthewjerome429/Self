package com.cathaypacific.mmbbizrule.oneaservice.convertoarloc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.oneaservice.convertoarloc.RetrieveByOARlocRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.convertoarloc.RetrieveByOARlocResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.convertoarloc.service.ConvertOARlocService;
import com.cathaypacific.oneaconsumer.model.request.paoarq_11_1_1a.PNRRetrieveByOARloc;
import com.cathaypacific.oneaconsumer.model.response.paoarr_11_1_1a.PNRRetrieveByOARlocReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;
import com.google.gson.Gson;

@Service
public class ConvertOARlocServiceImpl implements ConvertOARlocService{

	
	private static LogAgent logger = LogAgent.getLogAgent(ConvertOARlocServiceImpl.class);
	
	@Autowired
	private OneAWSClient oneAWSClient;
	
	@Override
	public String getRlocByGDSRloc(String gdsRloc) throws BusinessBaseException{
		logger.debug("enter getRlocByGDSRloc");
		
		RetrieveByOARlocRequestBuilder builder = new RetrieveByOARlocRequestBuilder();
		PNRRetrieveByOARloc request = builder.buildRlocRequest(gdsRloc);
		logger.debug("request json:"+new Gson().toJson(request));
		
		PNRRetrieveByOARlocReply pnrRetrieveByOARlocReply = oneAWSClient.getRlocByGDSRloc(request);
		
		
		String rloc = null; 
		if(pnrRetrieveByOARlocReply != null){
			rloc = new RetrieveByOARlocResponseParser().findOneARloc(pnrRetrieveByOARlocReply, gdsRloc);
		}
		if(rloc == null || gdsRloc.equals(rloc)){
			logger.warn("Can not find an valid 1A rloc for GDS rloc "+gdsRloc);
		}else{
			logger.info(String.format("got RLOC[%s] with gdsRloc [%s]", rloc, gdsRloc));
		}
		
		return rloc;
	}
}
