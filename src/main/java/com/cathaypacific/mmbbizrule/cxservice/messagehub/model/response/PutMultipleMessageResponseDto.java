package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

import java.util.List;

public class PutMultipleMessageResponseDto {

	List<PutMessageResponseDto> collections;

	private String jobId;

	public List<PutMessageResponseDto> getCollections() {
		return collections;
	}

	public void setCollections(List<PutMessageResponseDto> collections) {
		this.collections = collections;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}
