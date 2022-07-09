package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class AdditionalOperatorInfoKey implements Serializable {
	
	public AdditionalOperatorInfoKey() {
		super();
	}
	
	public AdditionalOperatorInfoKey(String appCode, String operatorName) {
		super();
		this.appCode = appCode;
		this.operatorName = operatorName;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2636198689978108484L;
	
	@NotNull
    @Column(name="app_code", length = 5)
    private String appCode;
	
    @NotNull
    @Column(name = "operator_name")
    private String operatorName;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appCode == null) ? 0 : appCode.hashCode());
		result = prime * result + ((operatorName == null) ? 0 : operatorName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdditionalOperatorInfoKey other = (AdditionalOperatorInfoKey) obj;
		if (appCode == null) {
			if (other.appCode != null)
				return false;
		} else if (!appCode.equals(other.appCode))
			return false;
		if (operatorName == null) {
			if (other.operatorName != null)
				return false;
		} else if (!operatorName.equals(other.operatorName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AdditionalOperatorInfoKey [appCode=" + appCode + ", operatorName=" + operatorName + "]";
	}


}
