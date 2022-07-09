package com.cathaypacific.mmbbizrule.aem.model;

import java.io.Serializable;

public class Country implements Serializable {

	private static final long serialVersionUID = -2638040804160677619L;
	
	private String code;

	private String defaultName;
	
	private String name;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDefaultName() {
		return defaultName;
	}
	
	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
