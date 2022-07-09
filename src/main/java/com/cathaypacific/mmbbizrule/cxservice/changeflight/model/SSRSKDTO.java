package com.cathaypacific.mmbbizrule.cxservice.changeflight.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class SSRSKDTO implements Serializable {

	private static final long serialVersionUID = 8477370489079333469L;
	@SerializedName("code")
	private String code;
	@SerializedName("description")
	private String description;
	
	/** code to mapping the message **/
	private String msgCode;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	@Override
	public String toString() {
		return "SSRSKDTO [code=" + code + ", description=" + description + ", msgCode=" + msgCode + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SSRSKDTO other = (SSRSKDTO) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (msgCode == null) {
			if (other.msgCode != null)
				return false;
		} else if (!msgCode.equals(other.msgCode))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((msgCode == null) ? 0 : msgCode.hashCode());
		return result;
	}

}
