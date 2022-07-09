package com.cathaypacific.mmbbizrule.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.mask.MaskFieldName;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.common.MaskInfo;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.TicketEmailBusiness;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.request.email.PaxEmailInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.SendEmailRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.email.SendEmailInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.email.SendEmailReponseDTO;
import com.cathaypacific.mmbbizrule.handler.MaskHelper;
import com.cathaypacific.mmbbizrule.handler.ValidateHelper;
import com.cathaypacific.mmbbizrule.oneaservice.email.EmailRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.email.EmailResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.email.model.EmailResponse;
import com.cathaypacific.mmbbizrule.oneaservice.email.service.EmailInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.session.service.PnrSessionInvokService;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.cryptic_07_3_1a.CommandCryptic;
import com.cathaypacific.oneaconsumer.model.response.cryptic_07_3_1a.CommandCrypticReply;


@Service
public class TicketEmailBusinessImpl implements TicketEmailBusiness {
	
	private static LogAgent logger = LogAgent.getLogAgent(TicketEmailBusinessImpl.class);
	
	@Autowired
	PnrInvokeService pnrInvokeService;
	
	@Autowired
	PnrSessionInvokService pnrSessionInvokService;
	
	@Autowired
	EmailInvokeService emailInvokeService;
	
	@Autowired
	MaskHelper maskHelper;
	
	@Autowired
	private ValidateHelper validateHelper;
	
	@Override
	public SendEmailReponseDTO sendTicketEmail(SendEmailRequestDTO requestDTO, LoginInfo loginInfo) throws BusinessBaseException {
		SendEmailReponseDTO reponse = new SendEmailReponseDTO();
		List<SendEmailInfoDTO> passengerInfos = new ArrayList<>();
		Map<String,String> originEmailMap = new HashMap<>();
		String rloc = requestDTO.getRloc();
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		List<RetrievePnrPassenger> pnrPassengers = pnrBooking.getPassengers();
		List<RetrievePnrPassengerSegment> pnrPassengerSegments = pnrBooking.getPassengerSegments();
		unmaskEmail(requestDTO, originEmailMap, rloc,pnrPassengers);
		List<ErrorInfo> errors = validateMaskFields(requestDTO);
		// if there's validation error, return the error
		if (!CollectionUtils.isEmpty(errors)) {
			reponse.setErrors(errors);
			return reponse;
		}
		// open session
		Session session = pnrSessionInvokService.openSessionOnly(rloc);
		
		List<PaxEmailInfoDTO> passengerEmails = requestDTO.getPassengerEmails();
		for(int i = 0; i < passengerEmails.size(); i++) {
			PaxEmailInfoDTO passengerEmail = passengerEmails.get(i);

			SendEmailInfoDTO sendEmailInfoDTO = new SendEmailInfoDTO();
			sendEmailInfoDTO.setPassengerId(passengerEmail.getPassengerId());
			sendEmailInfoDTO.setEmail(originEmailMap.get(passengerEmail.getPassengerId()));
			
			RetrievePnrPassenger pnrPassenger = PnrResponseParser.getPassengerById(pnrPassengers, passengerEmail.getPassengerId());
			if(pnrPassenger != null) {
				sendEmailInfoDTO.setFamilyName(pnrPassenger.getFamilyName());
				sendEmailInfoDTO.setGivenName(pnrPassenger.getGivenName());
			} else {
				sendEmailInfoDTO.setFamilyName(passengerEmail.getFamilyName());
				sendEmailInfoDTO.setGivenName(passengerEmail.getGivenName());
			}
			
			List<String> lineNumbers = getLineNumbers(pnrPassengerSegments, pnrPassengers, passengerEmail.getPassengerId());
			for(int j = 0; j < lineNumbers.size(); j++) {
				// set session status -> End for the final call in transaction
				if(i == passengerEmails.size()-1 && j == lineNumbers.size()-1) {
					session.setStatus(SessionStatus.END.getStatus());
				}
				// send email call
				send(passengerEmail.getEmail(), lineNumbers.get(j), session, sendEmailInfoDTO, rloc);		
				if(BooleanUtils.isFalse(sendEmailInfoDTO.isSuccess())) {
					break;
				}
			}
			passengerInfos.add(sendEmailInfoDTO);
		}
		
		reponse.setRloc(rloc);
		reponse.setPassengerInfos(passengerInfos);
		return reponse;
	}

	/**
	 * unmask Email
	 * @param requestDTO
	 * @param originEmailMap
	 * @param rloc
	 */
	private void unmaskEmail(SendEmailRequestDTO requestDTO, Map<String, String> originEmailMap, String rloc,List<RetrievePnrPassenger> pnrPassengers) {

		List<MaskInfo> maskInfoList = maskHelper.getMaskInfos(rloc);
		if (BooleanUtils.isTrue(requestDTO.getUseSameEmail())) {
			setToggleOnEmail(requestDTO, originEmailMap, maskInfoList,pnrPassengers);
		} else {
			for (PaxEmailInfoDTO passengerEmail : requestDTO.getPassengerEmails()) {
				String paxId=passengerEmail.getPassengerId();
				originEmailMap.put(paxId, passengerEmail.getEmail());
				String parentId = getParentIdbyPaxId(pnrPassengers,passengerEmail.getPassengerId());
				//get original email for infant use parentId
				if(!StringUtils.isEmpty(parentId)){
					paxId = parentId;
				}
				String originalValue = maskHelper.getOriginalValue(MaskFieldName.EMAIL, passengerEmail.getEmail(),
						paxId, null, maskInfoList);
				if (!StringUtils.isEmpty(originalValue)) {
					passengerEmail.setEmail(originalValue);
				}
			}
		}
	}

