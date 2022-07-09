package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;

public class CabinClassPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5603388449876322417L;

	@Column(name = "app_code")
	private String appCode;
	
	@Column(name = "basic_class")
	private String basicClass;
	
	@Column(name = "subclass")
	private String subclass;
	
	@Column(name = "description")
	private String description;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getBasicClass() {
		return basicClass;
	}

	public void setBasicClass(String basicClass) {
		this.basicClass = basicClass;
	}

	public String getSubclass() {
		return subclass;
	}

	public void setSubclass(String subclass) {
		this.subclass = subclass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
