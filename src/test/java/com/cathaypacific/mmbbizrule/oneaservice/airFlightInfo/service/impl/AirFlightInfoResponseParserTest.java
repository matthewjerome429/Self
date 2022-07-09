package com.cathaypacific.mmbbizrule.oneaservice.airFlightInfo.service.impl;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.AirFlightInfoResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.model.AirFlightInfoBean;
import com.cathaypacific.oneaconsumer.model.response.flires_07_1_1a.AirFlightInfoReply;
@RunWith(MockitoJUnitRunner.class)
public class AirFlightInfoResponseParserTest {
	@InjectMocks
	   private AirFlightInfoResponseParser airFlightInfoResponseParser;
	@Test
	public void test() {
		AirFlightInfoReply response=new  AirFlightInfoReply();
		AirFlightInfoReply.FlightScheduleDetails flightScheduleDetails=new AirFlightInfoReply.FlightScheduleDetails();
		AirFlightInfoReply.FlightScheduleDetails.InteractiveFreeText interactiveFreeText=new AirFlightInfoReply.FlightScheduleDetails.InteractiveFreeText();
		interactiveFreeText.setFreeText("CHIU/LILY 01DEC16");
		flightScheduleDetails.getInteractiveFreeText().add(interactiveFreeText);
		AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails additionalProductDetails=new AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails();
		AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails.LegDetails legDetails=new AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails.LegDetails();
		legDetails.setComplexingFlightIndicator("123");
		BigDecimal value=new BigDecimal("147");
		legDetails.setNumberOfStops(value);
		additionalProductDetails.setLegDetails(legDetails);
		AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails.FacilitiesInformation facilitiesInformation=new AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails.FacilitiesInformation();
		facilitiesInformation.setDescription("369");
		additionalProductDetails.getFacilitiesInformation().add(facilitiesInformation);
		
		flightScheduleDetails.setAdditionalProductDetails(additionalProductDetails);
		AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails boardPointAndOffPointDetails=new AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails();
		AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo generalFlightInfo=new AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo();
		AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo.OffPointDetails offPointDetails=new AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo.OffPointDetails();
		offPointDetails.setTrueLocationId("789");
		generalFlightInfo.setOffPointDetails(offPointDetails);
		AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo.BoardPointDetails boardPointDetails=new AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo.BoardPointDetails();
		boardPointDetails.setTrueLocationId("789");
		generalFlightInfo.setBoardPointDetails(boardPointDetails);
		boardPointAndOffPointDetails.setGeneralFlightInfo(generalFlightInfo);
		flightScheduleDetails.getBoardPointAndOffPointDetails().add(boardPointAndOffPointDetails);
		response.setFlightScheduleDetails(flightScheduleDetails);
		AirFlightInfoBean airFlightInfoBean=airFlightInfoResponseParser.getAirFlightInfo(response, "");
		 Assert.assertEquals("789", airFlightInfoBean.getStops().get(0));
		 Assert.assertEquals(true, airFlightInfoBean.isStopOverFlight());
		 Assert.assertEquals(value, airFlightInfoBean.getNumberOfStops());
		 Assert.assertEquals(null, airFlightInfoBean.getCarrierCode());
		 Assert.assertEquals(null, airFlightInfoBean.getAdditionalOperatorInfo());
	}
	
