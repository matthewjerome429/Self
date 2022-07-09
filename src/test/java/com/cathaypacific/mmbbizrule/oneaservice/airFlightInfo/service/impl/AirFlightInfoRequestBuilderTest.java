package com.cathaypacific.mmbbizrule.oneaservice.airFlightInfo.service.impl;



import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.AirFlightInfoRequestBuilder;
import com.cathaypacific.oneaconsumer.model.request.flireq_07_1_1a.AirFlightInfo;
import com.cathaypacific.oneaconsumer.model.request.flireq_07_1_1a.ObjectFactory;
@RunWith(MockitoJUnitRunner.class)
public class AirFlightInfoRequestBuilderTest {
	 @InjectMocks
	   private AirFlightInfoRequestBuilder airFlightInfoRequestBuilder;
	 @Mock
	 private ObjectFactory objectFactory;
	@Test
	public void test() {
		String departureDate="1";
		String boardLocationId="2"; 
		String OffLocationId="3";
        String marketingCompany="4"; 
        String flightNumber="5";
        AirFlightInfo airFlightInfo=new AirFlightInfo();
        AirFlightInfo.GeneralFlightInfo generalFlightInfo=new AirFlightInfo.GeneralFlightInfo();
        AirFlightInfo.GeneralFlightInfo.FlightDate flightDate=new AirFlightInfo.GeneralFlightInfo.FlightDate ();
        AirFlightInfo.GeneralFlightInfo.BoardPointDetails boardPointDetails=new AirFlightInfo.GeneralFlightInfo.BoardPointDetails();
        AirFlightInfo.GeneralFlightInfo.OffPointDetails offPointDetails=new AirFlightInfo.GeneralFlightInfo.OffPointDetails();
        AirFlightInfo.GeneralFlightInfo.CompanyDetails companyDetails=new AirFlightInfo.GeneralFlightInfo.CompanyDetails();
        AirFlightInfo.GeneralFlightInfo.FlightIdentification flightIdentification=new AirFlightInfo.GeneralFlightInfo.FlightIdentification();
         
        when(objectFactory.createAirFlightInfo()).thenReturn(airFlightInfo);
        when(objectFactory.createAirFlightInfoGeneralFlightInfo()).thenReturn(generalFlightInfo);
        when(objectFactory.createAirFlightInfoGeneralFlightInfoFlightDate()).thenReturn(flightDate);
        when(objectFactory.createAirFlightInfoGeneralFlightInfoBoardPointDetails()).thenReturn(boardPointDetails);
        when(objectFactory.createAirFlightInfoGeneralFlightInfoOffPointDetails()).thenReturn(offPointDetails);
        when(objectFactory.createAirFlightInfoGeneralFlightInfoCompanyDetails()).thenReturn(companyDetails);
        when(objectFactory.createAirFlightInfoGeneralFlightInfoFlightIdentification()).thenReturn(flightIdentification);
        AirFlightInfo airFlightInfos=airFlightInfoRequestBuilder.buildAirFlightInfoRequest(departureDate, boardLocationId, OffLocationId, marketingCompany, flightNumber);
        Assert.assertEquals("1", airFlightInfos.getGeneralFlightInfo().getFlightDate().getDepartureDate());
        Assert.assertEquals("2", airFlightInfos.getGeneralFlightInfo().getBoardPointDetails().getTrueLocationId());
        Assert.assertEquals("3", airFlightInfos.getGeneralFlightInfo().getOffPointDetails().getTrueLocationId());
        Assert.assertEquals("4", airFlightInfos.getGeneralFlightInfo().getCompanyDetails().getMarketingCompany());
        Assert.assertEquals("5", airFlightInfos.getGeneralFlightInfo().getFlightIdentification().getFlightNumber());
	}

}
