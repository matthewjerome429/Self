package com.cathaypacific.mmbbizrule.oneaservice.pnrcancel;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.constant.CommonConstants;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTO;
import com.cathaypacific.oneaconsumer.model.request.pnrxcl_16_1_1a.PNRCancel;

@RunWith(MockitoJUnitRunner.class)
public class DeletePnrRequestBuilderTest {
	private UpdatePassengerDetailsRequestDTO request = new UpdatePassengerDetailsRequestDTO();
	private Map<String, List<String>> deleteMap = new LinkedHashMap<>();
	
	@Before
	public void setUp(){
		/** initialize request **/
		request.setRloc("OXWKYR");
		request.setPassengerId("2");
		
		//{TD=[4, 7], CTCM=[1], PCTC=[3], CTCE=[2], DOCA_D=[6], DOCA_R=[5]}
		List<String> tdList = new ArrayList<>();
		tdList.add("4");
		tdList.add("7");
		deleteMap.put(CommonConstants.MAP_TRAVEL_DOCUMENT_KEY, tdList);
		
		List<String> docarList = new ArrayList<>();
		docarList.add("5");
		deleteMap.put(CommonConstants.MAP_COUNTRY_OF_RESIDENCE_KEY, docarList);
		
		List<String> ctcmList = new ArrayList<>();
		ctcmList.add("1");
		deleteMap.put(CommonConstants.MAP_PHONENO_KEY, ctcmList);
		
		List<String> ctceList = new ArrayList<>();
		ctceList.add("2");
		deleteMap.put(CommonConstants.MAP_EMAIL_KEY, ctceList);
		
		List<String> pctcList = new ArrayList<>();
		pctcList.add("3");
		deleteMap.put(CommonConstants.MAP_EMERGENCY_INFO_KEY, pctcList);
		
		List<String> docadList = new ArrayList<>();
		docadList.add("6");
		deleteMap.put(CommonConstants.MAP_DESTINATION_ADDRESS_KEY, docadList);
	}
	
	@Test
	public void buildRequestTest(){
		DeletePnrRequestBuilder builder = new DeletePnrRequestBuilder();
		PNRCancel result = builder.buildRequest(request.getRloc(), deleteMap, null);
		
		//check delete request
		assertEquals("E",result.getCancelElements().get(0).getEntryType());
		assertEquals("TD",result.getCancelElements().get(0).getElement().get(0).getIdentifier());
		assertEquals("4",result.getCancelElements().get(0).getElement().get(0).getNumber().toString());
		assertEquals("TD",result.getCancelElements().get(0).getElement().get(1).getIdentifier());
		assertEquals("7",result.getCancelElements().get(0).getElement().get(1).getNumber().toString());
		assertEquals("DOCA_R",result.getCancelElements().get(0).getElement().get(2).getIdentifier());
		assertEquals("5",result.getCancelElements().get(0).getElement().get(2).getNumber().toString());
		assertEquals("CTCM",result.getCancelElements().get(0).getElement().get(3).getIdentifier());
		assertEquals("1",result.getCancelElements().get(0).getElement().get(3).getNumber().toString());
		assertEquals("CTCE",result.getCancelElements().get(0).getElement().get(4).getIdentifier());
		assertEquals("2",result.getCancelElements().get(0).getElement().get(4).getNumber().toString());
		assertEquals("PCTC",result.getCancelElements().get(0).getElement().get(5).getIdentifier());
		assertEquals("3",result.getCancelElements().get(0).getElement().get(5).getNumber().toString());
		assertEquals("DOCA_D",result.getCancelElements().get(0).getElement().get(6).getIdentifier());
		assertEquals("6",result.getCancelElements().get(0).getElement().get(6).getNumber().toString());
	}
}
