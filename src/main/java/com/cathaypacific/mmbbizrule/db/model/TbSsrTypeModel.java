package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(TbSsrTypeModelPK.class)
@Table(name = "tb_ssr_type")
public class TbSsrTypeModel implements Serializable{
	
	private static final long serialVersionUID = -3821889417731601117L;

	@Id
	@Column(name = "app_code")
	private String appCode;
	
	@Id
	@Column(name = "type")
	private String type;
	
	@Id
	@Column(name = "value")
	private String value;
	
	@Id
	@Column(name = "action")
	private String action;
	
	@Column(name = "last_update_source")
	private String lastUpdateSource;
	
	@Column(name = "last_update_datetime")
	private Date lastUpdateDatetime;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getType() {
		return type;
	}

	public void setTyp(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getLastUpdateSource() {
		return lastUpdateSource;
	}

	public void setLastUpdateSource(String lastUpdateSource) {
		this.lastUpdateSource = lastUpdateSource;
	}

	public Date getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Date lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

}
