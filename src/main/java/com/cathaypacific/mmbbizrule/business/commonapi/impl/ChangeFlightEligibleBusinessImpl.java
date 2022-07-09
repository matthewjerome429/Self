package com.cathaypacific.mmbbizrule.business.commonapi.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.error.ErrorTypeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.business.commonapi.ChangeFlightEligibleBusiness;
import com.cathaypacific.mmbbizrule.constant.IBERedirectParameterConstants;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ChangeFlightEligibleResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ChangeFlightResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ReminderListResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.common.ErrorInfo;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.service.ChangeFlightEligibleService;
import com.cathaypacific.mmbbizrule.dto.request.changeflight.ChangeFlightDataRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.changeflight.ChangeFlightDataResponseDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.util.security.IBEEncryptionHelper;
import com.cathaypacific.mmbbizrule.util.security.EncryptionHelper;
import com.cathaypacific.mmbbizrule.util.security.EncryptionHelper.Encoding;
import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.PNRRetrieve;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;
import com.google.gson.Gson;

@Service
public class ChangeFlightEligibleBusinessImpl implements ChangeFlightEligibleBusiness{

	private static LogAgent logger = LogAgent.getLogAgent(ChangeFlightEligibleBusinessImpl.class);

    private static final String SK_TYPE_BAGW = "BAGW";

	private static final String BAGW_SEGMENT_PREFIX = "S";

	private static final String BAGW_SEPARATOR = "@";

	private static final String SK_DEPATURE_DATE_SUB_SEPARATOR = "@";

	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	
	@Autowired
	private PnrResponseParser pnrResponseParser;

	@Autowired
	private ChangeFlightEligibleService changeFlightEligibleService;

	@Autowired
	private PnrInvokeService pnrInvokeService;

	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;

	@Autowired
	private EncryptionHelper encryptionHelper;

	@Autowired
	private IBEEncryptionHelper ibeFlightEncryptionHelper;

	@Override
	public ChangeFlightResponseDTO changeFlightEligibleByRloc(String rloc,LoginInfo loginInfo) throws BusinessBaseException {
		ChangeFlightEligibleResponseDTO changeFlightEligibleResponseDTO;
		ChangeFlightResponseDTO changeFlightResponseDTO = new ChangeFlightResponseDTO();
		ErrorInfo changeFlightErrorInfo=new ErrorInfo();
		// check rloc is flight rloc
		if(!BizRulesUtil.isFlightRloc(rloc)){
			changeFlightErrorInfo.setErrorCode(ErrorCodeEnum.ERR_NON_SIXDIGITS_RLOC.getCode());
			changeFlightErrorInfo.setType(ErrorTypeEnum.BUSERROR);
			changeFlightResponseDTO.setChangeFlightErrorInfo(changeFlightErrorInfo);
			return changeFlightResponseDTO;
		}
		// build oneA request
		PnrRequestBuilder builder = new PnrRequestBuilder();
		PNRRetrieve request = builder.buildRlocRequest(rloc);
		logger.debug("request json:"+new Gson().toJson(request));

		PNRReply pnrReplay	= mmbOneAWSClient.retrievePnr(request);

		if(pnrReplay == null){
			return null;
		}
		// call change Flight Eligible Service
		changeFlightEligibleResponseDTO = changeFlightEligibleService.changeFlightEligibleByPnr(pnrReplay);

		if(!changeFlightEligibleResponseDTO.isCanChangeFlight()){
			//if the booking has FRBK SK then return another error code.
			RetrievePnrBooking booking = pnrResponseParser.paserResponse(pnrReplay);
			if(booking.isFrbkSK()){
				//
				changeFlightErrorInfo.setErrorCode("E000000101");
				changeFlightErrorInfo.setType(ErrorTypeEnum.BUSERROR);
				changeFlightResponseDTO.setChangeFlightErrorInfo(changeFlightErrorInfo);
			}else{
				changeFlightResponseDTO.setChangeFlightErrorInfo(changeFlightEligibleResponseDTO.getChangeFlightErrorInfo());
			}
			
		}else{
			// if Given the booking is eligible for change flight on 1A ATC ,will call Remind service
			buildChangeFlightResponse(changeFlightEligibleResponseDTO,changeFlightResponseDTO,pnrReplay);
		}
        // OLSSMMB-16763: Don't remove me!!!
        // --- begin ---
//		if(changeFlightResponseDTO.isCanChangeFlight()) {
			logger.info(String.format("Change Flight | Rloc | %s | Eligibility | %s", rloc, changeFlightResponseDTO.isCanChangeFlight() ? "Eligible": "Ineligible"), true);
//		}
		// --- end ---
		return changeFlightResponseDTO ;
	}

