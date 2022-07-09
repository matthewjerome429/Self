package com.cathaypacific.mmbbizrule.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.MDC;

import com.cathaypacific.mbcommon.constants.MDCConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mmbbizrule.business.RetrievePnrBusiness;
import com.cathaypacific.mmbbizrule.dto.request.retrievepnr.RetrievePnrAndConsentRecordRequestDTO;

@RunWith(MockitoJUnitRunner.class)
public class ConsentinfoRecordControllerTest {

	@InjectMocks
	private ConsentinfoRecordController consentinfoRecordController;
	
	@Mock
	private RetrievePnrBusiness retrievePnrService;
	
	@Mock
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	private RetrievePnrAndConsentRecordRequestDTO consentRecordRequestDTO;
	
	@Test
	public void test_consentInfoRecording() throws BusinessBaseException{
		
		MDC.put(MDCConstants.ACCEPT_LANGUAGE_MDC_KEY,"en-US");
		LoginInfo loginInfo = new LoginInfo();
		consentRecordRequestDTO = new RetrievePnrAndConsentRecordRequestDTO();
		loginInfo.setLoginFamilyName("QIN");
		loginInfo.setLoginGivenName("Dongdongqin");
		loginInfo.setLoginRloc("6PZN5Y");
		consentRecordRequestDTO.setConsentBoxCheck(true);
		when(mbTokenCacheRepository.get(any(), any(), any(), any())).thenReturn(Arrays.asList("6PZN5Y"));		
		consentinfoRecordController.consentInfoRecording(consentRecordRequestDTO, loginInfo);
		Mockito.verify(retrievePnrService, Mockito.times(1)).consentInfoRecord(loginInfo,"6PZN5Y","en-US");
	
	}
}
