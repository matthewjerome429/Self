package com.cathaypacific.mmbbizrule.cxservice.olci.model;

public enum DeliveryStatus {
	CDC, CND, CUD;

	public static boolean contains(String s) {
		for (DeliveryStatus status : values()) {
			if (status.name().equals(s)) {
				return true;
			}
		}
		return false;
	}
}
