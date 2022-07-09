package com.cathaypacific.mmbbizrule.model.common.destination;

import java.util.List;

public class PowerInfo {
	
	private String countryCode;

	private String powerFrequency;

	private String powerVoltage;

	private List<String> socketType;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPowerFrequency() {
		return powerFrequency;
	}

	public void setPowerFrequency(String powerFrequency) {
		this.powerFrequency = powerFrequency;
	}

	public String getPowerVoltage() {
		return powerVoltage;
	}

	public void setPowerVoltage(String powerVoltage) {
		this.powerVoltage = powerVoltage;
	}

	public List<String> getSocketType() {
		return socketType;
	}

	public void setSocketType(List<String> socketType) {
		this.socketType = socketType;
	}
}
