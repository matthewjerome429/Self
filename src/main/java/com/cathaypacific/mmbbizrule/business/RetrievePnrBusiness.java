package com.cathaypacific.mmbbizrule.business;

import java.text.ParseException;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.consent.ConsentAddRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentCommonRecordResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ConsentInfoRecordResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ReceivePnrByEticketResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ReceivePnrByRlocResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.RefreshBookingResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;

public interface RetrievePnrBusiness {

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
	public ReceivePnrByEticketResponseDTO bookingLoginByEticket(String familyName, String givenName, String eticket, String mmbToken, BookingBuildRequired required) 
			throws BusinessBaseException;
	
	/**
	 * @param familyName
	 * @param givenName
	 * @param eticket
	 * @param isKoreaLocaleSite
	 * @param mmbToken
	 * @param required
	 * @return
	 * @throws BusinessBaseException
	 * @throws ParseException 
	 */
	public ReceivePnrByRlocResponseDTO bookingByPNR(PNRReply pnr,String rloc, String familyName, String givenName,
			String unAuthMbToken, BookingBuildRequired required) throws BusinessBaseException;

	
	/**
	 * @param familyName
	 * @param givenName
	 * @param rloc
	 * @return
	 * @throws BusinessBaseException
	 * @throws ParseException 
	 */
	public RefreshBookingResponseDTO refreshBooking(LoginInfo loginInfo, String rloc, BookingBuildRequired required) throws BusinessBaseException, ParseException;

	
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
	public ReceivePnrByRlocResponseDTO bookingLoginByReference(String rloc, String familyName, String givenName, String mmbToken, BookingBuildRequired required)
			throws BusinessBaseException;
	
	
	/**
	 * consent Info Record
	 * @param loginInfo
	 * @param rloc
	 * @param requireConsentInfoRecord
	 * @return
	 * @throws BusinessBaseException
	 */
	public ConsentInfoRecordResponseDTO consentInfoRecord(LoginInfo loginInfo, String rloc, String acceptLanguage) throws BusinessBaseException;
	
	/**
	 * Get Booking by login info, only support flight booking in this method
	 * @param loginInfo
	 * @param rloc
	 * @param required
	 * @return
	 * @throws BusinessBaseException
	 */
	public Booking retrieveFlightBooking(LoginInfo loginInfo, String rloc, BookingBuildRequired required) throws BusinessBaseException;

	public ConsentCommonRecordResponseDTO saveConsentCommon(ConsentAddRequestDTO requestDTO,LoginInfo loginInfo, String rloc, String acceptLanguage)
			throws BusinessBaseException;
	
}