    /**
     * build Change Flight Service Response
     * @param changeFlightEligibleResponseDTO
     * @param changeFlightResponseDTO
     * @param pnrReplay
     * @throws UnexpectedException
     */
	private void buildChangeFlightResponse(ChangeFlightEligibleResponseDTO changeFlightEligibleResponseDTO,
			ChangeFlightResponseDTO changeFlightResponseDTO, PNRReply pnrReplay) throws UnexpectedException {

		changeFlightResponseDTO.setAtcBooking(changeFlightEligibleResponseDTO.isAtcBooking());
		changeFlightResponseDTO.setCanChangeFlight(changeFlightEligibleResponseDTO.isCanChangeFlight());
		changeFlightResponseDTO.setTimeStamp(changeFlightEligibleResponseDTO.getTimeStamp());
		changeFlightResponseDTO.setRetrieveOfficeId(changeFlightEligibleResponseDTO.getRetrieveOfficeId());
		// call Reminder List Service
		ReminderListResponseDTO reminderListResponseDTO = changeFlightEligibleService.reminderListResponseByPnr(pnrReplay);
		if(reminderListResponseDTO != null){
			changeFlightResponseDTO.setHasASREXL(reminderListResponseDTO.isHasASREXL());
			changeFlightResponseDTO.setHasEvent(reminderListResponseDTO.isHasEvent());
			changeFlightResponseDTO.setHasExtraBackage(reminderListResponseDTO.isHasExtraBackage());
			changeFlightResponseDTO.setHasHotel(reminderListResponseDTO.isHasHotel());
			changeFlightResponseDTO.setHasInsurance(reminderListResponseDTO.isHasInsurance());
			changeFlightResponseDTO.setReminderSK(!CollectionUtils.isEmpty(reminderListResponseDTO.getSklist()));
			changeFlightResponseDTO.setSsrList(!CollectionUtils.isEmpty(reminderListResponseDTO.getSsrlist())? reminderListResponseDTO.getSsrlist() : new ArrayList<>());
		}

	}

