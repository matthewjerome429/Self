package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request;

public class UpdateEventStatusRequestDto {
	
	private String src;
	private String jobId;
	private String evtId;
	private StatusRequestDto status;
	
	public String getSrc() {
		return src;
	}

	public UpdateEventStatusRequestDto setSrc(String src) {
		this.src = src;
		return this;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getEvtId() {
		return evtId;
	}

	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}

	public StatusRequestDto getStatus() {
		return status;
	}

	public void setStatus(StatusRequestDto status) {
		this.status = status;
	}
	
}
