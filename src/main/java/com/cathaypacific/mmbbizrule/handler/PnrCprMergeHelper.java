package com.cathaypacific.mmbbizrule.handler;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.booking.LoungeClass;
import com.cathaypacific.mbcommon.enums.error.ErrorTypeEnum;
import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OLCIConstants;
import com.cathaypacific.mmbbizrule.constant.OlciContactInfoEnum;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.constant.ContactType;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.ClaimedLounge;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneySegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.DesAddress;
import com.cathaypacific.mmbbizrule.model.booking.detail.Dob;
import com.cathaypacific.mmbbizrule.model.booking.detail.EmrContactInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Journey;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.PhoneInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.PurchasedLounge;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.TravelDoc;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.olciconsumer.model.response.CheckInOpenCloseTimeDTO;
import com.cathaypacific.olciconsumer.model.response.ContactDTO;
import com.cathaypacific.olciconsumer.model.response.DateDTO;
import com.cathaypacific.olciconsumer.model.response.DepartureArrivalTimeDTO;
import com.cathaypacific.olciconsumer.model.response.FlightDTO;
import com.cathaypacific.olciconsumer.model.response.JourneyDTO;
import com.cathaypacific.olciconsumer.model.response.LoginResponseDTO;
import com.cathaypacific.olciconsumer.model.response.PassengerDTO;
import com.cathaypacific.olciconsumer.model.response.TravelDocumentDTO;

@Component
public class PnrCprMergeHelper {

	private static LogAgent logger = LogAgent.getLogAgent(PnrCprMergeHelper.class);

	private static final String CPR_PASSENGER_ID_PREFIX = "P";
	private static final String CPR_SEGMENT_ID_PREFIX = "S";

	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;

	@Autowired
	private BookingBuildHelper bookingBuildHelper;

	/**
	 * Create booking model by CPR
	 * @param cpr
	 * @return
	 */
	public Booking buildBookingModelByCpr(LoginResponseDTO cpr) {
		if(cpr == null || CollectionUtils.isEmpty(cpr.getJourneys())
				|| !CollectionUtils.isEmpty(getErrorInfosByType(cpr.getJourneys(), OLCIConstants.CPR_ERROR_INFO_TYPE_S))) {
			return null;
		}

		List<JourneyDTO> journeys = cpr.getJourneys();

		Booking booking = new Booking();
		booking.setGotCpr(true);
		booking.setBasedOnCPR(true);

		booking.setOneARloc(getRlocFromJourney(journeys));
		booking.setPassengers(buildPassegners(getCprPassegners(journeys)));
		booking.setSegments(buildSegments(getCprSegments(journeys)));
		buildPassengerSegments(booking, journeys);
		buildBookingBaseInfo(booking, journeys);

		/** merge CPR contactInfo ... to booking */
		mergeCprToBookingModel(booking, cpr, new BookingBuildRequired());
		return booking;
	}

	/**
	 * Merge CPR To booking model
	 *
	 * @param booking
	 * @param cpr
	 * @param required 
	 */
	public void mergeCprToBookingModel(Booking booking, LoginResponseDTO cpr, BookingBuildRequired required) {
		if(booking == null || cpr == null || CollectionUtils.isEmpty(cpr.getJourneys())) {
			mergeErrors(booking,cpr);
			return;
		}
		// add flag to redis if get cpr success.
		mbTokenCacheRepository.add(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.GET_CPR_SUCCESS, booking.getOneARloc(), true);

		booking.setGotCpr(true);
		mergeCprBookingBaseInfo(booking, cpr);
		// merge cpr passenger info to pnr
        mergeCprPassengers(booking.getPassengers(), booking.getPassengerSegments(), cpr.getJourneys(), required.getJourneyId());
        // staff link booking
        if(required.isLinkBookingCheck()) {
            rebuildBookingForStaffLink(booking, cpr);
        }
		mergeCprPassengerSegments(booking, cpr.getJourneys());
		mergeCprJourneys(booking, cpr.getJourneys());
		
		List<JourneyDTO> cprOpenToCheckInJourneys = getOpenToCheckInJourneys(booking.getCprJourneys(), cpr.getJourneys());
		mergeCprPassengersDetail(booking.getPassengers(), cprOpenToCheckInJourneys, required.getJourneyId());
		mergeCprPassengerSegmentsDetail(booking.getPassengerSegments(), cprOpenToCheckInJourneys);

		//change logic like v2 cancheckin in segment.
		mergeCheckInFlag(booking);
	}
	
	/**
	 * Check if journey is valid to be merged.
	 * 
	 * @param cprJourney
	 * @return
	 */
	private List<JourneyDTO> getOpenToCheckInJourneys(List<Journey> mergedJourneys, List<JourneyDTO> cprJourneys) {
		if (CollectionUtils.isEmpty(mergedJourneys) || CollectionUtils.isEmpty(cprJourneys)) {
			return Collections.emptyList();
		}
		
		List<String> openToCheckInJourneyIds = mergedJourneys.stream()
				.filter(Journey::isOpenToCheckIn)
				.map(Journey::getJourneyId)
				.collect(Collectors.toList());
		
		return cprJourneys.stream().filter(
				jny -> openToCheckInJourneyIds.contains(jny.getJourneyId())
			).collect(Collectors.toList());
	}

    /**
     * @param booking
     * @param cpr
     */
    private void rebuildBookingForStaffLink(Booking booking, LoginResponseDTO cpr) {
        // will do this only when we found staff link booking from cpr
        // based on cpr booking will not run this logic
        // mobile will not run staff link logic
        // this is temp logic, we should use bookingbuildrequired -> checkstafflink 
        // to control booking build logic
        if (hasStaffLinkInfoFromCpr(cpr) && !booking.isBasedOnCPR()) {
            logger.info("detect staff link info from olci response, rebuild booking");
            // generate passengers from cpr response
            List<Passenger> cprPassengers = buildPassegners(getCprPassegners(cpr.getJourneys()));
            // find passgers which in cpr but not in pnr
            List<Passenger> removedPassenger = cprPassengers.stream().filter(passenger -> !inPnrPassenger(booking.getPassengers(),passenger)).collect(Collectors.toList());
            // add back passengers to pnr
            booking.getPassengers().addAll(removedPassenger);
            booking.setPassengers(booking.getPassengers());
            // due to add new passenger from cpr so rebuild passenger and flight connection
            buildPassengerSegments(booking, cpr.getJourneys());  
        }
    }

    /**
     * has staff link info from cpr
     * @param cpr
     * @return
     */
    private boolean hasStaffLinkInfoFromCpr(LoginResponseDTO cpr) {
        if (null != cpr && !CollectionUtils.isEmpty(cpr.getJourneys())) {
            return cpr.getJourneys().stream()
                    .filter(journey -> null != journey && !CollectionUtils.isEmpty(journey.getPassengers()))
                    .flatMap(journey -> journey.getPassengers().stream()).filter(passenger -> null != passenger)
                    .anyMatch(passenger -> passenger.isMergedPassenger());
        }
        return false;
    }

    /**
     * cpr passenger found in pnr list
     * @param pnrPassengers
     * @param cprPassenger
     * @return
     */
    private boolean inPnrPassenger(List<Passenger> pnrPassengers, Passenger cprPassenger) {
	    return pnrPassengers.stream().anyMatch(pnrPassenger -> pnrPassenger.getCprUniqueCustomerId().equals(cprPassenger.getCprUniqueCustomerId()));
    }

    /**
	 * Merge CPR booking base information
	 * 
	 * @param booking
	 * @param cpr
	 */
	private void mergeCprBookingBaseInfo(Booking booking, LoginResponseDTO cpr) {
		if((booking == null || cpr == null)) {
			return;
		}
		
		//merged booking rlocs
		booking.setMergedRlocs(cpr.getMergedRlocs());
	}

	/**
	 * Get errorInfos by errorType from CPR
	 *
	 * @param cprJourneys
	 * @param errorType
	 * @return
	 */
	private List<ErrorInfo> getErrorInfosByType(List<JourneyDTO> cprJourneys, String errorType) {
		return covertErrorInfos(getCprErrorInfosByType(cprJourneys, errorType));
	}

	/**
	 * Get CPR errorInfos by errorType from CPR
	 *
	 * @param cprJourneys
	 * @param errorType
	 * @return
	 */
	private List<com.cathaypacific.olciconsumer.model.response.ErrorInfo> getCprErrorInfosByType(List<JourneyDTO> cprJourneys, String errorType) {
		if(CollectionUtils.isEmpty(cprJourneys) || StringUtils.isEmpty(errorType)) {
			return null;
		}

		return cprJourneys.stream().filter(journey->CollectionUtils.isNotEmpty(journey.getErrors())).map(JourneyDTO::getErrors).flatMap(Collection::stream)
				.filter(e -> e != null && errorType.equals(e.getType())).collect(Collectors.toList());

	}

	/**
	 * Build booking base information
	 *
	 * @param booking
	 * @param journeys
	 */
	private void buildBookingBaseInfo(Booking booking, List<JourneyDTO> journeys) {
		PassengerDTO cprPassenger = Optional.ofNullable(journeys.get(0).getPassengers()).orElse(new ArrayList<>()).get(0);
		if(cprPassenger == null) {
			return;
		}

		booking.setCprStaffBooking(cprPassenger.isStaffBooking());
		booking.setMiceBooking(cprPassenger.getIsMiceBooking());
	}

