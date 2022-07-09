package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties;

import java.util.function.Consumer;

public class PassengerSegmentPropertiesDTOMocker {

	private PassengerSegmentPropertiesDTOMocker() {

	}

	public static interface PassengerSegmentPropertiesDTOPropMocker extends Consumer<PassengerSegmentPropertiesDTO> {

	}

	public static PassengerSegmentPropertiesDTO mock(PassengerSegmentPropertiesDTOPropMocker... propMockers) {
		PassengerSegmentPropertiesDTO obj = new PassengerSegmentPropertiesDTO();

		for (PassengerSegmentPropertiesDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static PassengerSegmentPropertiesDTOPropMocker passengerSegmentId(String passengerId, String segmentId) {
		return psPropDTO -> {
			psPropDTO.setPassengerId(passengerId);
			psPropDTO.setSegmentId(segmentId);
		};
	}

	public static PassengerSegmentPropertiesDTOPropMocker eticketIssued(Boolean eticketIssued) {
		return psPropDTO -> psPropDTO.setEticketIssued(eticketIssued);
	}

	public static PassengerSegmentPropertiesDTOPropMocker cxKaET(Boolean cxKaET) {
		return psPropDTO -> psPropDTO.setCxKaET(cxKaET);
	}

	public static PassengerSegmentPropertiesDTOPropMocker haveWaiverBaggage(Boolean haveWaiverBaggage) {
		return psPropDTO -> psPropDTO.setHaveWaiverBaggage(haveWaiverBaggage);
	}

}