	@Test
	public void test_AdditionalOperatorInfoModel() {
		AirFlightInfoReply response=new  AirFlightInfoReply();
		AirFlightInfoReply.FlightScheduleDetails flightScheduleDetails=new AirFlightInfoReply.FlightScheduleDetails();
		AirFlightInfoReply.FlightScheduleDetails.InteractiveFreeText interactiveFreeText=new AirFlightInfoReply.FlightScheduleDetails.InteractiveFreeText();
		interactiveFreeText.setFreeText("ORD STL   - OPERATED BY PSA AIRLINES AS AMERICAN EAGLE");
		flightScheduleDetails.getInteractiveFreeText().add(interactiveFreeText);
		AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails additionalProductDetails=new AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails();
		AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails.LegDetails legDetails=new AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails.LegDetails();
		legDetails.setComplexingFlightIndicator("123");
		BigDecimal value=new BigDecimal("147");
		legDetails.setNumberOfStops(value);
		additionalProductDetails.setLegDetails(legDetails);
		AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails.FacilitiesInformation facilitiesInformation=new AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails.FacilitiesInformation();
		facilitiesInformation.setDescription("369");
		additionalProductDetails.getFacilitiesInformation().add(facilitiesInformation);
		
		flightScheduleDetails.setAdditionalProductDetails(additionalProductDetails);
		AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails boardPointAndOffPointDetails=new AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails();
		AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo generalFlightInfo=new AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo();
		AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo.OffPointDetails offPointDetails=new AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo.OffPointDetails();
		offPointDetails.setTrueLocationId("789");
		generalFlightInfo.setOffPointDetails(offPointDetails);
		AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo.BoardPointDetails boardPointDetails=new AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo.BoardPointDetails();
		boardPointDetails.setTrueLocationId("789");
		generalFlightInfo.setBoardPointDetails(boardPointDetails);
		boardPointAndOffPointDetails.setGeneralFlightInfo(generalFlightInfo);
		flightScheduleDetails.getBoardPointAndOffPointDetails().add(boardPointAndOffPointDetails);
		response.setFlightScheduleDetails(flightScheduleDetails);
		AirFlightInfoBean airFlightInfoBean=airFlightInfoResponseParser.getAirFlightInfo(response, "");
		 Assert.assertEquals("789", airFlightInfoBean.getStops().get(0));
		 Assert.assertEquals(true, airFlightInfoBean.isStopOverFlight());
		 Assert.assertEquals(value, airFlightInfoBean.getNumberOfStops());
		 Assert.assertEquals(null, airFlightInfoBean.getCarrierCode());
		 Assert.assertEquals("PSA AIRLINES AS AMERICAN EAGLE", airFlightInfoBean.getAdditionalOperatorInfo().getOperatorName());
	}
	
	@Test
	public void test_AdditionalOperatorInfoModel_1() {
		AirFlightInfoReply response=new  AirFlightInfoReply();
		AirFlightInfoReply.FlightScheduleDetails flightScheduleDetails=new AirFlightInfoReply.FlightScheduleDetails();
		AirFlightInfoReply.FlightScheduleDetails.InteractiveFreeText interactiveFreeText=new AirFlightInfoReply.FlightScheduleDetails.InteractiveFreeText();
		interactiveFreeText.setFreeText("CHC SIN   - OPERATIONAL LEG SQ 0298");
		flightScheduleDetails.getInteractiveFreeText().add(interactiveFreeText);
		AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails additionalProductDetails=new AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails();
		AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails.LegDetails legDetails=new AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails.LegDetails();
		legDetails.setComplexingFlightIndicator("123");
		BigDecimal value=new BigDecimal("147");
		legDetails.setNumberOfStops(value);
		additionalProductDetails.setLegDetails(legDetails);
		AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails.FacilitiesInformation facilitiesInformation=new AirFlightInfoReply.FlightScheduleDetails.AdditionalProductDetails.FacilitiesInformation();
		facilitiesInformation.setDescription("369");
		additionalProductDetails.getFacilitiesInformation().add(facilitiesInformation);
		
		flightScheduleDetails.setAdditionalProductDetails(additionalProductDetails);
		AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails boardPointAndOffPointDetails=new AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails();
		AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo generalFlightInfo=new AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo();
		AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo.OffPointDetails offPointDetails=new AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo.OffPointDetails();
		offPointDetails.setTrueLocationId("789");
		generalFlightInfo.setOffPointDetails(offPointDetails);
		AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo.BoardPointDetails boardPointDetails=new AirFlightInfoReply.FlightScheduleDetails.BoardPointAndOffPointDetails.GeneralFlightInfo.BoardPointDetails();
		boardPointDetails.setTrueLocationId("789");
		generalFlightInfo.setBoardPointDetails(boardPointDetails);
		boardPointAndOffPointDetails.setGeneralFlightInfo(generalFlightInfo);
		flightScheduleDetails.getBoardPointAndOffPointDetails().add(boardPointAndOffPointDetails);
		response.setFlightScheduleDetails(flightScheduleDetails);
		AirFlightInfoBean airFlightInfoBean=airFlightInfoResponseParser.getAirFlightInfo(response, "");
		 Assert.assertEquals("789", airFlightInfoBean.getStops().get(0));
		 Assert.assertEquals(true, airFlightInfoBean.isStopOverFlight());
		 Assert.assertEquals(value, airFlightInfoBean.getNumberOfStops());
		 Assert.assertEquals("SQ", airFlightInfoBean.getCarrierCode());
		 Assert.assertEquals("0298", airFlightInfoBean.getFlightNumber());
		 Assert.assertEquals(null, airFlightInfoBean.getAdditionalOperatorInfo());
	}

}
