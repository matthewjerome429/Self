package com.cathaypacific.mmbbizrule.business.commonapi.impl;


import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ChangeFlightEligibleResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ChangeFlightResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ReminderListResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.SSRSKDTO;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.service.ChangeFlightEligibleService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrRequestBuilder;
import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.PNRRetrieve;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.InteractiveFreeTextTypeI136698S;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@RunWith(MockitoJUnitRunner.class)
public class ChangeFlightEligibleBusinessImplTest {

	@InjectMocks
	private ChangeFlightEligibleBusinessImpl changeFlightEligibleBusinessImpl;
	
	@Mock
	private OneAWSClient mmbOneAWSClient;
		
	@Mock
	private ChangeFlightEligibleService changeFlightEligibleService;
	
	PNRReply pnrReplay;
	
	String rloc;
	
	PnrRequestBuilder builder;
	
	PNRRetrieve request; 
	
	ChangeFlightEligibleResponseDTO changeFlightEligibleResponseDTO;
	
	ReminderListResponseDTO reminderListResponseDTO;
	
	@Before
	public void setUp() throws Exception {
		rloc="JQXEUS";
		pnrReplay = new PNRReply();
		InteractiveFreeTextTypeI136698S inter = new InteractiveFreeTextTypeI136698S();
		inter.getText().add("112233");
		pnrReplay.getFreeFormText().add(inter);
		builder = new PnrRequestBuilder();
		request = builder.buildRlocRequest(rloc);
		changeFlightEligibleResponseDTO = new ChangeFlightEligibleResponseDTO();
		reminderListResponseDTO = new ReminderListResponseDTO();
		when(mmbOneAWSClient.retrievePnr(anyObject())).thenReturn(pnrReplay);
		changeFlightEligibleResponseDTO.setAtcBooking(true);
		changeFlightEligibleResponseDTO.setRetrieveOfficeId("HKG0300");
		changeFlightEligibleResponseDTO.setTimeStamp("050618");
		changeFlightEligibleResponseDTO.setCanChangeFlight(true);
		when(changeFlightEligibleService.changeFlightEligibleByPnr(pnrReplay)).thenReturn(changeFlightEligibleResponseDTO);
		reminderListResponseDTO.setHasASREXL(true);
		reminderListResponseDTO.setHasEvent(true);
		reminderListResponseDTO.setHasExtraBackage(true);
		reminderListResponseDTO.setHasHotel(true);
		reminderListResponseDTO.setHasInsurance(true);
		List<SSRSKDTO> skList = new ArrayList<>();
		SSRSKDTO sk = new SSRSKDTO();
		sk.setCode("ABCD");
		skList.add(sk);
		reminderListResponseDTO.setSkList(skList);
		List<SSRSKDTO> ssrList = new ArrayList<>();
		SSRSKDTO ssr = new SSRSKDTO();
		ssr.setCode("ADCD");
		ssrList.add(ssr);
		reminderListResponseDTO.setSsrList(ssrList);
		when(changeFlightEligibleService.reminderListResponseByPnr(pnrReplay)).thenReturn(reminderListResponseDTO);
	}
	
	@Test
	public void test() throws Exception {
		LoginInfo loginInfo = new LoginInfo();
		ChangeFlightResponseDTO changeFlightResponseDTO=changeFlightEligibleBusinessImpl.changeFlightEligibleByRloc(rloc, loginInfo);
		Assert.assertEquals("HKG0300", changeFlightResponseDTO.getRetrieveOfficeId());
		Assert.assertEquals("ADCD", changeFlightResponseDTO.getSsrList().get(0).getCode());
		Assert.assertEquals(true, changeFlightResponseDTO.isAtcBooking());
		Assert.assertEquals(true, changeFlightResponseDTO.isCanChangeFlight());
		Assert.assertEquals(true, changeFlightResponseDTO.isHasASREXL());
		Assert.assertEquals(true, changeFlightResponseDTO.isHasExtraBackage());
		
	}

}
