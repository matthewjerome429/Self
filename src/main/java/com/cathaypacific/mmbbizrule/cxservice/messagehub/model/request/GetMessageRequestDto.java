package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request;

import java.util.List;

public class GetMessageRequestDto {

	private String jobId;

	private String collectionId;

	private String evtId;

	private String rLoc;

	private String pnrCreatedAt;

	private String memberId;

	private String surname;

	private String firstname;

	private String contact;

	private String msgId;

	private String channel;

	private List<String> tags;

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

	public String getEvtId() {
		return evtId;
	}

	public void setEvtId(String evtId) {
		this.evtId = evtId;
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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
