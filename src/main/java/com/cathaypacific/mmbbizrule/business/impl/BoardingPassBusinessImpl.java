package com.cathaypacific.mmbbizrule.business.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.mask.MaskFieldName;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.common.MaskInfo;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.LanguageUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.BoardingPassBusiness;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.constant.ContactType;
import com.cathaypacific.mmbbizrule.cxservice.olci.service.SentBPService;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.EmailAddressDTOV2;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.PhoneNumberDTOV2;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.PhoneNumbersDTOV2;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.SPBPRequestDTOV2;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.SendEmailRequestDTOV2;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.SendSMSRequestDTOV2;
import com.cathaypacific.mmbbizrule.dto.response.boardingpass.spbp.SPBPResponseDTOV2;
import com.cathaypacific.mmbbizrule.dto.response.boardingpass.spbp.SendEmailResponseDTOV2;
import com.cathaypacific.mmbbizrule.dto.response.boardingpass.spbp.SendSMSResponseDTOV2;
import com.cathaypacific.mmbbizrule.handler.BookingBuildHelper;
import com.cathaypacific.mmbbizrule.handler.MaskHelper;
import com.cathaypacific.mmbbizrule.handler.PnrCprMergeHelper;
import com.cathaypacific.mmbbizrule.handler.ValidateHelper;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;
import com.cathaypacific.olciconsumer.model.request.boardingpass.PhoneNumberDTO;
import com.cathaypacific.olciconsumer.model.request.boardingpass.SendEmailRequestDTO;
import com.cathaypacific.olciconsumer.model.request.boardingpass.SendSMSRequestDTO;
import com.cathaypacific.olciconsumer.model.response.BaseResponseDTO;
import com.cathaypacific.olciconsumer.model.response.boardingpass.BoardingPassResponseDTO;
import com.cathaypacific.olciconsumer.model.response.boardingpass.SendEmailResponseDTO;
import com.cathaypacific.olciconsumer.model.response.boardingpass.SendSMSResponseDTO;

@Service
public class BoardingPassBusinessImpl implements BoardingPassBusiness {
	
	private static LogAgent logger = LogAgent.getLogAgent(BoardingPassBusinessImpl.class);
	
	private static String sessionRegex = "OLCI_JSESSIONID=(.*?);";
	
	private static String emailRegex = "^([\\w\\.\\-\\_])+@(([\\w\\-\\_])+\\.)+(\\w)+$";
	
	@Autowired
	private SentBPService sentBPService;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private MaskHelper maskHelper;
	
	@Autowired
	private BookingBuildHelper bookingBuildHelper;
	
