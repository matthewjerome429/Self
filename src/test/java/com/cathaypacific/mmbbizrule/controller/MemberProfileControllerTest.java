package com.cathaypacific.mmbbizrule.controller;

import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mmbbizrule.business.MemberProfileBusiness;

@RunWith(MockitoJUnitRunner.class)
public class MemberProfileControllerTest {

	@InjectMocks
	private MemberProfileController memberProfileController;

	@Mock
	private MemberProfileBusiness memberProfileBusiness;

	@Mock
	private MbTokenCacheRepository mbTokenCacheRepository;

	@Test
	public void validate_getPnrByEticket() {
		try {
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setMemberId("1234567");
			memberProfileController.retrieveMemberProfileSummary(loginInfo);
			Mockito.verify(memberProfileBusiness, Mockito.times(1))
					.retrieveMemberProfileSummary(loginInfo.getMemberId(), loginInfo);
			return;
		} catch (BusinessBaseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		fail("Can't execute retrieveMemberProfileSummary when call get /member/profilesummary");
	}

	@Test
	public void validate_getPnrDetailsById() {
		try {
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setMemberId("1234567");
			memberProfileController.getMemberProfileTravelDocs(loginInfo, loginInfo.getMemberId(), null, null, false, false);
			Mockito.verify(memberProfileBusiness, Mockito.times(1))
					.retrieveMemberProfileTravelDoc(loginInfo.getMemberId(), null, null, false, false, loginInfo);
			return;
		} catch (BusinessBaseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		fail("Can't execute retrieveMemberProfileSummary when call get /member/availabletraveldocs");
	}
}
