package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.cathaypacific.mmbbizrule.dto.common.GroupBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class DestinaitionAddressDTO extends GroupBaseDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2770972405043101381L;
	/** Street name */
	private String street;
	/** city */
	private String city;
	/** State code */
	private String stateCode;
	/** Zip code */
	private String zipCode;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@JsonIgnore
	public Boolean isEmpty() {
		return (StringUtils.isEmpty(street) && StringUtils.isEmpty(city)
				&& StringUtils.isEmpty(stateCode))?true:false;
	}
}
