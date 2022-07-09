package com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary;

import java.io.Serializable;

public class SummaryDTOV2 implements Serializable {
	
	private static final long serialVersionUID = -7097385540190840555L;

	private int order;
	
	private Integer numberOfStops;
	
	private boolean firstAvlSector;
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public Integer getNumberOfStops() {
		return numberOfStops;
	}

	public void setNumberOfStops(Integer numberOfStops) {
		this.numberOfStops = numberOfStops;
	}

	public boolean isFirstAvlSector() {
		return firstAvlSector;
	}

	public void setFirstAvlSector(boolean firstAvlSector) {
		this.firstAvlSector = firstAvlSector;
	}
	
}
