package com.cathaypacific.mmbbizrule.dto.request.feedback;

public class SaveFeedbackRequestDTO {

	private String rloc;

	private String memberId;
	
	private String page;
	
	private int starRating;
	
	private String mcOption;
	
	private String comment;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public int getStarRating() {
		return starRating;
	}

	public void setStarRating(int starRating) {
		this.starRating = starRating;
	}

	public String getMcOption() {
		return mcOption;
	}

	public void setMcOption(String mcOption) {
		this.mcOption = mcOption;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
