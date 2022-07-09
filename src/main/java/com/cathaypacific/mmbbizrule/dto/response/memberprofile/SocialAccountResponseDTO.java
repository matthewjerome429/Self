package com.cathaypacific.mmbbizrule.dto.response.memberprofile;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class SocialAccountResponseDTO {
	
	@ApiModelProperty("the social accounts linked to member's profile")
	private List<String> socialAccounts;

	public List<String> getSocialAccounts() {
		return socialAccounts;
	}

	public void setSocialAccounts(List<String> socialAccounts) {
		this.socialAccounts = socialAccounts;
	}
	
}
