package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties;

import java.util.function.Consumer;

public class PassengerPropertiesDTOMocker {

	private PassengerPropertiesDTOMocker() {

	}

	public static interface PassengerPropertiesDTOPropMocker extends Consumer<PassengerPropertiesDTO> {

	}

	public static PassengerPropertiesDTO mock(PassengerPropertiesDTOPropMocker... propMockers) {
		PassengerPropertiesDTO obj = new PassengerPropertiesDTO();

		for (PassengerPropertiesDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static PassengerPropertiesDTOPropMocker passengerId(String passengerId) {
		return passengerPropDTO -> passengerPropDTO.setPassengerId(passengerId);
	}

	public static PassengerPropertiesDTOPropMocker infant(Boolean infant) {
		return passengerPropDTO -> passengerPropDTO.setInfant(infant);
	}

}
