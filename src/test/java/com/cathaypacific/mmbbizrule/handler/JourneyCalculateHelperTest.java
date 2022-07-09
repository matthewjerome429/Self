package com.cathaypacific.mmbbizrule.handler;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProductsResponse;
import com.cathaypacific.mmbbizrule.cxservice.aep.service.AEPService;
import com.cathaypacific.mmbbizrule.model.journey.JourneySummary;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;

@RunWith(MockitoJUnitRunner.class)
public class JourneyCalculateHelperTest {
	
	private Gson gson = new Gson();

	@InjectMocks
	private  JourneyCalculateHelper journeyCalculateHelper;
	
	@Mock
	private AEPService aepService;
	
	@Test
	public void journey_calculate_null() throws BusinessBaseException{
		List<JourneySummary> journeyList = journeyCalculateHelper.calculateJourneyFromDpEligibility(null);
		Assert.assertTrue(journeyList.isEmpty());
	}
}
	