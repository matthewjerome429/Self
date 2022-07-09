package com.cathaypacific.mmbbizrule.oneaservice.cache;

import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cathaypacific.mbcommon.constants.MDCConstants;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.request.pnrchg_16_1_1a.PNRChangeElement;
import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.PNRRetrieve;
import com.cathaypacific.oneaconsumer.model.request.pnrxcl_16_1_1a.PNRCancel;
import com.cathaypacific.oneaconsumer.model.response.OneaResponse;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Component
public class PNRCacheComponent extends OneAWSClientDecorator {

	private static final LogAgent logger = LogAgent.getLogAgent(PNRCacheComponent.class);
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Override
	public PNRReply retrievePnr(PNRRetrieve request) throws SoapFaultException {
		String rloc = getRlocFromRequest(request);
		String mbToken = MDC.get(MDCConstants.MMB_TOKEN_MDC_KEY);
		boolean validCacheKey = !(StringUtils.isEmpty(rloc) || StringUtils.isEmpty(mbToken));
		PNRReply response = null;
		if (validCacheKey) {
			response = mbTokenCacheRepository.get(mbToken, TokenCacheKeyEnum.PNR, rloc, PNRReply.class);
		}
		if (response == null) {
			response = oneAWSClient.retrievePnr(request);
			if (response != null && validCacheKey) {
				mbTokenCacheRepository.add(mbToken, TokenCacheKeyEnum.PNR, rloc, response);
			}
		}
		return response;
	}

	@Override
	public Session openSessionOnly(String oneARloc) throws SoapFaultException {
		
		String mbToken = MDC.get(MDCConstants.MMB_TOKEN_MDC_KEY);
		
		mbTokenCacheRepository.delete(mbToken, TokenCacheKeyEnum.PNR, mbToken);

		return oneAWSClient.openSessionOnly(oneARloc);
		
	}
	
	@Override
	public OneaResponse<PNRReply> addMultiElements(PNRAddMultiElements request, Session session)
			throws SoapFaultException {
		String mbToken = MDC.get(MDCConstants.MMB_TOKEN_MDC_KEY);
		String rloc = getRlocFromRequest(request);
		boolean validCacheKey = !(StringUtils.isEmpty(rloc) || StringUtils.isEmpty(mbToken));
		if (validCacheKey) {
			mbTokenCacheRepository.delete(mbToken, TokenCacheKeyEnum.PNR, rloc);
		}

		return oneAWSClient.addMultiElements(request, session);
	}

	@Override
	public OneaResponse<PNRReply> deleteMultiElements(PNRCancel request, Session session) throws SoapFaultException {
		String mbToken =  MDC.get(MDCConstants.MMB_TOKEN_MDC_KEY);
		String rloc = getRlocFromRequest(request);
		boolean validCacheKey = !(StringUtils.isEmpty(rloc) || StringUtils.isEmpty(mbToken));
		if (validCacheKey) {
			mbTokenCacheRepository.delete(mbToken, TokenCacheKeyEnum.PNR, rloc);
		}
		return oneAWSClient.deleteMultiElements(request, session);
	}

	/**
	 * Get Rloc from PNRRetrieve request, the data model may be have multiple
	 * rloc, but only the first is valid.
	 * 
	 * @param request
	 * @return
	 */
	private String getRlocFromRequest(PNRRetrieve request) {
		String rloc = null;
		try {
			rloc = request.getRetrievalFacts().getReservationOrProfileIdentifier().getReservation().get(0)
					.getControlNumber();
		} catch (Exception e) {
			logger.warn("Cannot find Rloc from request:", e);
		}
		return rloc;

	}

	/**
	 * Get Rloc from addMultiElements request, the data model may be have
	 * multiple rloc, but only the first is valid.
	 * 
	 * @param request
	 * @return
	 */
	private String getRlocFromRequest(PNRAddMultiElements request) {
		String rloc = null;
		try {
			if (request.getReservationInfo() != null && request.getReservationInfo().getReservation() != null) {
				rloc = request.getReservationInfo().getReservation().getControlNumber();
			}
		} catch (Exception e) {
			logger.warn("Cannot find Rloc from PNRAddMultiElements request",e);
		}
		return rloc;

	}

	/**
	 * Get Rloc from deleteMultiElements request, the data model may be have
	 * multiple rloc, but only the first is valid.
	 * 
	 * @param request
	 * @return
	 */
	private String getRlocFromRequest(PNRCancel request) {
		String rloc = null;
		try {
			if (request.getReservationInfo() != null && request.getReservationInfo().getReservation() != null) {
				rloc = request.getReservationInfo().getReservation().getControlNumber();
			}
		} catch (Exception e) {
			logger.warn("Cannot find Rloc from PNRAddMultiElements request",e);
		}
		return rloc;

	}
	@Override
	public void setOneAWSClient(OneAWSClient oneAWSClient) {
		super.setOneAWSClient(oneAWSClient);
	}
	
	@Override
	public OneaResponse<PNRReply> changePnrElement(PNRChangeElement request, Session session, String rloc) throws SoapFaultException {
		String mbToken =  MDC.get(MDCConstants.MMB_TOKEN_MDC_KEY);
		boolean validCacheKey = !(StringUtils.isEmpty(rloc) || StringUtils.isEmpty(mbToken));
		if (validCacheKey) {
			mbTokenCacheRepository.delete(mbToken, TokenCacheKeyEnum.PNR, rloc);
		}
		return oneAWSClient.changePnrElement(request, session, rloc);
	}
	
}
