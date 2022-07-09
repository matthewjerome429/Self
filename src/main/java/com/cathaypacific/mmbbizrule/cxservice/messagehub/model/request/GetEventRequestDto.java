package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request;

import java.util.List;

public class GetEventRequestDto {

	private String jobId;
	
	private String rLoc;
	
	/** sample format: 2019-08-21T07:26:15Z */
	private String pnrCreatedAt;

	private List<String> evtIds;

	private List<String> evtTypes;
	
	private String mktCarrier;
	
	private String mktFlightNo;
	
	/** sample format: 2019-11-06 */
	private String mktFlightDate;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getrLoc() {
		return rLoc;
	}

	public void setrLoc(String rLoc) {
		this.rLoc = rLoc;
	}

	public String getPnrCreatedAt() {
		return pnrCreatedAt;
	}

	public void setPnrCreatedAt(String pnrCreatedAt) {
		this.pnrCreatedAt = pnrCreatedAt;
	}

	public List<String> getEvtIds() {
        return evtIds;
    }

    public void setEvtIds(List<String> evtIds) {
        this.evtIds = evtIds;
    }

    public List<String> getEvtTypes() {
        return evtTypes;
    }

    public void setEvtTypes(List<String> evtTypes) {
        this.evtTypes = evtTypes;
    }

    public String getMktCarrier() {
		return mktCarrier;
	}

	public void setMktCarrier(String mktCarrier) {
		this.mktCarrier = mktCarrier;
	}

	public String getMktFlightNo() {
		return mktFlightNo;
	}

	public void setMktFlightNo(String mktFlightNo) {
		this.mktFlightNo = mktFlightNo;
	}

	public String getMktFlightDate() {
		return mktFlightDate;
	}

	public void setMktFlightDate(String mktFlightDate) {
		this.mktFlightDate = mktFlightDate;
	}
	
}
