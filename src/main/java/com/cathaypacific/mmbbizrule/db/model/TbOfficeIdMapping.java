package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(TbOfficeIdMappingKey.class)
@Table(name = "tb_officeid_mapping")
public class TbOfficeIdMapping implements Serializable {
	
	private static final long serialVersionUID = 6465648224589320806L;

	@Id
	@Column(name = "app_code")
	private String appCode;
	
	@Id
	@Column(name = "type")
	private String type;
	
	@Id
	@Column(name = "value")
	private String value;
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setValue(String value) {
		this.value = value;
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
	
}
