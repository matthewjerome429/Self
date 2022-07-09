package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.util.List;

public class RetrievePnrTicketPriceInfo {
	
	private float price;
	private String currency;
	private List<String> segmentIds;
	private List<String> paxIds;
	
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
	public List<String> getPaxIds() {
		return paxIds;
	}
	public void setPaxIds(List<String> paxIds) {
		this.paxIds = paxIds;
	}
	
}
