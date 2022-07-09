package com.cathaypacific.mmbbizrule.handler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cathaypacific.olciconsumer.model.response.db.OpenCloseTime;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.enums.flightstatus.RtfsStatusEnum;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.config.RtfsStatusConfig;
import com.cathaypacific.mmbbizrule.constant.CommonConstants;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.atcdw.ChangeFlightEligibleResponse;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.service.DpEligibilityService;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.constant.ContactType;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.model.response.PhoneNumberValidationResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.service.MBCommonSvcService;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.AdviceToPassengers;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.Flight;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.FlightStatusData;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.model.SectorDTO;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.repository.FlightStatusRepository;
import com.cathaypacific.mmbbizrule.cxservice.rtfs.service.FlightStatusService;
import com.cathaypacific.mmbbizrule.db.dao.BookingStatusDAO;
import com.cathaypacific.mmbbizrule.db.dao.CabinClassDAO;
import com.cathaypacific.mmbbizrule.db.dao.SpecialMealDAO;
import com.cathaypacific.mmbbizrule.db.dao.TbOfficeIdMappingDAO;
import com.cathaypacific.mmbbizrule.db.model.BookingStatus;
import com.cathaypacific.mmbbizrule.db.model.CabinClass;
import com.cathaypacific.mmbbizrule.db.service.TbOpenCloseTimeCacheHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.AdviceToPassengerInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.RtfsDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.RtfsFlightStatus;
import com.cathaypacific.mmbbizrule.model.booking.detail.RtfsFlightStatusInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.RtfsFlightSummary;
import com.cathaypacific.mmbbizrule.model.booking.detail.RtfsLegSummary;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialMeal;
import com.cathaypacific.mmbbizrule.model.booking.summary.SegmentSummary;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEticket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessCouponGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDetailGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDocGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessFlightInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.mmbbizrule.util.EntityUtil;
import com.cathaypacific.mmbbizrule.util.RtfsUtil;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;

@Component
public class BookingBuildHelper {
	
	private static LogAgent logger = LogAgent.getLogAgent(BookingBuildHelper.class);
	
	@Autowired
	private TbOfficeIdMappingDAO tbOfficeIdMappingDAO;
	
	@Autowired
	private FlightStatusRepository flightStatusRepository;
	
	@Autowired
	private BookingStatusDAO bookingStatusDAO;
	
	@Autowired
	private SpecialMealDAO specialMealDAO;
	
	@Autowired
	private CabinClassDAO cabinClassDAO;
	
	@Autowired
	private RtfsStatusConfig rtfsStatusConfig;
	
	@Autowired
	private FlightStatusService flightStatusService;
	
	@Autowired
	private DpEligibilityService dpEligibilityService;
	
	@Autowired
	private TbOpenCloseTimeCacheHelper tbOpenCloseTimeCacheHelper;
	
	@Autowired
	private MBCommonSvcService mbCommonSvcService;
	
	@Autowired
	private TicketProcessInvokeService ticketProcessInvokeService;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private BizRuleConfig bizRuleConfig;
	
	/**
	 * Check the booking is NDC booking or not
	 * 
	 * @param officeId
	 * @return boolean
	 */
	public boolean isNDCBooking(String officeId) {
		return tbOfficeIdMappingDAO.existByAppCodeAndTypeLikeValue(TBConstants.APP_CODE, TBConstants.TB_OFFICE_ID_MAPPING_TYPE_NDC, officeId) == 1;
	}
	
