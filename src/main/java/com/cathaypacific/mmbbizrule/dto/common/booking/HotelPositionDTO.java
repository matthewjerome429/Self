package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

public class HotelPositionDTO implements Serializable{
	
	private static final long serialVersionUID = -6494739655726169123L;

	private String longitude;
	
	private String latitude;
	
	private String address;
	
	private String city;

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
}
