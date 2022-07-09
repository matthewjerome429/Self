package com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.service;

import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.model.AirFlightInfoBean;

/**
 * Created by shane.tian.xia on 12/25/2017.
 */
public interface AirFlightInfoService {
    /**
     * Get Air_FlightInfo
     * @param departureDate
     * @param boardLocationId
     * @param OffLocationId
     * @param marketingCompany
     * @param flightNumber
     * @param isNotRunMatchOperateCompany
     * @return
     */
    public AirFlightInfoBean getAirFlightInfo(String departureDate, String boardLocationId, String offLocationId,
                                                  String marketingCompany, String flightNumber, String airCraftType);
}
