package com.cathaypacific.mmbbizrule.handler;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.journey.DpEligibilityJourney;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.journey.DpEligibilityJourneyResponse;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.journey.DpEligibilityJourneySegment;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.service.DpEligibilityService;
import com.cathaypacific.mmbbizrule.model.journey.JourneySegment;
import com.cathaypacific.mmbbizrule.model.journey.JourneySummary;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.google.common.collect.Lists;

@Component
public class JourneyCalculateHelper {

	@Autowired
	private DpEligibilityService dpEligibilityService;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	
	/**
	 * Async to retrieve journey from dp eligibility
	 * @param pnrBooking
	 * @return Future<List<JourneySummary>>
	 * @throws BusinessBaseException
	 */
	@Async
	public Future<List<JourneySummary>> asyncCalculateJourneyFromDpEligibility(String oaRloc) throws BusinessBaseException {
		return new AsyncResult<>(this.calculateJourneyFromDpEligibility(oaRloc));
	}

	/**
	 * Retrieve journey from dp eligibility based on DPE journey concept
	 * @param pnrBooking
	 * @return List<JourneySummary>
	 * @throws BusinessBaseException
	 */
	public List<JourneySummary> calculateJourneyFromDpEligibility(String oaRloc) throws BusinessBaseException {
		if (StringUtils.isEmpty(oaRloc)) {
			return Collections.emptyList();
		}
		
		// Retrieve PNR from 1A by 1A Rloc
		PNRReply pnrReply = pnrInvokeService.retrievePnrReplyByOneARloc(oaRloc);
		if (pnrReply == null) {
			return Collections.emptyList();
		}
		
		// Retrieve Journey from DEP by PNR
		DpEligibilityJourneyResponse dpEligibilityJourneyResponse = dpEligibilityService.getJourney(pnrReply);
		if (dpEligibilityJourneyResponse == null) {
			return Collections.emptyList();
		}
		
		// Parse the response
		List<JourneySummary> journeySummaries = convertDpeJourneyResponseToJourneySummary(dpEligibilityJourneyResponse);
		return journeySummaries;
	}
	
	/**
	 * Convert DEP journey response to journey summary
	 * @param dpEligibilityJourneyResponse
	 * @return List<JourneySummary>
	 */
	private List<JourneySummary> convertDpeJourneyResponseToJourneySummary(DpEligibilityJourneyResponse dpEligibilityJourneyResponse) {
		List<JourneySummary> journeySummaries = Lists.newArrayList(); 
		if (CollectionUtils.isEmpty(dpEligibilityJourneyResponse.getJourneys())) {
			return Collections.emptyList();
		}
		
		// Loop journey
		List<DpEligibilityJourney> dpEligibilityJourneys = dpEligibilityJourneyResponse.getJourneys();
		for (int i = 0; i < dpEligibilityJourneys.size(); i++) {
			DpEligibilityJourney dpEligibilityJourney = dpEligibilityJourneys.get(i);
			if (CollectionUtils.isEmpty(dpEligibilityJourney.getSegments())) {
				continue;
			}
			
			// Journey id
			JourneySummary journeySummary = new JourneySummary();
			String journeyId = "" + (i + 1);
			journeySummary.setJourneyId(journeyId);
			
			// Loop segment
			for(DpEligibilityJourneySegment dpEligibilityJourneySegment : dpEligibilityJourney.getSegments()) {
				JourneySegment journeySegment = new JourneySegment();
				journeySegment.setSegmentId(dpEligibilityJourneySegment.getSegmentID());
				journeySummary.addSegment(journeySegment);
			}
			
			journeySummaries.add(journeySummary);
		}
		
		return journeySummaries;
	}
}
