package com.cathaypacific.mmbbizrule.service;

import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingCommercePropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingPropertiesDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;

public interface BookingPropertiesBuildService {

	public BookingPropertiesDTO buildBookingProperties(Booking booking);
	
	public BookingCommercePropertiesDTO buildBookingCommerceProperties(Booking booking);
}
