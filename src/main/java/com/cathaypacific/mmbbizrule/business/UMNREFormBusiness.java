package com.cathaypacific.mmbbizrule.business;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormUpdateRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnrformupdate.UmnrFormUpdateResponseDTO;

public interface UMNREFormBusiness {

	/**
	 * Retrieve UMNR EForm data from PNR
	 * @param oneARloc
	 * @param loginInfo
	 * @return
	 * @throws BusinessBaseException
	 * @throws ParseException
	 */
	public UMNREFormResponseDTO retrieveEFormData(String oneARloc, LoginInfo loginInfo) throws BusinessBaseException, ParseException;
	
	/**
	 * saving UMNR E-form
	 * @param requestDTO
	 * @param loginInfo
	 * @return
	 */
	public UmnrFormUpdateResponseDTO updateUmnrEFormData(UmnrFormUpdateRequestDTO requestDTO, LoginInfo loginInfo) throws UnexpectedException;

	/**
	 * Generate UMNR EForm PDF by RLOC, by PaxId and by JourneyId (JourneyId is defined by umnrEFormBuildService)
	 * @param oneARloc
	 * @param paxId - pax that you want to generate the PDF
	 * @param umnrJourneyId - journey that you want to generate the PDF
	 * @param loginInfo
	 * @return OutputStream
	 * @throws IOException
	 * @throws ParseException
	 * @throws BusinessBaseException
	 */
	public OutputStream generateUMNREFormPDF(String oneARloc, String paxId, String umnrJourneyId, LoginInfo loginInfo) throws IOException, ParseException, BusinessBaseException;
}
