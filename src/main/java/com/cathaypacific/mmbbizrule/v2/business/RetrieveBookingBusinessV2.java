package com.cathaypacific.mmbbizrule.v2.business;

import java.text.ParseException;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ReceivePnrByRlocResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.v2.dto.response.retrievebooking.ReceivePnrByEticketResponseDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.retrievebooking.ReceivePnrByRlocResponseDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.retrievebooking.RefreshBookingResponseDTOV2;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;

public interface RetrieveBookingBusinessV2 {

	/**
	 * Get booking from 1A by Eticket, only 1A booking can be retrieve by eticket.
	 * @param familyName
	 * @param givenName
	 * @param eticket
	 * @param isKoreaLocaleSite
	 * @param mmbToken
	 * @param required
	 * @return
	 * @throws BusinessBaseException
	 */
	public ReceivePnrByEticketResponseDTOV2 bookingLoginByEticket(String familyName, String givenName, String eticket, String mmbToken, BookingBuildRequired required) 
			throws BusinessBaseException;
 
	
	/**
	 * @param familyName
	 * @param givenName
	 * @param rloc
	 * @return
	 * @throws BusinessBaseException
	 */
	public RefreshBookingResponseDTOV2 refreshBooking(LoginInfo loginInfo, String rloc, BookingBuildRequired required) throws BusinessBaseException;

	
	/**
	 * Get booking(s) by booking Reference, this method support OJ booking(hotel booking) and flight Booking(1A booking) 
	 * @param rloc
	 * @param familyName
	 * @param givenName
	 * @param mmbToken
	 * @param required
	 * @return
	 * @throws BusinessBaseException
	 * @throws Exception 
	 */
	public ReceivePnrByRlocResponseDTOV2 bookingLoginByReference(String rloc, String familyName, String givenName, String mmbToken, BookingBuildRequired required)
			throws BusinessBaseException;
	
	/**
	 * Get Booking by login info, only support flight booking in this method
	 * @param loginInfo
	 * @param rloc
	 * @param required
	 * @return
	 * @throws BusinessBaseException
	 */
	public Booking retrieveFlightBooking(LoginInfo loginInfo, String rloc, BookingBuildRequired required) throws BusinessBaseException;

	/**
	 * get booking by PNR
	 * @param familyName
	 * @param givenName
	 * @param eticket
	 * @param isKoreaLocaleSite
	 * @param mmbToken
	 * @param required
	 * @return ReceivePnrByRlocResponseDTOV2
	 * @throws BusinessBaseException
	 * @throws ParseException 
	 */
	ReceivePnrByRlocResponseDTOV2 bookingByPNR(PNRReply pnr, String rloc, String familyName, String givenName,
			String unAuthMbToken, BookingBuildRequired required) throws BusinessBaseException;
	
}