	@Override
	public ChangeFlightDataResponseDTO getChangeFlightData(ChangeFlightDataRequestDTO requestDTO, LoginInfo loginInfo) throws BusinessBaseException {

		String rloc = requestDTO.getOneARloc();
		RetrievePnrBooking retrievePnrBooking;
		try {
			retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		} catch (BusinessBaseException e) {
			throw new ExpectedException("Retrieve PNR failed",
					new com.cathaypacific.mbcommon.dto.error.ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}

		if (retrievePnrBooking == null || CollectionUtils.isEmpty(retrievePnrBooking.getPassengers())) {
			throw new ExpectedException(String.format("Cannot find booking by rloc: %s", rloc),
					new com.cathaypacific.mbcommon.dto.error.ErrorInfo(
							ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}

		try {
			paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		} catch (BusinessBaseException e) {
			throw new ExpectedException("Find primary passenger failed",
					new com.cathaypacific.mbcommon.dto.error.ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}

		ChangeFlightDataResponseDTO responseDTO = new ChangeFlightDataResponseDTO();

		Map<String, String> requestMap = new LinkedHashMap<>();
		requestMap.put(IBERedirectParameterConstants.ENTRYPOINT, requestDTO.getEntryPoint());
		requestMap.put(IBERedirectParameterConstants.ENTRYLANGUAGE, requestDTO.getEntryLanguage());
		requestMap.put(IBERedirectParameterConstants.ENTRYCOUNTRY, requestDTO.getEntryCountry());
		requestMap.put(IBERedirectParameterConstants.RETURNURL, requestDTO.getReturnUrl());
		requestMap.put(IBERedirectParameterConstants.ERRORURL, requestDTO.getErrorUrl());

		requestMap.put(IBERedirectParameterConstants.ONEA_RLOC, requestDTO.getOneARloc());
		requestMap.put(IBERedirectParameterConstants.OFFICE_ID, retrievePnrBooking.getOfficeId());

		List<RetrievePnrPassenger> passengers = retrievePnrBooking.getPassengers();
		RetrievePnrPassenger passenger = passengers.stream().filter(pax->BooleanUtils.isTrue(pax.isPrimaryPassenger())).findFirst().orElse(passengers.get(0));
		requestMap.put(IBERedirectParameterConstants.PAX_LASTNAME, passenger.getFamilyName());
		requestMap.put(IBERedirectParameterConstants.PAX_FIRSTNAME, passenger.getGivenName());

		List<RetrievePnrSegment> segments = retrievePnrBooking.getSegments();
		for (int i = 0; i < segments.size(); i++) {
			RetrievePnrSegment segment = segments.get(i);
			requestMap.put(String.format(IBERedirectParameterConstants.FLIGHT_CLASS, i + 1), segment.getSubClass());
			requestMap.put(String.format(IBERedirectParameterConstants.ORIGIN, i + 1), segment.getOriginPort());
			requestMap.put(String.format(IBERedirectParameterConstants.DESTINATION, i + 1), segment.getDestPort());

			String departureTime = convertToIBETimeFormat(segment.getDepartureTime().getTime());
			requestMap.put(String.format(IBERedirectParameterConstants.FLIGHT_DATE, i + 1), departureTime);
			requestMap.put(String.format(IBERedirectParameterConstants.FLIGHT_CARRIER, i + 1), segment.getPnrOperateCompany());
			requestMap.put(String.format(IBERedirectParameterConstants.FLIGHT_NUM, i + 1), segment.getPnrOperateSegmentNumber());
		}

		String isExistBcode = Boolean.toString(isExistBcode(retrievePnrBooking));
		requestMap.put(IBERedirectParameterConstants.IS_EXIST_BCODE, isExistBcode);

		String encryptedBagw = buildEncryptedBAGW(retrievePnrBooking);
		if (!StringUtils.isEmpty(encryptedBagw)) {
			requestMap.put(IBERedirectParameterConstants.BAGW, encryptedBagw);
		}

		String encryptedSKDepatureDateSub = buildEncryptedSKDepatureDate(retrievePnrBooking);
		if (!StringUtils.isEmpty(encryptedSKDepatureDateSub)) {
			requestMap.put(IBERedirectParameterConstants.SK_DEPATURE_DATE_SUB, encryptedSKDepatureDateSub);
		}

		String queryString = requestMap.entrySet().stream().map(
				entry -> entry.getKey() + "=" + entry.getValue()
			).reduce(
				(keyValue1, keyValue2) -> keyValue1 + "&" +keyValue2
			).orElse("");

		logger.info("Change flight data: " + queryString);

		String keyId = ibeFlightEncryptionHelper.getActiveKeyId();
		String encryptedData = ibeFlightEncryptionHelper.encryptMessage(queryString, keyId);

		responseDTO.setAedKey(keyId);
		responseDTO.setEncryptedData(encryptedData);

		return responseDTO;
	}

	private String convertToIBETimeFormat(String pnrDepartureArrivalTime) {
		String departureDate = DateUtil.convertDateFormat(pnrDepartureArrivalTime,
				RetrievePnrDepartureArrivalTime.TIME_FORMAT, DateUtil.DATE_PATTERN_YYYYMMDD);
		String departureTime = DateUtil.convertDateFormat(pnrDepartureArrivalTime,
				RetrievePnrDepartureArrivalTime.TIME_FORMAT, DateUtil.TIME_PATTERN_HHMM);

		return departureDate + departureTime;
	}

	private boolean isExistBcode(RetrievePnrBooking retrievePnrBooking) {
		return BooleanUtils.isTrue(retrievePnrBooking.getCorporateBooking());
	}

	private String buildEncryptedBAGW(RetrievePnrBooking retrievePnrBooking) {
		List<RetrievePnrDataElements> skList = retrievePnrBooking.getSkList();

		StringBuilder bagwBuilder = new StringBuilder();
		Optional.ofNullable(skList).orElse(Collections.emptyList()).stream()
				.filter(skElem -> SK_TYPE_BAGW.equals(skElem.getType()))
				.forEach(skElem ->
					bagwBuilder.append(BAGW_SEGMENT_PREFIX).append(skElem.getSegmentId()).append(":")
						.append(skElem.getFreeText()).append("/").append(BAGW_SEGMENT_PREFIX).append(skElem.getSegmentId())
						.append(BAGW_SEPARATOR)
						);

		if (bagwBuilder.length() > 0) {
			bagwBuilder.append(retrievePnrBooking.getOneARloc());
		}

		if (!StringUtils.isEmpty(bagwBuilder.toString())) {
			try {
				return encryptionHelper.encryptMessage(bagwBuilder.toString(), Encoding.BASE64, MMBBizruleConstants.IBE_KEY);
			} catch (UnexpectedException e) {
				logger.info("Encrypte BAGW failed");
			}
		}

		return null;
	}

	private String buildEncryptedSKDepatureDate(RetrievePnrBooking retrievePnrBooking) {
		List<RetrievePnrSegment> segments = retrievePnrBooking.getSegments();
		if(CollectionUtils.isEmpty(segments)) {
			return null;
		}

		StringBuilder skDepatureDateSubBuilder = new StringBuilder();
		int index = 1;
		String dateStr = segments.get(0).getDepartureTime().getTime();
		String str = convertToIBETimeFormat(dateStr);
		skDepatureDateSubBuilder = skDepatureDateSubBuilder.append(index).append(":").append(str);
		for(int j = 1; j < segments.size(); j++) {
			skDepatureDateSubBuilder = skDepatureDateSubBuilder.append(SK_DEPATURE_DATE_SUB_SEPARATOR);
			index++;
			String dateSt = segments.get(j).getDepartureTime().getTime();
			String st = convertToIBETimeFormat(dateSt);
			skDepatureDateSubBuilder = skDepatureDateSubBuilder.append(index).append(":").append(st);
		}

		try {
			return encryptionHelper.encryptMessage(skDepatureDateSubBuilder.toString(), Encoding.BASE64, MMBBizruleConstants.IBE_KEY);
		} catch (UnexpectedException e) {
			logger.info("Encrypte SK Depature Date failed");
		}

		return null;
	}

}
