package com.cathaypacific.mmbbizrule.cxservice.memberaward.model.request;

public class UserInformation {
	private String memberId;
	
	private String memberType;
	
	private String token;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
