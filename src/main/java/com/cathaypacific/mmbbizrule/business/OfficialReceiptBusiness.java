package com.cathaypacific.mmbbizrule.business;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.response.officialreceipt.OfficialReceiptResponseDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;

public interface OfficialReceiptBusiness {
	
	/**
	 * Get official receipt passenger informations
	 * 
	 * @param oneARloc
	 * @param loginInfo
	 * @return
	 * @throws BusinessBaseException
	 */
	public OfficialReceiptResponseDTO retrievePassengers(String oneARloc, LoginInfo loginInfo) throws BusinessBaseException;
	
	/**
	 * Generate official receipt PDF
	 * 
	 * @param pnrBooking
	 * @param passengerId
	 * @param loginInfo
	 * @param locale
	 * @return
	 * @throws BusinessBaseException
	 */
	public OutputStream generateOfficialReceiptPDF(RetrievePnrBooking pnrBooking, String passengerId, LoginInfo loginInfo, Locale locale) 
			throws BusinessBaseException, IOException;
	
	/**
	 * Generate official receipt filename
	 * 
	 * @param pnrBooking
	 * @param passengerId
	 * @param locale 
	 * @return
	 * @throws BusinessBaseException
	 */
	public String generateOfficialReceiptFileName(RetrievePnrBooking pnrBooking, String passengerId, Locale locale) throws BusinessBaseException;
		
}
