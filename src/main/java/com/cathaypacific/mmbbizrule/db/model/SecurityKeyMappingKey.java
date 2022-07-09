package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class SecurityKeyMappingKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4059870193068774947L;
	
	@NotNull
	@Column(name = "app_code")
	private String appCode;

	@NotNull
	@Column(name = "channel_name")
	private String channelName;
}
