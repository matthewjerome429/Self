package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request;

import java.io.Serializable;
import java.util.List;

public class PutMessageRequestDto implements Serializable {

	private static final long serialVersionUID = 6032660121656430802L;
	
	private String src;

	private String jobId;

	private String collectionId;

	private String evtId;

	private String rLoc;

	private String pnrCreatedAt;

	private String memberId;

	private String surname;

	private String firstname;
	
	private String msgType;

	private List<MessageRequestDto> messages;

	public String getSrc() {
		return src;
	}

	public PutMessageRequestDto setSrc(String src) {
		this.src = src;
		return this;
	}

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

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public List<MessageRequestDto> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageRequestDto> messages) {
		this.messages = messages;
	}
}
