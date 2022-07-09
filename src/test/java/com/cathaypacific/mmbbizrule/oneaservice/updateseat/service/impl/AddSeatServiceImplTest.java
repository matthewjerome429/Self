package com.cathaypacific.mmbbizrule.oneaservice.updateseat.service.impl;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.db.dao.StatusManagementDAO;
import com.cathaypacific.mmbbizrule.db.dao.TbSsrTypeDAO;
import com.cathaypacific.mmbbizrule.db.model.TbSsrTypeModel;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.RemarkInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.XlwrInfo;
import com.cathaypacific.mmbbizrule.oneaservice.handler.OneAErrorHandler;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookingCerateInfo;
import com.cathaypacific.mmbbizrule.oneaservice.updateseat.service.AddSeatService;
import com.cathaypacific.oneaconsumer.constant.onea.OneAActionEnum;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.response.OneaResponse;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ElementManagementSegmentType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ReservationSecurityInformationType204487S;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ResponsibilityInformationType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.SpecialRequirementsDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.SpecialRequirementsTypeDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.TicketElementType;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@RunWith(MockitoJUnitRunner.class)
public class AddSeatServiceImplTest {
	
	@InjectMocks
	private AddSeatServiceImpl addSeatServiceImpl;
	
	@Mock
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	
	@Mock
	AddSeatService updateSeatService;
	
	@Mock
	private BizRuleConfig bizRuleConfig;
	
	@Mock
	private OneAErrorHandler oneAErrorHandler;
	
	@Mock
	private TbSsrTypeDAO tbSsrTypeDAO;
	
	@Mock
	private StatusManagementDAO statusManagementDAO;
	
	@Mock
	private PnrResponseParser pnrResponseParser;
	
	@Test
	public void addSeat_Test() throws BusinessBaseException {
		UpdateSeatRequestDTO requestDTO=new UpdateSeatRequestDTO();
		Session session=new Session();
		OneaResponse<PNRReply> pnrReplay =new OneaResponse<>();
		pnrReplay.setOneAAction(OneAActionEnum.PNR_ADDMULTIELEMENTS_16_1_1A);
		PNRReply body=new PNRReply();
		PNRReply.DataElementsMaster dataElementsMaster =new PNRReply.DataElementsMaster();
		PNRReply.DataElementsMaster.DataElementsIndiv getDataElementsIndiv=new PNRReply.DataElementsMaster.DataElementsIndiv();
		TicketElementType ticketElementType=new TicketElementType();
		ticketElementType.setPassengerType("147258");
		getDataElementsIndiv.setTicketElement(ticketElementType);
		ElementManagementSegmentType elementManagementData=new ElementManagementSegmentType();
		elementManagementData.setSegmentName("JKL");
		getDataElementsIndiv.setElementManagementData(elementManagementData);
		SpecialRequirementsDetailsTypeI serviceRequest=new SpecialRequirementsDetailsTypeI();
		SpecialRequirementsTypeDetailsTypeI ssr=new SpecialRequirementsTypeDetailsTypeI();
		ssr.setType("EEEE");
		serviceRequest.setSsr(ssr);
		getDataElementsIndiv.setServiceRequest(serviceRequest);
		dataElementsMaster.getDataElementsIndiv().add(getDataElementsIndiv);
		body.setDataElementsMaster(dataElementsMaster);
		ReservationSecurityInformationType204487S securityInformation=new ReservationSecurityInformationType204487S();
		securityInformation.setCityCode("SH");
		ResponsibilityInformationType responsibilityInformation=new ResponsibilityInformationType();
		responsibilityInformation.setOfficeId("321");
		securityInformation.setResponsibilityInformation(responsibilityInformation);
		body.setSecurityInformation(securityInformation);
		pnrReplay.setBody(body);
		List<XlwrInfo> xlwrInfos=new ArrayList<>();
		List<RemarkInfo> remarkInfos=new ArrayList<>();
		TbSsrTypeModel str =new TbSsrTypeModel();
		str.setValue("1");
		List<TbSsrTypeModel> strs=new ArrayList<>();
		strs.add(str);
		List<String> str1=new ArrayList<>();
		str1.add("2");
		when(tbSsrTypeDAO.findByAppCodeAndTypeAndAction(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_TD, TBConstants.SSR_TRAVEL_DOC_AVAILABLE)).thenReturn(strs);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_EC)).thenReturn(strs);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_DA)).thenReturn(strs);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_RA)).thenReturn(strs);
		when(tbSsrTypeDAO.findMealCodeListByAppCode(PnrResponseParser.APP_CODE)).thenReturn(str1);
		when(statusManagementDAO.findByAppCode(MMBConstants.APP_CODE)).thenReturn(new ArrayList<>());
		when(mmbOneAWSClient.addMultiElements(anyObject(), anyObject())).thenReturn(pnrReplay);
		doNothing().when(oneAErrorHandler).parseOneAErrorCode(anyObject(), anyObject(), anyObject(), anyObject());
		
		RetrievePnrBookingCerateInfo cerateInfo = new RetrievePnrBookingCerateInfo();
		cerateInfo.setRpOfficeId("321");
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		pnrBooking.setBookingCreateInfo(cerateInfo);
		
		when(pnrResponseParser.paserResponse(anyObject())).thenReturn(pnrBooking);
		
		RetrievePnrBooking retrievePnrBooking=addSeatServiceImpl.addSeat(requestDTO, session, xlwrInfos, remarkInfos, null);
		
		Assert.assertEquals("321", retrievePnrBooking.getOfficeId());
	}

}
