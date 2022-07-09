package com.cathaypacific.mmbbizrule.service.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormUpdateRequestDTO;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormRemark;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.AddRemarkBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.sevice.AddPnrElementsInvokeService;
import com.cathaypacific.mmbbizrule.service.UMNREFormRemarkService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;

@RunWith(MockitoJUnitRunner.class)
public class UMNREFormUpdateServiceImplTest {
	
	@InjectMocks
	UMNREFormUpdateServiceImpl umnreFormUpdateServiceImpl;
	
	@Mock
	UMNREFormRemarkService umnrEFormRemarkService;
	
	@Mock
	PnrInvokeService pnrInvokeService;
	
	@Mock
	UMNREFormRemarkServiceImpl umnreFormRemarkServiceImpl;
	
	@Mock
	AddRemarkBuilder builder;
	
	@Mock
	AddPnrElementsInvokeService addPnrElementsInvokeService;
	
	@Test
	public void test_updateUMNREForm() throws BusinessBaseException {
		
		UmnrFormUpdateRequestDTO requestDTO = new UmnrFormUpdateRequestDTO(); 
		PNRReply pnrReply = new PNRReply();
	
		requestDTO.setRloc("LK7OKS");
		requestDTO.setAge("15");
		requestDTO.setGender("F");
		requestDTO.setPassengerId("1");
		
		RetrievePnrBooking retrievePnrBooking=new RetrievePnrBooking();
		retrievePnrBooking.setOneARloc("LK7OKS");
		List<UMNREFormRemark> umnrEFormRemarks = new ArrayList<>();
		PNRAddMultiElements request = new PNRAddMultiElements();
		Session session = new Session();
		when(pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc())).thenReturn(retrievePnrBooking);
		when(umnrEFormRemarkService.buildUMNREFormRemark(retrievePnrBooking)).thenReturn(umnrEFormRemarks);
		when(umnreFormRemarkServiceImpl.buildUMNREFormRMFreeText(requestDTO)).thenReturn("123456");
		when(builder.buildRequest(requestDTO.getRloc(), session, "123456")).thenReturn(request);
		when(addPnrElementsInvokeService.addMutiElements(request, session)).thenReturn(pnrReply);
		
		umnreFormUpdateServiceImpl.updateUMNREForm(requestDTO);
	}
	

}
