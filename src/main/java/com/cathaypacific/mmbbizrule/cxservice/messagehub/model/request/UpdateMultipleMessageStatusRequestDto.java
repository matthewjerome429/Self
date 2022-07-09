package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request;

import java.util.List;

public class UpdateMultipleMessageStatusRequestDto {

	private String jobId;

	private List<MessageCollectionDTO> collections;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public List<MessageCollectionDTO> getCollections() {
		return collections;
	}

	public void setCollections(List<MessageCollectionDTO> collections) {
		this.collections = collections;
	}
}
