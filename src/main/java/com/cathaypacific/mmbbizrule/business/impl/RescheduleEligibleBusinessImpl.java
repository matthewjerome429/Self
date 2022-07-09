package com.cathaypacific.mmbbizrule.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.RescheduleEligibleBusiness;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.dpatcirebook.model.request.RescheduleEligibleRequest;
import com.cathaypacific.mmbbizrule.cxservice.dpatcirebook.model.response.RescheduleEligibleResponse;
import com.cathaypacific.mmbbizrule.cxservice.dpatcirebook.service.DpAtciRebookService;
import com.cathaypacific.mmbbizrule.cxservice.dprebook.model.request.EncryptDeepLinkRequest;
import com.cathaypacific.mmbbizrule.cxservice.dprebook.model.response.EncryptDeepLinkResponse;
import com.cathaypacific.mmbbizrule.cxservice.dprebook.service.DpRebookService;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.model.ConstantData;
import com.cathaypacific.mmbbizrule.dto.response.reschedule.BookingRescheduleResponseDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrApEmail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEmail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEticket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;
import com.google.gson.Gson;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc reschedule eligible for passenger
 * @author fangfang.zhang
 * @date Jan 26, 2018 3:18:20 PM
 * @version V1.0
 */
@Service
public class RescheduleEligibleBusinessImpl implements RescheduleEligibleBusiness {
	private static LogAgent logger = LogAgent.getLogAgent(RescheduleEligibleBusinessImpl.class);
	
	private static final Gson GSON = new Gson();
	
	@Autowired
	private DpRebookService dpRebookService;
	
	@Autowired
	private DpAtciRebookService dpAtciRebookService;
	
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	
	@Autowired
	private PnrResponseParser pnrResponseParser;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;

	@Autowired
	private ConstantDataDAO constantDataDAO;
	
