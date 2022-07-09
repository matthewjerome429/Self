package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class Comment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4538238031846362233L;

	/**
	 * Comment type, for e.g., GTE - Gate
	 */
	private String type;

	/**
	 * Free text, for e.g. DCHK
	 */
	private String freeText;

	/**
	 * Status, for e.g. "H"
	 */
	private String status;

	private String commentType;

	private String commentId;
	/**
	 * Delivery status, for e.g. CDC - DeliveryCompleted, CND - NotDelivered, CUD - UnableToDeliver
	 */
	private DeliveryStatus otherStatus;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFreeText() {
		return freeText;
	}

	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DeliveryStatus getOtherStatus() {
		return otherStatus;
	}

	public void setOtherStatus(DeliveryStatus otherStatus) {
		this.otherStatus = otherStatus;
	}

	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

}
