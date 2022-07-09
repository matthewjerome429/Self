package com.cathaypacific.mmbbizrule.oneaservice.airflightinfo;

import com.cathaypacific.oneaconsumer.model.request.flireq_07_1_1a.AirFlightInfo;
import com.cathaypacific.oneaconsumer.model.request.flireq_07_1_1a.ObjectFactory;


/**
 * Created by shane.tian.xia on 12/25/2017.
 */
public class AirFlightInfoRequestBuilder {

    private ObjectFactory objectFactory = new ObjectFactory();

    public AirFlightInfo buildAirFlightInfoRequest(String departureDate, String boardLocationId, String OffLocationId,
                                                   String marketingCompany, String flightNumber){
        AirFlightInfo airFlightInfo = objectFactory.createAirFlightInfo();
        AirFlightInfo.GeneralFlightInfo generalFlightInfo = objectFactory.createAirFlightInfoGeneralFlightInfo();
        AirFlightInfo.GeneralFlightInfo.FlightDate flightDate =
                objectFactory.createAirFlightInfoGeneralFlightInfoFlightDate();
        AirFlightInfo.GeneralFlightInfo.BoardPointDetails boardPointDetails =
                objectFactory.createAirFlightInfoGeneralFlightInfoBoardPointDetails();
        AirFlightInfo.GeneralFlightInfo.OffPointDetails offPointDetails =
                objectFactory.createAirFlightInfoGeneralFlightInfoOffPointDetails();
        AirFlightInfo.GeneralFlightInfo.CompanyDetails companyDetails =
                objectFactory.createAirFlightInfoGeneralFlightInfoCompanyDetails();
        AirFlightInfo.GeneralFlightInfo.FlightIdentification flightIdentification =
                objectFactory.createAirFlightInfoGeneralFlightInfoFlightIdentification();
        flightDate.setDepartureDate(departureDate);
        boardPointDetails.setTrueLocationId(boardLocationId);
        offPointDetails.setTrueLocationId(OffLocationId);
        companyDetails.setMarketingCompany(marketingCompany);
        flightIdentification.setFlightNumber(flightNumber);
        generalFlightInfo.setFlightDate(flightDate);
        generalFlightInfo.setBoardPointDetails(boardPointDetails);
        generalFlightInfo.setOffPointDetails(offPointDetails);
        generalFlightInfo.setCompanyDetails(companyDetails);
        generalFlightInfo.setFlightIdentification(flightIdentification);
        airFlightInfo.setGeneralFlightInfo(generalFlightInfo);
        return airFlightInfo;

    }
}
