package com.cathaypacific.mmbbizrule.business.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.business.ReBookingBusiness;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.olci.service.OLCIService;
import com.cathaypacific.mmbbizrule.db.dao.CabinClassDAO;
import com.cathaypacific.mmbbizrule.db.model.BookingStatus;
import com.cathaypacific.mmbbizrule.dto.request.rebooking.AcceptFlightInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.rebooking.AcceptFlightRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.rebooking.AcceptInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.rebooking.ReBookingAcceptInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.rebooking.ReBookingAcceptRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.rebooking.ProtectFlightInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.rebooking.ProtectFlightInfoResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.rebooking.ProtectInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.rebooking.RebookAcceptResponseDTO;
import com.cathaypacific.mmbbizrule.handler.BookingBuildHelper;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.model.AirFlightInfoBean;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.service.AirFlightInfoService;
import com.cathaypacific.mmbbizrule.oneaservice.changepnrelement.ChangePnrElementRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.changepnrelement.service.ChangePnrElementInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrAtciCancelledSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRebookMapping;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.util.FreeTextUtil;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.AddRemarkBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.sevice.AddPnrElementsInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;
import com.cathaypacific.mmbbizrule.oneaservice.session.service.PnrSessionInvokService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.request.pnrchg_16_1_1a.PNRChangeElement;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;

@Service
public class ReBookingBusinessImpl implements ReBookingBusiness {
	
	private static LogAgent logger = LogAgent.getLogAgent(ReBookingBusinessImpl.class);
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private PnrSessionInvokService pnrSessionInvokService;
	
	@Autowired
	private ChangePnrElementInvokeService changePnrElementInvokeService;
	
	@Autowired
	private DeletePnrService deletePnrService;
	
	@Autowired
	private AddPnrElementsInvokeService addPnrElementsInvokeService;
	
	@Autowired
	private CabinClassDAO cabinClassDAO;
	
	@Autowired
	private BookingBuildHelper bookingBuildHelper;
	
	@Autowired
	private AirFlightInfoService airFlightInfoService;
	
	@Autowired
	private OLCIService olciService;
	
	@Value("${rebook.sendBP}")
	private boolean sendBPAfterRebook;
	
	@Override
	public RebookAcceptResponseDTO accept(ReBookingAcceptRequestDTO requestDTO, String accessChannel) throws BusinessBaseException {
		String rloc = requestDTO.getOneARloc();

		// retrieve PNR
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		
		// check accept information
		checkAcceptInfos(requestDTO.getAcceptInfos(), retrievePnrBooking);
		
		// get list of accept&cancelled segment IDs
		List<String> acceptSegmentIds = requestDTO.getAcceptInfos().stream().map(ReBookingAcceptInfoDTO::getAcceptSegmentIds).collect(Collectors.toList())
				.stream().flatMap(List<String>::stream).distinct().collect(Collectors.toList());
		List<String> cancelledSegmentIds = requestDTO.getAcceptInfos().stream().map(ReBookingAcceptInfoDTO::getCancelledSegmentIds).collect(Collectors.toList())
				.stream().flatMap(List<String>::stream).distinct().collect(Collectors.toList());
		
		// do accept actions
		doAccept(accessChannel, retrievePnrBooking, rloc, requestDTO.getAcceptFamilyName(), requestDTO.getAcceptGivenName(), acceptSegmentIds, cancelledSegmentIds);
		
		RebookAcceptResponseDTO response = new RebookAcceptResponseDTO();
		response.setSuccess(true);
		logger.info(String.format("Flight Rearrangement | Acceptance | Access Channel | %s", accessChannel), true);
		return response;
	}
	
