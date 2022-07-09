package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunicationLogging {

    private String marketingCampaign;

    //@JsonFormat(pattern="yyyy-MM-dd")
    private String openedDate;

    public String getMarketingCampaign() {
        return marketingCampaign;
    }

    public void setMarketingCampaign(final String marketingCampaign) {
        this.marketingCampaign = marketingCampaign;
    }

    public String getOpenedDate() {
		return openedDate;
	}

	public void setOpenedDate(String openedDate) {
		this.openedDate = openedDate;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CommunicationLogging{");
        sb.append("marketingCampaign='").append(marketingCampaign).append('\'');
        sb.append(", openedDate=").append(openedDate);
        sb.append('}');
        return sb.toString();
    }
}
