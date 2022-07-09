package com.cathaypacific.mmbbizrule.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.error.ErrorTypeEnum;
import com.cathaypacific.mbcommon.enums.mask.MaskFieldName;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.common.MaskInfo;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.MemberEnrollmentBusiness;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.ClsApiError;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.service.OJBookingService;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.request.CommunicationPref;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.request.RuEnrolLoginDetails;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.request.RuEnrolMemberProfile;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.request.RuEnrolMemberProfileDetails;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.request.RuEnrolName;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.request.RuEnrolPreference;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.request.RuEnrolPromotionConsent;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.request.RuEnrolRequest;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.response.RuEnrolResponse;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.service.RuEnrollmentService;
import com.cathaypacific.mmbbizrule.dto.request.ruenrollment.ActiveRuEnrolRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.ruenrollment.ActiveRuEnrolResponseDTO;
import com.cathaypacific.mmbbizrule.handler.MaskHelper;
import com.cathaypacific.mmbbizrule.handler.ValidateHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.addbooking.service.OneAAddBookingService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

@Service
public class MemberEnrollmentBusinessImpl implements MemberEnrollmentBusiness{
	
	private static LogAgent logger = LogAgent.getLogAgent(MemberEnrollmentBusinessImpl.class);
	
	@Autowired 
	private RuEnrollmentService ruEnrollmentService;
	
	@Autowired
	private MaskHelper maskHelper;
	
	@Autowired
	private OneAAddBookingService oneAAddBookingService;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private BookingBuildService bookingBuildService;
	
	@Autowired
	private TicketProcessInvokeService ticketProcessInvokeService;
	
	@Autowired
	private TempLinkedBookingRepository tempLinkedBookingRepository;
	
	@Autowired
	private OJBookingService ojBookingService;
	
	@Autowired
	private ValidateHelper validateHelper;
	
	@Autowired
    private PaxNameIdentificationService paxNameIdentificationService;
	
	@Override
	public ActiveRuEnrolResponseDTO enrolActiveRuAccount(ActiveRuEnrolRequestDTO requestDTO, LoginInfo loginInfo) throws BusinessBaseException {
		// get 6-digit 1A rloc by loginInfo
		String rloc = getRloc(loginInfo, requestDTO);
		
		ActiveRuEnrolResponseDTO activeRuEnrolResponseDTO = new ActiveRuEnrolResponseDTO();
		// unmask the email
		String unmaskedEmail = unmaskEmail(requestDTO.getEmail(), rloc, requestDTO.getPassengerId());
		requestDTO.setEmail(unmaskedEmail);
		// validate the request
		List<ErrorInfo> errors = validateMaskFields(requestDTO);
		// if there's validation error, return the error
		if (!CollectionUtils.isEmpty(errors)) {
			activeRuEnrolResponseDTO.setErrors(errors);
			return activeRuEnrolResponseDTO;
		}
		RuEnrolRequest request = buildRuEnrolRequest(requestDTO);
		RuEnrolResponse response = ruEnrollmentService.enrolActiveRuAccount(request, MMBUtil.getCurrentAppCode());

		if (registerSuccsess(response)) { // the email is registered successfully
			activeRuEnrolResponseDTO.setSuccess(true);
			activeRuEnrolResponseDTO.setEmail(unmaskedEmail);
			// add booking if needed
			activeRuEnrolResponseDTO.setBookingAdded(addBooking(requestDTO, response, loginInfo, rloc));
		} 
		else { // the registration fails
			// if error code returned from CLS handle the error code
			if (response != null && !CollectionUtils.isEmpty(response.getErrors())) {
				handleClsErrors(unmaskedEmail, response, activeRuEnrolResponseDTO, requestDTO);
			} else { // if registration fails with no CLS error code, throw exception
				logger.error(String.format("RU enrollment failed for email: %s because of technical issue", requestDTO.getEmail()));
				throw new ExpectedException(String.format("RU enrollment failed for email: %s because of technical issue", requestDTO.getEmail()),
						new ErrorInfo(ErrorCodeEnum.ERR_MEMBERENROLLMENT_TECHNICALISSUE));
			}
		}
		logger.info(String.format("Sign Up | RU Registration | Family Name | %s | Given Name | %s | Email | %s", requestDTO.getName().getFamilyName(), requestDTO.getName().getGivenName(), requestDTO.getEmail()), true);
		return activeRuEnrolResponseDTO;
	}

