package com.cathaypacific.mmbbizrule.model.detail.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;

public class FQTVInfoMocker {

	private FQTVInfoMocker() {

	}

	public static interface FQTVInfoPropMocker extends Consumer<FQTVInfo> {

	}

	public static FQTVInfo mock(FQTVInfoPropMocker... propMockers) {
		FQTVInfo obj = new FQTVInfo();

		for (FQTVInfoPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static FQTVInfoPropMocker tierLevel(String tierLevel) {
		return fqtvInfo -> fqtvInfo.setTierLevel(tierLevel);
	}
	
}
