package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

public class DocumentDTO implements Serializable{
	
	private static final long serialVersionUID = 1819840730773769477L;

	private String type;
	
	private String format;
	
	private String url;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
