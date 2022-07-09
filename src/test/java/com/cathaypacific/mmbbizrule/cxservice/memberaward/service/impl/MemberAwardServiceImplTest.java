package com.cathaypacific.mmbbizrule.cxservice.memberaward.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mmbbizrule.config.MemberAwardConfig;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.model.request.MemberAwardRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.model.response.MemberAwardResponse;

@RunWith(MockitoJUnitRunner.class)
public class MemberAwardServiceImplTest {
	@Mock
	private MemberAwardConfig memberAwardConfig;
	@Mock
	private RestTemplate restTemplate;
	@InjectMocks
	private MemberAwardServiceImpl memberAwardServiceImpl;
	
	@Test
	public void getMemberAward_test() {
		MemberAwardRequest request=new MemberAwardRequest();
		request.setMemberNumber("196185");
		String memberAwardUrl="www.qq.com";
		
		MemberAwardResponse memberAwardResponse =new MemberAwardResponse();
		memberAwardResponse.setStatusCode("200");
		memberAwardResponse.setMemberNumber("196185");
		ResponseEntity<MemberAwardResponse> responseEntity =new ResponseEntity<MemberAwardResponse>(memberAwardResponse,HttpStatus.OK);
		when(memberAwardConfig.getMemberAwardUrl()).thenReturn(memberAwardUrl);	
		when(restTemplate.exchange(anyString(), any(), any(), Matchers.eq(MemberAwardResponse.class))).thenReturn(responseEntity);
		MemberAwardResponse response =memberAwardServiceImpl.getMemberAward(request);
		assertEquals("196185", response.getMemberNumber());
		assertEquals("200", response.getStatusCode());
	}

}
