package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class FlightStatusInfo implements Serializable {

	private static final long serialVersionUID = 4125103262139451562L;

	private String indicator;

	private String action;

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
