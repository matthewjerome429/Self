package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request;

import java.io.Serializable;
import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response.PayloadDto;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageRequestDto implements Serializable {

	private static final long serialVersionUID = -84846845749481360L;
	
	private String expiredAt;
	
	private String countryCode;

	private String lang;

	private String channel;

	private String subject;

	private String content;

	private String contact;

	private List<PayloadDto> payloads;

	private List<String> tags;

	private StatusRequestDto status;

	public String getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(String expiredAt) {
		this.expiredAt = expiredAt;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public StatusRequestDto getStatus() {
		return status;
	}

	public void setStatus(StatusRequestDto status) {
		this.status = status;
	}
}