	/**
	 * 
	 * @param segment
	 * @param staffBooking
	 * @throws UnexpectedException 
	 * @throws ParseException 
	 */
	public void populateChekinWindow(Segment segment, boolean staffBooking) throws UnexpectedException {
		
		Date std = null;
		try {
			std = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, segment.getDepartureTime().getTime(), segment.getDepartureTime().getTimeZoneOffset());
		} catch (ParseException e) {
			throw new UnexpectedException("cannot parse std to date", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW),e);
		}
		int[] openCloseTime =getOpenCloseTimeLimit(segment.getSegmentStatus().getStatus(), staffBooking, segment.getOriginPort(), segment.getOperateCompany(), MMBUtil.getCurrentAppCode(),std);
		 
		if(openCloseTime!=null) {
			segment.setCheckInWindowOpenTimeLimitMins(openCloseTime[0]);
			segment.setCheckInWindowEndTimeLimitMins(openCloseTime[1]);
		}
		int departRemainingMins  = (int) ((std.getTime() - System.currentTimeMillis())/60000);
		segment.setDepartRemainingMins(departRemainingMins);
		segment.setCheckInWindowOpenTimeRemainingMins(departRemainingMins - segment.getCheckInWindowOpenTimeLimitMins());
		segment.setCheckInWindowEndTimeRemainingMins(departRemainingMins - segment.getCheckInWindowEndTimeLimitMins());
		// TODO testing log, will be removed
		logger.info(String.format("segment: %s, departRemainingMins: %s, CheckInWindowOpenTimeLimitMins %s, CheckInWindowEndTimeLimitMins: %s", segment.getSegmentID(), segment.getDepartRemainingMins(), segment.getCheckInWindowOpenTimeLimitMins(), segment.getCheckInWindowEndTimeLimitMins()));
	}
	/**
	 * Get checkin window form DB 
	 * @param segmentStatus
	 * @param staffBooking
	 * @param origin
	 * @param airlineCode
	 * @param appCode
	 * @param std
	 * @return  int[]: length is 3, 0: OpenTimeLimit; 1:CloseTimeLimit; 2:FlightClosedTime
	 */
	public int[] getOpenCloseTimeLimit(FlightStatusEnum segmentStatus, boolean staffBooking, final String origin,
			final String airlineCode, final String appCode, Date std) {

		
		String paxType = TBConstants.TB_TB_OPEN_CLOSE_TIME_PAX_TYPE_C;
		if (staffBooking) {
			if (FlightStatusEnum.STANDBY.equals(segmentStatus) || FlightStatusEnum.WAITLISTED.equals(segmentStatus)) {
				paxType = TBConstants.TB_TB_OPEN_CLOSE_TIME_PAX_TYPE_SS;// SBY
			} else {
				paxType = TBConstants.TB_TB_OPEN_CLOSE_TIME_PAX_TYPE_SB;// BKB
			}
		}
		OpenCloseTime tbOpenCloseTime = tbOpenCloseTimeCacheHelper.findByOriginAndAirlineCodeAndAppCodeAndPaxType(origin,
				airlineCode, appCode, paxType, std);

		if (null != tbOpenCloseTime) {
			int[] openCloseTime = new int[3];
			openCloseTime[0] = tbOpenCloseTime.getOpenTimeLimit();
			openCloseTime[1] = tbOpenCloseTime.getCloseTimeLimit();
			openCloseTime[2] = tbOpenCloseTime.getFlightClosedTime();
			return openCloseTime;
		}
		return null;
	}
	/**
	 * Check the booking is IBE booking or not
	 * 
	 * @param officeId
	 * @return boolean
	 */
	public boolean isIBEBooking(String officeId) {
		return tbOfficeIdMappingDAO.existByAppCodeAndTypeLikeValue(TBConstants.APP_CODE, TBConstants.TB_OFFICE_ID_MAPPING_TYPE_IBE, officeId) == 1;
	}
	
	/**
	 * Check the booking is GCC booking or not
	 * 
	 * @param officeId
	 * @return boolean
	 */
	public boolean isGCCBooking(String officeId) {
		return tbOfficeIdMappingDAO.existByAppCodeAndTypeLikeValue(TBConstants.APP_CODE, TBConstants.TB_OFFICE_ID_MAPPING_TYPE_GCC, officeId) == 1;
	}
	
	/**
	 * Check the booking is ARO booking or not
	 * 
	 * @param officeId
	 * @return boolean
	 */
	public boolean isAROBooking(String officeId) {
		return tbOfficeIdMappingDAO.existByAppCodeAndTypeLikeValue(TBConstants.APP_CODE, TBConstants.TB_OFFICE_ID_MAPPING_TYPE_ARO, officeId) == 1;
	}
	
	/**
	 * Check the booking is ATO booking or not
	 * 
	 * @param officeId
	 * @return boolean
	 */
	public boolean isATOBooking(String officeId) {
		return tbOfficeIdMappingDAO.existByAppCodeAndTypeLikeValue(TBConstants.APP_CODE, TBConstants.TB_OFFICE_ID_MAPPING_TYPE_ATO, officeId) == 1;
	}
	
	/**
	 * Check the booking is CTO booking or not
	 * 
	 * @param officeId
	 * @return boolean
	 */
	public boolean isCTOBooking(String officeId) {
		return tbOfficeIdMappingDAO.existByAppCodeAndTypeLikeValue(TBConstants.APP_CODE, TBConstants.TB_OFFICE_ID_MAPPING_TYPE_CTO, officeId) == 1;
	}
	
	/**
	 * Check the booking is Worldwide by Easyjet booking or not
	 * 
	 * @param officeId
	 * @return boolean
	 */
	public boolean isWORLDBEBooking(String officeId) {
		return tbOfficeIdMappingDAO.existByAppCodeAndTypeLikeValue(TBConstants.APP_CODE, TBConstants.TB_OFFICE_ID_MAPPING_TYPE_WORLDBE, officeId) == 1;
	}
	
	/**
	 * Check the booking is CDWL booking or not
	 * 
	 * @param officeId
	 * @return boolean
	 */
	public boolean isCBWLBooking(String officeId) {
		return tbOfficeIdMappingDAO.existByAppCodeAndTypeLikeValue(TBConstants.APP_CODE, TBConstants.TB_OFFICE_ID_MAPPING_TYPE_CBWL, officeId) == 1;
	}
	
	/**
	 * populate RTFS Time
	 * 
	 * @param segmentSummary
	 */
	public void populateRtfsTime(SegmentSummary segmentSummary) {
		if ((!"CX".equalsIgnoreCase(segmentSummary.getMarketCompany())
				&& !"KA".equalsIgnoreCase(segmentSummary.getMarketCompany()))
				|| StringUtils.isEmpty(segmentSummary.getDepartureTime().getPnrTime())
				|| StringUtils.isEmpty(segmentSummary.getDepartureTime().getTimeZoneOffset())) {
			return;
		}
		
		try {
			if (RtfsUtil.inRtfsTimeframe(segmentSummary.getDepartureTime().getPnrTime(), DepartureArrivalTime.TIME_FORMAT,
					segmentSummary.getDepartureTime().getTimeZoneOffset())) {

				// only call rtfs check depart time in
				List<FlightStatusData> flightStatusDataList = flightStatusRepository.findByFlightNumber(segmentSummary.getMarketCompany(),
						segmentSummary.getMarketSegmentNumber(), segmentSummary.getDepartureTime().getPnrTime());
				FlightStatusData flightStatus = null;
				if(!CollectionUtils.isEmpty(flightStatusDataList)) {
					flightStatus = flightStatusDataList.stream()
							.filter(fs -> isFlightStatusMatched(segmentSummary, fs))
							.findFirst().orElse(null);
				}
				populateRtfsToSegment(segmentSummary, flightStatus);
			}
		} catch (Exception e) {
			logger.error("Populate RTFS for member summary failed summary.", e);
		}
	}
	
	/**
	 * get trainCase by officeId
	 * @param officeId
	 */
	public String getTrainCaseByOfficeId(String officeId) {
		String trainCase = null;
		if(isAROBooking(officeId)) {
			trainCase = CommonConstants.TRAIN_CASE_ARO;
		} else if(isGDSBooking(officeId)) {
			trainCase = CommonConstants.TRAIN_CASE_GDS;
		}
		return trainCase;
	} 
	
	/**
	 * Check the booking is GDS booking or not.
	 * @param officeId
	 */
	public boolean isGDSBooking(String officeId) {
		return StringUtils.isEmpty(officeId) || officeId.length() != 9 
				|| (!OneAConstants.OFFICE_ID_CX0.equals(officeId.substring(3, 6)) && !OneAConstants.OFFICE_ID_KA0.equals(officeId.substring(3, 6)));
	}

	/**
	 * According to flight's OriginPort and Departure time to match corresponding flight.
	 * 
	 * @param segmentSummary
	 * @param flightStatus
	 * @return boolean
	 */
	private boolean isFlightStatusMatched(SegmentSummary segmentSummary, FlightStatusData flightStatus) {
		
		boolean matchedOperateFlightNumber = !CollectionUtils.isEmpty(flightStatus.getSectors()) 
				&& segmentSummary.getMarketCompany().equals(flightStatus.getOperatingFlight().getCarrierCode())
				&& segmentSummary.getMarketSegmentNumber().equals(flightStatus.getOperatingFlight().getFlightNumber());
		
		return flightStatus.getSectors().stream().anyMatch(sector -> {
			boolean matchedMartFlightNumber= false;
			if(!CollectionUtils.isEmpty(sector.getCodeShareFlights())) {
				matchedMartFlightNumber = sector.getCodeShareFlights().stream().anyMatch(f -> 
					segmentSummary.getMarketCompany().equals(f.getCarrierCode()) 
					&& segmentSummary.getMarketSegmentNumber().equals(f.getFlightNumber()));
			}
			if(!matchedOperateFlightNumber&&!matchedMartFlightNumber) {
				return false;
			}
			Date scheduledDate;
			try {
				scheduledDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, segmentSummary.getDepartureTime().getPnrTime());
			} catch (ParseException e) {
				logger.error("Exception thrown in Parsing PNR Scheduled Time: " + segmentSummary.getDepartureTime().getPnrTime(), e);
				return false;
			}
			return segmentSummary.getOriginPort().equals(sector.getOrigin()) && scheduledDate.equals(sector.getDepartScheduled());
		});
	}
	
	/**
	 * populate RTFS to segment
	 * 
	 * @param sector
	 * @param segmentSummary
	 */
	private void populateRtfsToSegment(SegmentSummary segmentSummary, FlightStatusData flightStatus) {
		if (flightStatus != null && !CollectionUtils.isEmpty(flightStatus.getSectors())) {
			for (SectorDTO sector : flightStatus.getSectors()) {
				if (segmentSummary.getOriginPort().equals(sector.getOrigin())) {
					segmentSummary.getDepartureTime().setRtfsScheduledTime(DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getDepartScheduled()));
					segmentSummary.getDepartureTime().setRtfsEstimatedTime(DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getDepartEstimated()));
					segmentSummary.getDepartureTime().setRtfsActualTime(DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getDepartActual()));
				}
				if (segmentSummary.getDestPort().equals(sector.getDestination())) {
					segmentSummary.getArrivalTime().setRtfsScheduledTime(DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getArrivalScheduled()));
					segmentSummary.getArrivalTime().setRtfsEstimatedTime(DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getArrivalEstimated()));
					segmentSummary.getArrivalTime().setRtfsActualTime(DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getArrivalActual()));
				}
			}
		}
	}
	
	/**
	 * populate RTFS to segment
	 * 
	 * @param sector
	 * @param segmentSummary
	 */
	public List<BookingStatus> getAvailableStatusList() {
		return bookingStatusDAO.findAvailableStatus(TBConstants.APP_CODE);
	}
	
	/**
	 * filter out hide segments
	 * 
	 * @param pnrSegmentList
	 * @param availableSegmentStatusList
	 * @return 
	 */
	public BookingStatus getFirstAvailableStatus(List<String> pnrStatusList) {
		List<BookingStatus> availableBookingStatusList = getAvailableStatusList();
		
		if (!CollectionUtils.isEmpty(pnrStatusList)) {
			for (String pnrBookingStatus : pnrStatusList) {
				BookingStatus availableBookingStatus = availableBookingStatusList.stream()
						.filter(bs -> pnrBookingStatus.equals(bs.getStatusCode())).findFirst().orElse(null);
				if (availableBookingStatus != null) {
					return availableBookingStatus;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * populate flight status to segment by flight status data.
	 * 
	 * @param segment
	 * @param flightStatusDataList
	 */
	public void populateFlightDetailTime(Segment segment,List<FlightStatusData> flightStatusDataList) {
		/*
		 * Populate flight status to segment.
		 */
		FlightStatusData flightStatus = new FlightStatusData();
		if(!CollectionUtils.isEmpty(flightStatusDataList)) {
			 flightStatus = flightStatusDataList.stream().filter(fs -> BookingBuildUtil.isFlightStatusMatched(segment, fs))
						.findFirst().orElse(null);
		}
		
		if (flightStatus == null) {
			logger.warn("Flight " + segment.getOperateCompany() + segment.getOperateSegmentNumber() + " "
					+ segment.findDepartureTime().getPnrTime() + " is not found in flight status.");
		} else {
			logger.debug("Populate flight status " + segment.getOperateCompany() + segment.getOperateSegmentNumber()
					+ " " + segment.getOriginPort() + "-" + segment.getDestPort());
			populateFlightStatus(segment, flightStatus);
		}
	}
	
	/**
	 * Populate flight status to PNR flight by giving flight status data.
	 * 
	 * @param segment
	 *            flight of PNR to be populated.
	 * @param flightStatus
	 *            data of flight status.
	 */
	public void populateFlightStatus(Segment segment, FlightStatusData flightStatus) {
		List<SectorDTO> sectorList = flightStatus.getSectors();
		String originPort = segment.getOriginPort();
		String segmentDestport = segment.getDestPort();
		List<SectorDTO> sectorDTOArrayList = BookingBuildUtil.matchAirlineFromOriginToDestport(sectorList, originPort, segmentDestport);
		BookingBuildUtil.setDepartureArrivalTimeForSegment(segment, sectorDTOArrayList);
	}
	
	/**
	 * populate flight status to rtfsStatusInfo by flight status data.
	 * 
	 * @param segment
	 * @param flightStatusDataList
	 */
	public void populateRTFSStatus(Segment segment,List<FlightStatusData> flightStatusDataList) {
		
		//populate flight status to segment.
		if(!CollectionUtils.isEmpty(flightStatusDataList)) {
			FlightStatusData flightStatus = null;
			if(!CollectionUtils.isEmpty(flightStatusDataList)) {
				 flightStatus = flightStatusDataList.stream().filter(fs -> BookingBuildUtil.isFlightStatusMatched(segment, fs))
							.findFirst().orElse(null);
			}
			
			if (flightStatus == null) {
				logger.warn("Flight " + segment.getOperateCompany() + segment.getOperateSegmentNumber() + " "
						+ segment.findDepartureTime().getPnrTime() + " is not found in flight status.");
			} else {
				logger.debug("Populate flight status " + segment.getOperateCompany() + segment.getOperateSegmentNumber()
						+ " " + segment.getOriginPort() + "-" + segment.getDestPort());
				buildRtfsStatusInfo(segment, flightStatus);
			}
		}
		
	}

	/**
	 * build RtfsStatusInfo
	 * 
	 * @param segment
	 * @param flightStatusData
	 */
	public void buildRtfsStatusInfo(Segment segment,FlightStatusData flightStatusData) {
		Flight flight= flightStatusData.getOperatingFlight();
		if(flight == null || CollectionUtils.isEmpty(flightStatusData.getSectors())) {
			return;
		}
		List<SectorDTO> matchSectors = BookingBuildUtil.matchAirlineFromOriginToDestport(flightStatusData.getSectors(),segment.getOriginPort(),segment.getDestPort());
		if(!BookingBuildUtil.hasSpecificFlight(matchSectors,rtfsStatusConfig)){
			return;
		}
		
		if(CollectionUtils.isEmpty(matchSectors)){
			return;
		}
		RtfsFlightStatusInfo flightStatusInfo = new RtfsFlightStatusInfo();
		buildFlightStatusInfo(flightStatusInfo,BookingBuildUtil.filterFinalStatusSectors(matchSectors,flightStatusData.getSectors(),rtfsStatusConfig));
		buildRtfsStatusList(flightStatusInfo,flightStatusData.getSectors());
		segment.setRtfsFlightStatusInfo(flightStatusInfo);
	}
	
	/**
	 * build FlightStatusInfo
	 * @param flightStatusInfo
	 * @param sector
	 */
	public void buildFlightStatusInfo(RtfsFlightStatusInfo flightStatusInfo,List<SectorDTO> sectors) {
		//set flight status for flightStatusInfo
		if(CollectionUtils.isEmpty(sectors)){
			flightStatusInfo.setFlightStatus(RtfsStatusEnum.OTHERSTATUS);
			return;
		}
		Integer scenarioId = sectors.stream().filter(s -> s.getScenarioID() != null
				).findFirst().map(SectorDTO::getScenarioID).orElse(null);
		flightStatusInfo.setFlightStatus(BookingBuildUtil.getRtfsStatusEnum(scenarioId,rtfsStatusConfig));

		// set adviceToPassengers for flightStatusInfo
		List<AdviceToPassengers> advices = new ArrayList<>();
		for (SectorDTO sector : sectors) {
			/**
			 * sectors has rerouted status, get advice on the original flight.
			 */
			if (BookingBuildUtil.isReroutedFlight(sector.getScenarioID(), rtfsStatusConfig)) {
				List<AdviceToPassengers> originalAdvices = sectors.stream()
						.filter(s -> sector.getSequenceId().intValue() == s.getSequenceId().intValue()
								&& !BookingBuildUtil.isReroutedFlight(s.getScenarioID(), rtfsStatusConfig))
						.findFirst().map(SectorDTO::getAdviceToPassengers).orElse(null);
				if (!CollectionUtils.isEmpty(originalAdvices)) {
					advices.addAll(originalAdvices);
				}
			}

			if (!CollectionUtils.isEmpty(sector.getAdviceToPassengers())) {
				advices.addAll(sector.getAdviceToPassengers());
			}
		}
		
		buildAdviceToPassengers(flightStatusInfo,advices);
	}
	
	/**
	 * build flightStatus use filtered sectors
	 * 
	 * @param segment
	 * @param flightStatusData
	 */
	public void buildRtfsStatusList(RtfsFlightStatusInfo flightStatusInfo,List<SectorDTO> sectors) {
		if(CollectionUtils.isEmpty(sectors)) {
			return;
		}
		for (SectorDTO sector : sectors) {
			RtfsFlightStatus rtfsFlightStatus  = new RtfsFlightStatus();
			rtfsFlightStatus.setSequenceId(sector.getSequenceId());
			rtfsFlightStatus.setOriginPort(sector.getOrigin());
			rtfsFlightStatus.setDestPort(sector.getDestination());
			rtfsFlightStatus.setScenarioId(sector.getScenarioID());
			rtfsFlightStatus.setStatus(BookingBuildUtil.getRtfsStatusEnum(sector.getScenarioID(),rtfsStatusConfig));
			setRTFSDepartureTime(rtfsFlightStatus.findDepartureTime(),sector);
			setRTFSArrivalTime(rtfsFlightStatus.findArrivalTime(),sector);
			flightStatusInfo.findRtfsLegsStatus().add(rtfsFlightStatus);
		}
	}
	
	/**
	 * set rtfs departureTime
	 * 
	 * @param departureTime
	 * @param sector
	 */
	public void setRTFSDepartureTime(RtfsDepartureArrivalTime departureTime,SectorDTO sector) {
		departureTime.setRtfsActualTime(
				DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getDepartActual()));
		departureTime.setRtfsScheduledTime(
				DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getDepartScheduled()));
		departureTime.setRtfsEstimatedTime(
				DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getDepartEstimated()));
	}
	
	/**
	 * set rtfs arrivalTime
	 * 
	 * @param arrivalTime
	 * @param sector
	 */
	public void setRTFSArrivalTime(RtfsDepartureArrivalTime arrivalTime,SectorDTO sector) {
		arrivalTime.setRtfsActualTime(
				DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getArrivalActual()));
		arrivalTime.setRtfsScheduledTime(
				DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getArrivalScheduled()));
		arrivalTime.setRtfsEstimatedTime(
				DateUtil.getDate2Str(DepartureArrivalTime.TIME_FORMAT, sector.getArrivalEstimated()));
	}
	
	/**
	 * build advice to passengers
	 * @param rtfsFlightStatus
	 * @param sector
	 */
	public void buildAdviceToPassengers(RtfsFlightStatusInfo flightStatusInfo,List<AdviceToPassengers> advices) {
		if(CollectionUtils.isEmpty(advices)){
			return;
		}
		for (AdviceToPassengers advice : advices) {
			AdviceToPassengerInfo adviceToPassengerInfo = new AdviceToPassengerInfo();
			adviceToPassengerInfo.setArrivalDeparture(advice.getArrivalDeparture());
			adviceToPassengerInfo.setMessage(advice.getMessage());
			flightStatusInfo.findAdviceToPassengers().add(adviceToPassengerInfo);
		}
	}
	
	/**
	 * sort Sectors By SequenceId
	 * @param sectors
	 * @return
	 */
	public static List<SectorDTO> sortSectorsBySequenceId(List<SectorDTO> sectors) {
		return sectors.stream().sorted(Comparator.comparing(SectorDTO::getSequenceId)).collect(Collectors.toList());
	}
	
	/**
	 * get flight status RTFS
	 * 
	 * @param segment
	 * @return
	 */
	public List<FlightStatusData> getFlightStatus(Segment segment) {
		List<FlightStatusData> flightStatusDataList = new ArrayList<>();
		try {
			if (RtfsUtil.inRtfsTimeframe(segment.findDepartureTime().getPnrTime(), DepartureArrivalTime.TIME_FORMAT,
					segment.findDepartureTime().getTimeZoneOffset())) {
				flightStatusDataList = flightStatusService.retrieveFlightStatus(segment.getOperateCompany(),
						segment.getOperateSegmentNumber(), segment.getDepartureTime().getPnrTime());
			} else {
				logger.info(String.format("No need to check RTFS status for flight[%s][%s], because it is not recently flight",
						segment.getOperateCompany() + segment.getOperateSegmentNumber(), segment.findDepartureTime().getPnrTime()));
			}
		} catch (Exception e) {
			logger.warn("Retrieve flight status error", e);
		}
		return flightStatusDataList;
	}
	
	public void populateRtfsSummary(Segment segment, List<FlightStatusData> flightStatusDataList) {
		
		if (CollectionUtils.isEmpty(flightStatusDataList)) {
			return;
		}
		
		FlightStatusData flightStatus = flightStatusDataList.stream().filter(fs -> BookingBuildUtil.isFlightStatusMatched(segment, fs))
				.findFirst().orElse(null);
		if (flightStatus == null) {
			return;
		}
		
		RtfsFlightSummary rtfsFlightSummary = new RtfsFlightSummary();
		
		List<RtfsLegSummary> legs;
		if (CollectionUtils.isEmpty(flightStatus.getSectors())) {
			legs = Collections.emptyList();
		} else {
			legs = flightStatus.getSectors().stream().map(this::buildRtfsLegSummary).collect(Collectors.toList());	
		}
		
		rtfsFlightSummary.setLegs(legs);
		
		segment.setRtfsSummary(rtfsFlightSummary);
	}
	
	private RtfsLegSummary buildRtfsLegSummary(SectorDTO rtfSectorDTO) {
		RtfsLegSummary rtfsLegSummary = new RtfsLegSummary();
		rtfsLegSummary.setSequenceId(rtfSectorDTO.getSequenceId());
		rtfsLegSummary.setOriginPort(rtfSectorDTO.getOrigin());
		rtfsLegSummary.setDestPort(rtfSectorDTO.getDestination());
		rtfsLegSummary.setScenarioId(rtfSectorDTO.getScenarioID());
		return rtfsLegSummary;
	}
	
	/**
	 * check the booking is dw booking 
	 * @param pnrReply
	 * @param pnrDwCodeList
	 * @return
	 */
	public boolean anySectorUnderDynamicWaiver(PNRReply pnrReply, List<String> pnrDwCodeList,String officeId) {

		ChangeFlightEligibleResponse changeFlightEligibleResponse = null;
		try {
			changeFlightEligibleResponse = dpEligibilityService.getAtcDwInfo(pnrReply, officeId);
		} catch (Exception e) {
			logger.error("Get dw info form DP error", e);
		}

		return changeFlightEligibleResponse != null && !StringUtils.isEmpty(changeFlightEligibleResponse.getDwCode())
//				&& changeFlightEligibleResponse.isCanChangeFlight()
//				&& (CollectionUtils.isEmpty(pnrDwCodeList) || !pnrDwCodeList.contains(changeFlightEligibleResponse.getDwCode()))
				;
	}  
	
	/**
	 * check the booking is with dw booking starting from 8 / 9 (DW8/9 indicates refund fee waiver and is configured in Jenkins)
	 *  Example		Existing	New Logic
	 *	8111		MMB			MMB (DW code start from 8 or 9)
	 *	7111		MMB			IBE (no DW code start from 8 or 9)
	 *	No DW code	IBE			IBE
	 * @param pnrReply
	 * @param pnrDwCodeList
	 * @return
	 */
	public boolean anySectorUnderDynamicWaiverStartWithRefundFeeWaiverCode(PNRReply pnrReply, String officeId, String mmbToken, String rloc) {
		List<String> dwCodes = getDwCodes(pnrReply, officeId, mmbToken, rloc);
		
		long matchedDwCodeCnt= (Optional.ofNullable(dwCodes).orElse(Collections.emptyList()).stream()
						.filter(dwCode -> bizRuleConfig.getBookingRefundFeeWaiverDwCodes().stream().anyMatch(dwCode :: startsWith)).count()) ;
		
		return matchedDwCodeCnt > 0;

	}  
	
	/**
	 * get DW codes
	 * @param pnrReply
	 * @param officeId
	 * @param mmbToken
	 * @param rloc
	 * @return List<String>
	 */
	public List<String> getDwCodes(PNRReply pnrReply, String officeId, String mmbToken, String rloc){
		@SuppressWarnings("unchecked")
		List<String> dwCodes = mbTokenCacheRepository.get(mmbToken, TokenCacheKeyEnum.DW_CODE, rloc, ArrayList.class);
		if (!CollectionUtils.isEmpty(dwCodes)) {
			return dwCodes;
		}
		ChangeFlightEligibleResponse changeFlightEligibleResponse = null;
		try {
			changeFlightEligibleResponse = dpEligibilityService.getAtcDwInfo(pnrReply, officeId);
		} catch (Exception e) {
			logger.error("Get dw info form DP error", e);
		}
		
		if (changeFlightEligibleResponse == null || changeFlightEligibleResponse.getDwCodeList()==null) {
			return dwCodes;
		}
		dwCodes = changeFlightEligibleResponse.getDwCodeList();
		mbTokenCacheRepository.add(mmbToken, TokenCacheKeyEnum.DW_CODE, rloc, dwCodes);
		return dwCodes;
	}
	
	/**
	 * check phone number valid
	 * @param phoneCountryNumber
	 * @param phonenNumber
	 * @param contactType
	 * @return
	 */
	public boolean isValidPhoneNumber(String phoneCountryNumber, String phonenNumber, ContactType contactType){
		PhoneNumberValidationResponseDTO phoneNumberValidationResponseDTO = null;
		try {
			 phoneNumberValidationResponseDTO = mbCommonSvcService.validatePhoneNumber(phoneCountryNumber, phonenNumber, contactType);
		} catch (Exception e) {
			logger.error(String.format("Invoke mbcommonsvc service failed. phoneNumber: %s, phoneCountryNumber: %s", phonenNumber, phoneCountryNumber));
		}
		
		return phoneNumberValidationResponseDTO != null && phoneNumberValidationResponseDTO.isValid();
	}
	

	/**
	 * check the flight wifi exist
	 * @param sectors
	 * @return
	 */
	public boolean isAvailableWifiExist(List<SectorDTO> sectors) {
		if (CollectionUtils.isEmpty(sectors)) {
			return false;
		}
		
		return sectors.stream().anyMatch(sector -> {
			if (sector == null || CollectionUtils.isEmpty(sector.getFacilities())) {
				return false;
			}
			return sector.getFacilities().stream().anyMatch(facilitie -> facilitie.getName().equalsIgnoreCase(MMBBizruleConstants.FACILITY_WIFI));
		});
	}
	
	/**
	 * Get true passenger eticketNumber in the segment 
	 * 
	 * @param pnrPassengerSegment
	 * @param pnrSegment
	 * @return
	 * @throws SoapFaultException
	 */
	public String getEticketNumber(RetrievePnrPassengerSegment pnrPassengerSegment, RetrievePnrSegment pnrSegment) throws SoapFaultException {
		if(pnrPassengerSegment == null || pnrSegment == null 
				|| CollectionUtils.isEmpty(pnrPassengerSegment.getEtickets())
				|| pnrSegment.getDepartureTime() == null
				|| StringUtils.isEmpty(pnrSegment.getDepartureTime().getPnrTime())) {
			return null;
		}
		
		List<String> etickets = pnrPassengerSegment.getEtickets().stream().filter(eticket -> eticket != null && StringUtils.isNotEmpty(eticket.getTicketNumber())).map(RetrievePnrEticket::getTicketNumber).distinct().collect(Collectors.toList());
		if(CollectionUtils.isEmpty(etickets)) {
			return null;
		}
		
		TicketProcessInfo ticketProcessInfo = ticketProcessInvokeService.getTicketProcessInfo(OneAConstants.TICKET_PROCESS_EDOC_MESSAGE_FUNCTION_ETICKET, etickets);
		if(ticketProcessInfo == null || CollectionUtils.isEmpty(ticketProcessInfo.getDocGroups())) {
			return null;
		}
		
		for(String eticket : etickets) {
			for(TicketProcessDocGroup docGroup : ticketProcessInfo.getDocGroups()) {
				if (CollectionUtils.isEmpty(docGroup.getDetailInfos())) {
					continue;
				}
				
				for(TicketProcessDetailGroup detailInfo : docGroup.getDetailInfos()){
					if(!eticket.equals(detailInfo.getEticket()) || detailInfo.getCouponGroups() == null) {
						continue;
					}
					
					for(TicketProcessCouponGroup couponInfo : detailInfo.getCouponGroups()) {
						if(couponInfo == null || CollectionUtils.isEmpty(couponInfo.getFlightInfos())) {
							continue;
						}
						
						boolean segmentInfoMatched = segmentInfoMatched(pnrSegment.getMarketCompany(), pnrSegment.getMarketSegmentNumber(),
								pnrSegment.getOriginPort(), pnrSegment.getDestPort(), pnrSegment.getDepartureTime().getPnrTime(), couponInfo);						
						if(segmentInfoMatched) {
							return eticket;
						}
					}
				}
				
			}
		}
		
		return null;
	}
	
	/**
	 * Copy from BookingBuildServiceImpl
	 * 
	 * @Description if the segment info is matched with info in flightInfo
	 * @param marketeCompany
	 * @param marketSegmentNumber
	 * @param originPort
	 * @param destPort
	 * @param pnrTime
	 * @param couponInfo
	 * @return boolean
	 * @author haiwei.jia
	 */
	private boolean segmentInfoMatched(String marketCompany, String marketSegmentNumber, String originPort,
			String destPort, String pnrTime, TicketProcessCouponGroup couponInfo) {
		if(StringUtils.isEmpty(marketCompany)
				|| StringUtils.isEmpty(marketSegmentNumber)
				|| StringUtils.isEmpty(originPort)
				|| StringUtils.isEmpty(destPort)
				|| StringUtils.isEmpty(pnrTime)
				|| couponInfo == null){
			return false;
		}
		
		String pnrTimeStr = DateUtil.convertDateFormat(pnrTime, DepartureArrivalTime.TIME_FORMAT, DateUtil.DATE_PATTERN_DDMMYY);
		
		for(TicketProcessFlightInfo flightInfo : couponInfo.getFlightInfos()){
			if(flightInfo == null 
					|| flightInfo.getFlightDate() == null
					|| StringUtils.isEmpty(flightInfo.getFlightDate().getDepartureTime())
					|| StringUtils.isEmpty(flightInfo.getFlightDate().getDepartureDate())){
				continue;
			}
			// departure time in flightBascInfo, time format ddMMyyHHmm
			//remove departureTime check because it is not needed for ticket call
			/*String departureTimeStr = flightBasicInfo.getFlightDate().getDepartureDate()+flightBasicInfo.getFlightDate().getDepartureTime();
			Date departureTimeDate = null;
			
			try {
				departureTimeDate = DateUtil.getStrToDate(DateUtil.DATE_PATTERN_DDMMYYHHMM, departureTimeStr);
			} catch (ParseException e) {
				logger.warn(String.format("Failed to conver string %s to date",departureTimeStr));
				continue;
			}*/
			
			if(marketCompany.equals(flightInfo.getMarketingCompany())
					&& marketSegmentNumber.equals(flightInfo.getFlightNumber())
					&& originPort.equals(flightInfo.getBoardPoint())
					&& destPort.equals(flightInfo.getOffpoint())
					&& flightInfo.getFlightDate().getDepartureDate().equals(pnrTimeStr)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get special meals
	 * 
	 * @param appCode
	 * @param operateCompany
	 * @param cabinClass
	 * @param originPort
	 * @param destport
	 * @return
	 */
	public List<SpecialMeal> getSpecialMeals(String appCode, String operateCompany, String cabinClass, String originPort, String destport) {
		List<Object[]> mealObjs = specialMealDAO.getMeals(appCode, operateCompany, cabinClass, originPort, destport);
		return EntityUtil.castEntity(mealObjs, SpecialMeal.class);
	}
	
	/**
	 * Get cabinClass by subClass
	 * 
	 * @param subClass
	 * @return
	 */
	public String getCabinClassBySubClass(String subClass) {
		Map<String, String> cabinClassMap = cabinClassDAO.findByAppCode(MMBConstants.APP_CODE).stream()
				.collect(Collectors.toMap(CabinClass::getSubclass, CabinClass::getBasicClass));
		String cabinClass = null;
		if(cabinClassMap != null && StringUtils.isNotBlank(subClass)) {
			cabinClass = cabinClassMap.get(subClass);
		}
		return cabinClass;
	}

	/**
	 * populate the number of gate to segment by flight status data.
	 *
	 * @param segment
	 * @param flightStatusDataList
	 */
	public void populateGateNumber(Segment segment,List<FlightStatusData> flightStatusDataList) {
		/*
		 * Populate flight status to segment.
		 */
		FlightStatusData flightStatus = new FlightStatusData();
		if(!CollectionUtils.isEmpty(flightStatusDataList)) {
			flightStatus = flightStatusDataList.stream().filter(fs -> BookingBuildUtil.isFlightStatusMatched(segment, fs))
					.findFirst().orElse(null);
		}

		if (flightStatus == null) {
			logger.warn("Flight " + segment.getOperateCompany() + segment.getOperateSegmentNumber() + " "
					+ segment.findDepartureTime().getPnrTime() + " is not found in flight status.");
		} else {
			logger.debug("Populate flight status " + segment.getOperateCompany() + segment.getOperateSegmentNumber()
					+ " " + segment.getOriginPort() + "-" + segment.getDestPort());

			List<SectorDTO> sectorList = flightStatus.getSectors();

			String originPort = segment.getOriginPort();

			String segmentDestport = segment.getDestPort();

			List<SectorDTO> sectorDTOArrayList = BookingBuildUtil.matchAirlineFromOriginToDestport(sectorList, originPort, segmentDestport);
			if(CollectionUtils.isEmpty(sectorDTOArrayList)){
				return;
			}else{
				segment.setGateNumber(sectorDTOArrayList.get(0).getGateNumber());
			}
		}
	}
	
}
