package com.cathaypacific.mmbbizrule.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.db.dao.ConsentRecordDAO;
import com.cathaypacific.mmbbizrule.db.dao.ConsentRecordDAOV2;
import com.cathaypacific.mmbbizrule.db.model.TbConsentLogInfo;
import com.cathaypacific.mmbbizrule.db.model.TbConsentRecord;
import com.cathaypacific.mmbbizrule.dto.request.consent.PaxConsentDTO;
import com.cathaypacific.mmbbizrule.dto.request.consent.ConsentAddRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.consent.ConsentsDeleteRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.consent.SegmentConsentDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentCommonRecordResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentDeleteResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentInfoRecordDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentsDeleteResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ConsentInfoRecordResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEticket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.service.ConsentInfoService;

@Service
public class ConsentInfoServiceImpl implements ConsentInfoService {

	private static LogAgent logger = LogAgent.getLogAgent(ConsentInfoServiceImpl.class);
	private static final String LOG_SITE = "CX";
	private static final String VERSION_NO = "1.0";
	private static final String LOG_SOURCE = "WEB";
	private static final String LOG_APPCODE = "MMB";

	@Autowired
	ConsentRecordDAO consentRecordDAO;
	@Autowired
	ConsentRecordDAOV2 consentRecordDAOV2;

	@Autowired
	AEMService aemService;

	@Override
	@Transactional
	public ConsentInfoRecordResponseDTO saveConsentInfo(RetrievePnrBooking retrievePnrBooking, LoginInfo loginInfo,
			String rloc, String acceptLanguage) throws BusinessBaseException {

		List<RetrievePnrPassenger> passengers = retrievePnrBooking.getPassengers();
		ConsentInfoRecordResponseDTO consentInfoRecord = new ConsentInfoRecordResponseDTO();
		boolean saveConsentInfoFlag = false;
		String localLanguage = null;
		String country = null;

		if (!StringUtils.isEmpty(acceptLanguage) && acceptLanguage.length() >= 5) {
			{
				country = acceptLanguage.substring(3, 5);
				localLanguage = acceptLanguage.substring(0, 2);
			}
		}

		if (!CollectionUtils.isEmpty(passengers)) {
			for (RetrievePnrPassenger passenger : passengers) {
				String passengerId = passenger.getPassengerID();
				TbConsentLogInfo consentLogInfo = new TbConsentLogInfo();
				consentLogInfo.setFirstName(passenger.getGivenName());
				consentLogInfo.setLastName(passenger.getFamilyName());
				consentLogInfo.setRecordLocator(retrievePnrBooking.getOneARloc());
				consentLogInfo.setLanguage(localLanguage);
				consentLogInfo.setCountry(country);
				getFlightInfo(retrievePnrBooking, consentLogInfo, passengerId);
				consentLogInfo.setConsentDate(new Date());
				consentLogInfo.setSite(LOG_SITE);
				consentLogInfo.setVersionNo(VERSION_NO);
				consentLogInfo.setSource(LOG_SOURCE);
				consentLogInfo.setAppCode(StringUtils.isEmpty(MMBUtil.getCurrentAppCode()) ? LOG_APPCODE : MMBUtil.getCurrentAppCode());
				if (LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())) {
					consentLogInfo.setMemberId(loginInfo.getMemberId());
				}

				// save each passenger to DB
				TbConsentLogInfo consentLogInfo2 = consentRecordDAO.save(consentLogInfo);
				if (consentLogInfo2 != null) {
					saveConsentInfoFlag = true;
				} else {
					saveConsentInfoFlag = false;
					break;
				}

			}
		}

