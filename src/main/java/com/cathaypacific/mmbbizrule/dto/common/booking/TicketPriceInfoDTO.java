package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;
import java.util.List;


public class TicketPriceInfoDTO implements Serializable{

	private static final long serialVersionUID = 9093579361937143077L;
	
	private float price;
	private String currency;
	private List<String> segmentIds;
	private List<String> passengerIds;
	
	
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public List<String> getSegmentIds() {
		return segmentIds;
	}
	public void setSegmentIds(List<String> segmentIds) {
		this.segmentIds = segmentIds;
	}
	public List<String> getPassengerIds() {
		return passengerIds;
	}
	public void setPassengerIds(List<String> passengerIds) {
		this.passengerIds = passengerIds;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
