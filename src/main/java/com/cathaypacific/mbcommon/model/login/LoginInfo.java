package com.cathaypacific.mbcommon.model.login;

public class LoginInfo {

	public static final String LOGINTYPE_MEMBER = "M";
	public static final String LOGINTYPE_RLOC = "R";
	public static final String LOGINTYPE_ETICKET = "E";
	public static final String LOGINCHANNEL_MMB = "MMB";
	public static final String LOGINCHANNEL_OLCI = "OLCI";
	
	private String memberId;
	
	/** The original ru member id, if the AM/MPO member upgrade from RU, this field will record the original member id*/
	private String originalRuMemberId;
	
	private String userType;

	private String loginType;

	private String loginRloc;

	private String loginEticket;
	
	// user input family name 
	private String loginFamilyName;
	
	// user input given name 
	private String loginGivenName;

	private String mmbToken;

	// user login channel, MMB or OLCI
	private String loginChannel;
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getLoginRloc() {
		return loginRloc;
	}

	public void setLoginRloc(String loginRloc) {
		this.loginRloc = loginRloc;
	}

	public String getLoginEticket() {
		return loginEticket;
	}

	public void setLoginEticket(String loginEticket) {
		this.loginEticket = loginEticket;
	}

	public String getLoginFamilyName() {
		return loginFamilyName;
	}

	public void setLoginFamilyName(String loginFamilyName) {
		this.loginFamilyName = loginFamilyName;
	}

	public String getLoginGivenName() {
		return loginGivenName;
	}

	public void setLoginGivenName(String loginGivenName) {
		this.loginGivenName = loginGivenName;
	}

	public String getMmbToken() {
		return mmbToken;
	}

	public void setMmbToken(String mmbToken) {
		this.mmbToken = mmbToken;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getOriginalRuMemberId() {
		return originalRuMemberId;
	}

	public void setOriginalRuMemberId(String originalRuMemberId) {
		this.originalRuMemberId = originalRuMemberId;
	}


	public String getLoginChannel() {
		return loginChannel;
	}

	public void setLoginChannel(String loginChannel) {
		this.loginChannel = loginChannel;
	}
}
