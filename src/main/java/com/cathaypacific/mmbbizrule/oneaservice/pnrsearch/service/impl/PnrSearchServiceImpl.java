package com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.PnrSearchRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.PnrSearchResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.model.PnrSearchBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.service.PnrSearchService;
import com.cathaypacific.oneaconsumer.model.request.pausrq_16_1_1a.PNRSearch;
import com.cathaypacific.oneaconsumer.model.response.pausrr_16_1_1a.PNRSearchReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Service
public class PnrSearchServiceImpl implements PnrSearchService {

	@Autowired
	private OneAWSClient oneAWSClient;

	@Override
	public List<PnrSearchBooking> retrieveBookingList(String memberId) throws BusinessBaseException {
		PnrSearchRequestBuilder requestBuilder = new PnrSearchRequestBuilder();
		PNRSearch request = requestBuilder.buildRlocRequest(memberId);
		PNRSearchReply pnrSearchReply =oneAWSClient.getPnrViews(request);
		PnrSearchResponseParser parser = new PnrSearchResponseParser();
		return parser.paserResponse(pnrSearchReply);
	}

}