	@Override
	public RebookAcceptResponseDTO acceptFlight(AcceptFlightRequestDTO requestDTO, String accessChannel) throws BusinessBaseException {
		String rloc = requestDTO.getRloc();
		
		// retrieve PNR
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		
		List<String> acceptSegmentIds = new ArrayList<>();
		List<String> cancelledSegmentIds = new ArrayList<>();
		// check accept information, build accept/cancelled segmentIds
		checkAcceptInfos(requestDTO, retrievePnrBooking, acceptSegmentIds, cancelledSegmentIds);
		
		// do accept actions
		doAccept(accessChannel, retrievePnrBooking, rloc, requestDTO.getAcceptFamilyName(), requestDTO.getAcceptGivenName(), acceptSegmentIds, cancelledSegmentIds);
		
		RebookAcceptResponseDTO response = new RebookAcceptResponseDTO();
		response.setSuccess(true);
		return response;
	}
	
	@Override
	public ProtectFlightInfoResponseDTO getProtectFlightInfo(String rloc) throws BusinessBaseException {		
		ProtectFlightInfoResponseDTO response = new ProtectFlightInfoResponseDTO();
		
		// retrieve PNR
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		
		List<RetrievePnrRebookMapping> rebookMappings = new ArrayList<>();
		rebookMappings.addAll(Optional.ofNullable(pnrBooking.getRebookMapping()).orElse(Collections.emptyList()));
		rebookMappings.addAll(Optional.ofNullable(pnrBooking.getAtciRebookMapping()).orElse(Collections.emptyList()));
		if(CollectionUtils.isEmpty(rebookMappings)) {
			return response;
		}
		List<RetrievePnrSegment> segments =pnrBooking.getSegments();
		for (RetrievePnrSegment pnrSegment : segments) {
			String departureTimeString = pnrSegment.getDepartureTime().getTime();
			AirFlightInfoBean airFlightInfoBean = airFlightInfoService.getAirFlightInfo(departureTimeString, pnrSegment.getOriginPort(),
					pnrSegment.getDestPort(), pnrSegment.getMarketCompany(), pnrSegment.getMarketSegmentNumber(), pnrSegment.getAirCraftType());
			BookingBuildUtil.setOperateByCompanyAndFlightNumber(pnrSegment, airFlightInfoBean);
		}
		
		response.setProtectFlightInfos(buildProtectFlightInfos(segments, pnrBooking.getAtciCancelledSegments(), rebookMappings));
		return response;
	}
	
