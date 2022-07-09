package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

import java.util.List;

public class PutMessageResponseDto {

	private String jobId;
	
	private String collectionId;

	private String createdAt;

	private List<MessageResponseDto> messages;

	private String lastUpdatedAt;

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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public List<MessageResponseDto> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageResponseDto> messages) {
		this.messages = messages;
	}

	public String getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(String lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}
}
