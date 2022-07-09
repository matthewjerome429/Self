package com.cathaypacific.mmbbizrule.dto.response.namesequence;

public class PaxNameSequenceDTO {
	
	private Integer familyNameSequence;
	
	private Integer givenNameSequence;
		
	private String dispalyModelNameBy;

	

	
	public String getDispalyModelNameBy() {
		return dispalyModelNameBy;
	}

	public void setDispalyModelNameBy(String dispalyModelNameBy) {
		this.dispalyModelNameBy = dispalyModelNameBy;
	}

	public Integer getFamilyNameSequence() {
		return familyNameSequence;
	}

	public void setFamilyNameSequence(Integer familyNameSequence) {
		this.familyNameSequence = familyNameSequence;
	}

	public Integer getGivenNameSequence() {
		return givenNameSequence;
	}

	public void setGivenNameSequence(Integer givenNameSequence) {
		this.givenNameSequence = givenNameSequence;
	}

}
