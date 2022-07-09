package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

public class DobDTO implements Serializable{
	
	private static final long serialVersionUID = 212768286423351416L;
	
	private String birthDateYear;
	
	private String birthDateMonth;
	
    private String birthDateDay;
    
    private Boolean dayMasked;
    
    private Boolean yearMasked;

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

	public Boolean getDayMasked() {
		return dayMasked;
	}

	public void setDayMasked(Boolean dayMasked) {
		this.dayMasked = dayMasked;
	}

	public Boolean getYearMasked() {
		return yearMasked;
	}

	public void setYearMasked(Boolean yearMasked) {
		this.yearMasked = yearMasked;
	}
}
