package com.cathaypacific.mmbbizrule.cxservice.dpeligibility.service;

import java.util.concurrent.Future;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.atcdw.ChangeFlightEligibleResponse;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.journey.DpEligibilityJourneyResponse;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;


public interface DpEligibilityService {

	public Future<DpEligibilityJourneyResponse> asyncGetJourney(PNRReply pnr) throws BusinessBaseException;

	public DpEligibilityJourneyResponse getJourney(PNRReply pnr) throws BusinessBaseException;
	/**
	 * get atc dw of booking
	 * @param pnr
	 * @return
	 * @throws BusinessBaseException
	 */
	public ChangeFlightEligibleResponse getAtcDwInfo(PNRReply pnr,String officeId) throws BusinessBaseException;
	
}
