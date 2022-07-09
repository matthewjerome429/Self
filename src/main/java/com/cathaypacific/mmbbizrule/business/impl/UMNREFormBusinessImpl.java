package com.cathaypacific.mmbbizrule.business.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.business.UMNREFormBusiness;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormUpdateRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormJourneyDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormPassengerDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnrformupdate.UmnrFormUpdateResponseDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.service.UMNREFormBuildService;
import com.cathaypacific.mmbbizrule.service.UMNREFormPDFService;
import com.cathaypacific.mmbbizrule.service.UMNREFormUpdateService;


@Service
public class UMNREFormBusinessImpl implements UMNREFormBusiness {
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private UMNREFormBuildService umnrEFormBuildService;
	
	@Autowired
	private UMNREFormPDFService umnrEFormPDFService;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;

	@Autowired
	private AEMService aemService;
	
	@Autowired
	private UMNREFormUpdateService umnreFormUpdateService;	
	
	private static LogAgent logger = LogAgent.getLogAgent(UMNREFormBusinessImpl.class);

	@Override
	public UMNREFormResponseDTO retrieveEFormData(String oneARloc, LoginInfo loginInfo) throws BusinessBaseException, ParseException {
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(oneARloc);
		
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);

		return umnrEFormBuildService.buildUMNREForm(pnrBooking);
	}
	
	@Override
	public OutputStream generateUMNREFormPDF(String oneARloc, String paxId, String umnrJourneyId, LoginInfo loginInfo) throws BusinessBaseException, ParseException, IOException {
		// UMNR EForm
		UMNREFormResponseDTO umnrEFormDTO = retrieveEFormData(oneARloc, loginInfo);
		
		// UMNR passenger
		UMNREFormPassengerDTO umnrPassenger = umnrEFormDTO.getPassengers().stream().filter(
				pax -> StringUtils.equalsIgnoreCase(pax.getPassengerId(), paxId)
		).findFirst().orElse(null);
		if (umnrPassenger == null) {
			throw new UnexpectedException(
				String.format("Passenger Id %s doesn't exist in umnrEFormDTO. UMNR EForm PDF won't be generated.", paxId),
				new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW)
			);
		}
		
		// UMNR Journey
		UMNREFormJourneyDTO umnrJourney = umnrPassenger.getUmnrEFormJourneys().stream().filter(
			journey -> StringUtils.equalsIgnoreCase(journey.getJourneyId(), umnrJourneyId)
		).findFirst().orElse(null);
		if (umnrJourney == null) {
			throw new UnexpectedException(
				String.format("Journey Id %s doesn't exist in umnrEFormDTO. UMNR EForm PDF won't be generated.", umnrJourneyId),
				new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW)
			);
		}
		
		// UMNR segments
		List<UMNREFormSegmentDTO> umnrSegments = umnrEFormDTO.getSegments();

		// PDF Template
		InputStream pdfTemplate = aemService.getUMNREFormPDFTemplate();
		
		// Build PDF by journey
		OutputStream pdf = umnrEFormPDFService.buildPDF(pdfTemplate, getRlocByPriority(umnrEFormDTO), umnrSegments, umnrPassenger, umnrJourney);

		// Close
		pdfTemplate.close();

		// OLSSMMB-16763: Don't remove me!!!
		// --- begin ---
		logger.info(String.format("UMNR | E Form | PDF Export | Rloc | %s | Journey ID | %s | Passenger ID | %s", oneARloc, umnrJourneyId, paxId), true);
		// --- end ---

		return pdf;
	}

	@Override
	public UmnrFormUpdateResponseDTO updateUmnrEFormData(UmnrFormUpdateRequestDTO requestDTO, LoginInfo loginInfo) throws UnexpectedException{
		UmnrFormUpdateResponseDTO response = new UmnrFormUpdateResponseDTO();
		try {
			response.setSuccess(umnreFormUpdateService.updateUMNREForm(requestDTO));
		} catch (BusinessBaseException e) {
			logger.error(String.format("UMNREFormBusinessImpl.updateUmnrEFormData -> failure when update booking[%s] UMNR form data", requestDTO.getRloc()), e);
			throw new UnexpectedException("addRmElements failure when update UMNR form date", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		return response;
	}
	
	private String getRlocByPriority(UMNREFormResponseDTO umnreFormResponseDTO) {
		if (!StringUtils.isEmpty(umnreFormResponseDTO.getOjRloc())) {
			return umnreFormResponseDTO.getOjRloc();
		} else if (!StringUtils.isEmpty(umnreFormResponseDTO.getOneARloc())) {
			return umnreFormResponseDTO.getOneARloc();
		} else if (!StringUtils.isEmpty(umnreFormResponseDTO.getGdsRloc())) {
			return umnreFormResponseDTO.getGdsRloc();
		}
		return null;
	}
}

