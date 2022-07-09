package com.cathaypacific.mmbbizrule.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RtfsStatusConfig {

	@Value("${rtfs.status.rerouted}")
	private String reroutedList;
	
	@Value("${rtfs.status.arrived}")
	private String arrivedList;
	
	@Value("${rtfs.status.cancelled}")
	private String cancelledList;
	
	@Value("${rtfs.status.ontime}")
	private String ontimeList;

	@Value("${rtfs.status.delayed}")
	private String delayedList;
	
	public List<String> getReroutedList() {
		return Arrays.asList(reroutedList.split(","));
	}

	public void setReroutedList(String reroutedList) {
		this.reroutedList = reroutedList;
	}

	public List<String> getArrivedList() {
		return Arrays.asList(arrivedList.split(","));
	}

	public void setArrivedList(String arrivedList) {
		this.arrivedList = arrivedList;
	}

	public List<String> getCancelledList() {
		return Arrays.asList(cancelledList.split(","));
	}

	public void setCancelledList(String cancelledList) {
		this.cancelledList = cancelledList;
	}

	public List<String> getOntimeList() {
		return Arrays.asList(ontimeList.split(","));
	}

	public void setOntimeList(String ontimeList) {
		this.ontimeList = ontimeList;
	}

	public List<String> getDelayedList() {
		return Arrays.asList(delayedList.split(","));
	}

	public void setDelayedList(String delayedList) {
		this.delayedList = delayedList;
	}

}
