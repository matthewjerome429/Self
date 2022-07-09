package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class UpdateMessageStatusResponseDto {

	private String jobId;
	
	private String collectionId;

	private String createdAt;

	private String lastUpdatedAt;

	private String msgId;

	private String lang;

	private String channel;

	private String contact;

	private String content;

	private String subject;

	private List<PayloadDto> payloads;

	private List<String> tags;

	private String src;

	private StatusResponseDto status;

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

	public String getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(String lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<PayloadDto> getPayloads() {
		return payloads;
	}

	public void setPayloads(List<PayloadDto> payloads) {
		this.payloads = payloads;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public StatusResponseDto getStatus() {
		return status;
	}

	public void setStatus(StatusResponseDto status) {
		this.status = status;
	}

	public void addPayloadDto(PayloadDto payloadDto) {
		if (CollectionUtils.isEmpty(this.payloads)) {
			this.payloads = new ArrayList<>();
		}
		this.payloads.add(payloadDto);
	}
}
