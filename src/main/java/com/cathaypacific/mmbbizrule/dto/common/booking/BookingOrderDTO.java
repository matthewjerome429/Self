package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BookingOrderDTO {
	
	@JsonIgnore
	private String id;
	
	private int order;
	
	@JsonIgnore
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
