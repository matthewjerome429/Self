package com.cathaypacific.mmbbizrule.cxservice.aep.mocker.aep_1_1;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPPassengerWithName;

public class AEPPassengerWithNameMocker {

	private AEPPassengerWithNameMocker() {

	}

	public static interface AEPPassengerWithNamePropMocker extends Consumer<AEPPassengerWithName> {

	}

	public static AEPPassengerWithName mock(AEPPassengerWithNamePropMocker... propMockers) {
		AEPPassengerWithName obj = new AEPPassengerWithName();

		for (AEPPassengerWithNamePropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static AEPPassengerWithNamePropMocker passengerRef(Integer passengerRef) {
		return aepPassenger -> aepPassenger.setPassengerRef(passengerRef);
	}

	public static AEPPassengerWithNamePropMocker name(String firstName, String lastName) {
		return aepPassenger -> {
			aepPassenger.setFirstName(firstName);
			aepPassenger.setLastName(lastName);
		};
	}

}
