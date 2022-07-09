package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

public class StatusResponseDto {

	private StatusDto read;

	private StatusDto active;

	private StatusDto sent;

	private StatusDto delivered;

	private StatusDto linkOpen;

	public StatusDto getRead() {
		return read;
	}

	public void setRead(StatusDto read) {
		this.read = read;
	}

	public StatusDto getActive() {
		return active;
	}

	public void setActive(StatusDto active) {
		this.active = active;
	}

	public StatusDto getSent() {
		return sent;
	}

	public void setSent(StatusDto sent) {
		this.sent = sent;
	}

	public StatusDto getDelivered() {
		return delivered;
	}

	public void setDelivered(StatusDto delivered) {
		this.delivered = delivered;
	}

	public StatusDto getLinkOpen() {
		return linkOpen;
	}

	public void setLinkOpen(StatusDto linkOpen) {
		this.linkOpen = linkOpen;
	}
}
