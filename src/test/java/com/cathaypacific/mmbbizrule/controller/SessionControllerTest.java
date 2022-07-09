package com.cathaypacific.mmbbizrule.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.business.token.TokenManagementBusiness;
import com.cathaypacific.mmbbizrule.controller.token.TokenManagementController;
import com.cathaypacific.mmbbizrule.dto.response.session.OperateResultDTO;

@RunWith(MockitoJUnitRunner.class)
public class SessionControllerTest {
 
	@InjectMocks
	private TokenManagementController controller;
	
	@Mock
	private TokenManagementBusiness sessionBusiness;
	@Test
	public void validate_expiration() throws Exception {
		
		when(sessionBusiness.getExpirationTime()).thenReturn(1800);
		
		assertEquals((Integer)1800, (Integer)controller.expiration().getTokenDurationSeconds());
	}
	
	@Test
	public void validate_delete() {
		try {
			when(sessionBusiness.delete(any())).thenReturn(true);
			OperateResultDTO response = controller.delete("");
			assertTrue(response.isSuccess());
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		fail("Can't execute delete when call /v1/session/delete");
	}
}
