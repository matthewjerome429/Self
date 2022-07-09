package com.cathaypacific.mmbbizrule.aem.model;

import java.io.Serializable;

public class SelectorOption implements Serializable {

	private static final long serialVersionUID = 5199659614160037223L;

	private String key;

	private String description;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
