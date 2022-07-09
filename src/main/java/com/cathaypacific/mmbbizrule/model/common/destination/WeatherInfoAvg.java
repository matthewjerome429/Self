package com.cathaypacific.mmbbizrule.model.common.destination;

public class WeatherInfoAvg {

	private String date;
	
	private String humidity;
	
	private String maxTemperature;
	
	private String meanTemperature;
	
	private String minTemperature;

	public WeatherInfoAvg() {

	}
	
	public WeatherInfoAvg(String humidity, String maxTemperature, String meanTemperature, String minTemperature) {
		this.humidity = humidity;
		this.maxTemperature = maxTemperature;
		this.meanTemperature = meanTemperature;
		this.minTemperature = minTemperature;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(String maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public String getMeanTemperature() {
		return meanTemperature;
	}

	public void setMeanTemperature(String meanTemperature) {
		this.meanTemperature = meanTemperature;
	}

	public String getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(String minTemperature) {
		this.minTemperature = minTemperature;
	}
}