	/**
	 * build segmentIds from PNR by marketCompanyId & marketFlightNumber & scheduledTime
	 * 
	 * @param rloc
	 * @param retrievePnrBooking
	 * @param segmentIds
	 * @param cancelledInfo
	 * @throws UnexpectedException
	 */
	private void buildIdsByCompanyAndNoAndStd(String rloc, RetrievePnrBooking retrievePnrBooking,
			List<String> segmentIds, AcceptFlightInfoDTO cancelledInfo) throws UnexpectedException {
		RetrievePnrSegment pnrSegment = retrievePnrBooking.getSegments().stream().filter(s -> cancelledInfo.getMarketCarrierCode().equals(s.getMarketCompany()) 
				&& cancelledInfo.getMarketFlightNumber().equals(s.getMarketSegmentNumber())
				&& cancelledInfo.getScheduledTime().equals(Optional.ofNullable(s.getDepartureTime()).orElseGet(RetrievePnrDepartureArrivalTime::new).getPnrTime()))
				.findFirst().orElse(null);
		if(pnrSegment == null) {
			throw new UnexpectedException(String.format("accept failure, can't find segment{companyId: %s, flightNumber: %s, std: %s} in rloc:[%s]",
					cancelledInfo.getMarketCarrierCode(), cancelledInfo.getMarketFlightNumber(), cancelledInfo.getScheduledTime(), rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		} else {
			segmentIds.add(pnrSegment.getSegmentID());
		}
	}

	/**
	 * accept re-book: 1) open session; 2) remove cancelled flight; 3) update flight status TK->HK; 4) write RM remark in 1A PNR
	 * @param accessChannel
	 * @param retrievePnrBooking
	 * @param rloc
	 * @param acceptFamilyName
	 * @param acceptGivenName
	 * @param acceptSegmentIds
	 * @param cancelledSegmentIds
	 * @throws BusinessBaseException
	 */
	private void doAccept(String accessChannel, RetrievePnrBooking retrievePnrBooking, String rloc,
			String acceptFamilyName, String acceptGivenName, List<String> acceptSegmentIds,
			List<String> cancelledSegmentIds) throws BusinessBaseException {
		
		/** 1) open session */
		Session session = pnrSessionInvokService.openSessionOnly(rloc);
		
		/** 2) remove cancelled flight */
		// remove the cancelled segment id that is build by RM (identify by "C" suffix)
		List<String> existingCancelledSegmentIds = cancelledSegmentIds.stream().filter(id -> !id.endsWith(MMBBizruleConstants.CANCELLED_SEGMENT_SUFFIX)).collect(Collectors.toList());
		RetrievePnrBooking booking;
		if (!CollectionUtils.isEmpty(existingCancelledSegmentIds)) {
			booking = removeCancelledFlight(session, existingCancelledSegmentIds, rloc);
		} else {
			booking = retrievePnrBooking;
		}
		
		/** 3) update flight status TK->HK */
		updateFlightsStatus(session, rloc, booking, acceptSegmentIds);
		
		/** 4) write RM remark in 1A PNR */
		session.setStatus(SessionStatus.END.getStatus());
		addRmProtectionRemark(retrievePnrBooking.getSegments(), acceptSegmentIds, rloc, acceptFamilyName, acceptGivenName, session, accessChannel);
		
		/** 5) send boarding pass in email */
		// TODO
		// as 10624 is the technical implementation of sendBP function,
		// this switch should be removed after commerical launch
		if(sendBPAfterRebook) {
			try {
				notifyCheckinPax(acceptSegmentIds, retrievePnrBooking, rloc, acceptFamilyName, acceptGivenName);				
			} catch(Exception e) {
				logger.error(String.format("doAccept sendBP => failure, rloc[%s]", rloc), e);
			}
		}
//		/** 5) close session */
//		closeSession(session);
	}

	/**
	 * build protect flight information
	 * 
	 * @param segments
	 * @param atciCancelledSegments 
	 * @param rebookMappings
	 * @return List<ProtectInfoDTO>
	 */
	private List<ProtectInfoDTO> buildProtectFlightInfos(List<RetrievePnrSegment> segments, List<RetrievePnrAtciCancelledSegment> atciCancelledSegments, List<RetrievePnrRebookMapping> rebookMappings) {
		if(CollectionUtils.isEmpty(segments) || CollectionUtils.isEmpty(rebookMappings)) {
			return Collections.emptyList();
		}
		
		List<ProtectInfoDTO> protectInfos = new ArrayList<>();
		for(RetrievePnrRebookMapping mapping : rebookMappings) {
			ProtectInfoDTO protectInfo = buildProtectInfo(mapping, segments, atciCancelledSegments);
			if(protectInfo != null) {
				protectInfos.add(protectInfo);
			}
		}
		
		return protectInfos;
	}

	/**
	 * build protect information including list of cancelled & confirmed flight info
	 * 
	 * @param mapping
	 * @param segments
	 * @param atciCancelledSegments 
	 * @return ProtectInfoDTO
	 */
	private ProtectInfoDTO buildProtectInfo(RetrievePnrRebookMapping mapping, List<RetrievePnrSegment> segments, List<RetrievePnrAtciCancelledSegment> atciCancelledSegments) {
		List<ProtectFlightInfoDTO> cancelledFlightInfos = buildProtectFlightInfo(mapping.getCancelledSegmentIds(), segments);
		List<ProtectFlightInfoDTO> confirmedFlightInfos = buildProtectFlightInfo(mapping.getAcceptSegmentIds(), segments);
		List<ProtectFlightInfoDTO> atciCancelledFlightInfos = buildAtciCancelledProtectFlightInfo(mapping.getCancelledSegmentIds(), atciCancelledSegments);
		
		if(CollectionUtils.isEmpty(cancelledFlightInfos) && CollectionUtils.isEmpty(confirmedFlightInfos) && CollectionUtils.isEmpty(atciCancelledFlightInfos)) {
			return null;
		}
		ProtectInfoDTO protectInfoDTO = new ProtectInfoDTO();
		protectInfoDTO.setCancelledFlightInfos(cancelledFlightInfos);
		protectInfoDTO.setConfirmedFlightInfos(confirmedFlightInfos);
		protectInfoDTO.setAtciCancelledFlightInfos(atciCancelledFlightInfos);
		
		return protectInfoDTO;
	}

	/**
	 * build cancelled or confirmed flightInfo list
	 * 
	 * @param segmentIds
	 * @param segments
	 * @return List<ProtectFlightInfoDTO>
	 */
	private List<ProtectFlightInfoDTO> buildProtectFlightInfo(List<String> segmentIds, List<RetrievePnrSegment> segments) {
		if(CollectionUtils.isEmpty(segmentIds)) {
			return Collections.emptyList();
		}
		
		List<ProtectFlightInfoDTO> protectFlightInfos = new ArrayList<>();
		for(String segmentId : segmentIds) {
			RetrievePnrSegment segment = PnrResponseParser.getSegmentById(segments, segmentId);
			if(segment != null) {
				BookingStatus bookingStatus = bookingBuildHelper.getFirstAvailableStatus(segment.getStatus());
				String status = bookingStatus==null? null:bookingStatus.getStatusCode();
				ProtectFlightInfoDTO protectFlightInfo = new ProtectFlightInfoDTO();
				protectFlightInfo.setSegmentId(segmentId);
				protectFlightInfo.setMarketCarrierCode(segment.getMarketCompany());
				protectFlightInfo.setMarketFlightNumber(segment.getMarketSegmentNumber());
				protectFlightInfo.setScheduledArrivalTime(Optional.ofNullable(segment.getArrivalTime()).orElseGet(RetrievePnrDepartureArrivalTime::new).getPnrTime());
				protectFlightInfo.setScheduledDepartureTime(Optional.ofNullable(segment.getDepartureTime()).orElseGet(RetrievePnrDepartureArrivalTime::new).getPnrTime());
				protectFlightInfo.setOriginAirportCode(segment.getOriginPort());
				protectFlightInfo.setDestAirportCode(segment.getDestPort());
				protectFlightInfo.setOperatingCarrierCode(segment.getPnrOperateCompany());
				protectFlightInfo.setSubclass(segment.getSubClass());
				protectFlightInfo.setCabinCode(BookingBuildUtil.getCabinClassBySubClass(cabinClassDAO,segment.getSubClass()));
				protectFlightInfo.setStatus(status);
				protectFlightInfos.add(protectFlightInfo);
			}
		}
		
		return protectFlightInfos;
	}
	
	/**
	 * build ATCI cancelled flightInfo list
	 * 
	 * @param segmentIds
	 * @param segments
	 * @return List<ProtectFlightInfoDTO>
	 */
	private List<ProtectFlightInfoDTO> buildAtciCancelledProtectFlightInfo(List<String> segmentIds, List<RetrievePnrAtciCancelledSegment> atciCancelledSegments) {
		if(CollectionUtils.isEmpty(segmentIds) || CollectionUtils.isEmpty(atciCancelledSegments)) {
			return Collections.emptyList();
		}
		
		List<ProtectFlightInfoDTO> protectFlightInfos = new ArrayList<>();
		for(String segmentId : segmentIds) {
			RetrievePnrAtciCancelledSegment segment = atciCancelledSegments.stream().filter(seg -> segmentId.equals(seg.getSegmentId())).findFirst().orElse(null);
			if(segment != null) {
				ProtectFlightInfoDTO protectFlightInfo = new ProtectFlightInfoDTO();
				protectFlightInfo.setSegmentId(segmentId);
				protectFlightInfo.setOriginAirportCode(segment.getOriginPort());
				protectFlightInfo.setDestAirportCode(segment.getDestPort());
				protectFlightInfo.setOperatingCarrierCode(segment.getOperateCompany());
				protectFlightInfos.add(protectFlightInfo);
			}
		}
		
		return protectFlightInfos;
	}

	/**
	 * add RM protection remark into 1A PNR
	 * @param segments
	 * @param acceptSegmentIds
	 * @param rloc
	 * @param acceptFamilyName
	 * @param acceptGivenName
	 * @param session
	 * @param accessChannel 
	 * @throws BusinessBaseException
	 */
	private void addRmProtectionRemark(List<RetrievePnrSegment> segments, List<String> acceptSegmentIds,
			String rloc, String acceptFamilyName, String acceptGivenName, Session session, String accessChannel) throws BusinessBaseException {
		
		String freeText = buildProtectionFreetext(acceptFamilyName, acceptGivenName, segments, acceptSegmentIds, accessChannel);
		AddRemarkBuilder builder = new AddRemarkBuilder();
		PNRAddMultiElements request = builder.buildRequest(rloc, session, freeText);
		addPnrElementsInvokeService.addMutiElements(request, session);
	}

	/**
	 * build protection freeText
	 * @param acceptFamilyName
	 * @param acceptGivenName
	 * @param segments
	 * @param acceptSegmentIds
	 * @param accessChannel 
	 * @return
	 */
	private String buildProtectionFreetext(String acceptFamilyName, String acceptGivenName, List<RetrievePnrSegment> segments,
			List<String> acceptSegmentIds, String accessChannel) {
		/** accept UTC DateTime */
		String acceptUTCDateTime = DateUtil.getCurrentCal2Str(DateUtil.DATE_PATTERN_DD_MMM_YYYY_HH_MM);//25 JUL 2018/04:07
		
		/** accept flightNumber and departureDate */
		StringBuilder flightNoBuilder = new StringBuilder();
		List<String> departureDates = new ArrayList<>();
		for(int i = 0; i < acceptSegmentIds.size(); i++) {
			String acceptSegmentId = acceptSegmentIds.get(i);
			for(RetrievePnrSegment segment : segments) {
				if(StringUtils.isNotEmpty(acceptSegmentId) && acceptSegmentId.equals(segment.getSegmentID())) {
					if(i != 0) {
						flightNoBuilder.append(" ");
					}
					flightNoBuilder.append(segment.getMarketCompany()).append(segment.getMarketSegmentNumber());
					departureDates.add(DateUtil.convertDateFormat(segment.getDepartureTime().getTime(),
							DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, DateUtil.DATE_PATTERN_DD_MMM));
				}
			}
		}
		String flightNo = flightNoBuilder.toString();//CX450 CX455
		String departureDate =  StringUtils.join(departureDates, " ");//10 AUG 11 AUG

		String freeText = String.format(FreeTextUtil.REBOOK_PROTECTION_REMARK_FORMAT, acceptFamilyName, acceptGivenName, acceptUTCDateTime, flightNo, departureDate, accessChannel);
		logger.info(String.format("add RM protection remark:%s", freeText));
		return freeText;
	}

	/**
	 * remove cancelled flight in 1A
	 * @param session
	 * @param cancelledSegmentIds
	 * @return RetrievePnrBooking
	 * @throws BusinessBaseException 
	 */
	private RetrievePnrBooking removeCancelledFlight(Session session, List<String> cancelledSegmentIds, String rloc) throws BusinessBaseException {
		Map<String, List<String>> map = new HashMap<>();
		map.put(OneAConstants.ST_QUALIFIER, cancelledSegmentIds);
		return deletePnrService.deletePnr(rloc, map, session);

	}

	/**
	 * check acceptInfo valid or not.
	 * 
	 * @param acceptInfos
	 * @param retrievePnrBooking
	 * @throws ExpectedException 
	 */
	private void checkAcceptInfos(List<ReBookingAcceptInfoDTO> acceptInfos, RetrievePnrBooking retrievePnrBooking) throws BusinessBaseException {
		// rebook mapping
		List<RetrievePnrRebookMapping> rebookMappings = Optional.ofNullable(retrievePnrBooking.getRebookMapping()).orElse(Collections.emptyList());
		// GDS rebook mapping
		List<RetrievePnrRebookMapping> atciRebookMappings = Optional.ofNullable(retrievePnrBooking.getAtciRebookMapping()).orElse(Collections.emptyList());

		// check rebookMapping in PNR is empty or not
		checkEmptyOfRebookMapping(retrievePnrBooking.getOneARloc(), rebookMappings, atciRebookMappings);
		
		// sort id list for later compare
		rebookMappings.stream().forEach(mapping -> {
			Collections.sort(mapping.getCancelledSegmentIds());
			Collections.sort(mapping.getAcceptSegmentIds());
		});
		
		atciRebookMappings.stream().forEach(mapping -> {
			Collections.sort(mapping.getCancelledSegmentIds());
			Collections.sort(mapping.getAcceptSegmentIds());
		});
		
		for(ReBookingAcceptInfoDTO acceptInfo : acceptInfos) {
			existInPnrRebookMapping(retrievePnrBooking.getOneARloc(), rebookMappings, atciRebookMappings, acceptInfo);
		}
	}
	
	/**
	 * check and build segmentIds from request & PNR
	 * 
	 * @param requestDTO
	 * @param retrievePnrBooking
	 * @param acceptSegmentIds
	 * @param cancelledSegmentIds
	 * @throws BusinessBaseException
	 */
	private void checkAcceptInfos(AcceptFlightRequestDTO requestDTO, RetrievePnrBooking retrievePnrBooking, 
			List<String> acceptSegmentIds, List<String> cancelledSegmentIds) throws BusinessBaseException {
		String rloc = requestDTO.getRloc();
		
		// check rebookMapping in PNR is empty or not
		List<RetrievePnrRebookMapping> rebookMappings = retrievePnrBooking.getRebookMapping();
		checkEmptyOfRebookMapping(rloc, rebookMappings);
		
		List<AcceptInfoDTO> acceptInfos = requestDTO.getAcceptInfos();
		for(AcceptInfoDTO acceptInfo : acceptInfos) {
			List<String> acceptIds = new ArrayList<>();
			List<String> cancelledIds = new ArrayList<>();
			
			//build acceptIds & cancelledIds
			for(AcceptFlightInfoDTO confirmedInfo : acceptInfo.getConfirmedFlightInfos()) {
				buildIdsByCompanyAndNoAndStd(rloc, retrievePnrBooking, acceptIds, confirmedInfo);
			}
			for(AcceptFlightInfoDTO cancelledInfo : acceptInfo.getCancelledFlightInfos()) {
				buildIdsByCompanyAndNoAndStd(rloc, retrievePnrBooking, cancelledIds, cancelledInfo);
			}
			
			// check acceptIds/cancelledIds are pairs in pnrRebookMapping or not
			existInPnrRebookMapping(rloc, rebookMappings, acceptIds, cancelledIds);
			
			acceptSegmentIds.addAll(acceptIds);
			cancelledSegmentIds.addAll(cancelledIds);
		}
	}
	
	/**
	 * check acceptSegmentIds and cancelledSegmentIds are in pnrRebookMapping or not.
	 * @param rloc
	 * @param rebookMappings
	 * @param acceptSegmentIds
	 * @param cancelledSegmentIds
	 * @throws UnexpectedException
	 */
	private void existInPnrRebookMapping(String rloc, List<RetrievePnrRebookMapping> rebookMappings,
			List<String> acceptSegmentIds, List<String> cancelledSegmentIds) throws UnexpectedException {
		if (!existInRebookMapping(acceptSegmentIds, cancelledSegmentIds, rebookMappings)) {
			throw new UnexpectedException(String.format("acceptInfo request is invalid , rloc:[%s]", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
	}

	/**
	 * check acceptSegmentIds and cancelledSegmentIds are in pnrRebookMapping or not.
	 * 
	 * @param rloc
	 * @param rebookMappings
	 * @param atciRebookMappings 
	 * @param acceptInfo
	 * @throws UnexpectedException
	 */
	private void existInPnrRebookMapping(String rloc, List<RetrievePnrRebookMapping> rebookMappings,
			List<RetrievePnrRebookMapping> atciRebookMappings, ReBookingAcceptInfoDTO acceptInfo) throws UnexpectedException {
		if(!existInRebookMapping(rebookMappings, atciRebookMappings, acceptInfo)) {
			throw new UnexpectedException(String.format("acceptInfo request is invalid , rloc:[%s]", rloc), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
	}

	/**
	 * check rebookMapping in PNR is empty or not
	 * 
	 * @param rloc
	 * @param rebookMappings
	 * @throws ExpectedException
	 */
	private void checkEmptyOfRebookMapping(String rloc, List<RetrievePnrRebookMapping> rebookMappings) throws ExpectedException {
		if(CollectionUtils.isEmpty(rebookMappings)) {
			throw new ExpectedException(String.format("booking have no rebook segment to accept, rloc:[%s]", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
	}
	
	/**
	 * check rebookMapping in PNR is empty or not
	 * 
	 * @param rloc
	 * @param rebookMappings
	 * @param atciRebookMappings 
	 * @throws ExpectedException
	 */
	private void checkEmptyOfRebookMapping(String rloc, List<RetrievePnrRebookMapping> rebookMappings, List<RetrievePnrRebookMapping> atciRebookMappings) throws ExpectedException {
		if(CollectionUtils.isEmpty(rebookMappings) && CollectionUtils.isEmpty(atciRebookMappings)) {
			throw new ExpectedException(String.format("booking have no rebook segment to accept, rloc:[%s]", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
	}
	
	/**
	 * to verify acceptInfo is in rebookingMappings or not
	 * @param acceptSegmentIds
	 * @param cancelledSegmentIds
	 * @param rebookMappings
	 * @return boolean
	 */
	private boolean existInRebookMapping(List<String> acceptSegmentIds, List<String> cancelledSegmentIds, List<RetrievePnrRebookMapping> rebookMappings) {
		Collections.sort(cancelledSegmentIds);
		Collections.sort(acceptSegmentIds);		for(RetrievePnrRebookMapping rebookMapping : rebookMappings) {
			if(acceptSegmentIds.equals(rebookMapping.getAcceptSegmentIds())
					&& cancelledSegmentIds.equals(rebookMapping.getCancelledSegmentIds())) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * to verify acceptInfo is in rebookingMappings or not
	 * @param rebookMappings
	 * @param atciRebookMappings 
	 * @param acceptInfo
	 * @return boolean
	 */
	private boolean existInRebookMapping(List<RetrievePnrRebookMapping> rebookMappings, List<RetrievePnrRebookMapping> atciRebookMappings, ReBookingAcceptInfoDTO acceptInfo) {
		List<String> acceptSegmentIds = acceptInfo.getAcceptSegmentIds();
		List<String> cancelledSegmentIds = Optional.ofNullable(acceptInfo.getCancelledSegmentIds()).orElse(Collections.emptyList());
		List<String> atciCancelledSegmentIds = Optional.ofNullable(acceptInfo.getAtciCancelledSegmentIds()).orElse(Collections.emptyList());
		Collections.sort(cancelledSegmentIds);
		Collections.sort(acceptSegmentIds);
		Collections.sort(atciCancelledSegmentIds);
		for(RetrievePnrRebookMapping rebookMapping : rebookMappings) {
			if(acceptSegmentIds.equals(rebookMapping.getAcceptSegmentIds())
					&& cancelledSegmentIds.equals(rebookMapping.getCancelledSegmentIds())) {
				return true;
			}
		}
		
		for(RetrievePnrRebookMapping atciRebookMapping : atciRebookMappings) {
			if(acceptSegmentIds.equals(atciRebookMapping.getAcceptSegmentIds())
					&& (CollectionUtils.isEmpty(atciCancelledSegmentIds) || atciCancelledSegmentIds.equals(atciRebookMapping.getCancelledSegmentIds()))) {
				/**
				 * for GDS ATCI case, all UN flight should has been removed and there shouldn't
				 * be any "cancelledSegmetIds" in the request, so remove all
				 * "cancelledSegmetIds" in case request was incorrectly added with
				 * "cancelledSegmetIds" which may block the following accept operation
				 */
				acceptInfo.setCancelledSegmentIds(Collections.emptyList());
				return true;
			}
		}
		
		return false;
	}

	/**
	 * update all flights status TK->HK
	 * @param session
	 * @param rloc
	 * @param retrievePnrBooking
	 * @param acceptSegmentIds
	 * @return 
	 * @throws BusinessBaseException
	 */
	private void updateFlightsStatus(Session session, String rloc,
			RetrievePnrBooking retrievePnrBooking, List<String> acceptSegmentIds) throws BusinessBaseException {
		List<String> lineNumbers = getSegmentLineNumbers(retrievePnrBooking.getSegments(), acceptSegmentIds);
		for(String lineNumber : lineNumbers) {
			updateFlightStatus(session, lineNumber, rloc);
		}
	}

	/**
	 * get lineNumbers of segments 
	 * @param acceptInfos
	 * @param segments
	 * @param acceptSegmentIds
	 * @return
	 */
	private List<String> getSegmentLineNumbers( List<RetrievePnrSegment> segments,
			List<String> acceptSegmentIds) {
		List<String> lineNumbers = new ArrayList<>();
		for(RetrievePnrSegment segment : segments) {
			if(acceptSegmentIds.contains(segment.getSegmentID())) {
				lineNumbers.add(segment.getLineNumber());
			} 
		}
		return lineNumbers;
	}

	/**
	 * update single flight status TK->HK
	 * @param session
	 * @param lineNumber
	 * @param rloc
	 * @return PNRReply
	 * @throws BusinessBaseException
	 */
	private PNRReply updateFlightStatus(Session session, String lineNumber, String rloc) throws BusinessBaseException {
		ChangePnrElementRequestBuilder requestBuilder = new ChangePnrElementRequestBuilder();
		PNRChangeElement request = requestBuilder.buildRequest(lineNumber + "/HK");
		PNRReply reply = null;
		try {
			reply = changePnrElementInvokeService.changePnrElement(request, session, rloc);
		} catch (Exception e) {
			throw new UnexpectedException(String.format("updateFlightStatus failure, segment lineNumber:%s", lineNumber),
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		return reply;
	}
	
	/**
	 * Notify check-in pax and send boarding pass in email
	 * @param acceptGivenName 
	 * @param acceptFamilyName 
	 * @param rloc 
	 * @throws SoapFaultException
	 */
	/*private void closeSession(Session session) throws SoapFaultException{
		session.setStatus(SessionStatus.END.getStatus());
		pnrSessionInvokService.closeSessionOnly(session);
	}*/

	/**
	 * Notify check-in pax and send boarding pass in email
	 * @param acceptSegmentIds 
	 * @param acceptGivenName 
	 * @param acceptFamilyName 
	 * @param rloc 
	 */
	private void notifyCheckinPax(List<String> acceptSegmentIds, RetrievePnrBooking retrievePnrBooking, String rloc, String acceptFamilyName, String acceptGivenName) {
		olciService.sendJourneyBP(acceptSegmentIds, retrievePnrBooking, rloc, acceptGivenName, acceptFamilyName);
	}
	
	@Override
	public RebookAcceptResponseDTO testSendBP(String acceptedSegmentId, String rloc, String familyName, String givenName) throws BusinessBaseException {
		// retrieve PNR
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		List<String> acceptSegmentIds = Arrays.asList(acceptedSegmentId);
		this.notifyCheckinPax(acceptSegmentIds, retrievePnrBooking, rloc, familyName, givenName);
		RebookAcceptResponseDTO response = new RebookAcceptResponseDTO();
		response.setSuccess(true);
		return response;
	}
}
