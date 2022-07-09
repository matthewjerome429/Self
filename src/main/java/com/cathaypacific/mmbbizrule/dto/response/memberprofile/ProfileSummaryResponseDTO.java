package com.cathaypacific.mmbbizrule.dto.response.memberprofile;

public class ProfileSummaryResponseDTO {
	
		private String title;

	    private String familyName;

	    private String givenName;
	    
	    private String memberId;
	    
	    private String tier;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getFamilyName() {
			return familyName;
		}

		public void setFamilyName(String familyName) {
			this.familyName = familyName;
		}

		public String getGivenName() {
			return givenName;
		}

		public void setGivenName(String givenName) {
			this.givenName = givenName;
		}

		public String getMemberId() {
			return memberId;
		}

		public void setMemberId(String memberId) {
			this.memberId = memberId;
		}

		public String getTier() {
			return tier;
		}

		public void setTier(String tier) {
			this.tier = tier;
		}

}
