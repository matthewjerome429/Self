package com.cathaypacific.mmbbizrule.util;

import com.cathaypacific.mbcommon.model.login.LoginInfo;

public class LoginInfoUtil {
	
	private LoginInfoUtil(){
		//donothing
	};
	/**
	 * 
	 * @Description build login info
	 * @param
	 * @return void
	 * @author fengfeng.jiang
	 */
	public static LoginInfo createLoginInfoForNonmember(String rloc, String familyName, String givenName, String eticket,
			String loginType, String mmbToken) {
		LoginInfo loginInfo = new LoginInfo();
		if (LoginInfo.LOGINTYPE_ETICKET.equals(loginType)) {
			loginInfo.setLoginType(LoginInfo.LOGINTYPE_ETICKET);
		} else {
			loginInfo.setLoginType(LoginInfo.LOGINTYPE_RLOC);
		}

		loginInfo.setLoginRloc(rloc);
		loginInfo.setLoginFamilyName(familyName);
		loginInfo.setLoginGivenName(givenName);
		loginInfo.setLoginEticket(eticket);
		loginInfo.setMmbToken(mmbToken);
		return loginInfo;
	}
	/**
	 * create login Info for member
	 * @param token
	 * @param memberId
	 * @return
	 */
	public static LoginInfo createLoginInfoForMember(String token, String memberId, String userType, String originalRuMemberId){
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginType(LoginInfo.LOGINTYPE_MEMBER);
		loginInfo.setMemberId(memberId);
		loginInfo.setUserType(userType);
		loginInfo.setMmbToken(token);
		loginInfo.setOriginalRuMemberId(originalRuMemberId);
		return loginInfo;
	}
	
}
