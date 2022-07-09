package com.cathaypacific.mbcommon.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;

@ApiModel
public class Updated implements Serializable{

	private static final long serialVersionUID = -7178315815603400663L;
	
	public boolean isBlank() {
		return false;
	}
	
}
