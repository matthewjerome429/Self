package com.cathaypacific.mmbbizrule.oneaservice.updatepassenger.service.impl;



import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.constants.OneAErrorConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.db.dao.StatusManagementDAO;
import com.cathaypacific.mmbbizrule.db.dao.TbSsrTypeDAO;
import com.cathaypacific.mmbbizrule.db.model.TbSsrTypeModel;
import com.cathaypacific.mmbbizrule.oneaservice.handler.OneAErrorHandler;
import com.cathaypacific.mmbbizrule.oneaservice.model.common.OneAError;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookingCerateInfo;
import com.cathaypacific.oneaconsumer.constant.onea.OneAActionEnum;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.response.OneaResponse;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ReservationSecurityInformationType204487S;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ResponsibilityInformationType;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@RunWith(MockitoJUnitRunner.class)
public class AddPassengerInfoServiceImplTest {
	@InjectMocks
	AddPassengerInfoServiceImpl addPassengerInfoServiceImpl;
	@Mock
	private OneAWSClient oneAWSClient;
	@Mock
	BizRuleConfig bizRuleConfig;
	@Mock
	private TbSsrTypeDAO tbSsrTypeDAO;
	@Mock
	private OneAErrorHandler oneAErrorHandler;
	@Mock
	private StatusManagementDAO statusManagementDAO;
	@Mock
	private PnrResponseParser pnrResponseParser;
	@Test
	public void test()throws BusinessBaseException, IOException {
		PNRAddMultiElements request=new  PNRAddMultiElements();
		Session session=null;
		OneaResponse<PNRReply> pnrReply=new OneaResponse<>();
		pnrReply.setOneAAction(OneAActionEnum.PNR_ADDMULTIELEMENTS_16_1_1A);
		PNRReply body=new PNRReply();
		ReservationSecurityInformationType204487S securityInformation=new ReservationSecurityInformationType204487S();
		ResponsibilityInformationType responsibilityInformation=new ResponsibilityInformationType();
		responsibilityInformation.setAgentId("1");
		responsibilityInformation.setOfficeId("1");
		securityInformation.setResponsibilityInformation(responsibilityInformation);
		body.setSecurityInformation(securityInformation);
		pnrReply.setBody(body);
		when(oneAWSClient.addMultiElements(anyObject(), anyObject())).thenReturn(pnrReply);
		when(bizRuleConfig.getCxkaTierLevel()).thenReturn(Arrays.asList("IN,DM,DMP,GO,SL,GR".split(",")));
		when(bizRuleConfig.getOneworldTierLevel()).thenReturn(Arrays.asList("RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTopTier()).thenReturn(Arrays.asList("IN,DM,GO,SL,RUBY,SAPH,EMER".split(",")));
		when(bizRuleConfig.getTdInfantGenders()).thenReturn(Arrays.asList("MI,FI".split(",")));
		when(bizRuleConfig.getTdPrimaryTypes()).thenReturn(Arrays.asList("P".split(",")));
		when(bizRuleConfig.getTdSecondaryTypes()).thenReturn(Arrays.asList("V".split(",")));
		List<OneAError> oneAErrorCodeList =new ArrayList<>();
		OneAError oneAError =new OneAError();
		oneAErrorCodeList.add(oneAError);
		doNothing().when(oneAErrorHandler).parseOneAErrorCode(oneAErrorCodeList, MMBConstants.APP_CODE,
				OneAErrorConstants.MMB_ACTION_NO_SPECIFIC, "O");
		List<String> avaliableTravelDocSsrTypes=new ArrayList<>();
		List<String> emrContactSsrTypes=new ArrayList<>();
		List<String> destinationAddrSsrTypes=new ArrayList<>();
		List<String> residenceAddrSsrTypes=new ArrayList<>();
		List<String> mealTypes=new ArrayList<>();
		
		when(tbSsrTypeDAO.findByAppCodeAndTypeAndAction(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_TD, TBConstants.SSR_TRAVEL_DOC_AVAILABLE).stream().map(TbSsrTypeModel :: getValue).collect(Collectors.toList())).thenReturn(avaliableTravelDocSsrTypes);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_EC).stream().map(TbSsrTypeModel :: getValue).collect(Collectors.toList())).thenReturn(emrContactSsrTypes);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_DA).stream().map(TbSsrTypeModel :: getValue).collect(Collectors.toList())).thenReturn(destinationAddrSsrTypes);
		when(tbSsrTypeDAO.findByAppCodeAndType(PnrResponseParser.APP_CODE, PnrResponseParser.SSR_TYPE_RA).stream().map(TbSsrTypeModel :: getValue).collect(Collectors.toList())).thenReturn(residenceAddrSsrTypes);
		when(statusManagementDAO.findByAppCode(MMBConstants.APP_CODE)).thenReturn(new ArrayList<>());
		when(tbSsrTypeDAO.findMealCodeListByAppCode(PnrResponseParser.APP_CODE)).thenReturn(mealTypes);	
		
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		RetrievePnrBookingCerateInfo cerateInfo = new RetrievePnrBookingCerateInfo();
		cerateInfo.setRpOfficeId("1");
		pnrBooking.setBookingCreateInfo(cerateInfo);
		when(pnrResponseParser.paserResponse(anyObject())).thenReturn(pnrBooking);
		
		RetrievePnrBooking booking=addPassengerInfoServiceImpl.addPassengerInfo(request, session);
		 Assert.assertEquals("1", booking.getOfficeId());
	}

}
