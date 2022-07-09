package com.cathaypacific.mmbbizrule.aem.model;

import java.io.Serializable;
import java.util.List;

public class SelectorOptions implements Serializable {

	private static final long serialVersionUID = 5199659614160037223L;

	private List<SelectorOption> selectorOption;

	public List<SelectorOption> getSelectorOption() {
		return selectorOption;
	}

	public void setSelectorOption(List<SelectorOption> selectorOption) {
		this.selectorOption = selectorOption;
	}
	
}
