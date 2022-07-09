package com.cathaypacific.mmbbizrule.oneaservice.convertOARloc.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.oneaservice.convertoarloc.RetrieveByOARlocResponseParser;
import com.cathaypacific.oneaconsumer.model.response.paoarr_11_1_1a.CodedAttributeInformationType;
import com.cathaypacific.oneaconsumer.model.response.paoarr_11_1_1a.CodedAttributeType;
import com.cathaypacific.oneaconsumer.model.response.paoarr_11_1_1a.PNRRetrieveByOARlocReply;
import com.cathaypacific.oneaconsumer.model.response.paoarr_11_1_1a.ReservationControlInformationDetailsType;
import com.cathaypacific.oneaconsumer.model.response.paoarr_11_1_1a.ReservationControlInformationType;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveByOARlocResponseParserTest {
	
	@InjectMocks
	private RetrieveByOARlocResponseParser retrieveByOARlocResponseParser;
	
	@Test
	public void test() {
		PNRRetrieveByOARlocReply retrieveByOARlocReply=new PNRRetrieveByOARlocReply();
		String inputRloc="MK520";
		PNRRetrieveByOARlocReply.PnrList pnrList=new PNRRetrieveByOARlocReply.PnrList();
		ReservationControlInformationType externalReservation=new ReservationControlInformationType();
		ReservationControlInformationDetailsType reservation=new ReservationControlInformationDetailsType();
		reservation.setControlNumber("MK520");
		reservation.setCompanyId("123456");
		externalReservation.setReservation(reservation);
		pnrList.setExternalReservation(externalReservation);
		CodedAttributeType pnrDetails=new CodedAttributeType();
		CodedAttributeInformationType attributeDetail=new CodedAttributeInformationType();
		attributeDetail.setAttributeType("OAP");
		pnrDetails.getAttributeDetails().add(attributeDetail);
		ReservationControlInformationType value=new ReservationControlInformationType();
		value.setReservation(reservation);
		pnrList.setPnrDetails(pnrDetails);
		pnrList.setAmadeusReservation(value);
		retrieveByOARlocReply.getPnrList().add(pnrList);
		String str=retrieveByOARlocResponseParser.findOneARloc(retrieveByOARlocReply, inputRloc);
		Assert.assertEquals("MK520", str);
	}
	
	@Test
	public void test1() {
		PNRRetrieveByOARlocReply retrieveByOARlocReply=new PNRRetrieveByOARlocReply();
		String inputRloc="MK520";
		PNRRetrieveByOARlocReply.PnrList pnrList=new PNRRetrieveByOARlocReply.PnrList();
		ReservationControlInformationType externalReservation=new ReservationControlInformationType();
		ReservationControlInformationDetailsType reservation=new ReservationControlInformationDetailsType();
		reservation.setControlNumber("MK520");
		reservation.setCompanyId("123456");
		externalReservation.setReservation(reservation);
		pnrList.setExternalReservation(externalReservation);
		CodedAttributeType pnrDetails=new CodedAttributeType();
		CodedAttributeInformationType attributeDetail=new CodedAttributeInformationType();
		attributeDetail.setAttributeType("CSP");
		pnrDetails.getAttributeDetails().add(attributeDetail);
		ReservationControlInformationType value=new ReservationControlInformationType();
		value.setReservation(reservation);
		pnrList.setPnrDetails(pnrDetails);
		pnrList.setAmadeusReservation(value);
		retrieveByOARlocReply.getPnrList().add(pnrList);
		String str=retrieveByOARlocResponseParser.findOneARloc(retrieveByOARlocReply, inputRloc);
		Assert.assertEquals("MK520", str);
	}
	
}