	@Override
	public BookingRescheduleResponseDTO rescheduleEligibleByRloc(String rloc,LoginInfo loginInfo,String languageLocale) throws BusinessBaseException {	
		
		if (StringUtils.isEmpty(rloc)) {
			throw new UnexpectedException(String.format("Unable to check reschedule eligible - Cannot find rloc:[%s]",
					rloc), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		// retrieve pnrBooking
		PnrRequestBuilder builder = new PnrRequestBuilder();
		PNRReply pnrReplay = mmbOneAWSClient.retrievePnr(builder.buildRlocRequest(rloc));
		if (pnrReplay == null) {
			throw new UnexpectedException(String.format("Unable to check reschedule eligible - Cannot find PNR by rloc[%s]",
					rloc), new ErrorInfo(ErrorCodeEnum.ERR_NOT_AUTHORIZED));
		}

		RetrievePnrBooking retrievePnrBooking = pnrResponseParser.paserResponse(pnrReplay);
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		RetrievePnrPassenger primarypassenger = retrievePnrBooking.getPassengers().stream()
				.filter(pax -> BooleanUtils.isTrue(pax.isPrimaryPassenger())).findFirst().orElse(null);
		if (primarypassenger == null) {
			throw new UnexpectedException(String.format("Get deepLink failure, can't find primary passenger in rloc:[%s]",
					retrievePnrBooking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_NOT_AUTHORIZED));
		}
		
		// get flight change option availability
		RescheduleEligibleResponse eligibleResponseDTO = getRescheduleEligible(rloc,primarypassenger,pnrReplay);
		
		BookingRescheduleResponseDTO response = new BookingRescheduleResponseDTO();
		if (eligibleResponseDTO != null && BooleanUtils.isTrue(eligibleResponseDTO.isEligible())) {
			response.setCanReschedule(true);
			
			// check email for get reschedule deep link
			String email = getEmailAddressRetrievePnr(primarypassenger,retrievePnrBooking.getApEmails());
			if (StringUtils.isEmpty(email)) {
				response.setCanGetEmail(false);
				return response;
			}
			
			// get reschedule chatbot deeplink
			EncryptDeepLinkResponse deepLinkResponse = getRescheduleDeepLink(languageLocale, retrievePnrBooking,primarypassenger,email);
			response.setDeeplinkUrl(Optional.ofNullable(deepLinkResponse).orElse(new EncryptDeepLinkResponse()).getMessage());
		}
		
		return response;
	}

	/**
	 * get reschedule eligible
	 * 
	 * @param rloc
	 * @param loginInfo
	 * @param pnrReplay
	 * @return
	 */
	private RescheduleEligibleResponse getRescheduleEligible(String rloc, RetrievePnrPassenger primarypassenger, PNRReply pnrReplay)
			throws UnexpectedException {
		RescheduleEligibleRequest eligibleRequest = buildEligibleRequest(rloc, primarypassenger, pnrReplay);
		return dpAtciRebookService.getBookingReschedule(eligibleRequest);
	}

	/**
	 * get reschedule deepLink
	 * 
	 * @param languageLocale
	 * @param retrievePnrBooking
	 * @return
	 * @throws ExpectedException
	 */
	private EncryptDeepLinkResponse getRescheduleDeepLink(String languageLocale, RetrievePnrBooking retrievePnrBooking,
			RetrievePnrPassenger primarypassenger, String email) throws UnexpectedException {
		EncryptDeepLinkRequest deepLinkRequest = buildDeepLinkRequest(retrievePnrBooking, languageLocale, primarypassenger,
				email);
		return dpRebookService.getRescheduleDeeplink(deepLinkRequest);
	}
	
	/**
	 * build eligible request
	 * @param rloc
	 * @param loginInfo
	 * @param pnrReplay
	 * @return
	 */
	private RescheduleEligibleRequest buildEligibleRequest(String rloc,RetrievePnrPassenger primarypassenger,PNRReply pnrReplay) throws UnexpectedException{
		RescheduleEligibleRequest requestDTO= new RescheduleEligibleRequest();
		requestDTO.setRloc(rloc);
		requestDTO.setNeedBody(false);
		if(pnrReplay == null) {
			throw new UnexpectedException(String.format("check reschedule eligible failure, can't get pnrReplay in rloc:[%s]", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		requestDTO.setPnrReply(GSON.toJson(pnrReplay));
		requestDTO.setLastName(primarypassenger.getFamilyName());
		return requestDTO;
	}
	
	
	/**
	 * build deepLink request
	 * @param rloc
	 * @param languageLocale
	 * @param passenger
	 * @return
	 */
	private EncryptDeepLinkRequest buildDeepLinkRequest(RetrievePnrBooking retrievePnrBooking,String languageLocale,RetrievePnrPassenger passenger,String email) throws UnexpectedException{
		EncryptDeepLinkRequest request= new EncryptDeepLinkRequest();
		List<String> nameTitles = constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE,OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList());
		String title = BookingBuildUtil.retrievePassengerTitle(passenger.getGivenName(), nameTitles);
		request.setRloc(retrievePnrBooking.getOneARloc());
		request.setPaxId(passenger.getPassengerID());
		request.setFamilyName(passenger.getFamilyName());
		request.setGivenName(BookingBuildUtil.removeTitleFromGivenName(passenger.getGivenName(), title));
		request.setEmail(email);
		request.setFlow(MMBBizruleConstants.CHATBOT_FLOW_ATCI);
		List<String> etickts = getEticketsRetrievePnr(retrievePnrBooking,passenger.getPassengerID());
		if(!CollectionUtils.isEmpty(etickts)){
			request.setEticket(etickts.get(0));
		}
		
		// The endpoint only accept format "en_HK"
		if(languageLocale == null) {
			throw new UnexpectedException(String.format("Get deepLink failure, can't find language locale in rloc:[%s]", retrievePnrBooking.getOneARloc()),
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}else {
			languageLocale = languageLocale.replaceAll("-", "_");
		}
		request.setLanguageLocale(languageLocale);
		return request;
	}
	
	/**
	 * get emailAddress from pnrEmails (order CTCE << APE )
	 * @param pnrEmails
	 * @param apEmails
	 * @return
	 */
	private String getEmailAddressRetrievePnr(RetrievePnrPassenger passenger, List<RetrievePnrApEmail> apEmails) {
		String emailAddress = "";
		if (passenger.getEmails() != null) {
			for (RetrievePnrEmail pnrEmail : passenger.getEmails()) {
				if (OneAConstants.CTCE.equals(pnrEmail.getType()) && !StringUtils.isEmpty(pnrEmail.getEmail())) {
					emailAddress = pnrEmail.getEmail();
				}
			}
		}

		if (StringUtils.isEmpty(emailAddress)) {
			emailAddress = getApEmailAddress(passenger, apEmails);
		}
		return emailAddress;
	}
	
	/**
	 * get APE from pnrEmails and apEmails
	 * @param pnrEmails
	 * @param apEmails
	 * @return
	 */
	private String getApEmailAddress(RetrievePnrPassenger passenger,List<RetrievePnrApEmail> apEmails) {
		String emailAddress ="";
		if (passenger.getEmails() != null) {
			for(RetrievePnrEmail pnrEmail: passenger.getEmails()) {
				if (OneAConstants.APE.equals(pnrEmail.getType()) && !StringUtils.isEmpty(pnrEmail.getEmail())) {
					emailAddress=pnrEmail.getEmail();
				}
			}
		}
		
		if (StringUtils.isEmpty(emailAddress) && apEmails != null) {
			emailAddress = apEmails.stream()
					.filter(ap -> !StringUtils.isEmpty(ap.getEmail()) && !StringUtils.isEmpty(passenger.getPassengerID())
							&& (CollectionUtils.isEmpty(ap.getPassengerIds())
									|| ap.getPassengerIds().contains(passenger.getPassengerID())))
					.map(RetrievePnrEmail::getEmail).findFirst().orElse("");
		}
		
		return emailAddress;
	}
	
	/**
	 * get Etickets from RetrievePnr
	 * @param retrievePnrBooking
	 * @param passengerID
	 * @return
	 */
	private List<String> getEticketsRetrievePnr(RetrievePnrBooking retrievePnrBooking,String passengerID) {
		List<String> etickets = new ArrayList<>();
		List<RetrievePnrPassengerSegment> passengerSegments = retrievePnrBooking.getPassengerSegments();
		logger.debug(String.format("get etickts with passengerID[%s]", passengerID));
		if (!CollectionUtils.isEmpty(passengerSegments)) {
			List<RetrievePnrPassengerSegment> passegerIdMatchSegements = passengerSegments.stream()
					.filter(passengerSegment -> passengerID.equals(passengerSegment.getPassengerId()) && !CollectionUtils.isEmpty(passengerSegment.getEtickets()))
					.collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(passegerIdMatchSegements)) {
				etickets = passegerIdMatchSegements.get(0).getEtickets().stream().map(RetrievePnrEticket::getTicketNumber).collect(Collectors.toList());
			}
		}
		return etickets;
	}
}
