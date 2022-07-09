package com.cathaypacific.mmbbizrule.cxservice.oj.model;

import java.io.Serializable;

public class NameInput implements Serializable {
	
	private static final long serialVersionUID = 4257398585947976901L;

	private String firstNameInput;
	
	private String lastNameInput;
	
	private String title;

	public String getFirstNameInput() {
		return firstNameInput;
	}

	public void setFirstNameInput(String firstNameInput) {
		this.firstNameInput = firstNameInput;
	}

	public String getLastNameInput() {
		return lastNameInput;
	}

	public void setLastNameInput(String lastNameInput) {
		this.lastNameInput = lastNameInput;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
