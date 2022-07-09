package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

import java.util.List;

public class UpdateMultipleMessageStatusResponseDto {

	private String jobId;
	
	private List<MessageCollectionResponseDTO> collections;


	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public List<MessageCollectionResponseDTO> getCollections() {
		return collections;
	}

	public void setCollections(List<MessageCollectionResponseDTO> collections) {
		this.collections = collections;
	}
}
