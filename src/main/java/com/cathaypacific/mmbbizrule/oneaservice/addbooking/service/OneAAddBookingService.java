package com.cathaypacific.mmbbizrule.oneaservice.addbooking.service;

import java.util.List;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;

public interface OneAAddBookingService {
	/**
	 * add booking by adding FQTV
	 * @param rloc
	 * @param passengerId
	 * @param segmentId
	 */
	public void addBookingByFQTV(String rloc, String passengerId, List<String> segmentIds, String memberId, RetrievePnrBooking booking) throws BusinessBaseException;
	
	/**
	 * add booking by adding SK
	 * @param rloc
	 * @param memberId
	 */
	public void addBookingBySK(String rloc, String memberId, List<String> companyIds) throws BusinessBaseException;
}
