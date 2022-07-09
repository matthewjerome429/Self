package com.cathaypacific.mmbbizrule.model.detail.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.AirportUpgradeInfo;

public class AirportUpgradeInfoMocker {

	private AirportUpgradeInfoMocker() {

	}

	public static interface AirportUpgradeInfoPropMocker extends Consumer<AirportUpgradeInfo> {

	}

	public static AirportUpgradeInfo mock(AirportUpgradeInfoPropMocker... propMockers) {
		AirportUpgradeInfo obj = new AirportUpgradeInfo();

		for (AirportUpgradeInfoPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static AirportUpgradeInfoPropMocker newCabinClass(String newCabinClass) {
		return airportUpgradeInfo -> airportUpgradeInfo.setNewCabinClass(newCabinClass);
	}

}