	/**
	 * Add booking
	 * @param requestDTO
	 * @param response
	 * @param loginInfo
	 * @param rloc
	 * @return boolean
	 */
	private boolean addBooking(ActiveRuEnrolRequestDTO requestDTO, RuEnrolResponse response, LoginInfo loginInfo,
			String rloc) {
		if (requestDTO.isAddBooking()) {
			// add this booking to the new RU account
			try {
				addBooking(response.getCustomer().getIdNumber(), loginInfo, rloc);
				// temporarily link this booking to member booking list
				tempLinkedBookingRepository.addRuEnrolBooking(rloc, requestDTO.getName().getFamilyName(), requestDTO.getName().getGivenName(), requestDTO.getPassengerId(), loginInfo.getMmbToken());
				return true;
			} catch (Exception e) {
				logger.error(String.format("Add booking failed after RU enrollment, booking: %s, email: %s ", rloc, requestDTO.getEmail()), e);
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Check the register is success or not
	 * @param response
	 * @return boolean
	 */
	private boolean registerSuccsess(RuEnrolResponse response) {
		return response != null && CollectionUtils.isEmpty(response.getErrors()) 
				&& MMBBizruleConstants.CLS_STATUS_CODE_SUCCESS.equals(response.getStatusCode())
				&& response.getCustomer() != null && !StringUtils.isEmpty(response.getCustomer().getIdNumber());
	}
	
	/**
	 * get 6-digit 1A RLOC by login info
	 * @param loginInfo
	 * @return String
	 * @throws BusinessBaseException 
	 */
	private String getRloc(LoginInfo loginInfo, ActiveRuEnrolRequestDTO requestDTO) throws BusinessBaseException {
		String rloc = null;
		if (LoginInfo.LOGINTYPE_RLOC.equals(loginInfo.getLoginType())) {
			rloc = loginInfo.getLoginRloc();
		} else if (LoginInfo.LOGINTYPE_ETICKET.equals(loginInfo.getLoginType())) {
			rloc = ticketProcessInvokeService.getRlocByEticket(loginInfo.getLoginEticket());
		} else {
			throw new ExpectedException(String.format("Unable to register RU account for email: %s - Can't register RU account in member-login session", requestDTO.getEmail()),
					new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}
		
		// if the RLOC is 7-digit, get 6-digit 1A rloc 
		if (!StringUtils.isEmpty(rloc) && rloc.length() != 6) {
			OJBooking ojBooking = ojBookingService.getBooking(requestDTO.getName().getGivenName(), requestDTO.getName().getFamilyName(), rloc);
			if (ojBooking == null) {
				throw new ExpectedException(String.format("Unable to register RU account for email: %s - Cannot find OJBooking by rloc from ojSErvice:%s", requestDTO.getEmail(), rloc),
						new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
			}

			if (ojBooking.getFlightBooking() != null && StringUtils.isNotEmpty(ojBooking.getFlightBooking().getBookingReference())) {
				rloc = ojBooking.getFlightBooking().getBookingReference();
			} else {
				throw new ExpectedException(String.format("Unable to register RU account for email: %s - Cannot find 1A rloc by OJ rloc:%s", requestDTO.getEmail(), rloc),
						new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
			}
		}
		
		if (StringUtils.isEmpty(rloc)) {
			throw new ExpectedException(String.format("Unable to register RU account for email: %s - Cannot get rloc by loginInfo", requestDTO.getEmail()),
					new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}
		return rloc;
	}

	/**
	 * validate mask fields
	 * @param requestDTO
	 * @throws ExpectedException 
	 * @throws UnexpectedException 
	 */
	private List<ErrorInfo> validateMaskFields(ActiveRuEnrolRequestDTO requestDTO) throws UnexpectedException {
		return validateHelper.validate(requestDTO, MaskFieldGroup.class);
	}

	/**
	 * handle CLS error code
	 * @param unmaskedEmail
	 * @param response
	 * @param activeRuEnrolResponseDTO
	 * @param requestDTO 
	 */
	private void handleClsErrors(String unmaskedEmail, RuEnrolResponse response,
			ActiveRuEnrolResponseDTO activeRuEnrolResponseDTO, ActiveRuEnrolRequestDTO requestDTO) {
		// if error CLSAPI_CFRX_21_908/CLSAPI_CFRX_21_8243 is returned, means the enrollment failed because the email is already registered	
		if (registeredErrorCodeExist(response.getErrors())) {
			activeRuEnrolResponseDTO.setSuccess(false);
			activeRuEnrolResponseDTO.setEmailRegistered(true);
			activeRuEnrolResponseDTO.setEmail(unmaskedEmail);
		} else { // build the error list according to CLS errors
			List<ErrorInfo> errorInfos = new ArrayList<>();
			for (ClsApiError clsError : response.getErrors()) {
				ErrorInfo errorInfo = new ErrorInfo();
				errorInfo.setErrorCode(clsError.getCode());
				errorInfo.setType(ErrorTypeEnum.BUSERROR);
				errorInfos.add(errorInfo);
				logger.error(String.format("RU enrollment failed for email: %s because of CLS error code : %s", requestDTO.getEmail(), clsError.getCode()));
			}
			activeRuEnrolResponseDTO.setErrors(errorInfos);
		}
	}
	
	/**
	 * add booking to the member
	 * @param requestDTO
	 * @param rloc 
	 * @param idNumber
	 * @throws BusinessBaseException 
	 */
	private void addBooking(String memberId, LoginInfo loginInfo, String rloc) throws BusinessBaseException {
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		if (pnrBooking == null) {
			throw new ExpectedException(String.format("Unable to add booking - Cannot find booking by rloc:%s", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		Booking booking = bookingBuildService.buildBooking(pnrBooking, loginInfo, initializeAddBookingRequired());

		// operating company id(CX/KA) list
		List<String> operatingCompanys = booking.getSegments().stream()
				.filter(seg -> seg != null && (OneAConstants.COMPANY_CX.equals(seg.getOperateCompany())
						|| OneAConstants.COMPANY_KA.equals(seg.getOperateCompany())))
				.map(Segment::getOperateCompany).distinct().collect(Collectors.toList());
		
		oneAAddBookingService.addBookingBySK(rloc, memberId, operatingCompanys);
	}
	
	/**
	 * initialize BookingBuildRequired to build booking for add-booking function.
	 * 
	 * @return BookingBuildRequired
	 */
	private BookingBuildRequired initializeAddBookingRequired() {
		BookingBuildRequired required = new BookingBuildRequired();
		required.setBaggageAllowances(false);
		required.setCprCheck(false);
		required.setCountryOfResidence(false);
		required.setEmergencyContactInfo(false);
		required.setMealSelection(false);
		required.setMemberAward(false);
		required.setOperateInfoAndStops(true);
		required.setPassenagerContactInfo(false);
		required.setRtfs(false);
		required.setSeatSelection(false);
		required.setTravelDocument(false);
		return required;
	}

	/**
	 * check if there is any CLSAPI_CFRX_21_908/CLSAPI_CFRX_21_8243 error in the error list
	 * @param errors
	 * @return
	 */
	private boolean registeredErrorCodeExist(List<ClsApiError> errors) {
		return !CollectionUtils.isEmpty(errors) && errors.stream().anyMatch(error -> MMBBizruleConstants.CLS_EMAIL_RIGISTERED_ERRORS.contains(error.getCode()));
	}

	/**
	 * build RuEnrolRequest
	 * @param requestDTO
	 * @return RuEnrolRequest
	 */
	private RuEnrolRequest buildRuEnrolRequest(ActiveRuEnrolRequestDTO requestDTO) {
		RuEnrolRequest request = new RuEnrolRequest();
		request.setApplicationName(MMBUtil.getCurrentAppCode());
		
		RuEnrolMemberProfile ruEnrolMemberProfile = new RuEnrolMemberProfile();
		RuEnrolName ruEnrolName = new RuEnrolName();
		ruEnrolName.setFamilyName(requestDTO.getName().getFamilyName().toUpperCase());
		ruEnrolName.setGivenName(requestDTO.getName().getGivenName().toUpperCase());
		ruEnrolName.setTitle(requestDTO.getName().getTitle().toUpperCase());
		ruEnrolMemberProfile.setName(ruEnrolName);
		ruEnrolMemberProfile.setNamingConvention("GF");
		request.setMemberProfile(ruEnrolMemberProfile);
		
		RuEnrolMemberProfileDetails ruEnrolMemberProfileDetails = new RuEnrolMemberProfileDetails();
		ruEnrolMemberProfileDetails.setEmail(requestDTO.getEmail());
		RuEnrolLoginDetails ruEnrolLoginDetails = new RuEnrolLoginDetails();
		ruEnrolLoginDetails.setPin(requestDTO.getLoginDetails().getPin());
		// if "userName" in request is empty, use email as user name
		if (!StringUtils.isEmpty(requestDTO.getLoginDetails().getUserName())) {
			ruEnrolLoginDetails.setUserName(requestDTO.getLoginDetails().getUserName());
		} else {
			ruEnrolLoginDetails.setUserName(requestDTO.getEmail());
		}
		
		ruEnrolMemberProfileDetails.setLoginDetails(ruEnrolLoginDetails);
		RuEnrolPreference ruEnrolPreference = new RuEnrolPreference();
		ruEnrolPreference.setCommunicationPreferredOrigin(requestDTO.getPreferredOrigin());
		ruEnrolMemberProfileDetails.setPreference(ruEnrolPreference);
		ruEnrolMemberProfileDetails.setEnrolmentSource(MMBBizruleConstants.ENROL_REQUEST_ENROLMENT_SOURCE);
		
		request.setMemberProfileDetails(ruEnrolMemberProfileDetails);
		
		RuEnrolPromotionConsent ruEnrolPromotionConsent = new RuEnrolPromotionConsent();
		ruEnrolPromotionConsent.setServiceConvenienceConsent(requestDTO.isPromotionConsent());
		request.setPromotionConsent(ruEnrolPromotionConsent);
		
		List<CommunicationPref> communicationPrefs = new ArrayList<>();
		CommunicationPref communicationPref = new CommunicationPref();
		communicationPref.setChannelCode(MMBBizruleConstants.ENROL_REQUEST_CHANNEL_CODE);
		communicationPref.setNatureCode(MMBBizruleConstants.ENROL_REQUEST_NATION_CODE);
		communicationPref.setSelectionOptionCode(requestDTO.isPromotionConsent() ? MMBBizruleConstants.ENROL_REQUEST_SELECTION_OPTION_CODE_SELECTED : MMBBizruleConstants.ENROL_REQUEST_SELECTION_OPTION_CODE_NOT_SELECTED);
		communicationPrefs.add(communicationPref);
		request.setCommunicationPrefs(communicationPrefs);

		
		return request;
	}

	/**
	 * unmask the email if it is masked
	 * @param email
	 * @param oneARloc
	 * @param passengerId
	 * @return String
	 */
	private String unmaskEmail(String email, String oneARloc, String passengerId) {
		List<MaskInfo> maskInfos =  maskHelper.getMaskInfos(oneARloc);
		if (CollectionUtils.isEmpty(maskInfos)) {
			return email;
		}
		String unmaskedEmail = maskHelper.getOriginalValue(MaskFieldName.EMAIL, email, passengerId, null, maskInfos);
		return StringUtils.isEmpty(unmaskedEmail) ? email : unmaskedEmail;
	}

}
