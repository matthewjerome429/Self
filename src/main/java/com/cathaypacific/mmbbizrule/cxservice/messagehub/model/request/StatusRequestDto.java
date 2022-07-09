package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request;

import java.io.Serializable;

public class StatusRequestDto implements Serializable {

	private static final long serialVersionUID = 8153040249277217010L;

	private Boolean read;

	private Boolean active;

	private Boolean sent;

	private Boolean delivered;

	private Boolean linkOpen;

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getSent() {
		return sent;
	}

	public void setSent(Boolean sent) {
		this.sent = sent;
	}

	public Boolean getDelivered() {
		return delivered;
	}

	public void setDelivered(Boolean delivered) {
		this.delivered = delivered;
	}

	public Boolean getLinkOpen() {
		return linkOpen;
	}

	public void setLinkOpen(Boolean linkOpen) {
		this.linkOpen = linkOpen;
	}
}
