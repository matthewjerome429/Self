package com.cathaypacific.mmbbizrule.business.impl;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormUpdateRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnrformupdate.UmnrFormUpdateResponseDTO;
import com.cathaypacific.mmbbizrule.service.UMNREFormUpdateService;

@RunWith(MockitoJUnitRunner.class)
public class UMNREFormBusinessImplTest {

	@InjectMocks
	UMNREFormBusinessImpl umnreFormBusinessImpl;
	
	@Mock
	UMNREFormUpdateService umnreFormUpdateService;
	
	@Test
	public void test_updateUmnrEFormData() throws BusinessBaseException {
		UmnrFormUpdateRequestDTO requestDTO = new UmnrFormUpdateRequestDTO(); 
		LoginInfo loginInfo = new LoginInfo();
	
		requestDTO.setRloc("ASDFGH");
		requestDTO.setAge("15");
		requestDTO.setGender("F");
		requestDTO.setPassengerId("1");
		
		when(umnreFormUpdateService.updateUMNREForm(requestDTO)).thenReturn(true);
		UmnrFormUpdateResponseDTO umnrFormUpdateResponseDTO = umnreFormBusinessImpl.updateUmnrEFormData(requestDTO, loginInfo);
		Assert.assertTrue(umnrFormUpdateResponseDTO.isSuccess());
	}
}
