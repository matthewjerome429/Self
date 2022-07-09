package com.cathaypacific.mmbbizrule.cxservice.olci.model;

public enum CheckInChannel {
	EXT, CRY, JFE, KSK, MOB, SMS, TEL, WEB, EFE;

	public static boolean contains(String s) {
		for (CheckInChannel channel : values()) {
			if (channel.name().equals(s)) {
				return true;
			}
		}
		return false;
	}
}
