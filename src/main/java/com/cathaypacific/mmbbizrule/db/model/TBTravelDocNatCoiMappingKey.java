package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Embeddable
public class TBTravelDocNatCoiMappingKey implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@NotNull
	@Column(name = "APP_CODE", length = 5)
	private String appCode;
	
	@Id
	@NotNull
	@Column(name = "DOC_TYPE", length = 4)
	private String docType;
	
	@Id
	@NotNull
	@Column(name = "NAT", length = 3)
	private String nat;
	
	@Id
	@NotNull
	@Column(name = "COI", length = 3)
	private String coi;
	
	public TBTravelDocNatCoiMappingKey() {
		// Empty constructor.
	}
	
	public TBTravelDocNatCoiMappingKey(String appCode, String docType, String nat, String coi) {
		this.appCode = appCode;
		this.docType = docType;
		this.nat = nat;
		this.coi = coi;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getNat() {
		return nat;
	}

	public void setNat(String nat) {
		this.nat = nat;
	}

	public String getCoi() {
		return coi;
	}

	public void setCoi(String coi) {
		this.coi = coi;
	}


}
