package com.cathaypacific.mmbbizrule.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookingStatusConfig {

	@Value("${booking.status.confirmed}")
	private String confirmedList;
	
	@Value("${booking.status.waitlisted}")
	private String waitlistedList;
	
	@Value("${booking.status.cancelled}")
	private String cancelledList;
	
	@Value("${booking.status.standby}")
	private String standbyList;
	
	public List<String> getConfirmedList() {
		return Arrays.asList(confirmedList.split(","));
	}

	public void setConfirmedList(String confirmedList) {
		this.confirmedList = confirmedList;
	}

	public void setWaitlistedList(String waitlistedList) {
		this.waitlistedList = waitlistedList;
	}

	public void setCancelledList(String cancelledList) {
		this.cancelledList = cancelledList;
	}

	public void setStandbyList(String standbyList) {
		this.standbyList = standbyList;
	}

	public List<String> getWaitlistedList() {
		 return Arrays.asList(waitlistedList.split(","));
	}

	public List<String> getCancelledList() {
		return Arrays.asList(cancelledList.split(","));
	}

	public List<String> getStandbyList() {
		return Arrays.asList(standbyList.split(","));
	}
	
	
}