	/**
	 * set ToggleOn Email
	 * @param requestDTO
	 * @param originEmailMap
	 * @param rloc
	 */
	private void setToggleOnEmail(SendEmailRequestDTO requestDTO, Map<String, String> originEmailMap, List<MaskInfo> maskInfoList,List<RetrievePnrPassenger> pnrPassengers) {
		String toggleOnEmail = null;

		for (PaxEmailInfoDTO passengerEmail : requestDTO.getPassengerEmails()) {
			String paxId=passengerEmail.getPassengerId();
			String parentId = getParentIdbyPaxId(pnrPassengers,passengerEmail.getPassengerId());
			//get original email for infant use parentId
			if(!StringUtils.isEmpty(parentId)){
				paxId = parentId;
			}
			
			toggleOnEmail = maskHelper.getOriginalValue(MaskFieldName.EMAIL, passengerEmail.getEmail(), paxId, null, maskInfoList);
			if (!StringUtils.isEmpty(toggleOnEmail)) {
				break;
			}
		}

		for (PaxEmailInfoDTO passengerEmail : requestDTO.getPassengerEmails()) {
			originEmailMap.put(passengerEmail.getPassengerId(), passengerEmail.getEmail());
			if (!StringUtils.isEmpty(toggleOnEmail)) {
				passengerEmail.setEmail(toggleOnEmail);
			}
		}

	}

	/**
	 * send email call
	 * @param email
	 * @param lineNumber
	 * @param session
	 * @param sendEmailInfoDTO
	 * @param rloc
	 */
	private void send(String email, String lineNumber, Session session, SendEmailInfoDTO sendEmailInfoDTO, String rloc) {
		EmailRequestBuilder emailRequestBuilder = new EmailRequestBuilder();
		CommandCryptic request = emailRequestBuilder.buildRequest(email, lineNumber);
		try {
			CommandCrypticReply reply = emailInvokeService.sendTicketEmail(request, session);
			EmailResponseParser parser = new EmailResponseParser();
			EmailResponse emailResponse = parser.parser(reply);
			if(emailResponse != null) {
				sendEmailInfoDTO.setMessage(emailResponse.getMessage());					
			}
		} catch(Exception e) {
			sendEmailInfoDTO.setSuccess(false);
			logger.error(String.format("send ticket Email failure, familyName[%s], givenName[%s], lineNumber[%s], passnegerId[%s], rloc[%s]",
					sendEmailInfoDTO.getFamilyName(), sendEmailInfoDTO.getGivenName(), lineNumber, sendEmailInfoDTO.getPassengerId(), rloc), e);
		}
		if(StringUtils.isEmpty(sendEmailInfoDTO.getMessage())
				|| !sendEmailInfoDTO.getMessage().contains("ITINERARY RECEIPT EMAIL SENT")) {
			sendEmailInfoDTO.setSuccess(false);
		}
	}

	/**
	 * get all lineNumbers including adult and his infant
	 * @param pnrPassengerSegments
	 * @param pnrPassengers
	 * @param passengerId
	 * @return List<String>
	 */
	private List<String> getLineNumbers(List<RetrievePnrPassengerSegment> pnrPassengerSegments,
			List<RetrievePnrPassenger> pnrPassengers, String passengerId) {
		List<String> lineNumbers = new ArrayList<>();
		
		List<String> passengerIds = new ArrayList<>();		
		passengerIds.add(passengerId);
		// set infant IDs
		List<String> infantIds = pnrPassengers.stream().filter(pax->passengerId.equals(pax.getParentId())).map(RetrievePnrPassenger::getPassengerID).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(infantIds)) {
			passengerIds.addAll(infantIds);
		}
		
		passengerIds.stream().forEach(id-> {
			String lineNumber = getLineNumberbyPaxId(pnrPassengerSegments, id);
			if(!StringUtils.isEmpty(lineNumber)) {
				lineNumbers.add(lineNumber);
			}
		});
		
		return lineNumbers;
	}

	/**
	 * get E-ticket lineNumber by passengerId
	 * @param pnrPassengerSegments
	 * @param passengerId
	 * @return String
	 */
	private String getLineNumberbyPaxId(List<RetrievePnrPassengerSegment> pnrPassengerSegments, String passengerId) {
		for(RetrievePnrPassengerSegment pnrPassengerSegment : pnrPassengerSegments) {
			if(pnrPassengerSegment == null 
					|| passengerId == null
					|| !passengerId.equals(pnrPassengerSegment.getPassengerId())
					|| CollectionUtils.isEmpty(pnrPassengerSegment.getEtickets())
					|| pnrPassengerSegment.getEtickets().get(0) == null) {
				continue;
			}
			return pnrPassengerSegment.getEtickets().get(0).getLineNumber();
		}
		return null;
	}
	
	/**
	 * validate Mask Fields
	 * @param requestDTO
	 * @throws UnexpectedException 
	 */
	private List<ErrorInfo> validateMaskFields(SendEmailRequestDTO requestDTO) throws UnexpectedException {
		return validateHelper.validate(requestDTO, MaskFieldGroup.class);
	}
	
	/**
	 * get parentId by passengerId
	 * @param pnrPassengers
	 * @param passengerId
	 * @return
	 */
	private String getParentIdbyPaxId(List<RetrievePnrPassenger> pnrPassengers, String passengerId) {
		for(RetrievePnrPassenger pnrPassenger : pnrPassengers) {
			if(pnrPassenger == null 
					|| passengerId == null
					|| !passengerId.equals(pnrPassenger.getPassengerID())
					|| !OneAConstants.PASSENGER_TYPE_INF.equals(pnrPassenger.getPassengerType())
					|| StringUtils.isEmpty(pnrPassenger.getParentId())) {
				continue;
			}
			return pnrPassenger.getParentId();
		}
		return null;
	}
}
