package com.cathaypacific.mmbbizrule.model.booking.summary;

import java.util.List;

public class UMFormInfoSummary {
	
	/** whether display fill in form link */
	private boolean hasNotFilledUMForm;
	
	/** comfirm passengerId without UMNR form when jump to umnr form */
	private String passengerId;
	
	/** whether need to display UMNR Consent spoke page */
	private boolean hasFilledUMForm;
	
	/** record segmentId without UMNR e-Form */
	private List<String> segmentIdList;

	public boolean isHasNotFilledUMForm() {
		return hasNotFilledUMForm;
	}

	public void setHasNotFilledUMForm(boolean hasNotFilledUMForm) {
		this.hasNotFilledUMForm = hasNotFilledUMForm;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public boolean isHasFilledUMForm() {
		return hasFilledUMForm;
	}

	public void setHasFilledUMForm(boolean hasFilledUMForm) {
		this.hasFilledUMForm = hasFilledUMForm;
	}

	public List<String> getSegmentIdList() {
		return segmentIdList;
	}

	public void setSegmentIdList(List<String> segmentIdList) {
		this.segmentIdList = segmentIdList;
	}
	
}
