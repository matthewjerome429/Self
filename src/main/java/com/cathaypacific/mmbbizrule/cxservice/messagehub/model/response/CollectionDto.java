package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class CollectionDto {

	private String createdAt;

	private String lastUpdatedAt;

	private String collectionId;

	private String evtId;

	private String rLoc;

	private String pnrCreatedAt;

	private String memberId;

	private String surname;

	private String firstname;
	
	private String msgType;

	private List<MessageDto> messages;

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

	public List<MessageDto> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageDto> messages) {
		this.messages = messages;
	}

	public void addMessage(MessageDto message) {
		if (CollectionUtils.isEmpty(this.messages)) {
			this.messages = new ArrayList<>();
		}
		this.messages.add(message);
	}
}
