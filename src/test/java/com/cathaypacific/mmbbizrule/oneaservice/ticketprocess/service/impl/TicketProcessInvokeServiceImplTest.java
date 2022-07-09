package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.impl;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessInfo;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.BaggageDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.CompanyIdentificationTypeI51997C;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.ExcessBaggageTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.LocationTypeI52002C;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.ProductDateTimeTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.ProductIdentificationDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.ReservationControlInformationDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.ReservationControlInformationTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TicketNumberDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TicketNumberTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TicketProcessEDocReply;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TravelProductInformationTypeI29340S;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;


@RunWith(MockitoJUnitRunner.class)
public class TicketProcessInvokeServiceImplTest {
	
	@InjectMocks
	TicketProcessInvokeServiceImpl ticketProcessInvokeServiceImpl;
	
	@Mock
	private OneAWSClient oneAWSClient;
	
	@Test
	public void test()throws SoapFaultException  {
		List<String> tickets=new ArrayList<>();
		tickets.add("123456");
		TicketProcessEDocReply reply=new TicketProcessEDocReply();
		ReservationControlInformationTypeI value =new ReservationControlInformationTypeI();
		ReservationControlInformationDetailsTypeI reservation=new ReservationControlInformationDetailsTypeI();
		reservation.setCompanyId("1");
		reservation.setControlNumber("123456");
		reservation.setControlType("");
		value.getReservation().add(reservation);
		TicketProcessEDocReply.DocGroup docGroup =new TicketProcessEDocReply.DocGroup();
		docGroup.setReferenceInfo(value);

		TicketProcessEDocReply.DocGroup.DocDetailsGroup docDetailsGroup=new TicketProcessEDocReply.DocGroup.DocDetailsGroup();
		TicketProcessEDocReply.DocGroup.DocDetailsGroup.CouponGroup couponGroup=new TicketProcessEDocReply.DocGroup.DocDetailsGroup.CouponGroup();
		TravelProductInformationTypeI29340S  leg =new TravelProductInformationTypeI29340S();
	
	    ProductDateTimeTypeI flightDate=new ProductDateTimeTypeI();
		flightDate.setArrivalDate("2018-04-12");
		flightDate.setArrivalTime("2018-04-13");
		flightDate.setDepartureDate("2018-04-14");
		flightDate.setDepartureTime("2018-04-15");
		LocationTypeI52002C boardPointDetails =new LocationTypeI52002C();
		boardPointDetails.setTrueLocationId("1");
		CompanyIdentificationTypeI51997C companyDetails =new CompanyIdentificationTypeI51997C();
		companyDetails.setMarketingCompany("CX");
		companyDetails.setOperatingCompany("KA");
		ProductIdentificationDetailsTypeI flightIdentification =new ProductIdentificationDetailsTypeI();
		flightIdentification.setBookingClass("147");
		flightIdentification.setFlightNumber("K1478");
		leg.setFlightDate(flightDate);
		leg.setBoardPointDetails(boardPointDetails);
		leg.setOffpointDetails(boardPointDetails);
		leg.setCompanyDetails(companyDetails);
		leg.setFlightIdentification(flightIdentification);
		docGroup.getDocDetailsGroup().add(docDetailsGroup);
		docDetailsGroup.getCouponGroup().add(couponGroup);
		couponGroup.getLeg().add(leg);
		ExcessBaggageTypeI baggageInfo=new ExcessBaggageTypeI();
		BaggageDetailsTypeI baggageDetails=new BaggageDetailsTypeI();
		BigInteger b=new BigInteger("123456");
		baggageDetails.setFreeAllowance(b);
		baggageDetails.setQuantityCode("N");
		baggageDetails.setUnitQualifier("21");
		baggageInfo.getBaggageDetails().add(baggageDetails);
		couponGroup.setBaggageInfo(baggageInfo);
		
		reply.getDocGroup().add(docGroup);
		when(oneAWSClient.ticketprocess(anyObject(), anyObject())).thenReturn(reply);	
		TicketProcessInfo ticketProcessInfo=ticketProcessInvokeServiceImpl.getTicketProcessInfo(OneAConstants.TICKET_PROCESS_EDOC_MESSAGE_FUNCTION_ETICKET, tickets);
		Assert.assertEquals("CX", ticketProcessInfo.getDocGroups().get(0).getDetailInfos().get(0).getCouponGroups().get(0).getFlightInfos().get(0).getMarketingCompany());
		Assert.assertEquals("KA", ticketProcessInfo.getDocGroups().get(0).getDetailInfos().get(0).getCouponGroups().get(0).getFlightInfos().get(0).getOperatingCompany());
		Assert.assertEquals("147", ticketProcessInfo.getDocGroups().get(0).getDetailInfos().get(0).getCouponGroups().get(0).getFlightInfos().get(0).getBookingClass());
		Assert.assertEquals("K1478", ticketProcessInfo.getDocGroups().get(0).getDetailInfos().get(0).getCouponGroups().get(0).getFlightInfos().get(0).getFlightNumber());
		Assert.assertEquals("2018-04-12", ticketProcessInfo.getDocGroups().get(0).getDetailInfos().get(0).getCouponGroups().get(0).getFlightInfos().get(0).getFlightDate().getArrivalDate());
		Assert.assertEquals("1", ticketProcessInfo.getDocGroups().get(0).getDetailInfos().get(0).getCouponGroups().get(0).getFlightInfos().get(0).getBoardPoint());
	}
	@Test
	public void test1()throws SoapFaultException  {
		String eticket="123456";
		TicketProcessEDocReply reply=new TicketProcessEDocReply();
		ReservationControlInformationTypeI value =new ReservationControlInformationTypeI();
		ReservationControlInformationDetailsTypeI reservation=new ReservationControlInformationDetailsTypeI();
		reservation.setCompanyId("1");
		reservation.setControlNumber("123456");
		reservation.setControlType("");
		value.getReservation().add(reservation);
		TicketProcessEDocReply.DocGroup docGroup =new TicketProcessEDocReply.DocGroup();
		docGroup.setReferenceInfo(value);

		TicketProcessEDocReply.DocGroup.DocDetailsGroup docDetailsGroup=new TicketProcessEDocReply.DocGroup.DocDetailsGroup();
		TicketProcessEDocReply.DocGroup.DocDetailsGroup.CouponGroup couponGroup=new TicketProcessEDocReply.DocGroup.DocDetailsGroup.CouponGroup();
		TravelProductInformationTypeI29340S  leg =new TravelProductInformationTypeI29340S();
	
	    ProductDateTimeTypeI flightDate=new ProductDateTimeTypeI();
		flightDate.setArrivalDate("2018-04-12");
		flightDate.setArrivalTime("2018-04-13");
		flightDate.setDepartureDate("2018-04-14");
		flightDate.setDepartureTime("2018-04-15");
		LocationTypeI52002C boardPointDetails =new LocationTypeI52002C();
		boardPointDetails.setTrueLocationId("1");
		CompanyIdentificationTypeI51997C companyDetails =new CompanyIdentificationTypeI51997C();
		companyDetails.setMarketingCompany("CX");
		companyDetails.setOperatingCompany("KA");
		ProductIdentificationDetailsTypeI flightIdentification =new ProductIdentificationDetailsTypeI();
		flightIdentification.setBookingClass("147");
		flightIdentification.setFlightNumber("K1478");
		leg.setFlightDate(flightDate);
		leg.setBoardPointDetails(boardPointDetails);
		leg.setOffpointDetails(boardPointDetails);
		leg.setCompanyDetails(companyDetails);
		leg.setFlightIdentification(flightIdentification);
		docGroup.getDocDetailsGroup().add(docDetailsGroup);
		docDetailsGroup.getCouponGroup().add(couponGroup);
		TicketNumberTypeI docInfo=new TicketNumberTypeI();
		TicketNumberDetailsTypeI documentDetails=new TicketNumberDetailsTypeI();
		documentDetails.setNumber("123456");
		docInfo.setDocumentDetails(documentDetails);
		docDetailsGroup.setDocInfo(docInfo);
		couponGroup.getLeg().add(leg);
		ExcessBaggageTypeI baggageInfo=new ExcessBaggageTypeI();
		BaggageDetailsTypeI baggageDetails=new BaggageDetailsTypeI();
		BigInteger b=new BigInteger("123456");
		baggageDetails.setFreeAllowance(b);
		baggageDetails.setQuantityCode("N");
		baggageDetails.setUnitQualifier("21");
		baggageInfo.getBaggageDetails().add(baggageDetails);
		couponGroup.setBaggageInfo(baggageInfo);
		
		reply.getDocGroup().add(docGroup);
		when(oneAWSClient.ticketprocess(anyObject(), anyObject())).thenReturn(reply);	
		String str=ticketProcessInvokeServiceImpl.getRlocByEticket(eticket);
		Assert.assertEquals("123456", str);
	}
}
