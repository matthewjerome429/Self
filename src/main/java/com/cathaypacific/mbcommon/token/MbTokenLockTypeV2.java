package com.cathaypacific.mbcommon.token;

public enum MbTokenLockTypeV2 {

	LOCK("lock"),

	READ_LOCK("read lock"),

	WRITE_LOCK("write lock");

	private final String name;

	String getName() {
		return name;
	}

	MbTokenLockTypeV2(String name) {
		this.name = name;
	}

}