	@Autowired
	private ValidateHelper validateHelper;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Override
	public SPBPResponseDTOV2 selfPrintBP(LoginInfo loginInfo, SPBPRequestDTOV2 requestDTO) throws BusinessBaseException {
		if(null == requestDTO || StringUtils.isEmpty(requestDTO.getJourneyId()) || CollectionUtils.isEmpty(requestDTO.getUniqueCustomerIds())){
			throw new ExpectedException(String.format("self print boarding pass failure => passengers can't be found in requestDTO"), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		/** get eligible self print boarding pass ucis */
		BoardingPassResponseDTO getEligibleResponse = sentBPService.getEligibleBPPaxs(requestDTO.getJourneyId(), requestDTO.getUniqueCustomerIds(), true, requestDTO.getRloc());
	 	
	 	/** if get eligible failure, covert OLCI errorInfos to MMB errorInfos, and return response */
	 	List<ErrorInfo> getEligibleErrorInfos = getAndCovertErrorInfos(getEligibleResponse);
		if(!CollectionUtils.isEmpty(getEligibleErrorInfos) && BizRulesUtil.hasOlciStopError(getEligibleErrorInfos)) {
			return new SPBPResponseDTOV2(getEligibleErrorInfos);
		}
		if (null == getEligibleResponse || CollectionUtils.isEmpty(getEligibleResponse.getEligibleBoardingPassPassengerUcis())) {
			logger.warn(String.format("self print boarding pass failure => can't find passengers of eligible send boarding pass in booking[%s]",requestDTO.getRloc()));
			return new SPBPResponseDTOV2(new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		/** if request has eligible passengers, get olci current session id  */
		String olciJSession = getJSessionOLCI(getOlciSessionCookie(requestDTO.getRloc(), null));
		if (StringUtils.isEmpty(olciJSession)) {
			logger.warn(String.format("self print boarding pass failure => can't get olci session in booking[%s]",requestDTO.getRloc()));
			throw new ExpectedException("self print boarding pass failure => can't get olci session in requestDTO", new ErrorInfo(ErrorCodeEnum.ERR_BP_OLCISESSION_NOTFOUND));
		}
		
		SPBPResponseDTOV2 responseDTO = new SPBPResponseDTOV2();
		responseDTO.setOlciJsessionId(olciJSession);
		responseDTO.setJourneyId(requestDTO.getJourneyId());
		responseDTO.setEligibleBoardingPassPassengerUcis(getEligibleResponse.getEligibleBoardingPassPassengerUcis());
		return responseDTO;
	}
	
	
	@Override
	public SendEmailResponseDTOV2 sendBPEmail(LoginInfo loginInfo, SendEmailRequestDTOV2 requestDTO) throws BusinessBaseException {
		if(null == requestDTO || StringUtils.isEmpty(requestDTO.getRloc()) || StringUtils.isEmpty(requestDTO.getJourneyId()) || CollectionUtils.isEmpty(requestDTO.getEmailAddresses())){
			throw new ExpectedException("send email boarding pass failure =>can't find any passenger in requestDTO", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc());
		List<RetrievePnrPassenger> pnrPassengers = pnrBooking.getPassengers();
		
		/** unmack email address in requestDTO */
		SendEmailRequestDTOV2 unmaskRequestDTO = unmaskEmailRequestDTO(requestDTO,pnrPassengers);
		// after unmasking the fields, validate them
		List<ErrorInfo> errors = validateEmailMaskFields(unmaskRequestDTO);
		// if there's validation error, return the error
		if (!CollectionUtils.isEmpty(errors)) {
			return new SendEmailResponseDTOV2(errors, false);
		}
		
		/** filter ucis is valid emailAddress*/
		List<String> validUcis = unmaskRequestDTO.getEmailAddresses().stream().filter(this::isValidEmailAddress).map(EmailAddressDTOV2::getUniqueCustomerId).collect(Collectors.toList());
		
		if(CollectionUtils.isEmpty(validUcis)){
			throw new ExpectedException(String.format("send email boarding pass failure => can't find valid email addresses rloc:[%s]",requestDTO.getRloc()), new ErrorInfo(ErrorCodeEnum.ERR_BP_EMAIL_SENT_FAIL));
		}
		/** get eligible self print boarding pass ucis */
		BoardingPassResponseDTO getEligibleResponse = sentBPService.getEligibleBPPaxs(requestDTO.getJourneyId(), validUcis, false, pnrBooking.getOneARloc());
	 	
		/** if get eligible failure, covert OLCI errorInfos to MMB errorInfos, and return response */
	 	List<ErrorInfo> getEligibleErrorInfos = getAndCovertErrorInfos(getEligibleResponse);
		if(!CollectionUtils.isEmpty(getEligibleErrorInfos) && BizRulesUtil.hasOlciStopError(getEligibleErrorInfos)) {
			return new SendEmailResponseDTOV2(getEligibleErrorInfos);
		}
		if (null == getEligibleResponse || CollectionUtils.isEmpty(getEligibleResponse.getEligibleBoardingPassPassengerUcis())) {
			throw new ExpectedException(String.format("send email boarding pass failure => can't found passengers eligible send boarding pass rloc:[%s]",requestDTO.getRloc()), new ErrorInfo(ErrorCodeEnum.ERR_BP_EMAIL_SENT_FAIL));
		}
		return doSendEmail(unmaskRequestDTO,getEligibleResponse.getEligibleBoardingPassPassengerUcis());
	}
	
	
	private SendEmailResponseDTOV2 doSendEmail(SendEmailRequestDTOV2 unmaskRequestDTO, List<String> eligibleBoardingPassPassengerUcis) throws ExpectedException{
		SendEmailResponseDTO sendEmailResponse = new SendEmailResponseDTO();
		boolean asyncAnyFailed = false;
		List<String> sendSuccessUcis= new ArrayList<>();
		
		if(BooleanUtils.isTrue(unmaskRequestDTO.getUseSameEmail())){
			sendEmailResponse = sentBPService.sendEmail(buildSendEmailRequestDTO(unmaskRequestDTO.getJourneyId(), unmaskRequestDTO.getEmailAddresses(),
					eligibleBoardingPassPassengerUcis), unmaskRequestDTO.getRloc());
		}else {
			List<Future<SendEmailResponseDTO>> futures = new ArrayList<>();
			for (String eligibleUci : eligibleBoardingPassPassengerUcis) {
				SendEmailRequestDTO  requestDTO = new SendEmailRequestDTO();
				List<String> emails = unmaskRequestDTO.getEmailAddresses().stream().filter(email -> StringUtils.isNotEmpty(email.getUniqueCustomerId()) && email.getUniqueCustomerId().equalsIgnoreCase(eligibleUci)).map(EmailAddressDTOV2::getEmailAddress).collect(Collectors.toList());
				requestDTO.setJourneyId(unmaskRequestDTO.getJourneyId());
				requestDTO.setUcis(Arrays.asList(eligibleUci));
				requestDTO.setEmailAddresses(emails);
				Future<SendEmailResponseDTO> future = sentBPService.asyncSendEmail(requestDTO, unmaskRequestDTO.getRloc());
				futures.add(future);
			}

			for(int i = 0; i < futures.size(); i++) {
				SendEmailResponseDTO asyncSendEmailResponseDTO = null;
				try {
					asyncSendEmailResponseDTO = futures.get(i) == null ? null : futures.get(i).get();
				} catch (Exception e) {
					logger.warn(String.format("send boarding pass failed. rloc:%s, passenger uci:%s",unmaskRequestDTO.getRloc(),eligibleBoardingPassPassengerUcis.get(i)),e);
				}
				
				if (asyncSendEmailResponseDTO == null || !CollectionUtils.isEmpty(asyncSendEmailResponseDTO.getErrors())) {
					asyncAnyFailed = true;
				}else{
					sendSuccessUcis.add(eligibleBoardingPassPassengerUcis.get(i));
					logger.info(String.format("send boarding pass success. rloc:%s, passenger uci:%s",unmaskRequestDTO.getRloc(),eligibleBoardingPassPassengerUcis.get(i)));
				}
			}
		}
		
		if(asyncAnyFailed){
			throw new ExpectedException("send boarding pass failed!",new ErrorInfo(ErrorCodeEnum.ERR_BP_EMAIL_SENT_FAIL));
		}
		
		/** if send email failure, covert OLCI errorInfos to MMB errorInfos, and return response */
	 	List<ErrorInfo> sendErrorInfos = getAndCovertErrorInfos(sendEmailResponse);
		if(!CollectionUtils.isEmpty(sendErrorInfos)) {
			return new SendEmailResponseDTOV2(sendErrorInfos);
		}
		sendSuccessUcis = CollectionUtils.isEmpty(sendSuccessUcis) ? eligibleBoardingPassPassengerUcis : sendSuccessUcis;
		SendEmailResponseDTOV2 responseDTOV2 = new SendEmailResponseDTOV2();
		responseDTOV2.setUniqueCustomerIds(sendSuccessUcis);
		responseDTOV2.setSuccess(true);
		return responseDTOV2;
	}
	
	@Override
	public SendSMSResponseDTOV2 sendBPSms(LoginInfo loginInfo, SendSMSRequestDTOV2 requestDTO) throws BusinessBaseException {
		if(null == requestDTO || StringUtils.isEmpty(requestDTO.getJourneyId()) || CollectionUtils.isEmpty(requestDTO.getPhoneNumbers())){
			throw new ExpectedException("send sms boarding pass failure => can't find any passenger in requestDTO", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc());
		List<RetrievePnrPassenger> pnrPassengers = pnrBooking.getPassengers();
		
		/** unmack phoneNumbers in requestDTO */
		SendSMSRequestDTOV2 unmaskRequestDTO = unmaskPhoneNumberRequestDTO(requestDTO,pnrPassengers);

		// after unmasking the fields, validate them
		List<ErrorInfo> errors = validatePhoneNumberMaskFields(unmaskRequestDTO);
		// if there's validation error, return the error
		if (!CollectionUtils.isEmpty(errors)) {
			return new SendSMSResponseDTOV2(errors, false);
		}
		/** filter ucis is valid phoneNumbers*/
		List<String> validUcis = unmaskRequestDTO.getPhoneNumbers().stream().filter(this::isValidPhoneNumber).map(PhoneNumbersDTOV2::getUniqueCustomerId).collect(Collectors.toList());
		
		if(CollectionUtils.isEmpty(validUcis)){
			throw new ExpectedException(String.format("send sms boarding pass failure => can't fond a valid phoneNumber rloc:[%s]",requestDTO.getRloc()), new ErrorInfo(ErrorCodeEnum.ERR_BP_SMS_SENT_FAIL));
		}
		/** get eligible self print boarding pass ucis */
		BoardingPassResponseDTO getEligibleResponse = sentBPService.getEligibleBPPaxs(requestDTO.getJourneyId(), validUcis, false, pnrBooking.getOneARloc());
	 	
		/** if get eligible failure, covert OLCI errorInfos to MMB errorInfos, and return response */
	 	List<ErrorInfo> getEligibleErrorInfos = getAndCovertErrorInfos(getEligibleResponse);
		if(!CollectionUtils.isEmpty(getEligibleErrorInfos) && BizRulesUtil.hasOlciStopError(getEligibleErrorInfos)) {
			return new SendSMSResponseDTOV2(getEligibleErrorInfos);
		}
		if (null == getEligibleResponse || CollectionUtils.isEmpty(getEligibleResponse.getEligibleBoardingPassPassengerUcis())) {
			throw new ExpectedException(String.format("send sms boarding pass failure => can't fond passengers eligible send boarding pass rloc[%s]",requestDTO.getRloc()), new ErrorInfo(ErrorCodeEnum.ERR_BP_SMS_SENT_FAIL));
		}
		
		return doSendSms(unmaskRequestDTO,getEligibleResponse.getEligibleBoardingPassPassengerUcis());
	}
	
	private SendSMSResponseDTOV2 doSendSms(SendSMSRequestDTOV2 unmaskRequestDTO, List<String> eligibleBoardingPassPassengerUcis) throws ExpectedException{
		SendSMSResponseDTO sendSMSResponse = new SendSMSResponseDTO();
		boolean asyncAnyFailed = false;
		List<String> sendSuccessUcis= new ArrayList<>();
		
		if(BooleanUtils.isTrue(unmaskRequestDTO.getUseSamePhoneNum())){
			sendSMSResponse = sentBPService.sendSms(buildSendSmsRequestDTO(unmaskRequestDTO,eligibleBoardingPassPassengerUcis), unmaskRequestDTO.getRloc());
		}else {
			List<Future<SendSMSResponseDTO>> futures = new ArrayList<>();
			for (String eligibleUci : eligibleBoardingPassPassengerUcis) {
				SendSMSRequestDTO  requestDTO = new SendSMSRequestDTO();
				List<PhoneNumberDTO> phones = new ArrayList<>();
				List<PhoneNumberDTOV2> requestPhones = unmaskRequestDTO.getPhoneNumbers().stream().filter(phone -> StringUtils.isNotEmpty(phone.getUniqueCustomerId()) && phone.getUniqueCustomerId().equalsIgnoreCase(eligibleUci)).map(PhoneNumbersDTOV2::getPhoneNumber).collect(Collectors.toList());
				for (PhoneNumberDTOV2 requestPhone : requestPhones) {
					PhoneNumberDTO phoneNumber = new PhoneNumberDTO();
					phoneNumber.setCountryCode(requestPhone.getCountryCode());
					phoneNumber.setNumber(requestPhone.getNumber());
					phones.add(phoneNumber);
				}
				requestDTO.setJourneyId(unmaskRequestDTO.getJourneyId());
				requestDTO.setUcis(Arrays.asList(eligibleUci));
				requestDTO.setPhoneNumbers(phones);
				Future<SendSMSResponseDTO> future = sentBPService.asyncSendSms(requestDTO, unmaskRequestDTO.getRloc());
				futures.add(future);
			}

			for(int i = 0; i < futures.size(); i++) {
				SendSMSResponseDTO asyncSendSMSResponseDTO = null;
				try {
					asyncSendSMSResponseDTO = futures.get(i) == null ? null : futures.get(i).get();
				} catch (Exception e) {
					logger.warn(String.format("send boarding pass failed. rloc:%s, passenger uci:%s",unmaskRequestDTO.getRloc(),eligibleBoardingPassPassengerUcis.get(i)),e);
				}
				
				if (asyncSendSMSResponseDTO == null || !CollectionUtils.isEmpty(asyncSendSMSResponseDTO.getErrors())) {
					asyncAnyFailed = true;
				}else{
					sendSuccessUcis.add(eligibleBoardingPassPassengerUcis.get(i));
					logger.info(String.format("send boarding pass success. rloc:%s, passenger uci:%s",unmaskRequestDTO.getRloc(),eligibleBoardingPassPassengerUcis.get(i)));
				}
			}
		}
		
		if(asyncAnyFailed){
			throw new ExpectedException("send boarding pass failed!",new ErrorInfo(ErrorCodeEnum.ERR_BP_EMAIL_SENT_FAIL));
		}
		
		/** if send email failure, covert OLCI errorInfos to MMB errorInfos, and return response */
	 	List<ErrorInfo> sendErrorInfos = getAndCovertErrorInfos(sendSMSResponse);
		if(!CollectionUtils.isEmpty(sendErrorInfos)) {
			return new SendSMSResponseDTOV2(sendErrorInfos);
		}
		sendSuccessUcis = CollectionUtils.isEmpty(sendSuccessUcis) ? eligibleBoardingPassPassengerUcis : sendSuccessUcis;
		SendSMSResponseDTOV2 responseDTOV2 = new SendSMSResponseDTOV2();
		responseDTOV2.setUniqueCustomerIds(sendSuccessUcis);
		responseDTOV2.setSuccess(true);
		return responseDTOV2;
	}
	
	@Override
	public InputStream getBPAppleWallet(String acceptLanguage, String applePassNumber, String rloc) throws Exception {
		if (StringUtils.isEmpty(acceptLanguage) || StringUtils.isEmpty(applePassNumber)) {
			throw new UnexpectedException("generate apple boarding pass file  failure => applepassnumber can't be found in requestDTO",
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		ResponseEntity<Resource> response = sentBPService.getAppleWalletBP(LanguageUtil.getLanguageByName(acceptLanguage), applePassNumber, rloc);
		
		if (null == response || !hasValidApplePassFile(response.getHeaders()) || null == response.getBody()) {
			throw new UnexpectedException(String.format("generate apple boarding pass file failure applePassNumber:[%s]",applePassNumber),
					new ErrorInfo(ErrorCodeEnum.ERR_BP_GENERATE_APPLEPASS_FAIL));
		}

		return response.getBody().getInputStream();
	}

	/**
	 * build send boarding pass requestDTO
	 * 
	 * @param requestDTO
	 * @param eligibleBPUcis
	 * @return
	 */
	private SendEmailRequestDTO buildSendEmailRequestDTO(String journeyId, List<EmailAddressDTOV2> emailAddresses,
			List<String> eligibleUcis) {
		SendEmailRequestDTO emailRequestDTO = new SendEmailRequestDTO();
		emailRequestDTO.setJourneyId(journeyId);
		// filter eligble passenger emailAddresses
		List<String> eligibleEmails = emailAddresses.stream().filter(item -> eligibleUcis.contains(item.getUniqueCustomerId()))
				.map(EmailAddressDTOV2::getEmailAddress).collect(Collectors.toList());
		//remove duplicate email addresses
		List<String> distinctEmails = eligibleEmails.stream().distinct().collect(Collectors.toList());
		List<String> distinctUcis = eligibleUcis.stream().distinct().collect(Collectors.toList());
		emailRequestDTO.setUcis(distinctUcis);
		emailRequestDTO.setEmailAddresses(distinctEmails);
		return emailRequestDTO;
	}

	
	/**
	 * build send boarding pass requestDTO
	 * 
	 * @param requestDTO
	 * @param eligibleBPUcis
	 * @return
	 */
	private SendSMSRequestDTO buildSendSmsRequestDTO(SendSMSRequestDTOV2 requestDTO, List<String> eligibleBPUcis) {
		SendSMSRequestDTO smsRequestDTO = new SendSMSRequestDTO();
		smsRequestDTO.setJourneyId(requestDTO.getJourneyId());
		
		// filter eligble passenger PhoneNumbers
		List<PhoneNumberDTOV2> eligiblePhoneNumbers = requestDTO.getPhoneNumbers().stream().filter(item -> eligibleBPUcis.contains(item.getUniqueCustomerId()))
				.map(PhoneNumbersDTOV2::getPhoneNumber).collect(Collectors.toList());
		
		List<PhoneNumberDTO> phoneNumbers = new ArrayList<>();
		for (PhoneNumberDTOV2 phoneNumberDTOV2 : eligiblePhoneNumbers) {
			PhoneNumberDTO phoneNumber = new PhoneNumberDTO();
			phoneNumber.setCountryCode(phoneNumberDTOV2.getCountryCode());
			phoneNumber.setNumber(phoneNumberDTOV2.getNumber());
			phoneNumbers.add(phoneNumber);
		}
		//remove duplicate phone numbers
		List<PhoneNumberDTO> distinctPhoneNumbers = phoneNumbers.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(phone -> { return phone.getCountryCode()+ "," +phone.getNumber(); }))), ArrayList<PhoneNumberDTO>::new));
		List<String> distinctUcis = eligibleBPUcis.stream().distinct().collect(Collectors.toList());
		smsRequestDTO.setUcis(distinctUcis);
		smsRequestDTO.setPhoneNumbers(distinctPhoneNumbers);
		return smsRequestDTO;
	}

	/**
	 * get OLCI_JSESSIONID base on cookie of olci
	 * 
	 * @param olciCookie
	 * @return
	 */
	private String getJSessionOLCI(String olciCookie) {
		Pattern pattern = Pattern.compile(sessionRegex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(olciCookie);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}
	
	/**
	 * Get olci session cookie by rloc & eticket
	 * 
	 * @param rloc
	 * @param eticket
	 * @return
	 */
	private String getOlciSessionCookie(String rloc, String eticket) {
		String olciSessionCookie = null;
		
		Object olciSessionCookieObj = mbTokenCacheRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.OLCI_SESSION_COOKIE, null, Object.class);
		if(olciSessionCookieObj == null) {
	 		logger.info(String.format("getOlciSessionCookie -> can't get olciSessionCookie[%s] by rloc[%s] or eticket[%s]", olciSessionCookie, rloc, eticket));
	 		return olciSessionCookie;
	 	}
		
		//TODO remove this after the Release5.1, codes below just for backward compatible handling
	 	if(olciSessionCookieObj instanceof String) {
	 		olciSessionCookie = (String) olciSessionCookieObj;
	 		logger.info(String.format("getOlciSessionCookie -> get olciSessionCookie[%s] from old construct[String] by rloc[%s] or eticket[%s]", olciSessionCookie, rloc, eticket));
	 		return olciSessionCookie;
	 	}
		
		@SuppressWarnings("unchecked")
		Map<String, String> olciSessionCookieMap = (Map<String, String>) olciSessionCookieObj;
		if(CollectionUtils.isEmpty(olciSessionCookieMap)) {
			logger.info(String.format("getOlciSessionCookie -> can't get olciSessionCookie[%s] by rloc[%s] or eticket[%s]", olciSessionCookie, rloc, eticket));
			return olciSessionCookie;
		}
		
		if(StringUtils.isNotEmpty(rloc)) {
			olciSessionCookie = olciSessionCookieMap.get(rloc);
		}
		
		if(StringUtils.isEmpty(olciSessionCookie)) {
			logger.info(String.format("getOlciSessionCookie -> can't get olciSessionCookie[%s] by rloc[%s]", olciSessionCookie, rloc));
			
			if(StringUtils.isNotEmpty(eticket)) {
				olciSessionCookie = olciSessionCookieMap.get(eticket);
				logger.info(String.format("getOlciSessionCookie -> can't get olciSessionCookie by rloc[%s] and olciSessionCookie[%s] gotten by eticket[%s]", olciSessionCookie, rloc, eticket));
			}
		}
		
		return olciSessionCookie;
	}

	/**
	 * Covert OLCI ErrorInfos to MMB ErrorInfo
	 * 
	 * @param responseDTO
	 * @return
	 */
	private List<ErrorInfo> getAndCovertErrorInfos(BaseResponseDTO responseDTO) {
		if (responseDTO == null) {
			return new ArrayList<>();
		}

		return PnrCprMergeHelper.covertErrorInfos(responseDTO.getErrors());
	}
	
	/**
	 * check whether generate apple pass file 
	 * @param resHeaders
	 * @return
	 */
	private boolean hasValidApplePassFile(HttpHeaders resHeaders) {
		if (null == resHeaders || null == resHeaders.getContentType()) {
			return false;
		}
		
		return MMBConstants.HTTP_CONTENT_TYPE_PKPASS.equalsIgnoreCase(resHeaders.getContentType().toString());
	}
	
	/**
	 * check whether valid email address 
	 * @param emailAddress
	 * @return
	 */
	private boolean isValidEmailAddress(EmailAddressDTOV2 emailAddress) {
		if (null == emailAddress || StringUtils.isEmpty(emailAddress.getEmailAddress())) {
			return false;
		}
		Pattern pattern = Pattern.compile(emailRegex);
		
		return pattern.matcher(emailAddress.getEmailAddress()).matches();
	}
	
	/**
	 * check whether valid phone number 
	 * @param phoneNumber
	 * @return
	 */
	private boolean isValidPhoneNumber(PhoneNumbersDTOV2 phoneNumber) {
		if (null == phoneNumber || null == phoneNumber.getPhoneNumber()
				|| StringUtils.isEmpty(phoneNumber.getPhoneNumber().getCountryCode())
				|| StringUtils.isEmpty(phoneNumber.getPhoneNumber().getNumber())) {
			return false;
		}

		return bookingBuildHelper.isValidPhoneNumber(phoneNumber.getPhoneNumber().getCountryCode(),
				phoneNumber.getPhoneNumber().getNumber(), ContactType.CONTACT);
	}
	
	
	/**
	 * unmask the phone number requestDTO if it is masked
	 * 
	 * @param rloc
	 * @param emailAddresses
	 * @return
	 */
	private SendSMSRequestDTOV2 unmaskPhoneNumberRequestDTO(SendSMSRequestDTOV2 requestDTO ,List<RetrievePnrPassenger> pnrPassengers) {
		List<MaskInfo> maskInfos = maskHelper.getMaskInfos(requestDTO.getRloc());
		if (CollectionUtils.isEmpty(maskInfos) || CollectionUtils.isEmpty(requestDTO.getPhoneNumbers())) {
			return requestDTO;
		}
		
		if (BooleanUtils.isTrue(requestDTO.getUseSamePhoneNum())) {
			setToggleOnPhoneNum(requestDTO, maskInfos,pnrPassengers);
		} else {
			for (PhoneNumbersDTOV2 phoneNumberDTO : requestDTO.getPhoneNumbers()) {
				String paxId=phoneNumberDTO.getPassengerId();
				String parentId = getParentIdbyPaxId(pnrPassengers,phoneNumberDTO.getPassengerId());
				//get original email for infant use parentId
				if(!StringUtils.isEmpty(parentId)){
					paxId = parentId;
				}
				String originalValue = unmaskField(maskInfos, MaskFieldName.PHONE_NO, phoneNumberDTO.getPhoneNumber().getNumber(),
						paxId);
				if (!StringUtils.isEmpty(originalValue)) {
					phoneNumberDTO.findPhoneNumber().setNumber(originalValue);
				}
			}
		}

		return requestDTO;
	}
	
	
	/**
	 * unmask the email requestDTO if it is masked
	 * 
	 * @param rloc
	 * @param emailAddresses
	 * @return
	 */
	private SendEmailRequestDTOV2 unmaskEmailRequestDTO(SendEmailRequestDTOV2 requestDTO, List<RetrievePnrPassenger> pnrPassengers) {
		List<MaskInfo> maskInfos = maskHelper.getMaskInfos(requestDTO.getRloc());
		if (CollectionUtils.isEmpty(maskInfos) || CollectionUtils.isEmpty(requestDTO.getEmailAddresses())) {
			return requestDTO;
		}		
		if (BooleanUtils.isTrue(requestDTO.getUseSameEmail())) {
			setToggleOnEmail(requestDTO, maskInfos,pnrPassengers);
		} else {
			for (EmailAddressDTOV2 passengerEmail : requestDTO.getEmailAddresses()) {
				String paxId=passengerEmail.getPassengerId();
				String parentId = getParentIdbyPaxId(pnrPassengers,passengerEmail.getPassengerId());
				//get original email for infant use parentId
				if(!StringUtils.isEmpty(parentId)){
					paxId = parentId;
				}
				String originalValue = unmaskField(maskInfos, MaskFieldName.EMAIL, passengerEmail.getEmailAddress(),
						paxId);
				if (!StringUtils.isEmpty(originalValue)) {
					passengerEmail.setEmailAddress(originalValue);
				}
			}
		}

		return requestDTO;
	}
	
	/**
	 * set ToggleOn Email
	 * @param requestDTO
	 * @param originEmailMap
	 * @param rloc
	 */
	private void setToggleOnEmail(SendEmailRequestDTOV2 requestDTO, List<MaskInfo> maskInfoList,List<RetrievePnrPassenger> pnrPassengers) {
		String toggleOnEmail = null;

		for (EmailAddressDTOV2 passengerEmail : requestDTO.getEmailAddresses()) {
			String paxId=passengerEmail.getPassengerId();
			String parentId = getParentIdbyPaxId(pnrPassengers,paxId);
			//get original email for infant use parentId
			if(!StringUtils.isEmpty(parentId)){
				paxId = parentId;
			}
			
			toggleOnEmail =unmaskField(maskInfoList, MaskFieldName.EMAIL, passengerEmail.getEmailAddress(),
					paxId);
			if (!StringUtils.isEmpty(toggleOnEmail)) {
				break;
			}
		}

		for (EmailAddressDTOV2 passengerEmail : requestDTO.getEmailAddresses()) {
			if (!StringUtils.isEmpty(toggleOnEmail)) {
				passengerEmail.setEmailAddress(toggleOnEmail);
			}
		}

	}
	
	
	/**
	 * set ToggleOn PhoneNum
	 * @param requestDTO
	 * @param originEmailMap
	 * @param rloc
	 */
	private void setToggleOnPhoneNum(SendSMSRequestDTOV2 requestDTO, List<MaskInfo> maskInfoList,List<RetrievePnrPassenger> pnrPassengers) {
		String toggleOnPhoneNum = null;

		for (PhoneNumbersDTOV2 phoneNumberDTO : requestDTO.getPhoneNumbers()) {
			String paxId=phoneNumberDTO.getPassengerId();
			String parentId = getParentIdbyPaxId(pnrPassengers,paxId);
			//get original email for infant use parentId
			if(!StringUtils.isEmpty(parentId)){
				paxId = parentId;
			}
			
			toggleOnPhoneNum =unmaskField(maskInfoList, MaskFieldName.PHONE_NO, phoneNumberDTO.getPhoneNumber().getNumber(),
					paxId);
			if (!StringUtils.isEmpty(toggleOnPhoneNum)) {
				break;
			}
		}

		for (PhoneNumbersDTOV2 phoneNumberDTO : requestDTO.getPhoneNumbers()) {
			if (!StringUtils.isEmpty(toggleOnPhoneNum)) {
				phoneNumberDTO.findPhoneNumber().setNumber(toggleOnPhoneNum);
			}
		}

	}
	
	/**
	 * unmask the field if it is masked
	 * 
	 * @param field
	 * @param maskFieldName
	 * @param oneARloc
	 * @param passengerId
	 * @return
	 */
	private String unmaskField(List<MaskInfo> maskInfos, MaskFieldName maskFieldName, String maskField,
			String passengerId) {
		if (CollectionUtils.isEmpty(maskInfos)) {
			return maskField;
		}
		
		String unmasked = maskHelper.getOriginalValue(maskFieldName, maskField, passengerId, null, maskInfos);
		return StringUtils.isEmpty(unmasked) ? maskField : unmasked;
	}
	
	/**
	 * 
	 * @param requestDTO
	 * @throws ExpectedException 
	 * @throws UnexpectedException 
	 */
	private List<ErrorInfo> validateEmailMaskFields(SendEmailRequestDTOV2 requestDTO) throws UnexpectedException {
		return validateHelper.validate(requestDTO, MaskFieldGroup.class);
	}
	
	/**
	 * 
	 * @param requestDTO
	 * @throws ExpectedException 
	 * @throws UnexpectedException 
	 */
	private List<ErrorInfo> validatePhoneNumberMaskFields(SendSMSRequestDTOV2 requestDTO) throws UnexpectedException {
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
