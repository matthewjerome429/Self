package com.cathaypacific.mmbbizrule.oneaservice.airflightinfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mmbbizrule.model.booking.detail.AdditionalOperatorInfo;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.model.AirFlightInfoBean;
import com.cathaypacific.oneaconsumer.model.response.flires_07_1_1a.AirFlightInfoReply;
import com.cathaypacific.oneaconsumer.model.response.flires_07_1_1a.AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails;

/**
 * Created by shane.tian.xia on 12/25/2017.
 */
public class AirFlightInfoResponseParser {

    /**
     * Get Air_FlightInfo
     * @param response
     * @param isNotRunMatchOperateCompany
     * @return
     */
	public AirFlightInfoBean getAirFlightInfo(AirFlightInfoReply response, String airCraftType) {

		if (response == null || response.getFlightScheduleDetails() == null) {
			return null;
		}
		AirFlightInfoBean airFlightInfoBean = new AirFlightInfoBean();
		String operationalRegex = "(\\bOPERATIONAL LEG\\b)([\\s\\S]+)";
		String operateByRegex = "(\\bOPERATED BY\\b)([\\s\\S]+)";
		Pattern operationalPattern = Pattern.compile(operationalRegex);
		Pattern operateByPattern = Pattern.compile(operateByRegex);

		Optional.ofNullable(response.getFlightScheduleDetails().getInteractiveFreeText()).orElseGet(Collections::emptyList).forEach(text->{
			String freeText = text.getFreeText();
			if (StringUtils.isNotBlank(freeText)) {
				Matcher operateByMatcher = operateByPattern.matcher(freeText);
				if (operateByMatcher.find()) {
					AdditionalOperatorInfo info = new AdditionalOperatorInfo();
					info.setOperatorName(operateByMatcher.group(2).trim());
					airFlightInfoBean.setAdditionalOperatorInfo(info);
				}
				Matcher	operationalMatcher = operationalPattern.matcher(freeText);
				if (operationalMatcher.find()) {
					if (StringUtils.isNotEmpty(operationalMatcher.group(2).trim()) && operationalMatcher.group(2).trim().split("\\s+").length == 2){
						airFlightInfoBean.setCarrierCode(operationalMatcher.group(2).trim().split("\\s+")[0]);
						airFlightInfoBean.setFlightNumber(operationalMatcher.group(2).trim().split("\\s+")[1]);
					}
					return;
				}
			}
		});

		// set number of stops and total duration
		if (response.getFlightScheduleDetails().getAdditionalProductDetails() != null) {
			// set number of stops
			if (response.getFlightScheduleDetails().getAdditionalProductDetails().getLegDetails() != null) {
				airFlightInfoBean.setNumberOfStops(response.getFlightScheduleDetails().getAdditionalProductDetails()
						.getLegDetails().getNumberOfStops());
				if (airFlightInfoBean.getNumberOfStops() != null
						&& airFlightInfoBean.getNumberOfStops().intValue() > 0) {
					// Initialize stop arrayList in airFlightInfoBean
					airFlightInfoBean.setStops(new ArrayList<String>(airFlightInfoBean.getNumberOfStops().intValue()));
					airFlightInfoBean.setStopOverFlight(true);
				}
			}

			// set total duration
			if (!CollectionUtils.isEmpty(
					response.getFlightScheduleDetails().getAdditionalProductDetails().getFacilitiesInformation())) {
				airFlightInfoBean.setTotalDuration(response.getFlightScheduleDetails().getAdditionalProductDetails()
						.getFacilitiesInformation().get(0).getDescription());
			}
		}

		// set stops
		if (airFlightInfoBean.isStopOverFlight()) {
			if (!CollectionUtils.isEmpty(response.getFlightScheduleDetails().getBoardPointAndOffPointDetails())) {
				String boardPoint, offPoint;
				for (BoardPointAndOffPointDetails detailItem : response.getFlightScheduleDetails()
						.getBoardPointAndOffPointDetails()) {
					if (detailItem != null && detailItem.getGeneralFlightInfo() != null) {
						boardPoint = null;
						offPoint = null;
						if (detailItem.getGeneralFlightInfo().getBoardPointDetails() != null) {
							boardPoint = detailItem.getGeneralFlightInfo().getBoardPointDetails().getTrueLocationId();
						}
						if (detailItem.getGeneralFlightInfo().getOffPointDetails() != null) {
							offPoint = detailItem.getGeneralFlightInfo().getOffPointDetails().getTrueLocationId();
						}
						if (StringUtils.isNotBlank(boardPoint) && StringUtils.isNotBlank(offPoint)
								&& boardPoint.equalsIgnoreCase(offPoint)) {
							airFlightInfoBean.getStops().add(boardPoint);
							if (airFlightInfoBean.getStops().size() == airFlightInfoBean.getNumberOfStops()
									.intValue()) {
								break;
							}
						}
					}
				}
			}
		}
		return airFlightInfoBean;
	}

}
