package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties;

import java.util.Arrays;
import java.util.function.Consumer;

public class BookingPropertiesDTOMocker {

	private BookingPropertiesDTOMocker() {

	}

	public static interface BookingPropertiesDTOPropMocker extends Consumer<BookingCommercePropertiesDTO> {

	}

	public static BookingCommercePropertiesDTO mock(BookingPropertiesDTOPropMocker... propMockers) {
		BookingCommercePropertiesDTO obj = new BookingCommercePropertiesDTO();

		for (BookingPropertiesDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static BookingPropertiesDTOPropMocker staffBooking(Boolean staffBooking) {
		return bookingPropDTO -> bookingPropDTO.setStaffBooking(staffBooking);
	}

	public static BookingPropertiesDTOPropMocker groupBooking(Boolean groupBooking) {
		return bookingPropDTO -> bookingPropDTO.setGroupBooking(groupBooking);
	}

	public static BookingPropertiesDTOPropMocker passengerProperties(PassengerPropertiesDTO... passengerPropDTOs) {
		return bookingPropDTO -> bookingPropDTO.setPassengerProperties(Arrays.asList(passengerPropDTOs));
	}

	public static BookingPropertiesDTOPropMocker segmentProperties(SegmentPropertiesDTO... segmentPropDTOs) {
		return bookingPropDTO -> bookingPropDTO.setSegmentProperties(Arrays.asList(segmentPropDTOs));
	}

	public static BookingPropertiesDTOPropMocker passengerSegmentProperties(
			PassengerSegmentPropertiesDTO... passengerSegmentPropDTOs) {
		return bookingPropDTO -> bookingPropDTO.setPassengerSegmentProperties(Arrays.asList(passengerSegmentPropDTOs));
	}

}
