package com.cathaypacific.mmbbizrule.oneaservice.pnr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.oneaservice.convertoarloc.service.ConvertOARlocService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.PNRRetrieve;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;
import com.google.gson.Gson;

@Service
public class PnrInvokeServiceImpl implements PnrInvokeService{

	
	private static LogAgent logger = LogAgent.getLogAgent(PnrInvokeServiceImpl.class);
	
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;

	@Autowired
	private ConvertOARlocService convertOARlocService;
	
	@Autowired
	private PnrResponseParser pnrResponseParser;
	
	@Override
	public RetrievePnrBooking retrievePnrByRloc(String rloc) throws BusinessBaseException {
		if (StringUtils.isEmpty(rloc)) {
			return null;
		}
		RetrievePnrBooking booking = null;
		try {
			booking = getPnrByRloc(rloc);
//			if(booking != null) {
//				booking.setDisplayRloc(rloc);
//			}
		} catch (SoapFaultException e) {
			logger.info(String.format("Cannot find booking for rloc %s, will try to check if it is GDS rloc ", rloc));
		}
		booking = booking != null ? booking : getPnrByGDSRloc(rloc);
		if (logger.isDebugEnabled()) {
			logger.debug("RetrievePnrBooking json:"+new Gson().toJson(booking));
		}
		return booking;
	}
	
	@Override
	public RetrievePnrBooking PNRReplyToBooking(PNRReply pnr, String rloc) throws BusinessBaseException {
		RetrievePnrBooking booking = null;
		try {
			booking = convertPnrToBooking(pnr);
//			if(booking != null) {
//				booking.setDisplayRloc(rloc);
//			}
		} catch (SoapFaultException e) {
			logger.info(String.format("Cannot find booking for rloc %s, will try to check if it is GDS rloc ", rloc));
		}
		
		return booking;
	}
	
	
	/**
	* 
	* @Description get Booking by 1A rloc
	* @param
	* @return Booking
	* @author fengfeng.jiang
	 * @throws SoapFaultException 
	 * @throws ExpectedException 
	*/
	private RetrievePnrBooking getPnrByRloc(String rloc) throws SoapFaultException {
		PnrRequestBuilder builder = new PnrRequestBuilder();
		PNRRetrieve request = builder.buildRlocRequest(rloc);
		PNRReply pnrReplay	= mmbOneAWSClient.retrievePnr(request);
		 
		if(pnrReplay == null){
			return null;
		}
		
		return pnrResponseParser.paserResponse(pnrReplay);
	}

	
	/**
	* 
	* @Description get Booking by 1A rloc
	* @param
	* @return Booking
	* @author fengfeng.jiang
	 * @throws SoapFaultException 
	 * @throws ExpectedException 
	*/
	private RetrievePnrBooking convertPnrToBooking(PNRReply _pnr) throws SoapFaultException {
	
		PNRReply pnrReplay	= _pnr;
		 
		if(pnrReplay == null){
			return null;
		}
		
		return pnrResponseParser.paserResponse(pnrReplay);
	}

	
	/**
	* 
	* @Description get Booking by gds rloc
	* @param
	* @return Booking
	* @author fengfeng.jiang
	 * @throws ExpectedException 
	*/
	private RetrievePnrBooking getPnrByGDSRloc(String gdsRloc) throws BusinessBaseException {
		String rloc = convertOARlocService.getRlocByGDSRloc(gdsRloc);
		if(StringUtils.isEmpty(rloc)){
			return null;
		}
		logger.info(String.format("get pnr with new rloc[%s]", rloc));
		
		RetrievePnrBooking booking = getPnrByRloc(rloc);
		
		//replace booking's rloc value with user input rloc and save 1A rloc
//		if(booking != null){
//			booking.setOneARloc(rloc);
//			booking.setGdsRloc(gdsRloc);
//			booking.setDisplayRloc(gdsRloc);
//		}
		return booking;
	}

	@Override
	public PNRReply retrievePnrReplyByOneARloc(String rloc) throws BusinessBaseException {
		PnrRequestBuilder builder = new PnrRequestBuilder();
		PNRRetrieve request = builder.buildRlocRequest(rloc);
		return mmbOneAWSClient.retrievePnr(request);
	}
	
	@Override
	public PNRReply retrievePnrReplyByOneARloc(String rloc, Session session) throws BusinessBaseException {
		PnrRequestBuilder builder = new PnrRequestBuilder();
		PNRRetrieve request = builder.buildRlocRequest(rloc);
		return mmbOneAWSClient.retrievePnr(request, session);
	}
}