	/**
	 * merge Check In Flag
	 * @param booking
	 */
	private void mergeCheckInFlag(Booking booking) {
		if(CollectionUtils.isEmpty(booking.getCprJourneys())){
			return;
		}
		for(Journey journey: booking.getCprJourneys()){
			if(!CollectionUtils.isEmpty(journey.getSegments())){
				for(CprJourneySegment cprJourneySegment : journey.getSegments()){
					if(cprJourneySegment == null){
						continue;
					}
					Segment segment = getSegmentBySegmentId(booking.getSegments(),cprJourneySegment.getSegmentId());
					if(segment != null){
						segment.setCanCheckIn(cprJourneySegment.getCanCheckIn());
						segment.setCheckedIn(journey.getPassengerSegments().stream().filter(ps -> ps.getSegmentId().equals(segment.getSegmentID()))
								.anyMatch(ps -> ps != null && ps.getCheckedIn() == true));
					}
				}
			}

			if(!CollectionUtils.isEmpty(journey.getPassengerSegments())){
				for(CprJourneyPassengerSegment cprJourneyPassengerSegment : journey.getPassengerSegments()){
					if(cprJourneyPassengerSegment == null){
						continue;
					}
					PassengerSegment passengerSegment = getPassengerSegmentBySegmentIdAndPassengerId(booking.getPassengerSegments(), cprJourneyPassengerSegment.getSegmentId(), cprJourneyPassengerSegment.getPassengerId());
					if(passengerSegment != null){
						passengerSegment.setCheckedIn(cprJourneyPassengerSegment.getCheckedIn());
					}
				}
			}
		}
		if (CollectionUtils.isEmpty(booking.getCprJourneys())) {
			booking.setCanCheckIn(false);
		}else {
			//use journey level can checkIn flag
			booking.setCanCheckIn(booking.getCprJourneys().stream().anyMatch(Journey::isCanCheckIn));
		}
	}

	/**
	 * get Segment By SegmentId
	 * @param segments
	 * @param segmentId
	 * @return
	 */
	private Segment getSegmentBySegmentId(List<Segment> segments, String segmentId) {
		if(CollectionUtils.isEmpty(segments)){
			return null;
		}
		return segments.stream().filter(segment -> Objects.equals(segment.getSegmentID(), segmentId)).findFirst().orElse(null);
	}

	/**
	 * get PassengerSegment By SegmentId And PassengerId
	 * @param passengerSegments
	 * @param segmentId
	 * @param passengerId
	 * @return
	 */
	private PassengerSegment getPassengerSegmentBySegmentIdAndPassengerId(
			List<PassengerSegment> passengerSegments, String segmentId, String passengerId) {
		if(CollectionUtils.isEmpty(passengerSegments)){
			return null;
		}
		return passengerSegments.stream().filter(ps -> Objects.equals(ps.getSegmentId(), segmentId) && Objects.equals(ps.getPassengerId(), passengerId)).findFirst().orElse(null);
	}

