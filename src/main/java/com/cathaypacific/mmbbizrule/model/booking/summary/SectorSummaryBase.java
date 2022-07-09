package com.cathaypacific.mmbbizrule.model.booking.summary;

import java.io.Serializable;
import java.util.Date;

public class SectorSummaryBase implements Serializable {

	private static final long serialVersionUID = 6016453277082331093L;
	
	private String type;
	
	private String id;

	private int order;
	
	private Date orderDate;
	
	private boolean firstAvlSector;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public boolean isFirstAvlSector() {
		return firstAvlSector;
	}

	public void setFirstAvlSector(boolean firstAvlSector) {
		this.firstAvlSector = firstAvlSector;
	}
	
}
