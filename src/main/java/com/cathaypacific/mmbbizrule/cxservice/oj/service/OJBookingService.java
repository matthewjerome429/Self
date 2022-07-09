package com.cathaypacific.mmbbizrule.cxservice.oj.service;

import java.util.List;
import java.util.concurrent.Future;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJBooking;
import com.cathaypacific.mmbbizrule.model.booking.summary.BookingSummary;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;

public interface OJBookingService {
	
	public Future<BookingSummary> asyncGetBookingSummary(String firstName, String surName, String reference) throws BusinessBaseException;

	public OJBooking getBooking(String firstName, String surName, String reference) throws BusinessBaseException;
	
	public Future<OJBooking> asyncGetBooking(String firstName, String surName, String reference) throws BusinessBaseException;
	
	public List<BookingSummary> getOjBookingList(ProfilePersonInfo profilePersonInfo, String mbToken) throws BusinessBaseException;
	
	public Future<List<BookingSummary>> asynGetOjBookingList(ProfilePersonInfo profilePersonInfo, String mbToken) throws BusinessBaseException;
}
