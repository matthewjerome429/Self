package com.cathaypacific.mbcommon.applicationcache;

public enum ApplicationCacheKeyEnum {
	
	TOKEN_DATA("TOKEN_DATA");

	private String key;
	
	private ApplicationCacheKeyEnum(String key) {
		this.key = key;
	}
	public String getKey() {
		return key;
	}
}
