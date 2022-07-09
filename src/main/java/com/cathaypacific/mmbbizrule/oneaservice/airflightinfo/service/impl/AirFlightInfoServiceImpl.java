package com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.service.impl;

import java.text.DateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.AirFlightInfoRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.AirFlightInfoResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.model.AirFlightInfoBean;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.service.AirFlightInfoService;
import com.cathaypacific.oneaconsumer.model.request.flireq_07_1_1a.AirFlightInfo;
import com.cathaypacific.oneaconsumer.model.response.flires_07_1_1a.AirFlightInfoReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

/**
 * Created by shane.tian.xia on 12/25/2017.
 */
@Service
public class AirFlightInfoServiceImpl implements AirFlightInfoService {

    private static LogAgent logger = LogAgent.getLogAgent(AirFlightInfoServiceImpl.class);

    @Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;

    @Override
    public AirFlightInfoBean getAirFlightInfo(String departureDate, String boardLocationId, String offLocationId,
    		String marketingCompany, String flightNumber, String airCraftType) {
        try {
            DateFormat timeFormat = DateUtil.getDateFormat(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM);
            DateFormat dateFormat = DateUtil.getDateFormat(DateUtil.DATE_PATTERN_DDMMYY);
            departureDate = dateFormat.format(timeFormat.parse(departureDate));
            AirFlightInfoRequestBuilder builder = new AirFlightInfoRequestBuilder();
            AirFlightInfo request = builder.buildAirFlightInfoRequest(departureDate, boardLocationId, offLocationId, marketingCompany, flightNumber);
            AirFlightInfoReply airFlightInfoReply = mmbOneAWSClient.findFlightInfo(request);    
            if (airFlightInfoReply == null) {
                return null;
            }
            AirFlightInfoResponseParser responseParser = new AirFlightInfoResponseParser();
            return responseParser.getAirFlightInfo(airFlightInfoReply, airCraftType);
        }catch(Exception ex){
            logger.error("AirFlightInfoServiceImpl returnOperationalLeg:" + ex.getMessage(), ex);
            return null;
        }
    }
    
}
