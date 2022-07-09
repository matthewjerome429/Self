package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(CabinClassPK.class)
@Table(name = "tb_cabin_class")
public class CabinClass implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -423106419562591147L;

	@Id
	@Column(name = "app_code")
	private String appCode;
	
	@Id
	@Column(name = "basic_class")
	private String basicClass;
	
	@Id
	@Column(name = "subclass")
	private String subclass;
	
	@Id
	@Column(name = "description")
	private String description;
	
	@Column(name = "last_update_source")
	private String lastUpdateSource;
	
	@Column(name = "last_update_timestamp")
	private Date lastUpdateTimestamp;

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

	public String getLastUpdateSource() {
		return lastUpdateSource;
	}

	public void setLastUpdateSource(String lastUpdateSource) {
		this.lastUpdateSource = lastUpdateSource;
	}

	public Date getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
