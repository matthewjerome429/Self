package com.cathaypacific.mmbbizrule.model.umnreform;

import java.util.List;

import com.google.common.collect.Lists;

public class UMNREFormSegmentRemark {
	
	private String flightNumber;
	
	private String flightDate;
	
	private List<UMNREFormPortInfoRemark> portInfo;

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}

	public List<UMNREFormPortInfoRemark> getPortInfo() {
		if (portInfo == null) {
			portInfo = Lists.newArrayList();
		}
		return portInfo;
	}

	public void setPortInfo(List<UMNREFormPortInfoRemark> portInfo) {
		this.portInfo = portInfo;
	}
	
	
}
