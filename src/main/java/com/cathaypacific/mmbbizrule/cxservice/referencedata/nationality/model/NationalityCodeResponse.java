package com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.model;

import java.io.Serializable;
import java.util.List;

public class NationalityCodeResponse implements Serializable{
	 
	private static final long serialVersionUID = 4673186153813605228L;
	
	private List<NationalityCode> nationalityCode;

	public List<NationalityCode> getNationalityCode() {
		return nationalityCode;
	}

	public void setNationalityCode(List<NationalityCode> nationalityCode) {
		this.nationalityCode = nationalityCode;
	}

}
