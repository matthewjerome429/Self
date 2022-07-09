package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class AttributeDetails implements Serializable {

	private static final long serialVersionUID = -990885568124514382L;

	private String attributeType;

	private String attributeDescription;

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	public String getAttributeDescription() {
		return attributeDescription;
	}

	public void setAttributeDescription(String attributeDescription) {
		this.attributeDescription = attributeDescription;
	}
}
