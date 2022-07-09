package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class GenderDobDTO {
	/** The gender of passenger */
	private String gender;
    /** The birthDateYear. */
    private String birthDateYear;
    /** The birthDateMonth. */
    private String birthDateMonth;
    /** The birthDateDay. */
    private String birthDateDay;
	/** The OT number in pnr */
	private BigInteger qualifierId;
	

	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public BigInteger getQualifierId() {
		return qualifierId;
	}
	public void setQualifierId(BigInteger qualifierId) {
		this.qualifierId = qualifierId;
	}
	public String getBirthDateYear() {
		return birthDateYear;
	}
	public void setBirthDateYear(String birthDateYear) {
		this.birthDateYear = birthDateYear;
	}
	public String getBirthDateMonth() {
		return birthDateMonth;
	}
	public void setBirthDateMonth(String birthDateMonth) {
		this.birthDateMonth = birthDateMonth;
	}
	public String getBirthDateDay() {
		return birthDateDay;
	}
	public void setBirthDateDay(String birthDateDay) {
		this.birthDateDay = birthDateDay;
	}

	@JsonIgnore
	public Boolean isEmpty() {
		
		return (StringUtils.isEmpty(gender) && StringUtils.isEmpty(birthDateYear) && StringUtils.isEmpty(birthDateMonth))? true:false;
		
	}
}
