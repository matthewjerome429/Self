package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.cathaypacific.eodsconsumer.util.MarshallerFactory;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessCouponGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDetailGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessFlightInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDocGroup;
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

@RunWith(MockitoJUnitRunner.class)
public class TicketProcessResponseParserTest {
	
	@InjectMocks
	TicketProcessResponseParser ticketProcessResponseParser;
	
	@Test
	public void test() {
		TicketProcessEDocReply reply =new TicketProcessEDocReply();
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
		TicketProcessInfo ticketProcessInfo= ticketProcessResponseParser.paserResponse(reply);
		Assert.assertEquals("CX", ticketProcessInfo.getDocGroups().get(0).getDetailInfos().get(0).getCouponGroups().get(0).getFlightInfos().get(0).getMarketingCompany());
		Assert.assertEquals("KA", ticketProcessInfo.getDocGroups().get(0).getDetailInfos().get(0).getCouponGroups().get(0).getFlightInfos().get(0).getOperatingCompany());
		Assert.assertEquals("147", ticketProcessInfo.getDocGroups().get(0).getDetailInfos().get(0).getCouponGroups().get(0).getFlightInfos().get(0).getBookingClass());
		Assert.assertEquals("K1478", ticketProcessInfo.getDocGroups().get(0).getDetailInfos().get(0).getCouponGroups().get(0).getFlightInfos().get(0).getFlightNumber());
		Assert.assertEquals("2018-04-12", ticketProcessInfo.getDocGroups().get(0).getDetailInfos().get(0).getCouponGroups().get(0).getFlightInfos().get(0).getFlightDate().getArrivalDate());
		Assert.assertEquals("1", ticketProcessInfo.getDocGroups().get(0).getDetailInfos().get(0).getCouponGroups().get(0).getFlightInfos().get(0).getBoardPoint());
	}
	
	@Test
	public void testParser() throws IOException {
		Resource resource = new ClassPathResource("xml/Ticket_ProcessEDoc_Reply.xml");
		InputStream is = resource.getInputStream();
		Jaxb2Marshaller marshaller = MarshallerFactory.getMarshaller(TicketProcessEDocReply.class);
		TicketProcessEDocReply ticketProcessReply = (TicketProcessEDocReply) marshaller.unmarshal(new StreamSource(is));
		
		TicketProcessInfo ticketProcessInfo = ticketProcessResponseParser.paserResponse(ticketProcessReply);
		
		Assert.assertEquals("791", ticketProcessInfo.getMessageFunction());
		List<TicketProcessDocGroup> infoGroups = ticketProcessInfo.getDocGroups();
		Assert.assertEquals(2, infoGroups.size());
		for (int i = 0; i < infoGroups.size(); i++) {
			TicketProcessDocGroup infoGroup = infoGroups.get(i);
			if (i == 0) {
				Assert.assertEquals("PW57S9", infoGroup.getRlocs().get(0).getControlNumber());
				TicketProcessDetailGroup detailInfo = infoGroup.getDetailInfos().get(0);
				Assert.assertEquals("1604552079723", detailInfo.getEticket());
				
				TicketProcessCouponGroup couponInfo = detailInfo.getCouponGroups().get(0);
				TicketProcessFlightInfo flightInfo = couponInfo.getFlightInfos().get(0);
				Assert.assertEquals("YVR", flightInfo.getBoardPoint());
				Assert.assertEquals("JFK", flightInfo.getOffpoint());
				Assert.assertEquals("SEAT_EXL", couponInfo.getPurchaseProductType().getType());
								
				Assert.assertEquals("TEST", infoGroup.getPaxInfo().getFamilyName());
				Assert.assertEquals("E IVAN MR", infoGroup.getPaxInfo().getGivenName());
				Assert.assertEquals(new BigDecimal("74.80"), infoGroup.getFareInfo().getAmount());
				Assert.assertEquals("CAD", infoGroup.getFareInfo().getCurrency());
				Assert.assertEquals("100918", infoGroup.getProductInfo().getProductDate());
				Assert.assertEquals(new BigDecimal("100.86"), infoGroup.getTaxInfo().getAmount());
				Assert.assertEquals("CAD", infoGroup.getTaxInfo().getCurrency());				
			} else if (i == 1) {
				Assert.assertEquals("LW7Y34", infoGroup.getRlocs().get(0).getControlNumber());
				TicketProcessDetailGroup detailInfo = infoGroup.getDetailInfos().get(0);
				Assert.assertEquals("1604552186537", detailInfo.getEticket());
				
				TicketProcessCouponGroup couponInfo1 = detailInfo.getCouponGroups().get(0);
				TicketProcessFlightInfo flightInfo1 = couponInfo1.getFlightInfos().get(0);
				Assert.assertEquals("HKG", flightInfo1.getBoardPoint());
				Assert.assertEquals("TPE", flightInfo1.getOffpoint());
				Assert.assertEquals(new BigInteger("5"), couponInfo1.getBaggageAllowances().get(0).getNumber());
				Assert.assertEquals("K", couponInfo1.getBaggageAllowances().get(0).getUnit().getUnit());
				Assert.assertEquals("BAGGAGE", couponInfo1.getPurchaseProductType().getType());
				
				TicketProcessCouponGroup couponInfo2 = detailInfo.getCouponGroups().get(1);
				TicketProcessFlightInfo flightInfo2 = couponInfo2.getFlightInfos().get(0);
				Assert.assertEquals("TPE", flightInfo2.getBoardPoint());
				Assert.assertEquals("HKG", flightInfo2.getOffpoint());
				Assert.assertEquals(new BigInteger("5"), couponInfo2.getBaggageAllowances().get(0).getNumber());
				Assert.assertEquals("K", couponInfo2.getBaggageAllowances().get(0).getUnit().getUnit());
				Assert.assertEquals("BAGGAGE", couponInfo2.getPurchaseProductType().getType());
								
				Assert.assertEquals("TEST", infoGroup.getPaxInfo().getFamilyName());
				Assert.assertEquals("MAN MR", infoGroup.getPaxInfo().getGivenName());
				Assert.assertEquals(new BigDecimal(784), infoGroup.getFareInfo().getAmount());
				Assert.assertEquals("HKD", infoGroup.getFareInfo().getCurrency());
				Assert.assertEquals("221018", infoGroup.getProductInfo().getProductDate());
				Assert.assertNull(infoGroup.getTaxInfo());
			}
		}
	}

}
