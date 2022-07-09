package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MembHoliday {

    private String membHolidayInd;

    //@JsonFormat(pattern="yyyy-MM-dd")
    private String membHolidayStartDt;

    //@JsonFormat(pattern="yyyy-MM-dd")
    private String membHolidayEndDt;

    public String getMembHolidayInd() {
        return membHolidayInd;
    }

    public void setMembHolidayInd(final String membHolidayInd) {
        this.membHolidayInd = membHolidayInd;
    }

    public String getMembHolidayStartDt() {
		return membHolidayStartDt;
	}

	public void setMembHolidayStartDt(String membHolidayStartDt) {
		this.membHolidayStartDt = membHolidayStartDt;
	}

	public String getMembHolidayEndDt() {
		return membHolidayEndDt;
	}

	public void setMembHolidayEndDt(String membHolidayEndDt) {
		this.membHolidayEndDt = membHolidayEndDt;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MembHoliday{");
        sb.append("membHolidayInd='").append(membHolidayInd).append('\'');
        sb.append(", membHolidayStartDt='").append(membHolidayStartDt).append('\'');
        sb.append(", membHolidayEndDt='").append(membHolidayEndDt).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
