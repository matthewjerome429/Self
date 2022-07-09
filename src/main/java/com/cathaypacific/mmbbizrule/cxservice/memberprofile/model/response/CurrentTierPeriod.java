package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentTierPeriod {

    //@JsonFormat(pattern="yyyy-MM-dd")
    private String currentTierStartDate;

  //  @JsonFormat(pattern="yyyy-MM-dd")
    private String currentTierEndDate;

    
    public String getCurrentTierStartDate() {
		return currentTierStartDate;
	}


	public void setCurrentTierStartDate(String currentTierStartDate) {
		this.currentTierStartDate = currentTierStartDate;
	}


	public String getCurrentTierEndDate() {
		return currentTierEndDate;
	}


	public void setCurrentTierEndDate(String currentTierEndDate) {
		this.currentTierEndDate = currentTierEndDate;
	}


	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CurrentTierPeriod{");
        sb.append("currentTierStartDate='").append(currentTierStartDate).append('\'');
        sb.append(", currentTierEndDate='").append(currentTierEndDate).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
