package com.cathaypacific.mmbbizrule.model.umnreform;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class UMNREFormRemark {
	
	private String passengerId;
	
	private String age;
	
	private String gender;
	
	private UMNREFormAddressRemark address;
	
	private UMNREFormGuardianInfoRemark parentInfo;
	
	private List<UMNREFormSegmentRemark> segments;
	
	private List<String> otQualifierList;

	public String getPassengerId() {
		return passengerId;
	}
	
	public String getPassengerIdDigit() {
		String passengerIdDigit = null;
		if (StringUtils.isNotEmpty(passengerId)) {
			Pattern pattern = Pattern.compile("(\\d+)\\D*$");
			Matcher matcher = pattern.matcher(passengerId);
			if (matcher.find()){
				passengerIdDigit = matcher.group(1);
			}
		}
		return passengerIdDigit;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public UMNREFormAddressRemark getAddress() {
		if (address == null) {
			address = new UMNREFormAddressRemark();
		}
		return address;
	}

	public void setAddress(UMNREFormAddressRemark address) {
		this.address = address;
	}

	public List<UMNREFormSegmentRemark> getSegments() {
		return segments;
	}

	public void setSegments(List<UMNREFormSegmentRemark> segments) {
		this.segments = segments;
	}

	public List<String> getOtQualifierList() {
		return otQualifierList;
	}

	public void setOtQualifierList(List<String> otQualifierList) {
		this.otQualifierList = otQualifierList;
	}

	public UMNREFormGuardianInfoRemark getParentInfo() {
		if (parentInfo == null) {
			parentInfo = new UMNREFormGuardianInfoRemark();
		}
		return parentInfo;
	}

	public void setParentInfo(UMNREFormGuardianInfoRemark parentInfo) {
		this.parentInfo = parentInfo;
	}
	
	
}
