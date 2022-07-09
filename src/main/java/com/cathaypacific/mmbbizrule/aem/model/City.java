package com.cathaypacific.mmbbizrule.aem.model;

import java.io.Serializable;

public class City implements Serializable {
	
	private static final long serialVersionUID = -1509650941204160895L;

	private String code;
	
	private String defaultName;
	
	private String image;
	
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
