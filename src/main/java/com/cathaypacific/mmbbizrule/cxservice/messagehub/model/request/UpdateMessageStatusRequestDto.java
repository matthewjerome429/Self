package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request;

public class UpdateMessageStatusRequestDto {

	private String jobId;

	private String collectionId;

	private String msgId;

	private StatusRequestDto status;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public StatusRequestDto getStatus() {
		return status;
	}

	public void setStatus(StatusRequestDto status) {
		this.status = status;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}
