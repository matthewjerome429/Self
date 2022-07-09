package com.cathaypacific.mmbbizrule.cxservice.eods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.eodsconsumer.model.request.bookingsummary_v1.BookingSummaryRQ;
import com.cathaypacific.eodsconsumer.model.response.bookingsummary_v1.BookingSummaryRS;
import com.cathaypacific.eodsconsumer.webservice.service.client.EODSWebClient;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.cxservice.eods.EODSRequestBuilder;
import com.cathaypacific.mmbbizrule.cxservice.eods.EODSResponseParser;
import com.cathaypacific.mmbbizrule.cxservice.eods.model.EODSBooking;
import com.cathaypacific.mmbbizrule.cxservice.eods.service.EODSBookingService;

@Service
public class EODSBookingServiceImpl implements EODSBookingService{
	
	private static LogAgent logger = LogAgent.getLogAgent(EODSBookingServiceImpl.class);
	@Autowired
	private EODSWebClient eodsBookingWSClient;
	
	@Override
	public List<EODSBooking> getBookingList(String memberId) throws UnexpectedException {
		List<EODSBooking> eodsBookingList = null;
		try {
			EODSRequestBuilder builder = new EODSRequestBuilder();
			BookingSummaryRQ request = builder.buildRequest(memberId);
			
			BookingSummaryRS response = eodsBookingWSClient.getBookingSummary(request);
			EODSResponseParser parser = new EODSResponseParser();
			eodsBookingList = parser.parseResponseAndBuildToBookingList(response);
		} catch (Exception e) {
			//ignore exception in this cause, because memeber login will try to get 1A booking still even eods down. 
			logger.error("Retrieve Eods booking failed.",e);
			throw new UnexpectedException("Error to retrieve EODS bookings", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW),e);
		}

		return eodsBookingList;
	}

}
