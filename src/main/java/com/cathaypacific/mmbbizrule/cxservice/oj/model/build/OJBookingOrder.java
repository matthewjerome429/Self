package com.cathaypacific.mmbbizrule.cxservice.oj.model.build;

import java.util.Date;

public class OJBookingOrder {
	
	private String id;
	
	private int order;
	
	private Date orderDate;

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
	
}