		consentInfoRecord.setConsentInfoRecord(saveConsentInfoFlag);
		return consentInfoRecord;

	}
	
	@Override
	@Transactional
	public ConsentCommonRecordResponseDTO saveConsentCommon(ConsentAddRequestDTO requestDTO, RetrievePnrBooking retrievePnrBooking, LoginInfo loginInfo,
			String rloc, String acceptLanguage) throws BusinessBaseException {
		ConsentCommonRecordResponseDTO consentInfoRecord = new ConsentCommonRecordResponseDTO();
		List<ConsentInfoRecordDTO> consents= new ArrayList<ConsentInfoRecordDTO>();
		String language = null;
		String locale = null;
		
		if (!StringUtils.isEmpty(acceptLanguage) && acceptLanguage.length() >= 5) {
			{
				locale = acceptLanguage.substring(3, 5);
				language = acceptLanguage.substring(0, 2);
			}
		}
		
		List<RetrievePnrPassenger> passengers = retrievePnrBooking.getPassengers();
		List<PaxConsentDTO> paxConsents = requestDTO.getPassengers();
		if (!CollectionUtils.isEmpty(paxConsents)) {
			for(PaxConsentDTO paxContsent : paxConsents){
				RetrievePnrPassenger pnrPassenger = passengers.stream().filter(p->p.getPassengerID().equals(paxContsent.getPassengerId())).findFirst().orElse(null);
				if(null == pnrPassenger){
					break;
				}
				for(SegmentConsentDTO segmentConsent : paxContsent.getSegments()){
					RetrievePnrSegment pnrSegment = retrievePnrBooking.getSegments().stream().filter(f->f.getSegmentID().equals(segmentConsent.getSegmentId())).findFirst().orElse(null);
					if(null == pnrSegment){
						break;
					}
					
					if (!StringUtils.isEmpty(segmentConsent.getSsr())) {
						consents.add(saveConsentInfoRecord(language, locale, loginInfo, rloc,retrievePnrBooking, pnrPassenger, pnrSegment, segmentConsent.getSsr()));
					} else if (!CollectionUtils.isEmpty(segmentConsent.getSsrList())) {
						for (String ssr : segmentConsent.getSsrList()) {
							consents.add(saveConsentInfoRecord(language, locale, loginInfo,rloc,retrievePnrBooking, pnrPassenger, pnrSegment, ssr));
						}
					}
				}
			}
		}
		
		consentInfoRecord.setConsentInfoRecords(consents);
		
		return consentInfoRecord;

	}
	
	private ConsentInfoRecordDTO saveConsentInfoRecord(String language, String locale, LoginInfo loginInfo, String rloc, RetrievePnrBooking pnrBooking,
			RetrievePnrPassenger pnrPassenger, RetrievePnrSegment pnrSegment, String ssr) {

		TbConsentRecord consentLogInfo = new TbConsentRecord();
		RetrievePnrPassenger loginPassenger = pnrBooking.getPassengers().stream()
				.filter(passenger -> passenger != null && BooleanUtils.isTrue(passenger.isPrimaryPassenger())).findFirst().orElse(null);
		consentLogInfo.setLanguage(language);
		consentLogInfo.setLocale(locale);
		if(loginPassenger != null){
			consentLogInfo.setLoginPaxFirstName(loginPassenger.getGivenName());
			consentLogInfo.setLoginPaxLastName(loginPassenger.getFamilyName());
		}
		consentLogInfo.setLoginSource(loginInfo.getLoginChannel());
		consentLogInfo.setRloc(rloc);
		consentLogInfo.setSsrPaxFirstName(pnrPassenger.getGivenName());
		consentLogInfo.setSsrPaxLastName(pnrPassenger.getFamilyName());
		consentLogInfo.setSsr(ssr);
		consentLogInfo.setAppCode(MMBUtil.getCurrentAppCode());
		consentLogInfo.setLoginSource(MMBUtil.getCurrentAccessChannel());
		
		getFlightInfoV2(pnrBooking,consentLogInfo,pnrPassenger.getPassengerID(),pnrSegment);
		TbConsentRecord consentLogInfo2 = consentRecordDAOV2.save(consentLogInfo);
		ConsentInfoRecordDTO consentInfo = new ConsentInfoRecordDTO();
		if (consentLogInfo2 != null) {
			consentInfo.setId(consentLogInfo2.getId());
			consentInfo.setConsentInfoRecord(true);
			consentInfo.setPaxId(pnrPassenger.getPassengerID());
			consentInfo.setSegmentId(pnrSegment.getSegmentID());
			return consentInfo;
		} else {
			consentInfo.setConsentInfoRecord(false);
			consentInfo.setPaxId(pnrPassenger.getPassengerID());
			consentInfo.setSegmentId(pnrSegment.getSegmentID());
			return consentInfo;
		}
	}
	
	/**
	 * get flight info
	 * 
	 * @param retrievePnrBooking
	 * @param consentLogInfo
	 * @param passengerId
	 */
	private void getFlightInfoV2(RetrievePnrBooking retrievePnrBooking, TbConsentRecord consentRecord,
			String passengerId,RetrievePnrSegment segment) {

		List<RetrievePnrPassengerSegment> passengerSegments = retrievePnrBooking.getPassengerSegments();

		if (!CollectionUtils.isEmpty(passengerSegments)) {
			List<RetrievePnrPassengerSegment> passegerIdMatchSegements = passengerSegments.stream()
					.filter(passengerSegment -> passengerId.equals(passengerSegment.getPassengerId()))
					.collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(passegerIdMatchSegements)) {
				// save the first ticket to db
				List<String> etickets = passegerIdMatchSegements.get(0).getEtickets().stream().map(RetrievePnrEticket::getTicketNumber).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(etickets)) {
					String eticketNo = etickets.get(0);
					consentRecord.seteTicket(eticketNo);
				}
			}
		}

		if (null != segment) {
			consentRecord.setFlightNo(segment.getMarketSegmentNumber());
			consentRecord.setMarketingcarrier(segment.getMarketCompany());
			consentRecord.setOperatingCarrier(segment.getPnrOperateCompany());
			try {
				consentRecord.setFlightDepartureTime(DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM,
						segment.getDepartureTime().getTime()));

			} catch (ParseException e) {
				String errorMsg= String.format("Unexpect date format: +%s,the expect format is %s", segment.getDepartureTime().getPnrTime(),DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM);
				logger.error(errorMsg);
			}

		}

	}

	/**
	 * get flight info
	 * 
	 * @param retrievePnrBooking
	 * @param consentLogInfo
	 * @param passengerId
	 */
	private void getFlightInfo(RetrievePnrBooking retrievePnrBooking, TbConsentLogInfo consentLogInfo,
			String passengerId) {

		List<RetrievePnrPassengerSegment> passengerSegments = retrievePnrBooking.getPassengerSegments();
		List<RetrievePnrSegment> segments = retrievePnrBooking.getSegments();
		List<String> segmentIds = new ArrayList<>();
		List<RetrievePnrSegment> matchSegment = new ArrayList<>();

		if (!CollectionUtils.isEmpty(passengerSegments)) {
			List<RetrievePnrPassengerSegment> passegerIdMatchSegements = passengerSegments.stream()
					.filter(passengerSegment -> passengerId.equals(passengerSegment.getPassengerId()))
					.collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(passegerIdMatchSegements)) {
				// save the first ticket to db
				List<String> etickets = passegerIdMatchSegements.get(0).getEtickets().stream().map(RetrievePnrEticket::getTicketNumber).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(etickets)) {
					String eticketNo = etickets.get(0);
					consentLogInfo.seteTicketNo(eticketNo);
				}
				for (RetrievePnrPassengerSegment passegerIdMatchSegement : passegerIdMatchSegements) {
					segmentIds.add(passegerIdMatchSegement.getSegmentId());
				}
			}
		}

		if (!CollectionUtils.isEmpty(segments)) {
			List<RetrievePnrSegment> matchSegments = segments.stream()
					.filter(segment -> segmentIds.contains(segment.getSegmentID())
							&& (BooleanUtils.isFalse(isFlightFlown(segment))) && (BooleanUtils.isFalse(isCancelFlight(segment)))    )
					.collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(matchSegments)) {
				String segmentId = findUpcomingSegments(matchSegments);
				matchSegment = matchSegments.stream().filter(segment -> segmentId.equals(segment.getSegmentID()))
						.collect(Collectors.toList());
			}
		}

		if (!CollectionUtils.isEmpty(matchSegment)) {
			RetrievePnrSegment segment = matchSegment.get(0);
			consentLogInfo.setFlightNo(segment.getMarketSegmentNumber());
			try {
				consentLogInfo.setFlightDate(DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM,
						segment.getDepartureTime().getTime()));

			} catch (ParseException e) {
				String errorMsg= String.format("Unexpect date format: +%s,the expect format is %s", segment.getDepartureTime().getPnrTime(),DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM);
				logger.error(errorMsg);
			}

		}

	}

	/**
	 * findUpcomingSegments
	 * 
	 * @param matchSegments
	 * @return
	 */
	private String findUpcomingSegments(List<RetrievePnrSegment> matchSegments) {

		Map<String, String> upComingSegmentMap = new HashMap<>();
		List<String> scheduleTimeList = new ArrayList<>();
		for (RetrievePnrSegment matchSegment : matchSegments) {
			String segmentId = matchSegment.getSegmentID();
			RetrievePnrDepartureArrivalTime departureArrivalTime = matchSegment.getDepartureTime();
			if (departureArrivalTime != null) {
				String scheduleTime = departureArrivalTime.getTime();
				upComingSegmentMap.put(scheduleTime, segmentId);
				scheduleTimeList.add(scheduleTime);
			}
		}

		return upComingSegmentMap.get(findUpcomingDepartureTime(scheduleTimeList));

	}

	/**
	 * findUpcomingDepartureTime
	 * 
	 * @param scheduleTimeList
	 * @return
	 */
	private String findUpcomingDepartureTime(List<String> scheduleTimeList) {

		String upcomingDepartureTime = null;
		if (!CollectionUtils.isEmpty(scheduleTimeList)) {
			upcomingDepartureTime = scheduleTimeList.get(0);
			for (int i = 1; i < scheduleTimeList.size(); i++) {
				Date upcomingDate;
				Date transferDate;
				try {
					upcomingDate = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM,
							upcomingDepartureTime);
					transferDate = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM,
							scheduleTimeList.get(i));
					int result = DateUtil.compareDate(upcomingDate, transferDate, Calendar.MINUTE);
					if (result > 0) {
						upcomingDepartureTime = scheduleTimeList.get(i);
					}
				} catch (ParseException e) {
					String errorMsg= String.format("Unexpect date format!");
					logger.error(errorMsg);
				}
			}
		}

		return upcomingDepartureTime;
	}
	
	private boolean isCancelFlight(RetrievePnrSegment retrievePnrSegment){
		
		boolean cancelFlag = false;
		List<String> status = retrievePnrSegment.getStatus();
		if(status.contains(OneAConstants.SEGMENTSTATUS_CANCEL)){
			cancelFlag = true;
		}
		return cancelFlag;
	}

	/**
	 * is Flight Flown
	 * 
	 * @param retrievePnrSegment
	 * @return
	 */
	private boolean isFlightFlown(RetrievePnrSegment retrievePnrSegment) {

		boolean flagStauts = false;
		boolean flagTime = false;
		List<String> status = retrievePnrSegment.getStatus();
		if (!CollectionUtils.isEmpty(status)) {
			flagStauts = status.contains(OneAConstants.SEGMENTSTATUS_FLOWN);
		}

		if (flagStauts) {
			return flagStauts;
		}

		RetrievePnrDepartureArrivalTime departureArrivalTime = retrievePnrSegment.getDepartureTime();
		DepartureArrivalTime departureTime = buildDepartArrivalTime(departureArrivalTime);
		if (departureTime != null && !StringUtils.isEmpty(departureTime.getRtfsActualTime())) {
			flagTime = true;
		}
		return flagTime;

	}

	/**
	 * build Depart ArrivalTime
	 * 
	 * @param pnrTime
	 * @return
	 */
	private DepartureArrivalTime buildDepartArrivalTime(RetrievePnrDepartureArrivalTime pnrTime) {
		if (pnrTime != null) {
			DepartureArrivalTime result = new DepartureArrivalTime();
			result.setPnrTime(pnrTime.getPnrTime());
			result.setRtfsActualTime(pnrTime.getRtfsActualTime());
			result.setRtfsEstimatedTime(pnrTime.getRtfsEstimatedTime());
			result.setRtfsScheduledTime(pnrTime.getRtfsScheduledTime());
			return result;
		}
		return null;
	}

	@Override
	public ConsentDeleteResponseDTO deleteConsentCommon(int id) throws BusinessBaseException {
		ConsentDeleteResponseDTO consentInfoRecordResponseDTO = new ConsentDeleteResponseDTO();
		TbConsentRecord consentLogInfo = new TbConsentRecord();
		consentLogInfo.setId(id);
		try{
			consentRecordDAOV2.delete(consentLogInfo);
			consentInfoRecordResponseDTO.setConsentInfoDelete(true);
		}catch(Exception e){
			
			consentInfoRecordResponseDTO.setConsentInfoDelete(false);
			consentInfoRecordResponseDTO.setId(id);
		}
		
		return consentInfoRecordResponseDTO;
	}
	
	@Override
	public ConsentsDeleteResponseDTO deleteConsentCommons(ConsentsDeleteRequestDTO requestDTO) throws BusinessBaseException {
		ConsentsDeleteResponseDTO consentInfoRecordResponseDTO = new ConsentsDeleteResponseDTO();
		try{
			List<TbConsentRecord> consentRecords = consentRecordDAOV2.findByIdIn(requestDTO.getIds());
			consentRecordDAOV2.deleteInBatch(consentRecords);
			consentInfoRecordResponseDTO.setConsentInfoDelete(true);
		}catch(Exception e){
			
			consentInfoRecordResponseDTO.setConsentInfoDelete(false);
			consentInfoRecordResponseDTO.setIds(requestDTO.getIds());
		}
		
		return consentInfoRecordResponseDTO;
	}

}
