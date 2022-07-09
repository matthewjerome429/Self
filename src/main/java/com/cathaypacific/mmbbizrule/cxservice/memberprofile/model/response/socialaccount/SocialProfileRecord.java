package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.socialaccount;

public class SocialProfileRecord {
	
	private String socialChannelCode;
	
	private CustomerSocialAccount customerSocialAccount;

	public String getSocialChannelCode() {
		return socialChannelCode;
	}

	public void setSocialChannelCode(String socialChannelCode) {
		this.socialChannelCode = socialChannelCode;
	}

	public CustomerSocialAccount getCustomerSocialAccount() {
		return customerSocialAccount;
	}

	public void setCustomerSocialAccount(CustomerSocialAccount customerSocialAccount) {
		this.customerSocialAccount = customerSocialAccount;
	}
}