	/**
	 * Merge CPR journeys to booking
	 *
	 * @param booking
	 * @param journeys
	 */
	private void mergeCprJourneys(Booking booking, List<JourneyDTO> cprJourneys) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengers())
				|| CollectionUtils.isEmpty(booking.getSegments())
				|| CollectionUtils.isEmpty(booking.getPassengerSegments())
				|| CollectionUtils.isEmpty(cprJourneys)) {
			return;
		}

		List<Journey> journeys = new ArrayList<>();

		/**
		 * If any journeys contains any error with errorType="S",
		 * Stop, return the journeys with these S errors
		 * */
		List<ErrorInfo> errorInfos = getErrorInfosByType(cprJourneys, OLCIConstants.CPR_ERROR_INFO_TYPE_S);
		if(CollectionUtils.isNotEmpty(errorInfos)) {
			Journey journey = new Journey();
			journey.setErrors(errorInfos);
			journeys.add(journey);
			booking.setCprJourneys(journeys);
			logger.info(String.format("mergeCprJourneys[%s] -> errorInfos with type S exist. return the journey with these errors", booking.getOneARloc()));
			return;
		}

		for(JourneyDTO cprJourney : cprJourneys) {
			Journey journey = new Journey();
			List<CprJourneySegment> cprJourneySegments = buildJourneySegments(booking.getSegments(), cprJourney.getPassengers());

			CprJourneySegment firstCprJourneySegment = Optional.ofNullable(cprJourneySegments).orElseGet(Collections::emptyList).stream().filter(seg -> seg != null && !seg.isDisplayOnly()).findFirst().orElse(null);
			Segment firstSegment =  BookingBuildUtil.getSegmentById(booking.getSegments(), firstCprJourneySegment != null? firstCprJourneySegment.getSegmentId() : null);

			journey.setJourneyId(cprJourney.getJourneyId());
			journey.setDisplayOnly(cprJourney.isDisplayOnly());
			journey.setInhibitUSBP(cprJourney.getInhibitUSBP());
			journey.setAllowMBP(cprJourney.getAllowMBP());
			journey.setAllowSPBP(cprJourney.getAllowSPBP());
			journey.setOpenCloseTime(cprJourney.getOpenCloseTime());
			journey.setNextOpenCloseTime(cprJourney.getNextOpenCloseTime());
			journey.setOcciEligible(cprJourney.isOcciEligible());
			journey.setErrors(covertErrorInfos(cprJourney.getErrors()));
			journey.setPriorityCheckInEligible(journey.isPriorityCheckInEligible(firstSegment, booking.isStaffBooking()));

			journey.setPassengers(buildJourneyPassengers(booking.getPassengers(), cprJourney.getPassengers()));
			journey.setSegments(cprJourneySegments);
			journey.setPassengerSegments(buildJourneyPassengerSegments(booking, cprJourney.getPassengers(), journey));
			populateCheckInWindow(journey, booking, firstSegment);
			journeys.add(journey);

		}

		if(CollectionUtils.isNotEmpty(journeys)) {
			booking.setCprJourneys(journeys);
		}
	}

	/**
	 * populate CheckIn Window in for to journey
	 * @param journey
	 * @param booking
	 */
	private void populateCheckInWindow(Journey journey,Booking booking, Segment firstSegment) {
		List<String> segmentIds = journey.getSegments().stream().map(seg->seg.getSegmentId()).collect(Collectors.toList());
		journey.setOpenToCheckIn(booking.getSegments().stream().anyMatch(seg->segmentIds.contains(seg.getSegmentID()) && seg.isOpenToCheckIn()));
		journey.setPostCheckIn(booking.getSegments().stream().filter(seg->segmentIds.contains(seg.getSegmentID())).allMatch(seg->seg.isPostCheckIn()));
		journey.setBeforeCheckIn(!journey.isDisplayOnly() && isJourneyBeforeCheckIn(firstSegment, journey.getOpenCloseTime()));
	}


	/**
	 * Check if segment is before check in window
	 * @param segment
	 * @return
	 */

	private boolean isJourneyBeforeCheckIn(Segment segment, CheckInOpenCloseTimeDTO openCloseTime) {
		// check whether the segment or openCloseTime is empty or not
		if (segment == null || openCloseTime == null) return false;

		try {
			Date departureTime = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, segment.getDepartureTime().getTime(), segment.getDepartureTime().getTimeZoneOffset());
			Date afterTime = new Date(System.currentTimeMillis() + openCloseTime.getOpenTimeLimit() * 60 * 1000);

			return org.apache.commons.lang.BooleanUtils.isNotTrue(segment.isFlown())
					&& (segment.getSegmentStatus() != null && !FlightStatusEnum.CANCELLED.equals(segment.getSegmentStatus().getStatus()))
					&& BizRulesUtil.isFlight(segment.getAirCraftType())
					&& !segment.isPostCheckIn()
					&& departureTime.after(afterTime);
		} catch (Exception e) {
			logger.error("Error to convert departure time", e);
			return false;
		}
	}


	/**
	 * Build journey segments
	 *
	 * @param segments
	 * @param passengers
	 * @return
	 */
	private List<CprJourneySegment> buildJourneySegments(List<Segment> segments, List<PassengerDTO> cprPassengers) {
		if(CollectionUtils.isEmpty(segments) || CollectionUtils.isEmpty(cprPassengers)) {
			return null;
		}

		List<CprJourneySegment> journeySegments = new ArrayList<>();
		for(Segment segment : segments) {
			if(segment == null) {
				continue;
			}

			List<FlightDTO> cprSegments = cprPassengers.stream().map(PassengerDTO::getFlights).flatMap(Collection::stream).collect(Collectors.toList());
			FlightDTO cprSegment = getCprSegmentByBaseInfo(cprSegments, segment);
			if(cprSegment != null) {
				CprJourneySegment cprJourneySegment = new CprJourneySegment();
				cprJourneySegment.setSegmentId(segment.getSegmentID());
				/**
				 * Using first segment to set segment's "displayOnly" & "canCheckIn"
				 * because field displayOnly is set by flight's OD port in OLCI,
				 * we can use any the same segments under all journeys to set "displayOnly".
				 * */
				cprJourneySegment.setDisplayOnly(cprSegment.isDisplayOnly());
				cprJourneySegment.setCanCheckIn(!cprSegment.isDisplayOnly() && !cprSegment.isInhibited()
						//block post checkin case because OLCI will return the post checkin window flights.
						&& !segment.isPostCheckIn());

				// combine & distinct all errorInfos of segments with the same base information.
				cprJourneySegment.setErrors(covertErrorInfos(cprSegment.getErrors()));

				journeySegments.add(cprJourneySegment);
			}
		}

		return journeySegments;
	}

	/**
	 * Wrapper class for ErrorInfo from OLCI
	 */
	class WrapperCprErrorInfo {
		private com.cathaypacific.olciconsumer.model.response.ErrorInfo e;
		public WrapperCprErrorInfo(com.cathaypacific.olciconsumer.model.response.ErrorInfo e) {
			this.e = e;
		}
		public com.cathaypacific.olciconsumer.model.response.ErrorInfo unwrap() {
			return this.e;
		}
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			WrapperCprErrorInfo that = (WrapperCprErrorInfo) o;
			return Objects.equals(e.getErrorCode() + e.getType() + e.getFieldName(), that.e.getErrorCode() + that.e.getType() + that.e.getFieldName());
		}
		@Override
		public int hashCode() {
			return Objects.hash(e.getErrorCode() + e.getType() + e.getFieldName());
		}
	}

	/**
	 * Build journey passengers
	 *
	 * @param passengers
	 * @param cprPassengers
	 * @return
	 */
	private List<CprJourneyPassenger> buildJourneyPassengers(List<Passenger> passengers, List<PassengerDTO> cprPassengers) {
		if(CollectionUtils.isEmpty(passengers) || CollectionUtils.isEmpty(cprPassengers)) {
			return null;
		}

		List<CprJourneyPassenger> cprJourneyPassengers = new ArrayList<>();
		for(Passenger passenger : passengers) {
			if(passenger == null) {
				continue;
			}

			PassengerDTO cprPassenger = getCprPassengerByCprUniqueCustomerId(cprPassengers, passenger.getCprUniqueCustomerId());

			if(cprPassenger != null) {
				CprJourneyPassenger cprJourneyPassenger = new CprJourneyPassenger();
				cprJourneyPassenger.setPassengerId(passenger.getPassengerId());
				cprJourneyPassenger.setCprUniqueCustomerId(cprPassenger.getUniqueCustomerId());
				cprJourneyPassenger.setInhibitBP(cprPassenger.isInhibitBP());
				cprJourneyPassenger.setAllowMBP(cprPassenger.getAllowMBP());
				cprJourneyPassenger.setAllowSPBP(cprPassenger.isAllowSPBP());
				cprJourneyPassenger.setDisplayOnly(cprPassenger.isDisplayOnly());
				cprJourneyPassenger.setCanCheckIn(buildCprPassengerCanCheckIn(cprPassenger));
				cprJourneyPassenger.setCheckedIn(cprPassenger.isCheckInAccepted());
				cprJourneyPassenger.setCheckInStandBy(cprPassenger.isStandBy());
				cprJourneyPassenger.setErrors(covertErrorInfos(cprPassenger.getErrors()));
				cprJourneyPassenger.setCanCancelCheckIn(!cprPassenger.isInhibitCancelCheckIn());
				cprJourneyPassengers.add(cprJourneyPassenger);
			}
		}

		return cprJourneyPassengers;
	}

	/**
	 * Passenger can checkIn:
	 * 1. displayOnly = false,
	 * 2. have no error except "E13Z00119" with type="D"
	 * 3. have no error except "E13Z00260" with type="D"
	 *
	 * @param cprPassenger
	 * @return
	 */
	private boolean buildCprPassengerCanCheckIn(PassengerDTO cprPassenger) {
		if(cprPassenger == null) {
			return false;
		}

		boolean checkInErrorExist = Optional.ofNullable(cprPassenger.getErrors()).orElse(new ArrayList<>()).stream()
				.anyMatch(e -> OLCIConstants.CPR_ERROR_INFO_TYPE_D.equals(e.getType()) && !OLCIConstants.CPR_PASSENGER_CAN_CHECKIN_EXCEPT_EXCEPTION_CODE.equals(e.getErrorCode())
						&& !OLCIConstants.CPR_PASSENGER_PARTIAL_ACCPENTED_JOURNEY.equals(e.getErrorCode()));

		return !cprPassenger.isDisplayOnly() && !checkInErrorExist;
	}

	/**
	 * Get cprPassenger by cprUniqueCustomerId
	 *
	 * @param cprPassengers
	 * @param cprUniqueCustomerId
	 * @return
	 */
	public PassengerDTO getCprPassengerByCprUniqueCustomerId(List<PassengerDTO> cprPassengers, String cprUniqueCustomerId) {
		if(CollectionUtils.isEmpty(cprPassengers) || StringUtils.isEmpty(cprUniqueCustomerId)) {
			return null;
		}

		return cprPassengers.stream().filter(p -> p != null && cprUniqueCustomerId.equals(p.getUniqueCustomerId())).findFirst().orElse(null);
	}
	
	/**
     * covert Olci pax type to MMB pax type
     * @param olciErrorType
     * @return
     */
    public static String covertOlciPaxTypeToMmb(String paxType) {

        String mmbPaxType = "";
        switch (paxType) {
        case OLCIConstants.CUSTOMERLEVEL_PAX_TYPE_ADULT:
            mmbPaxType = OneAConstants.PASSENGER_TYPE_DEFAULT;
            break;
        case OLCIConstants.CUSTOMERLEVEL_PAX_TYPE_INFANT:
            mmbPaxType = OneAConstants.PASSENGER_TYPE_INF;
            break;
        case OLCIConstants.CUSTOMERLEVEL_PAX_TYPE_INFANT_SEAT:
            mmbPaxType = OneAConstants.PASSENGER_TYPE_INS;
            break;
        case OLCIConstants.CUSTOMERLEVEL_PAX_TYPE_CHILDREN:
            mmbPaxType = OneAConstants.PASSENGER_TYPE_CHILD;
            break;
        default:
            mmbPaxType = OneAConstants.PASSENGER_TYPE_DEFAULT;
            break;
        }
        return mmbPaxType;
    }

	/**
	 * covert Olci error type to MMB error enum
	 * @param olciErrorType
	 * @return
	 */
	public static ErrorTypeEnum covertOlciErrorTypeToMmb(String olciErrorType) {

		ErrorTypeEnum mbErrorType = null;
		switch (olciErrorType) {
		case OLCIConstants.CPR_ERROR_INFO_TYPE_D:
			mbErrorType = ErrorTypeEnum.BUSERROR;
			break;
		case OLCIConstants.CPR_ERROR_INFO_TYPE_L:
			mbErrorType = ErrorTypeEnum.REMINDER;
			break;
		case OLCIConstants.CPR_ERROR_INFO_TYPE_W:
			mbErrorType = ErrorTypeEnum.WARNING;
			break;
		case OLCIConstants.CPR_ERROR_INFO_TYPE_S:
			mbErrorType = ErrorTypeEnum.SYSERROR;
			break;
		default:
			mbErrorType = ErrorTypeEnum.BUSERROR;
			break;
		}
		return mbErrorType;
	}

	/**
	 * Covert errorInfo in CPR to the format of PNR errorInfo
	 * S - Stop & standBy
	 * B - By Pass, ignore
	 * D - Display, E
	 * L - LightBox, L
	 *
	 * @param cprErrorInfos
	 * @return
	 */
	public static List<ErrorInfo> covertErrorInfos(List<com.cathaypacific.olciconsumer.model.response.ErrorInfo> cprErrorInfos) {
		if(CollectionUtils.isEmpty(cprErrorInfos)) {
			return null;
		}

		List<ErrorInfo> errorInfos = new ArrayList<>();
		for(com.cathaypacific.olciconsumer.model.response.ErrorInfo cprErrorInfo : cprErrorInfos) {
			if(OLCIConstants.CPR_ERROR_INFO_TYPE_B.equals(cprErrorInfo.getType())){
				continue;
			}
			ErrorInfo errorInfo = new ErrorInfo();
			errorInfo.setType(covertOlciErrorTypeToMmb(cprErrorInfo.getType()));
			errorInfo.setErrorCode(cprErrorInfo.getErrorCode());
			errorInfo.setFieldName(cprErrorInfo.getFieldName());

			errorInfos.add(errorInfo);
		}

		return errorInfos;
	}

	/**
	 * Build Journey passengerSegments for booking
	 *
	 * @param booking
	 * @param journey
	 * @param passengers
	 * @return
	 */
	private List<CprJourneyPassengerSegment> buildJourneyPassengerSegments(Booking booking, List<PassengerDTO> cprPassengers, Journey journey) {
		List<Passenger> passengers = booking.getPassengers();
		List<PassengerSegment> passengerSegments = booking.getPassengerSegments();

		List<CprJourneyPassengerSegment> journeyPassengerSegments = new ArrayList<>();
		for(PassengerDTO cprPassenger : cprPassengers) {
			Passenger passenger = getPassengerByCprUniqueCustomerId(passengers, cprPassenger.getUniqueCustomerId());
			if(passenger == null) {
				logger.info(String.format("Build JourneyPassengerSegments - can't find passenger in booking[%s] by cprPassenger UniqueCustomerId[%s]",
						booking.getOneARloc(), cprPassenger.getUniqueCustomerId()));
				continue;
			}

			for(FlightDTO cprSegment : cprPassenger.getFlights()) {
				CprJourneyPassengerSegment journeyPassengerSegment = new CprJourneyPassengerSegment();

				/** segment Info*/
				PassengerSegment passengerSegment = getPassengerSegmentByCprIds(passengerSegments, cprPassenger.getUniqueCustomerId(), cprSegment.getProductIdentifierDID(), cprSegment.getProductIdentifierJID());
				if(passengerSegment == null) {
					logger.info(String.format("Build JourneyPassengerSegments - can't find passengerSegment in booking[%s] by cprPassenger's UniqueCustomerId[%s] & cprSegment's DID[%s] JID[%s]",
							booking.getOneARloc(), cprPassenger.getUniqueCustomerId(), cprSegment.getProductIdentifierDID(), cprSegment.getProductIdentifierJID()));
					continue;
				}
				journeyPassengerSegment.setSegmentId(passengerSegment.getSegmentId());
				journeyPassengerSegment.setCprProductIdentifierDID(cprSegment.getProductIdentifierDID());
				journeyPassengerSegment.setCprProductIdentifierJID(cprSegment.getProductIdentifierJID());

				/** passenger Info*/
				journeyPassengerSegment.setPassengerId(passenger.getPassengerId());
				journeyPassengerSegment.setCprUniqueCustomerId(cprPassenger.getUniqueCustomerId());

				/**canCheckIn | checkedIn */
				journeyPassengerSegment.setCheckedIn(cprSegment.isCheckInAccepted());
				journeyPassengerSegment.setCanCheckIn(buildJourneyPassengerSegmentCanCheckIn(journey, passengerSegment.getSegmentId(), passenger.getPassengerId()));

				/** stand by status | stand by security number */
				journeyPassengerSegment.setCheckInStandBy(cprSegment.isStandBy());
				journeyPassengerSegment.setSecurityNumber(cprSegment.getSecurityNumber());
				
				journeyPassengerSegments.add(journeyPassengerSegment);
			}
		}

		return journeyPassengerSegments;
	}

	/**
	 * Build canCheckIn in JourneyPassengerSegment by passenger's & segment's canCheckIn
	 *
	 * @param journey
	 * @param segmentId
	 * @param passengerId
	 * @return
	 */
	private boolean buildJourneyPassengerSegmentCanCheckIn(Journey journey, String segmentId, String passengerId) {
		if(journey == null || StringUtils.isEmpty(segmentId)
				|| StringUtils.isEmpty(passengerId)
				|| CollectionUtils.isEmpty(journey.getSegments())
				|| CollectionUtils.isEmpty(journey.getPassengers())) {
			return false;
		}

		CprJourneyPassenger journeyPassenger = journey.getPassengers().stream().filter(p -> passengerId.equals(p.getPassengerId())).findFirst().orElse(null);
		CprJourneySegment journeySegment = journey.getSegments().stream().filter(s -> segmentId.equals(s.getSegmentId())).findFirst().orElse(null);
		return (journeyPassenger == null || journeySegment == null) ? false : (journeyPassenger.getCanCheckIn() && journeySegment.getCanCheckIn());
	}

	/**
	 * Get passengerSegment by uniqueCustomerId, productIdentifierDID and productIdentifierJID
	 *
	 * @param passengerSegments
	 * @param uniqueCustomerId
	 * @param productIdentifierDID
	 * @param productIdentifierJID
	 * @return
	 */
	private PassengerSegment getPassengerSegmentByCprIds(List<PassengerSegment> passengerSegments, String uniqueCustomerId, String productIdentifierDID, String productIdentifierJID) {
		return passengerSegments.stream().filter(ps -> ps != null
				&& Objects.equals(ps.getCprUniqueCustomerId(), uniqueCustomerId)
				&& Objects.equals(ps.getCprProductIdentifierDID(), productIdentifierDID)
				&& Objects.equals(ps.getCprProductIdentifierJID(), productIdentifierJID)).findFirst().orElse(null);
	}

	/**
	 * Get passenger by uniqueCustomerId
	 *
	 * @param passengers
	 * @param uniqueCustomerId
	 * @return
	 */
	private Passenger getPassengerByCprUniqueCustomerId(List<Passenger> passengers, String uniqueCustomerId) {
		return passengers.stream().filter(p -> p != null && Objects.equals(p.getCprUniqueCustomerId(), uniqueCustomerId)).findFirst().orElse(null);
	}

	/**
	 * Merge CPR passengerSegments To booking passengerSegments
	 *
	 * @param booking
	 * @param cprJourneys
	 */
	private void mergeCprPassengerSegments(Booking booking, List<JourneyDTO> cprJourneys) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengers())
				|| CollectionUtils.isEmpty(booking.getSegments())
				|| CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return;
		}

		List<Passenger> passengers = booking.getPassengers();
		List<Segment> segments = booking.getSegments();
		List<PassengerSegment> passengerSegments = booking.getPassengerSegments();

		for(PassengerSegment passengerSegment : passengerSegments) {
			if(passengerSegment == null) {
				continue;
			}

			Passenger passenger = getPassengerById(passengers, passengerSegment.getPassengerId());
			Segment segment = getSegmentById(segments, passengerSegment.getSegmentId());

			FlightDTO cprPassengerSegment = getCprSegmentByPaxAndSegment(passenger, segment, cprJourneys);
			JourneyDTO cprJourney = getCprJourneyWhichContainsFlight(cprJourneys, cprPassengerSegment);
			if(cprPassengerSegment != null) {
				PassengerDTO cprPassenger = getCprPassengerByIDs(cprJourneys, passenger.getCprUniqueCustomerId(), cprPassengerSegment.getProductIdentifierDID(), cprPassengerSegment.getProductIdentifierJID());

				/** flight related IDs */
				passengerSegment.setCprProductIdentifierDID(cprPassengerSegment.getProductIdentifierDID());
				passengerSegment.setCprProductIdentifierJID(cprPassengerSegment.getProductIdentifierJID());
				passengerSegment.setCprUniqueCustomerId(passenger.getCprUniqueCustomerId());
				/** merge flight info from CPR */
				passengerSegment.setDepartureGate(cprPassengerSegment.getDepartureGate());
				passengerSegment.setReverseCheckinCarrier(cprPassenger == null ? null : cprPassenger.getReverseCheckinCarrier());
				passengerSegment.setCprApplePassNumber(cprPassengerSegment.getApplePassNumber());
				/** merge package info from CPR */
				segment.setHasCheckedBaggge(cprPassengerSegment.isHasCheckedBaggage());
				passengerSegment.setHasCheckedBaggage(cprPassengerSegment.isHasCheckedBaggage());
				/** merge claimed lounge */
				mergeClaimedLounge(passengerSegment, cprPassengerSegment);
				/** merge purchased lounge */
				mergePurchasedLounge(passengerSegment, cprPassengerSegment);
				/** merge Seat Num */
				mergeSeatNum(passengerSegment, cprPassengerSegment, cprJourney, booking.getSegments());
			}
		}
	}
	
	/**
	 * get the cpr journey which contains the flight
	 * @param cprJourneys
	 * @param flight
	 * @return Journey
	 */
	private JourneyDTO getCprJourneyWhichContainsFlight(List<JourneyDTO> cprJourneys, FlightDTO flight) {
		if (cprJourneys == null || CollectionUtils.isEmpty(cprJourneys)) {
			return null;
		}
		
		return cprJourneys.stream()
				.filter(journey -> !CollectionUtils.isEmpty(journey.getPassengers())
						&& journey.getPassengers().stream().anyMatch(
								pax -> !CollectionUtils.isEmpty(pax.getFlights()) && pax.getFlights().contains(flight)))
				.findFirst().orElse(null);
	}

	/**
	 * Get cprPassenger by cprUniqueCustomerId, JID & DID
	 *
	 * @param cprJourneys
	 * @param cprUniqueCustomerId
	 * @param productIdentifierDID
	 * @param productIdentifierJID
	 * @return
	 */
	private PassengerDTO getCprPassengerByIDs(List<JourneyDTO> cprJourneys, String cprUniqueCustomerId,
			String did, String jid) {
		if(CollectionUtils.isEmpty(cprJourneys)) {
			return null;
		}

		List<PassengerDTO> cprPassengers = getCprPassengersByUniqueCustomerId(cprJourneys, cprUniqueCustomerId);
		return cprPassengers.stream().filter(p -> p != null && CollectionUtils.isNotEmpty(p.getFlights())
				&& p.getFlights().stream().anyMatch(s -> s != null && Objects.equals(s.getProductIdentifierDID(), did) && Objects.equals(s.getProductIdentifierJID(), jid)))
		.findFirst().orElse(null);
	}

	/**
	 * merge Seat Num
	 * @param passengerSegment
	 * @param cprPassengerSegment
	 * @param cprJourney 
	 * @param segments 
	 */
	private void mergeSeatNum(PassengerSegment passengerSegment, FlightDTO cprPassengerSegment, JourneyDTO cprJourney, List<Segment> segments) {

		SeatDetail pnrseat = passengerSegment.getSeat();
		passengerSegment.setPnrSeat(pnrseat);

		if(anyFlightOfJourneyPassedOpenCheckinTime(cprJourney, segments) && cprPassengerSegment.getSeat() != null && StringUtils.isNotEmpty(cprPassengerSegment.getSeat().getSeatNum())){
				SeatDetail seat = new SeatDetail();
				seat.setSeatNo(cprPassengerSegment.getSeat().getSeatNum());
				seat.setFromDCS(true);
				seat.setExlSeat(cprPassengerSegment.getSeat().isExtraLegRoomSeat());
				seat.setAsrSeat(!cprPassengerSegment.getSeat().isExtraLegRoomSeat() && !MMBBizruleConstants.SEAT_PAYMENT_STATUS_NO_NEED_TO_PAY.equals(cprPassengerSegment.getSeat().getPaymentStatus()));
				seat.setPaymentStatus(cprPassengerSegment.getSeat().getPaymentStatus());
				seat.setPaid(cprPassengerSegment.getSeat().isPaid());
				// we can believe all seats from OLCI is confirmed
				seat.setStatus(OneAConstants.HK_STATUS);
				// if the payment ticket from OLCI matched with ticket in PNR, prepopulate the payment info to the seat
				if (pnrseat != null && pnrseat.getPaymentInfo() != null && !StringUtils.isEmpty(pnrseat.getPaymentInfo().getTicket())
					&& pnrseat.getPaymentInfo().getTicket().equals(cprPassengerSegment.getSeat().getPaymentTicket())) {
					seat.setPaymentInfo(pnrseat.getPaymentInfo());
				}
				passengerSegment.setSeat(seat);
				passengerSegment.setCprSeat(seat);
		}
	}

	/**
	 * check if any flight of the journey has passed check-in open time
	 * 		i.e. within check-in window or post check-in window or flown
	 * @param cprJourney
	 * @param cprJourney
	 * @param segments 
	 * @return boolean
	 */
	private boolean anyFlightOfJourneyPassedOpenCheckinTime(JourneyDTO cprJourney, List<Segment> segments) {
		if (cprJourney == null || CollectionUtils.isEmpty(cprJourney.getPassengers())) {
			return false;
		}
		
		for (PassengerDTO cprPax : cprJourney.getPassengers()) {
			if (!CollectionUtils.isEmpty(cprPax.getFlights())) {
				for (FlightDTO flight : cprPax.getFlights()) {
					Segment segment = getSegmentByBaseInfo(segments, flight);
					if (segment != null && (segment.isOpenToCheckIn() || segment.isPostCheckIn() || BooleanUtils.isTrue(segment.isFlown()))) {
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * merge Purchased Lounge
	 * @param passengerSegment
	 * @param cprPassengerSegment
	 */
	private void mergePurchasedLounge(PassengerSegment passengerSegment, FlightDTO cprPassengerSegment) {
		PurchasedLounge cprPurLounge = null;
		if(CollectionUtils.isNotEmpty(cprPassengerSegment.getSsrList())) {
			if(cprPassengerSegment.getSsrList().contains(OneAConstants.PURCHASE_LOUNGE_LGAF)) {
				cprPurLounge = new PurchasedLounge();
				cprPurLounge.setType(LoungeClass.parseCode(OneAConstants.SERVICE_LOUNGE_FLAC));
			} else if (cprPassengerSegment.getSsrList().contains(OneAConstants.PURCHASE_LOUNGE_LGAB)) {
				cprPurLounge = new PurchasedLounge();
				cprPurLounge.setType(LoungeClass.parseCode(OneAConstants.SERVICE_LOUNGE_BLAC));
			}
		}
		if(cprPurLounge != null) {
			PurchasedLounge pnrPurLounge = passengerSegment.getPurchasedLounge();
			if (pnrPurLounge != null && pnrPurLounge.getType().equals(cprPurLounge.getType())) {
				cprPurLounge.setPaymentInfo(pnrPurLounge.getPaymentInfo());
			}			
			passengerSegment.setPurchasedLounge(cprPurLounge);
		}
	}

	/**
	 * merge Claimed Lounge
	 * @param passengerSegment
	 * @param cprPassengerSegment
	 */
	private void mergeClaimedLounge(PassengerSegment passengerSegment, FlightDTO cprPassengerSegment) {
		ClaimedLounge cprClaimedLounge = null;
		if(CollectionUtils.isNotEmpty(cprPassengerSegment.getSkList())) {
			if(cprPassengerSegment.getSkList().contains(OneAConstants.SERVICE_LOUNGE_FLAC)) {
				cprClaimedLounge = new ClaimedLounge();
				cprClaimedLounge.setType(LoungeClass.parseCode(OneAConstants.SERVICE_LOUNGE_FLAC));
			} else if (cprPassengerSegment.getSkList().contains(OneAConstants.SERVICE_LOUNGE_BLAC)) {
				cprClaimedLounge = new ClaimedLounge();
				cprClaimedLounge.setType(LoungeClass.parseCode(OneAConstants.SERVICE_LOUNGE_BLAC));
			}
		}
		if (cprClaimedLounge != null) {
			passengerSegment.setClaimedLounge(cprClaimedLounge);
		}
	}
	
	/**
	 * Merge CPR flight detail to booking passenger segments
	 * 
	 * @param passengerSegments
	 * @param cprJourneys
	 * @param journeyId
	 */
	private void mergeCprPassengerSegmentsDetail(List<PassengerSegment> passengerSegments, List<JourneyDTO> cprJourneys) {

		if (CollectionUtils.isEmpty(cprJourneys)) {
			logger.info("No CPR journey to merge passenger segment detail");
			return;
		}
		
		List<PassengerDTO> cprPassengers = cprJourneys.stream()
				.flatMap(jny -> jny.getPassengers().stream())
				.collect(Collectors.toList());
		List<FlightDTO> cprFlights = cprPassengers.stream()
				.flatMap(pax -> pax.getFlights().stream())
				.collect(Collectors.toList());
		for (PassengerSegment passengerSegment : passengerSegments) {
			String did = passengerSegment.getCprProductIdentifierDID();
			String jid = passengerSegment.getCprProductIdentifierJID();
			
			if (StringUtils.isEmpty(did)) {
				continue;
			}
			
			Predicate<FlightDTO> cprFlightMatcher = flight -> 
				did.equals(flight.getProductIdentifierDID()) && Objects.equals(jid, flight.getProductIdentifierJID());

			FlightDTO cprPassengerSegment = cprFlights.stream().filter(cprFlightMatcher).findFirst().orElse(null);
				
			PassengerDTO cprPassenger = cprPassengers.stream().filter(
					pax -> pax.getFlights().stream().anyMatch(cprFlightMatcher)
				).findFirst().orElse(null);
			
			if (cprPassengerSegment == null) {
				continue;
			}
			
			passengerSegment.setPriTravelDoc(buildTravelDoc(cprPassenger, cprPassengerSegment.getTravelDoc()));
			passengerSegment.setSecTravelDoc(buildTravelDoc(cprPassenger, cprPassengerSegment.getSecTravelDoc()));
			if (cprPassenger != null) {
				passengerSegment.setCountryOfResidence(cprPassenger.getResidenceCountry());
				// populate inhibit change seat from cpr response
	            passengerSegment.setInhibitChangeSeat(cprPassenger.isInhibitChangeSeat());
			}
			passengerSegment.setIsTransit(cprPassengerSegment.getTransit());
		}
	}

	/**
	 * Build booking travelDocument from CPR travelDoc
	 * @param cprPassenger
	 *
	 * @param cprTravelDoc
	 * @return
	 */
	private TravelDoc buildTravelDoc(PassengerDTO cprPassenger, TravelDocumentDTO cprTravelDoc) {
		if(cprTravelDoc == null) {
			return null;
		}

		TravelDoc travelDoc = new TravelDoc();

		travelDoc.setTravelDocumentType(cprTravelDoc.getType());
		travelDoc.setTravelDocumentNumber(cprTravelDoc.getNumber());
		travelDoc.setFamilyName(cprTravelDoc.getRegulatorySurname());
		travelDoc.setGivenName(cprTravelDoc.getRegulatoryFirstName());
		travelDoc.setGender(cprTravelDoc.getRegulatoryGender());
		travelDoc.setCountryOfIssuance(cprTravelDoc.getIssueCountry());
		travelDoc.setNationality(cprTravelDoc.getNationality());

		if(cprPassenger != null) {
			/** country of residence */
			travelDoc.setCountryOfResidence(cprPassenger.getResidenceCountry());

			/** DOB */
			if(cprPassenger.getDateOfBirth() != null) {
				travelDoc.setBirthDateYear(cprPassenger.getDateOfBirth().getYear());
				travelDoc.setBirthDateMonth(cprPassenger.getDateOfBirth().getMonth());
				travelDoc.setBirthDateDay(cprPassenger.getDateOfBirth().getDay());
			}
		}

		if(cprTravelDoc.getExpiryDate() != null) {
			travelDoc.setExpiryDateYear(cprTravelDoc.getExpiryDate().getYear());
			travelDoc.setExpiryDateMonth(cprTravelDoc.getExpiryDate().getMonth());
			travelDoc.setExpiryDateDay(cprTravelDoc.getExpiryDate().getDay());
		}

		return travelDoc;
	}

	/**
	 * Get cpr flight under passenger by booking pax & segment
	 *
	 * @param passenger
	 * @param segment
	 * @param cprJourneys
	 * @return
	 */
	private FlightDTO getCprSegmentByPaxAndSegment(Passenger passenger, Segment segment, List<JourneyDTO> cprJourneys) {
		if(passenger == null || segment == null) {
			return null;
		}

		List<PassengerDTO> cprPassengers = getCprPassengersByUniqueCustomerId(cprJourneys, passenger.getCprUniqueCustomerId());
		List<FlightDTO> cprSegments = cprPassengers.stream().map(PassengerDTO::getFlights).flatMap(Collection::stream).collect(Collectors.toList());
		return getCprSegmentByBaseInfo(cprSegments, segment);
	}

	/**
	 * Get CprPassengers by UniqueCustomerId
	 *
	 * @param cprJourneys
	 * @param cprUniqueCustomerId
	 * @return
	 */
	private List<PassengerDTO> getCprPassengersByUniqueCustomerId(List<JourneyDTO> cprJourneys, String cprUniqueCustomerId) {
		if(CollectionUtils.isEmpty(cprJourneys) || StringUtils.isEmpty(cprUniqueCustomerId)) {
			return Collections.emptyList();
		}
		return cprJourneys.stream().map(JourneyDTO::getPassengers).flatMap(Collection::stream)
				.filter(p -> p != null && Objects.equals(cprUniqueCustomerId, p.getUniqueCustomerId()))
				.collect(Collectors.toList());
	}

	/**
	 * Get CPR segment by base information
	 *
	 * @param cprSegments
	 * @param segment
	 * @return
	 */
	private FlightDTO getCprSegmentByBaseInfo(List<FlightDTO> cprSegments, Segment segment) {
		return cprSegments.stream().filter(s -> Objects.equals(segment.getOriginPort(), s.getOriginPort())
				&& Objects.equals(segment.getDestPort(), s.getDestPort())
				&& Objects.equals(segment.getOperateSegmentNumber(), s.getOperateFlightNumber())
				&& ((segment.getDepartureTime() == null && s.getDepartureTime() == null)
						|| Objects.equals(segment.getDepartureTime().getPnrTime(), s.getDepartureTime().getCprScheduledTime())))
				.findFirst().orElse(null);
	}

	/**
	 * Get Segment by segment ID
	 *
	 * @param segments
	 * @param segmentId
	 * @return
	 */
	private Segment getSegmentById(List<Segment> segments, String segmentId) {
		if(StringUtils.isEmpty(segmentId) || CollectionUtils.isEmpty(segments)) {
			return null;
		}
		return segments.stream().filter(s -> segmentId.equals(s.getSegmentID())).findFirst().orElse(null);
	}

	/**
	 * Get Passenger by passenger ID
	 *
	 * @param passengers
	 * @param passengerId
	 * @return
	 */
	private Passenger getPassengerById(List<Passenger> passengers, String passengerId) {
		if(StringUtils.isEmpty(passengerId) || CollectionUtils.isEmpty(passengers)) {
			return null;
		}
		return passengers.stream().filter(p -> passengerId.equals(p.getPassengerId())).findFirst().orElse(null);
	}

	/**
	 * Merge CPR passengers To booking passengers
	 * priority:
	 * 1. using eTickets
	 * 2. using names match
	 *
	 *
	 * @param passengers
	 * @param passengerSegments
	 * @param cprJourneys
	 * @param journeyId
	 */
	private void mergeCprPassengers(List<Passenger> passengers, List<PassengerSegment> passengerSegments,
			List<JourneyDTO> cprJourneys, String journeyId) {
		
		if (StringUtils.isEmpty(journeyId)) {
			logger.info("Merge cprPassenger of default journey");
		} else {
			logger.info(String.format("Merge cprPassenger of journey[%s]", journeyId));
		}
		
		for(Passenger passenger : passengers) {
			if(passenger == null) {
				continue;
			}

			List<PassengerDTO> cprPassengers = getCprPassengersByEtickets(passenger, passengerSegments, cprJourneys, journeyId);
			if(CollectionUtils.isEmpty(cprPassengers)) {
				logger.info(String.format("Merge cprPassenger[%s] - can't find cprPassenger by etickets, will try to merge with names[%s, %s, %s]",
						passenger.getPassengerId(), passenger.getFamilyName(), passenger.getGivenName(), passenger.getTitle()));

				cprPassengers = getCprPassengersByNames(cprJourneys, passenger.getFamilyName(), passenger.getGivenName(),
						passenger.getTitle(), journeyId);
				
				if (CollectionUtils.isEmpty(cprPassengers)) {
					return;
				}
			}
			
			PassengerDTO cprPassenger = cprPassengers.get(0);
			passenger.setCprUniqueCustomerId(cprPassenger.getUniqueCustomerId());
		}
	}

	/**
	 * Get passenger in CPR by passenger's eTickets
	 *
	 * @param passenger
	 * @param passengerSegments
	 * @param cprJourneys
	 * @param journeyId
	 * @return
	 */
	private List<PassengerDTO> getCprPassengersByEtickets(Passenger passenger, List<PassengerSegment> passengerSegments,
			List<JourneyDTO> cprJourneys, String journeyId) {
		
		if(StringUtils.isEmpty(passenger.getPassengerId())) {
			return null;
		}

		List<String> eTicketNumbers = getEticketsByPassengerId(passengerSegments, passenger.getPassengerId());
		if(CollectionUtils.isEmpty(eTicketNumbers)) {
			return null;
		}
		
		Predicate<FlightDTO> flightMatcher =
				flight -> eTicketNumbers.contains(flight.getAssocETicketNumber());
		
		if (StringUtils.isNotEmpty(journeyId)) {
			JourneyDTO cprJourney = cprJourneys.stream().filter(
					jny -> journeyId.equals(jny.getJourneyId())
				).findFirst().orElse(null);
			
			if (cprJourney == null) {
				logger.warn(String.format("Journey[%s], cannot be found in booking", journeyId));
			} else {
				PassengerDTO cprPassenger = cprJourney.getPassengers().stream().filter(
					p -> CollectionUtils.isNotEmpty(p.getFlights())
						&& p.getFlights().stream().anyMatch(flightMatcher)
				).findFirst().orElse(null);
				
				if (cprPassenger == null) {
					logger.warn(String.format("Passenger Eticket cannot be found in journey[%s]",
							journeyId));
				} else {
					return Arrays.asList(cprPassenger);
				}
			}
		}

		List<PassengerDTO> cprPassengers = cprJourneys.stream()
				.map(JourneyDTO::getPassengers)
				.flatMap(Collection::stream)
				.filter(p -> CollectionUtils.isNotEmpty(p.getFlights())
						&& p.getFlights().stream().anyMatch(flightMatcher))
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(cprPassengers)) {
			logger.warn("Passenger Eticket cannot be found in CPR");
		}
		return cprPassengers;
	}

	/**
	 * Get the list of eTicket numbers by passengerId
	 *
	 * @param passengerSegments
	 * @param passengerId
	 * @return
	 */
	private List<String> getEticketsByPassengerId(List<PassengerSegment> passengerSegments, String passengerId) {
		if(CollectionUtils.isEmpty(passengerSegments) || StringUtils.isEmpty(passengerId)) {
			return null;
		}
		return passengerSegments.stream().filter(ps -> passengerId.equals(ps.getPassengerId()) && StringUtils.isNotEmpty(ps.getEticketNumber()))
				.map(PassengerSegment::getEticketNumber)
				.collect(Collectors.toList());
	}
	
	/**
	 * Merge CPR passengers detail to booking passengers
	 * 
	 * @param passengers
	 * @param cprJourneys
	 * @param journeyId
	 */
	private void mergeCprPassengersDetail(List<Passenger> passengers, List<JourneyDTO> cprJourneys,
			String journeyId) {
		
		JourneyDTO cprJourney;
		if (CollectionUtils.isEmpty(cprJourneys)) {
			logger.info("No CPR journey to merge passenger detail");
			return;
		} else if (StringUtils.isNotEmpty(journeyId)) {
			cprJourney = cprJourneys.stream().filter(
					jny -> journeyId.equals(jny.getJourneyId())
				).findFirst().orElse(null);

			if (cprJourney == null) {
				logger.info(String.format("Journey[%s], cannot be found to merge passenger detail", journeyId));
				return;
			}			
		} else {
			cprJourney = cprJourneys.get(0);
		}
		
		if (StringUtils.isEmpty(journeyId)) {
			logger.info("Merge CPR passenger detail of first journey");
		} else {
			logger.info(String.format("Merge CPR passenger detail of journey[%s]", journeyId));
		}
		
		for (Passenger passenger : passengers) {
			String uci = passenger.getCprUniqueCustomerId();
			if (StringUtils.isEmpty(uci)) {
				continue;
			}
			
			PassengerDTO cprPassenger = cprJourney.getPassengers().stream()
					.filter(pax -> uci.equals(pax.getUniqueCustomerId()))
					.findFirst().orElse(null);
			
			if (cprPassenger != null) {
				passenger.setCorrespondingCprPassengerFound(true);
				passenger.setCprGender(cprPassenger.getGender());
				passenger.setCountryOfResidence(cprPassenger.getResidenceCountry());
				mergeCprContactInfo(passenger, cprPassenger);
				mergeDesAddress(passenger, cprPassenger);
				mergeKtn(passenger, cprPassenger);
				mergeRedress(passenger, cprPassenger);
			}
		}
	}

	/**
	 * merge Ktn
	 *
	 * @param passenger
	 * @param cprPassenger
	 */
	private void mergeKtn(Passenger passenger, PassengerDTO cprPassenger) {
		if(CollectionUtils.isNotEmpty(cprPassenger.getFlights())) {
			FlightDTO flightDTO = cprPassenger.getFlights().stream().filter(flight -> flight != null && CollectionUtils.isEmpty(flight.getErrors())).findFirst().orElse(null);
			if(flightDTO != null && flightDTO.getKtnTravelDoc() != null && StringUtils.isNotEmpty(flightDTO.getKtnTravelDoc().getNumber())) {
				passenger.findKtn().setNumber(flightDTO.getKtnTravelDoc().getNumber());
			}
		}
	}

	private void mergeRedress(Passenger passenger, PassengerDTO cprPassenger) {
		if (CollectionUtils.isNotEmpty(cprPassenger.getFlights())) {
			FlightDTO flightDTO = cprPassenger.getFlights().stream().filter(flight -> flight != null && CollectionUtils.isEmpty(flight.getErrors())).findFirst().orElse(null);
			if(flightDTO != null && flightDTO.getRedressTravelDoc() != null && StringUtils.isNotEmpty(flightDTO.getRedressTravelDoc().getNumber())) {
				passenger.findRedress().setNumber(flightDTO.getRedressTravelDoc().getNumber());
			}
		}
	}

	/**
	 * merge DesAddress
	 *
	 * @param passenger
	 * @param cprPassenger
	 */
	private void mergeDesAddress(Passenger passenger, PassengerDTO cprPassenger) {
		passenger.setDesTransit(BooleanUtils.isTrue(cprPassenger.getTransit()));
		if(cprPassenger.getDestinationAddress() == null) {
			passenger.setDesAddress(null);
			return;
		}
		if(StringUtils.isNotEmpty(cprPassenger.getDestinationAddress().getCity()) && StringUtils.isNotEmpty(cprPassenger.getDestinationAddress().getCountryCode())
				&& StringUtils.isNotEmpty(cprPassenger.getDestinationAddress().getStateCode()) && StringUtils.isNotEmpty(cprPassenger.getDestinationAddress().getStateName())
				&& StringUtils.isNotEmpty(cprPassenger.getDestinationAddress().getStreet()) && StringUtils.isNotEmpty(cprPassenger.getDestinationAddress().getZipCode())) {
			DesAddress desAddress = passenger.findDesAddress();
			desAddress.setCity(cprPassenger.getDestinationAddress().getCity());
			desAddress.setStateCode(cprPassenger.getDestinationAddress().getStateCode());
			desAddress.setStreet(cprPassenger.getDestinationAddress().getStreet());
			desAddress.setZipCode(cprPassenger.getDestinationAddress().getZipCode());
		}
	}

	/**
	 * Merge CPR ContactInfo To booking ContactInfo
	 *
	 * @param passenger
	 * @param cprPassenger
	 */
	private void mergeCprContactInfo(Passenger passenger, PassengerDTO cprPassenger) {
		if(cprPassenger.getContacts() == null) {
			passenger.setContactInfo(null);
			return;
		}

		ContactDTO cprContact = cprPassenger.getContacts();

		// Email address
		if(StringUtils.isNotEmpty(cprContact.getEmailAddress())) {
			passenger.findContactInfo().findEmail().setEmailAddress(cprContact.getEmailAddress());
			passenger.findContactInfo().findEmail().setOlssContact(cprContact.isEmailOlciVersion());
			passenger.findContactInfo().findEmail().setType(OlciContactInfoEnum.retireveMmbType(cprContact.getContactEmailSource()));
		} else {
			passenger.findContactInfo().setEmail(null);
		}

		// Phone number
		if(StringUtils.isNotEmpty(cprContact.getMobileNumber()) && StringUtils.isNotEmpty(cprContact.getMobileCountryNumber())
				&& StringUtils.isNotEmpty(cprContact.getMobileCountryCode())) {
			if (bookingBuildHelper.isValidPhoneNumber(cprContact.getMobileCountryNumber(), cprContact.getMobileNumber(), ContactType.CONTACT)) {
				PhoneInfo phoneInfo = passenger.findContactInfo().findPhoneInfo();
				phoneInfo.setCountryCode(cprContact.getMobileCountryCode());
				phoneInfo.setPhoneCountryNumber(cprContact.getMobileCountryNumber());
				phoneInfo.setPhoneNo(cprContact.getMobileNumber());
				phoneInfo.setOlssContact(cprContact.isMobileOlciVersion());
				phoneInfo.setType(OlciContactInfoEnum.retireveMmbType(cprContact.getContactMobileSource()));
				phoneInfo.setIsValid(true);
			} else {
				PhoneInfo phoneInfo = passenger.findContactInfo().findPhoneInfo();
				phoneInfo.setPhoneNo(cprContact.getMobileCountryNumber() + cprContact.getMobileNumber());
				phoneInfo.setOlssContact(cprContact.isMobileOlciVersion());
				phoneInfo.setType(OlciContactInfoEnum.retireveMmbType(cprContact.getContactMobileSource()));
				phoneInfo.setIsValid(false);
			}
		} else {
			passenger.findContactInfo().setPhoneInfo(null);
		}

		// Emergency Info
		if(StringUtils.isNotEmpty(cprContact.getEmergencyContactNumber()) && StringUtils.isNotEmpty(cprContact.getEmergencyContactCountryNumber())
				&& StringUtils.isNotEmpty(cprContact.getEmergencyContactCountryCode()) && StringUtils.isNotEmpty(cprContact.getEmergencyContactPerson())
				&& bookingBuildHelper.isValidPhoneNumber(cprContact.getEmergencyContactCountryNumber(), cprContact.getEmergencyContactNumber(), ContactType.EMR_CONTACT)) {
			EmrContactInfo emrContactInfo = passenger.findEmrContactInfo();
			emrContactInfo.setCountryCode(cprContact.getEmergencyContactCountryCode());
			emrContactInfo.setName(cprContact.getEmergencyContactPerson());
			emrContactInfo.setPhoneCountryNumber(cprContact.getEmergencyContactCountryNumber());
			emrContactInfo.setPhoneNumber(cprContact.getEmergencyContactNumber());
			emrContactInfo.setIsValid(true);
		} else {
			passenger.setEmrContactInfo(null);
		}

	}

	/**
	 * Get CPR passengers by familyName, givenName & title
	 *
	 * @param cprJourneys
	 * @param familyName
	 * @param givenName
	 * @param title
	 * @param journeyId
	 * @return
	 */
	private List<PassengerDTO> getCprPassengersByNames(List<JourneyDTO> cprJourneys, String familyName, String givenName,
			String title, String journeyId) {
		
		BiPredicate<String, String> nameMatcher = (expected, actual) -> {
			if (expected == null && actual == null) {
				return true;
			} else if (expected == null || actual == null) {
				return false;
			} else {
				return expected.equalsIgnoreCase(actual);
			}
		};
		
		Predicate<PassengerDTO> passengerNameMatcher =
				passenger -> nameMatcher.test(familyName, passenger.getFamilyName())
							&& nameMatcher.test(givenName, passenger.getGivenName())
							&& nameMatcher.test(title, passenger.getTitle());
				
		if (StringUtils.isNotEmpty(journeyId)) {
			JourneyDTO cprJourney = cprJourneys.stream().filter(
					jny -> journeyId.equals(jny.getJourneyId())
				).findFirst().orElse(null);
			
			if (cprJourney == null) {
				logger.warn(String.format("Journey[%s], cannot be found in booking", journeyId));
			} else {
				PassengerDTO cprPassenger = cprJourney.getPassengers().stream()
					.filter(Objects::nonNull).filter(passengerNameMatcher)
					.findFirst().orElse(null);
				
				if (cprPassenger == null) {
					logger.warn(String.format("Passenger[%s %s %s] cannot be found in journey[%s]",
							familyName, givenName, title, journeyId));
				} else {
					return Arrays.asList(cprPassenger);
				}
			}
		}
		
		List<PassengerDTO> cprPassengers = cprJourneys.stream()
				.map(JourneyDTO::getPassengers)
				.flatMap(Collection::stream)
				.filter(Objects::nonNull).filter(passengerNameMatcher)
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(cprPassengers)) {
			logger.warn(String.format("Passenger[%s %s %s] cannot be found in CPR",
					familyName, givenName, title));
		}
		return cprPassengers;
	}

	/**
	 * Build PassegnerSegments' base information
	 *
	 * @param booking
	 * @param journeys
	 */
	private void buildPassengerSegments(Booking booking, List<JourneyDTO> journeys) {
		if(CollectionUtils.isEmpty(booking.getPassengers())
				|| CollectionUtils.isEmpty(booking.getSegments())
				|| CollectionUtils.isEmpty(journeys)) {
			return;
		}

		List<Passenger> passengers = booking.getPassengers();
		List<Segment> segments = booking.getSegments();

		List<PassengerSegment> passengerSegments = new ArrayList<>();
		for(Passenger passenger : passengers) {
		    // if passengerid is not start with P
		    // we will not build passengergsegment for this pax, it indicate we already has it in pnr
			if(passenger == null || !passenger.getPassengerId().startsWith(CPR_PASSENGER_ID_PREFIX)) {
				continue;
			}

			List<FlightDTO> cprSegments = getCprSegmentsByUniqueCustomerId(journeys, passenger.getCprUniqueCustomerId());
			passengerSegments.addAll(buildPassengerSegmentsByPax(passenger, segments, cprSegments));
		}

		booking.getPassengerSegments().addAll(passengerSegments);
	}

	/**
	 * Build passenerSegments by passenger
	 *
	 * @param passenger
	 * @param segments
	 * @param cprSegments
	 * @return
	 */
	private List<PassengerSegment> buildPassengerSegmentsByPax(Passenger passenger, List<Segment> segments, List<FlightDTO> cprSegments) {
		if(passenger == null
				|| CollectionUtils.isEmpty(segments)
				|| CollectionUtils.isEmpty(cprSegments)) {
			return Collections.emptyList();
		}

		List<PassengerSegment> passengerSegments = new ArrayList<>();
		for(FlightDTO cprSegment : cprSegments) {
			if(cprSegment == null) {
				continue;
			}

			PassengerSegment passengerSegment = new PassengerSegment();
			passengerSegment.setPassengerId(passenger.getPassengerId());
			passengerSegment.setCprUniqueCustomerId(passenger.getCprUniqueCustomerId());

			Segment segment = getSegmentByBaseInfo(segments, cprSegment);
			passengerSegment.setSegmentId(segment == null ? null : segment.getSegmentID());
			// fqtv
			if(StringUtils.isNotEmpty(cprSegment.getFqtvNumber()) && StringUtils.isNotEmpty(cprSegment.getFqtvProgram())) {
			    FQTVInfo fqtvInfo = new FQTVInfo();
			    fqtvInfo.setCompanyId(cprSegment.getFqtvProgram());
			    fqtvInfo.setMembershipNumber(cprSegment.getFqtvNumber());
	            passengerSegment.setFqtvInfo(fqtvInfo);  
			}
			
			passengerSegments.add(passengerSegment);
		}
		return passengerSegments;
	}

	/**
	 * Get segment by base information
	 *
	 * @param segments
	 * @param cprSegment
	 * @return
	 */
	private Segment getSegmentByBaseInfo(List<Segment> segments, FlightDTO cprSegment) {
		return segments.stream().filter(s -> Objects.equals(s.getOriginPort(), cprSegment.getOriginPort())
				&& Objects.equals(s.getDestPort(), cprSegment.getDestPort())
				&& (Objects.equals(s.getOperateSegmentNumber(), cprSegment.getOperateFlightNumber())
				|| Objects.equals(s.getMarketSegmentNumber(), cprSegment.getMarketFlightNumber()))
				&& ((s.getDepartureTime() == null && cprSegment.getDepartureTime() == null)
						|| Objects.equals(s.getDepartureTime().getPnrTime(), cprSegment.getDepartureTime().getCprScheduledTime())))
				.findFirst().orElse(null);
	}

	/**
	 * build segments' base information
	 *
	 * @param cprSegments
	 * @return
	 */
	private List<Segment> buildSegments(List<FlightDTO> cprSegments) {
		if(CollectionUtils.isEmpty(cprSegments)) {
			return Collections.emptyList();
		}

		List<Segment> segments = new ArrayList<>();
		for(int i = 0; i < cprSegments.size(); i++) {
			FlightDTO cprSegment = cprSegments.get(i);
			if(cprSegment == null) {
				continue;
			}

			Segment segment = new Segment();
			segment.setSegmentID(generateId(CPR_SEGMENT_ID_PREFIX, i));
			segment.setOriginPort(cprSegment.getOriginPort());
			segment.setDestPort(cprSegment.getDestPort());
			segment.setOperateSegmentNumber(cprSegment.getOperateFlightNumber());
			segment.setMarketSegmentNumber(cprSegment.getMarketFlightNumber());
			segment.setOperateCompany(cprSegment.getOperateCompany());
			segment.setMarketCompany(cprSegment.getMarketingCompany());

			//TODO Pay attention[defect may happen]: OLCI have no operating/marketing cabinClass & subClass
			segment.setCabinClass(cprSegment.getCabinClass());
			segment.setMarketCabinClass(cprSegment.getCabinClass());
			segment.setSubClass(cprSegment.getSubClass());
			segment.setMarketSubClass(cprSegment.getSubClass());

			segment.setDepartureTime(buildSegmentDepartureTime(cprSegment.getDepartureTime()));
			segment.setArrivalTime(buildSegmentArrivalTime(cprSegment.getArrivalTime()));

			segments.add(segment);
		}

		return segments;
	}

	/**
	 * Build segment arrival time
	 *
	 * @param cprArrivalTime
	 * @return
	 */
	private DepartureArrivalTime buildSegmentArrivalTime(DepartureArrivalTimeDTO cprArrivalTime) {
		if(cprArrivalTime == null) {
			return null;
		}

		DepartureArrivalTime arrivalTime = new DepartureArrivalTime();
		arrivalTime.setPnrTime(cprArrivalTime.getCprScheduledTime());
		arrivalTime.setRtfsEstimatedTime(cprArrivalTime.getRtfsEstimatedTime());
		arrivalTime.setRtfsScheduledTime(cprArrivalTime.getRtfsScheduledTime());

		return arrivalTime;
	}

	/**
	 * Build segment departure time
	 *
	 * @param cprDepartureTime
	 * @return
	 */
	private DepartureArrivalTime buildSegmentDepartureTime(DepartureArrivalTimeDTO cprDepartureTime) {
		if(cprDepartureTime == null) {
			return null;
		}

		DepartureArrivalTime departureTime = new DepartureArrivalTime();
		departureTime.setPnrTime(cprDepartureTime.getCprScheduledTime());
		departureTime.setRtfsEstimatedTime(cprDepartureTime.getRtfsEstimatedTime());
		departureTime.setRtfsScheduledTime(cprDepartureTime.getRtfsScheduledTime());

		return departureTime;
	}

	/**
	 * Try to get 1 passegner's all segments in all journeys
	 *
	 * @param journeys
	 * @return
	 */
	private List<FlightDTO> getCprSegments(List<JourneyDTO> journeys) {
		String paxUniqueCustomerId = journeys.stream().filter(j -> CollectionUtils.isNotEmpty(j.getPassengers()))
					.map(JourneyDTO::getPassengers).flatMap(Collection::stream)
					.filter(p -> StringUtils.isNotEmpty(p.getUniqueCustomerId()))
					.findFirst().map(PassengerDTO::getUniqueCustomerId).orElse(null);

		if(StringUtils.isEmpty(paxUniqueCustomerId)) {
			return Collections.emptyList();
		}

		return getCprSegmentsByUniqueCustomerId(journeys, paxUniqueCustomerId);
	}

	/**
	 * Get CPR segments by passenger uniqueCustomerId
	 *
	 * @param journeys
	 * @param paxUniqueCustomerId
	 * @return
	 */
	private List<FlightDTO> getCprSegmentsByUniqueCustomerId(List<JourneyDTO> journeys, String paxUniqueCustomerId) {
		if(CollectionUtils.isEmpty(journeys)) {
			return Collections.emptyList();
		}
		return journeys.stream().map(JourneyDTO::getPassengers).flatMap(Collection::stream)
				.filter(p -> paxUniqueCustomerId.equals(p.getUniqueCustomerId()))
				.map(PassengerDTO::getFlights).flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	/**
	 * Reintegrate & Distinct passenger data
	 *
	 * @param journeys
	 * @return
	 */
	private List<PassengerDTO> getCprPassegners(List<JourneyDTO> journeys) {
		if(CollectionUtils.isEmpty(journeys)) {
			return Collections.emptyList();
		}

		List<PassengerDTO> cprPassegners = journeys.stream().map(JourneyDTO::getPassengers)
				.flatMap(Collection::stream).collect(Collectors.toList());

		return cprPassegners.stream().map(WrapperPassengerDTO::new)
				.distinct().map(WrapperPassengerDTO::unwrap).collect(toList());
	}

	/**
	 * Wrapper class for PassengerDTO
	 */
	class WrapperPassengerDTO {
		private PassengerDTO e;
		public WrapperPassengerDTO(PassengerDTO e) {
			this.e = e;
		}
		public PassengerDTO unwrap() {
			return this.e;
		}
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			WrapperPassengerDTO that = (WrapperPassengerDTO) o;
			return Objects.equals(e.getUniqueCustomerId() + e.getType(), that.e.getUniqueCustomerId() + that.e.getType());
		}
		@Override
		public int hashCode() {
			return Objects.hash(e.getUniqueCustomerId() + e.getType());
		}
	}

	/**
	 * build passengers' base information
	 *
	 * @param cprPassengers
	 * @return
	 */
	private List<Passenger> buildPassegners(List<PassengerDTO> cprPassengers) {
		if(CollectionUtils.isEmpty(cprPassengers)) {
			return Collections.emptyList();
		}

		List<Passenger> passengers = new ArrayList<>();
		for(int i = 0; i < cprPassengers.size(); i++) {
			PassengerDTO cprPassenger = cprPassengers.get(i);
			if(cprPassenger == null || isCbbgOrExstPax(cprPassenger)) {
				continue;
			}

			Passenger passenger = new Passenger();
			passenger.setPassengerId(generateId(CPR_PASSENGER_ID_PREFIX, i));
			passenger.setCprUniqueCustomerId(cprPassenger.getUniqueCustomerId());
			passenger.setFamilyName(StringUtils.upperCase(cprPassenger.getFamilyName()));
			passenger.setGivenName(StringUtils.upperCase(cprPassenger.getGivenName()));
			passenger.setTitle(StringUtils.upperCase(cprPassenger.getTitle()));
			passenger.setPassengerType(covertOlciPaxTypeToMmb(cprPassenger.getType()));
			passenger.setDob(buildDob(cprPassenger.getDateOfBirth()));
			passenger.setCprGender(cprPassenger.getGender());
			if(cprPassenger.isMergedPassenger()) {
			    passenger.setLinkedRloc(cprPassenger.getRloc());
			}
			mergeCprContactInfo(passenger, cprPassenger);
	        mergeDesAddress(passenger, cprPassenger);
	        mergeKtn(passenger, cprPassenger);
	        mergeRedress(passenger, cprPassenger);
			passengers.add(passenger);
		}
		
		// build infant from cpr
		buildInfantFromCpr(cprPassengers,passengers);

		return passengers;
	}
	
	
	/**
	 * ignore cbbg or exst case
	 * @param cprPassenger
	 * @return
	 */
	private boolean isCbbgOrExstPax(PassengerDTO cprPassenger) {
        return StringUtils.equalsIgnoreCase(cprPassenger.getType(), OLCIConstants.CUSTOMERLEVEL_PAX_TYPE_BAGGAGE) || StringUtils.equalsIgnoreCase(cprPassenger.getType(), OLCIConstants.CUSTOMERLEVEL_PAX_TYPE_EXTRA);
    }

    /**
	 * build infantinfo from cpr
	 * @param cprPassengers
	 * @param passengers
	 */
    private void buildInfantFromCpr(List<PassengerDTO> cprPassengers, List<Passenger> passengers) {
        if (!CollectionUtils.isEmpty(passengers)) {
            for (Passenger pnrPassenger : passengers) {
                // find infant
                if (OneAConstants.PASSENGER_TYPE_INF.equals(pnrPassenger.getPassengerType())) {
                    // retrieve parent according to pnr infantinfo
                    // retrieve cpr infant according to pnr infant
                    PassengerDTO cprInfant = cprPassengers.stream()
                            .filter(cprPassenger -> null != cprPassenger && StringUtils
                                    .equals(cprPassenger.getUniqueCustomerId(), pnrPassenger.getCprUniqueCustomerId()))
                            .findFirst().orElse(null);
                    if (null != cprInfant) {
                        buildParentIdForPnrInfant(cprPassengers, passengers, pnrPassenger, cprInfant);
                    }
                }
            }
        }

    }

    /**
     * build parentid for pnr infant
     * 
     * @param cprPassengers
     * @param passengers
     * @param pnrPassenger
     * @param cprInfant
     */
    private void buildParentIdForPnrInfant(List<PassengerDTO> cprPassengers, List<Passenger> passengers,
            Passenger pnrInfant, PassengerDTO cprInfant) {
        PassengerDTO cprPassenger = retrieveParentFromCpr(cprPassengers, cprInfant);
        if (null != cprPassenger) {
            // retrieve pnr passenger according to cpr passenger
            Passenger pnrAdult = passengers
                    .stream().filter(p -> null != p && StringUtils
                            .equals(cprPassenger.getUniqueCustomerId(), p.getCprUniqueCustomerId()))
                    .findFirst().orElse(null);
            if(null != pnrAdult) {
                pnrInfant.setParentId(pnrAdult.getPassengerId());
            }
        }
    }

    /**
     * retrieve parent according to infant info
     * @param cprPassengers
     * @param cprInfant
     * @return
     */
    private static PassengerDTO retrieveParentFromCpr(List<PassengerDTO> cprPassengers,PassengerDTO cprInfant) {
	        List<FlightDTO> infantFlights =cprInfant.getFlights();
	        FlightDTO infantFlight = infantFlights.get(0);
	        for (PassengerDTO passenger : cprPassengers) {
	            List<FlightDTO> flights = passenger.getFlights();
	            for (FlightDTO flight : flights) {
	                String parentJID = flight.getProductIdentifierJID();
	                if (parentJID != null && parentJID.equals(infantFlight.getProductIdentifierDID())) {
	                    return passenger;
	                }
	            }
	        }
	        return null;
	    }

	/**
	 * Build passenger DOB from CPR
	 *
	 * @param cprDob
	 * @return
	 */
	private Dob buildDob(DateDTO cprDob) {
		if(cprDob == null) {
			return null;
		}

		Dob dob = new Dob();
		dob.setBirthDateYear(cprDob.getYear());
		dob.setBirthDateMonth(cprDob.getMonth());
		dob.setBirthDateDay(cprDob.getDay());

		return dob;
	}

	/**
	 * Generate passenger/segment Id by prefix & order
	 *
	 * @param prefix
	 * @param order
	 * @return
	 */
	private String generateId(String prefix, int order) {
		return prefix + order;
	}

	/**
	 * Get RLOC from passenger in list of Journeys whose RLOC is not empty
	 *
	 * @param journeys
	 * @return
	 */
	private String getRlocFromJourney(List<JourneyDTO> journeys) {
		return journeys.stream().map(JourneyDTO::getPassengers)
				.flatMap(Collection::stream).filter(p -> StringUtils.isNotEmpty(p.getRloc()))
				.findFirst().map(PassengerDTO::getRloc).orElse(null);
	}

	/**
	 * Get FlightDTO from List of FlightDTOs by DID & JID
	 *
	 * @param cprPassenger
	 * @param productIdentifierDID
	 * @param productIdentifierJID
	 * @return
	 */
	public FlightDTO getCprSegmentByIds(PassengerDTO cprPassenger, String productIdentifierDID, String productIdentifierJID) {
		if(cprPassenger == null
				|| CollectionUtils.isEmpty(cprPassenger.getFlights())
				|| (StringUtils.isEmpty(productIdentifierDID) && StringUtils.isEmpty(productIdentifierJID))) {
			return null;
		}

		return cprPassenger.getFlights().stream().filter(segment -> segment != null && Objects.equals(segment.getProductIdentifierDID(), productIdentifierDID)
				&& Objects.equals(segment.getProductIdentifierJID(), productIdentifierJID)).findFirst().orElse(null);
	}

	/**
	 * merge the cpr errors into booking
	 * @param booking
	 * @param cpr
	 */
	private void mergeErrors(Booking booking,LoginResponseDTO cpr){
		if(!CollectionUtils.isEmpty(cpr.getErrors())){
			booking.setCprErrors(covertErrorInfos(cpr.getErrors()));
		}
	}
	
}
