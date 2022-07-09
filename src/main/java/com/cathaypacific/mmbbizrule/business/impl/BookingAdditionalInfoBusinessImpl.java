package com.cathaypacific.mmbbizrule.business.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.business.BookingAdditionalInfoBusiness;
import com.cathaypacific.mmbbizrule.business.commonapi.TaggingBusiness;
import com.cathaypacific.mmbbizrule.config.OLCIConfig;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common.ProductTypeEnum;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.service.EcommService;
import com.cathaypacific.mmbbizrule.cxservice.hzmredemtionbanner.model.response.HZMBannerResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.hzmredemtionbanner.service.HZMBannerEligibleService;
import com.cathaypacific.mmbbizrule.cxservice.plusgrade.service.PlusGradeService;
import com.cathaypacific.mmbbizrule.dto.request.bookingproperties.additional.BookingAdditionalInfoRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.additional.BookingAdditionalInfoResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.additional.PassengerSegmentAdditionalInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.additional.SegmentAdditionalInfoDTO;
import com.cathaypacific.mmbbizrule.handler.JourneyCalculateHelper;
import com.cathaypacific.mmbbizrule.handler.UpgradeBidEligibleHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.journey.JourneySummary;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookingCerateInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

@Service
public class BookingAdditionalInfoBusinessImpl implements BookingAdditionalInfoBusiness {

	private static LogAgent logger = LogAgent.getLogAgent(BookingAdditionalInfoBusinessImpl.class);

	@Value("${hzmBannerEligible.cxTransportationCallFlag}")
	private boolean checkToCallTransportationApi;
	
	@Autowired
	private OLCIConfig olciConfig;

	@Autowired
	private PnrInvokeService pnrInvokeService;

	@Autowired
	private BookingBuildService bookingBuildService;

	@Autowired
	private JourneyCalculateHelper journeyCalculateHelper;

	@Autowired
	private UpgradeBidEligibleHelper upgradeBidEligibleHelper;

	@Autowired
	private PlusGradeService plusGradeService;

	@Autowired
	private EcommService ecommService;

	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;

	@Autowired
	private TaggingBusiness taggingBusiness;
	
	@Autowired
	private HZMBannerEligibleService hzmBannerEligibleService;

	@Override
	public BookingAdditionalInfoResponseDTO getBookingAdditional(LoginInfo loginInfo, String rloc, String language,
			BookingAdditionalInfoRequestDTO requestDTO) throws BusinessBaseException {
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		if (retrievePnrBooking == null || CollectionUtils.isEmpty(retrievePnrBooking.getPassengers())) {
			throw new ExpectedException(String.format("Cannot find booking by rloc:%s", loginInfo.getLoginRloc()),
					new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		Booking booking = bookingBuildService.buildBooking(retrievePnrBooking, loginInfo,
				buildBookingPropertiesRequired());
			
		BookingAdditionalInfoResponseDTO responseDTO = new BookingAdditionalInfoResponseDTO();

		// create segments
		booking.getSegments().stream().forEach(st -> {
			SegmentAdditionalInfoDTO segmentAdditional = new SegmentAdditionalInfoDTO();
			segmentAdditional.setSegmentId(st.getSegmentID());
			responseDTO.addSegment(segmentAdditional);
		});
		if (BooleanUtils.isTrue(requestDTO.isPlusgrade())) {
			buildPlusgradeInfo(loginInfo, booking, responseDTO, language, retrievePnrBooking);
		}
		// set UpgradeBidEligible in segment level
		for (SegmentAdditionalInfoDTO seg : responseDTO.getSegments()) {
			seg.setRedemptionBannerEligible(taggingBusiness.checkRedemptionBannerUpgrade(booking));
		}
		
		if (!CollectionUtils.isEmpty(booking.getSegments())) {
			ProductsResponseDTO ecommServiceProducts = new ProductsResponseDTO();
			// if any seat and baggage in requestDTO is true , call ecomm service
			if(BooleanUtils.isTrue(requestDTO.isAncillaryASRSeatBanner())
					|| BooleanUtils.isTrue(requestDTO.isAncillaryEXLSeatBanner())
					|| BooleanUtils.isTrue(requestDTO.isAncillaryBaggageBanner())
					|| BooleanUtils.isTrue(requestDTO.isAncillaryLoungeBanner())){
				// call Ecomm Service and retrieve Seat, Baggage and lounge response from Ecomm
				ecommServiceProducts = ecommService.getEcommEligibleProducts(rloc,
						retrievePnrBooking.getPos(), loginInfo.getMmbToken());
			}
			if (BooleanUtils.isTrue(requestDTO.isAncillaryASRSeatBanner())
					|| BooleanUtils.isTrue(requestDTO.isAncillaryEXLSeatBanner())) {
				// build segmentIdList that excludes the flights with status = flown or the flight is within checkin window e.g ETD-48hrs
				List<String> segmentIdListOfSeat = buildSegmentIdListOfSeat(booking.getSegments());
				// build Exl Banner Display Eligible and ASR Banner Display Eligible
				buildASRAndEXLBannerDisplayEligible(booking, segmentIdListOfSeat, responseDTO,
						ecommServiceProducts);
			}
			if (BooleanUtils.isTrue(requestDTO.isAncillaryBaggageBanner())) {
				List<JourneySummary> journeys = journeyCalculateHelper.calculateJourneyFromDpEligibility(booking.getOneARloc());
				// build segmentIdList that excludes the flights with status = flown or the 1st flights of journey is within 24hrs
				List<String> segmentIdListOfBaggage = buildSegmentIdListOfBaggage(booking.getSegments(), journeys);
				// build Baggage Banner Display Eligible
				buildBaggageBannerDisplayEligible(segmentIdListOfBaggage, responseDTO, ecommServiceProducts);
			}
			if (BooleanUtils.isTrue(requestDTO.isAncillaryLoungeBanner())){
				buildPaidLoungePassAvailable(booking, responseDTO, ecommServiceProducts);
			}
			if (BooleanUtils.isTrue(requestDTO.isSeatProductAvailableCheck())) {
				if(!CollectionUtils.isEmpty(booking.getPassengerSegments())) {
					if(CollectionUtils.isEmpty(responseDTO.getPassengerSegments())) {
						responseDTO.setPassengerSegments(new ArrayList<>());
					}
					for(PassengerSegment ps : booking.getPassengerSegments()) {
						if (responseDTO.getPassengerSegments().stream().noneMatch(rps -> Objects.equals(ps.getPassengerId(), rps.getPassengerId()) && Objects.equals(ps.getSegmentId(), rps.getSegmentId()))) {
							PassengerSegmentAdditionalInfoDTO additionalPS = new PassengerSegmentAdditionalInfoDTO();
							additionalPS.setPassengerId(ps.getPassengerId());
							additionalPS.setSegmentId(ps.getSegmentId());
							responseDTO.getPassengerSegments().add(additionalPS);
						}
					}
				}
				ProductsResponseDTO seatProducts = ecommService.getEcommSeatProducts(rloc, retrievePnrBooking.getPos(), loginInfo.getMmbToken());
				for(PassengerSegmentAdditionalInfoDTO psAdditionalInfo : responseDTO.getPassengerSegments()) {
					psAdditionalInfo.setSeatProductAvailable(checkSeatProductAvailale(psAdditionalInfo, seatProducts));
				}
			}
		}
		
		if (eligibleToLandTransportation(booking)) {
			callLandTransportationApi(booking, responseDTO);
		}
		
		return responseDTO;
	}

	/**
	 * checkSeatProductAvailale
	 * @param psAdditionalInfo
	 * @param seatProducts
	 * @return
	 */
	private Boolean checkSeatProductAvailale(PassengerSegmentAdditionalInfoDTO psAdditionalInfo,
			ProductsResponseDTO seatProducts) {
		if (seatProducts == null || CollectionUtils.isEmpty(seatProducts.getProducts())) {
			return null;
		}
		
		return seatProducts.getProducts().stream().anyMatch(product -> Objects.equals(psAdditionalInfo.getPassengerId(), product.getPassengerId()) 
				&& !CollectionUtils.isEmpty(product.getSegmentIds()) && product.getSegmentIds().contains(psAdditionalInfo.getSegmentId()));
	}

	/**
	 * build PaidLounge Pass Available
	 * @param booking
	 * @param responseDTO
	 * @param ecommServiceProducts
	 */
	private void buildPaidLoungePassAvailable(Booking booking, BookingAdditionalInfoResponseDTO responseDTO,
			ProductsResponseDTO ecommServiceProducts) {
		if (ecommServiceProducts != null && !CollectionUtils.isEmpty(ecommServiceProducts.getProducts())) {
			List<ProductDTO> loungeproducts = ecommServiceProducts.getProducts().stream()
					.filter(product -> Objects.equals(product.getProductType(), ProductTypeEnum.LOUNGE_BUSINESS)
							|| Objects.equals(product.getProductType(), ProductTypeEnum.LOUNGE_FIRST))
					.collect(Collectors.toList());
			List<Segment> segments = booking.getSegments();
			if (!CollectionUtils.isEmpty(loungeproducts)) {
				responseDTO.setPaidLoungePassAvailable(booking.getPassengerSegments().stream()
						.anyMatch(ps -> checkPaidLoungeStatus(ps, segments, loungeproducts)));
			}
		}
	}

	/**
	 * check PaidLounge Status
	 * @param passengerSegment
	 * @param segments 
	 * @param products
	 * @return
	 */
	private boolean checkPaidLoungeStatus(PassengerSegment passengerSegment, List<Segment> segments, List<ProductDTO> products) {
		Segment segment = getSegmentById(segments, passengerSegment.getSegmentId());
		if(segment == null || BooleanUtils.isTrue(segment.isFlown())){
			return false;
		}
		for(ProductDTO product : products){
			if(Objects.equals(product.getPassengerId(),passengerSegment.getPassengerId()) 
					&& !CollectionUtils.isEmpty(product.getSegmentIds()) && product.getSegmentIds().contains(passengerSegment.getSegmentId())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Call land transportation api
	 * 
	 * @param booking
	 * @param responseDTO
	 * 
	 * @author kent.cheung
	 */
	private void callLandTransportationApi(Booking booking, BookingAdditionalInfoResponseDTO responseDTO) {
		Segment firstSegment = booking.getSegments().get(0);
		
		PassengerSegment firstPassengerSegment = booking.getPassengerSegments().get(0);
		
		String deptDate = firstSegment.getDepartureTime().getTime().toString().replaceAll("[-+.^:, ]", "");
		try {
			
			Date createDate = DateUtil.getStrToDate(
					RetrievePnrBookingCerateInfo.CREATE_DATE_FOMRAT + RetrievePnrBookingCerateInfo.CREATE_TIME_FOMRAT,
					booking.getBookingCreateInfo().getCreateDate() + booking.getBookingCreateInfo().getCreateTime());

			String createDateString = DateUtil.getDate2Str("yyyyMMddHHmm", createDate);
			
			HZMBannerResponseDTO hzmBannerResponseDTO = hzmBannerEligibleService.checkHZMBannerEligible(firstSegment.getOriginPort(), firstSegment.getDestPort(), firstSegment.getSubClass(),
					firstPassengerSegment.getEticketOriginatorId(), createDateString, firstSegment.getMarketCompany(), firstSegment.getMarketSegmentNumber(), deptDate);
			
			if (hzmBannerResponseDTO != null) {
				setHzmApiResponseDataToBooking(responseDTO, hzmBannerResponseDTO);
			}
		} catch (Exception e) {
			logger.error(String.format("Retrieve pnr create time failed, pnr create date[%s],pnr create time[%s]",
					booking.getBookingCreateInfo().getCreateDate(), booking.getBookingCreateInfo().getCreateTime()));
		}
		
	}
	
	
	/**
	 * Check eligibility to call land transportation api
	 * 
	 * @param booking
	 * 
	 * @author kent.cheung
	 */
	private boolean eligibleToLandTransportation(Booking booking) {
		Segment firstSegment = booking.getSegments().get(0);
		
		boolean isAllPassengerHasEticket = true;
		for(PassengerSegment passengerSegment: booking.getPassengerSegments()) {
			if (passengerSegment.getEticketNumber() == null) {
				isAllPassengerHasEticket = false;
			}
		}
		
		if (firstSegment.getSegmentStatus() != null) {
			
			if ((checkToCallTransportationApi) 
				&& (!firstSegment.isFlown()) 
				&& (isAllPassengerHasEticket) 
				&& (FlightStatusEnum.CONFIRMED.getCode().equals(firstSegment.getSegmentStatus().getStatus().getCode()))) {
				
				return true;
			}
		}
		return false;
		
	}
	
	
	
	
	/**
	 * Set data(eligible and type) from land transportation api to booking level
	 * 
	 * @param responseDTO
	 * @param hzmBannerResponseDTO
	 * 
	 * @author kent.cheung
	 */
	private void setHzmApiResponseDataToBooking(BookingAdditionalInfoResponseDTO responseDTO, HZMBannerResponseDTO hzmBannerResponseDTO) {
		
		responseDTO.setHzmTransportationEligible(hzmBannerResponseDTO.getEligible());
		responseDTO.setHzmTransportationType(hzmBannerResponseDTO.getType());
		
	}
	
	
	
	
	/**
	 * build SegmentId List Of Baggage ， excludes the flights with status =
	 * flown or the flight is within 24hrs
	 * 
	 * @param segments
	 * @author jiajian.guo
	 */
	private List<String> buildSegmentIdListOfBaggage(List<Segment> segments, List<JourneySummary> journeys) {
		List<Segment> journeySegmentList = new ArrayList<>();
		journeys.stream().forEach(journey -> {
			if (!CollectionUtils.isEmpty(journey.getSegments())) {
				journeySegmentList.addAll(segments.stream()
						.filter(segment -> journey.getSegments().get(0).getSegmentId().equals(segment.getSegmentID()))
						.collect((Collectors.toList())));
			}
		});

		return journeySegmentList.stream().filter(segment -> !segment.isFlown() && !isSegmentIn24hrs(segment))
				.map(Segment::getSegmentID).collect((Collectors.toList()));
	}

	/**
	 * judge Segment is In 24hrs
	 * 
	 * @param segments
	 * @author jiajian.guo
	 */
	private boolean isSegmentIn24hrs(Segment segment) {

		boolean isSegmentIn24hrs = false;
		try {
			Date departureTime = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM,
					segment.getDepartureTime().getTime(), segment.getDepartureTime().getTimeZoneOffset());
			Date beforeTime = new Date();
			Date afterTime = new Date();
			beforeTime.setTime(System.currentTimeMillis());
			afterTime.setTime(System.currentTimeMillis() + olciConfig.getBaggageBannerDisplayTime());
			if ((departureTime.before(afterTime) && departureTime.after(beforeTime))
					|| departureTime.compareTo(afterTime) == 0) {
				isSegmentIn24hrs = true;
			}
		} catch (ParseException e) {
			logger.error("Error to convert depaeture time");
		}

		return isSegmentIn24hrs;
	}
    /**
     * build Baggage Banner Display Eligible
     * @param segmentIdList
     * @param responseDTO
     * @param ancillaryBannerResponseDTO
     * @author jiajian.guo
     */
	private void buildBaggageBannerDisplayEligible(List<String> segmentIdList,
			BookingAdditionalInfoResponseDTO responseDTO, ProductsResponseDTO ecommServiceProducts) {

		// At least one pax is a condition that satisfies the purchase, return
		// true
		List<PassengerSegmentAdditionalInfoDTO> passengerSegmentAdditionalInfos = responseDTO.getPassengerSegments();
		List<PassengerSegmentAdditionalInfoDTO> newPassengerSegmentAdditionalInfo = new ArrayList<>();
		if (ecommServiceProducts == null
				|| CollectionUtils.isEmpty(ecommServiceProducts.getProducts())
				|| CollectionUtils.isEmpty(segmentIdList)) {
			return;
		}
		// if passengerSegmentAdditionalInfos is empty ,need build empty list in responseDTO and set new List
		if (CollectionUtils.isEmpty(passengerSegmentAdditionalInfos)) {
			responseDTO.setPassengerSegments(new ArrayList<>());
		}
		ecommServiceProducts.getProducts().stream().forEach(product -> {
			if (!CollectionUtils.isEmpty(product.getSegmentIds())) {
				product.getSegmentIds().stream().forEach(eSegmentId -> {
					if (segmentIdList.contains(eSegmentId) && (ProductTypeEnum.BAGGAGE_COMMON.equals(product.getProductType())
							|| ProductTypeEnum.BAGGAGE_USA.equals(product.getProductType())
							|| ProductTypeEnum.BAGGAGE_NZL.equals(product.getProductType()))) {
						buildBaggageBannerEligible(passengerSegmentAdditionalInfos,newPassengerSegmentAdditionalInfo,product,eSegmentId);			
					}
				});
			}
		});
		responseDTO.getPassengerSegments().addAll(newPassengerSegmentAdditionalInfo);
	}
    /**
     * if passengerSegmentAdditionalInfos is empty ，set eligible value in list with
     * @param passengerSegmentAdditionalInfos
     * @param newPassengerSegmentAdditionalInfo
     * @param product
     * @param eSegmentId
     * @author jiajian.guo
     */
	private void buildBaggageBannerEligible(List<PassengerSegmentAdditionalInfoDTO> passengerSegmentAdditionalInfos,
			List<PassengerSegmentAdditionalInfoDTO> newPassengerSegmentAdditionalInfo, ProductDTO product,
			String eSegmentId) {
		
		if (CollectionUtils.isEmpty(passengerSegmentAdditionalInfos)) {
			setBaggageBannerEligible(newPassengerSegmentAdditionalInfo, product, eSegmentId);
		}else {
			setBaggageBannerEligibleInAdditionalInfo(passengerSegmentAdditionalInfos,
					newPassengerSegmentAdditionalInfo, product, eSegmentId);
		}	
		
	}
    /**
     * Judge repeat and set new baggage value in new list
     * @param passengerSegmentAdditionalInfos
     * @param newPassengerSegmentAdditionalInfo
     * @param product
     * @param eSegmentId
     * @author jiajian.guo
     */
	private void setBaggageBannerEligibleInAdditionalInfo(
			List<PassengerSegmentAdditionalInfoDTO> passengerSegmentAdditionalInfos,
			List<PassengerSegmentAdditionalInfoDTO> newPassengerSegmentAdditionalInfo, ProductDTO product,
			String eSegmentId) {
			if (passengerSegmentAdditionalInfos.stream().anyMatch(
					p -> p.getPassengerId().equals(product.getPassengerId()) && p.getSegmentId().equals(eSegmentId))) {
				passengerSegmentAdditionalInfos.stream().filter(
						p -> p.getPassengerId().equals(product.getPassengerId()) && p.getSegmentId().equals(eSegmentId))
						.forEach(s -> s.setBaggageBannerEligible(true));
			} else {
				setBaggageBannerEligible(newPassengerSegmentAdditionalInfo, product, eSegmentId);
			}
	}

	private void setBaggageBannerEligible(List<PassengerSegmentAdditionalInfoDTO> newPassengerSegmentAdditionalInfo,
			ProductDTO product, String eSegmentId) {

		PassengerSegmentAdditionalInfoDTO passengerSegmentsAdditionalInfo = new PassengerSegmentAdditionalInfoDTO();
		passengerSegmentsAdditionalInfo.setPassengerId(product.getPassengerId());
		passengerSegmentsAdditionalInfo.setSegmentId(eSegmentId);
		passengerSegmentsAdditionalInfo.setBaggageBannerEligible(true);
		newPassengerSegmentAdditionalInfo.add(passengerSegmentsAdditionalInfo);

	}

	/**
	 * Check at least one pax that satisfies the condition can be purchase
	 * ASRseat or purchase EXLseat
	 * 
	 * @param booking
	 * @author jiajian.guo
	 * @param responseDTO
	 * @param EligibleSeatProductExist
	 * @param string
	 */
	private void buildASRAndEXLBannerDisplayEligible(Booking booking, List<String> segmentIdList,
			BookingAdditionalInfoResponseDTO responseDTO, ProductsResponseDTO ecommServiceProducts) {

		if (CollectionUtils.isEmpty(segmentIdList)
				|| ecommServiceProducts == null
				|| CollectionUtils.isEmpty(ecommServiceProducts.getProducts())
				|| CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return;
		}
		List<PassengerSegmentAdditionalInfoDTO> passengerSegmentsAdditionalInfos = new ArrayList<>();
		// At least one pax is a condition that satisfies the purchase, return true
		ecommServiceProducts.getProducts().stream().forEach(product -> {
			if (!CollectionUtils.isEmpty(product.getSegmentIds())) {
				product.getSegmentIds().stream().forEach(eSegmentId -> {
					if (segmentIdList.contains(eSegmentId) && (ProductTypeEnum.SEAT_EXTRA_LEG_ROOM.equals(product.getProductType())
							|| ProductTypeEnum.SEAT_ASR_REGULAR.equals(product.getProductType()))) {
						setASRAndEXLEligibleBanner(booking, eSegmentId, passengerSegmentsAdditionalInfos, product);
					}
				});
			}
		});
		responseDTO.setPassengerSegments(passengerSegmentsAdditionalInfos);
	}

	/**
	 * Loop traversal to get eligible segment Id , passenger id and seat banner
	 * @param booking
	 * @param eSegmentId
	 * @param passengerSegmentsAdditionalInfos
	 * @param product
	 * @author jiajian.guo
	 */
	private void setASRAndEXLEligibleBanner(
			Booking booking,
			String eSegmentId,
			List<PassengerSegmentAdditionalInfoDTO> passengerSegmentsAdditionalInfos,
			ProductDTO product) {

		for (PassengerSegment passengerSegment : booking.getPassengerSegments()) {
			Passenger passenger = getPasssengerById(booking.getPassengers(), passengerSegment.getPassengerId());
			Segment segment = getSegmentById(booking.getSegments(), passengerSegment.getSegmentId());
			if (passengerSegment.getSegmentId().equals(eSegmentId)) {
				PassengerSegmentAdditionalInfoDTO passengerSegmentsAdditionalInfo = new PassengerSegmentAdditionalInfoDTO();
				setASRAndEXLEligibleInAdditionalInfo(
						passengerSegmentsAdditionalInfos,
						passengerSegmentsAdditionalInfo,
						passengerSegment,
						passenger,
						segment,
						product);
				}
			}
		}
	/**
	 * Pass the value of the segment id and passenger id to the passengersegments in AdditionalInfo
	 * @param passengerSegmentsAdditionalInfos
	 * @param passengerSegmentsAdditionalInfo
	 * @param passengerSegment
	 * @param product
	 */
	private void setASRAndEXLEligibleInAdditionalInfo(
			List<PassengerSegmentAdditionalInfoDTO> passengerSegmentsAdditionalInfos,
			PassengerSegmentAdditionalInfoDTO passengerSegmentsAdditionalInfo,
			PassengerSegment passengerSegment,
			Passenger passenger,
			Segment segment,
			ProductDTO product) {
		if(CollectionUtils.isEmpty(passengerSegmentsAdditionalInfos)){
			passengerSegmentsAdditionalInfo.setPassengerId(passengerSegment.getPassengerId());
			passengerSegmentsAdditionalInfo.setSegmentId(passengerSegment.getSegmentId());
			setASRAndEXLEligible(product, passengerSegmentsAdditionalInfo, passengerSegment, passenger, segment);
			passengerSegmentsAdditionalInfos.add(passengerSegmentsAdditionalInfo);
		}else {
			// Determine if the list has existing data
			if(passengerSegmentsAdditionalInfos.stream().noneMatch(p->p.getPassengerId().equals(passengerSegment.getPassengerId())
					&& p.getSegmentId().equals(passengerSegment.getSegmentId()))){
				passengerSegmentsAdditionalInfo.setPassengerId(passengerSegment.getPassengerId());
				passengerSegmentsAdditionalInfo.setSegmentId(passengerSegment.getSegmentId());
				setASRAndEXLEligible(product, passengerSegmentsAdditionalInfo, passengerSegment, passenger, segment);
				passengerSegmentsAdditionalInfos.add(passengerSegmentsAdditionalInfo);
			}else {
				// Make changes to individual attributes in duplicate data
				passengerSegmentsAdditionalInfos.stream()
				.filter(s-> s.getPassengerId().equals(passengerSegment.getPassengerId()) 
						&& s.getSegmentId().equals(passengerSegment.getSegmentId()))
				.forEach(k-> setASRAndEXLEligible(product, k, passengerSegment, passenger, segment)
				);	
			}
			
		}
		
	}

	private void setASRAndEXLEligible(
			ProductDTO product,
			PassengerSegmentAdditionalInfoDTO passengerSegmentsAdditionalInfo,
			PassengerSegment passengerSegment,
			Passenger passenger,
			Segment segment) {

		if (ProductTypeEnum.SEAT_EXTRA_LEG_ROOM.equals(product.getProductType())) {
			passengerSegmentsAdditionalInfo.setExlBannerEligible(
				BooleanUtils.isTrue(passengerSegment.getMmbSeatSelection().isEligible()) &&
				!BooleanUtils.isTrue(passengerSegment.getMmbSeatSelection().isXlFOC()) &&
				judgePaxHasSeat(passengerSegment) &&
				// If any of upcoming sector(s) is not operated by A320
				segment != null && !OneAConstants.EQUIPMENT_A320.equalsIgnoreCase(segment.getAirCraftType()) &&
				// If logged in pax is travelling in econ cabin for at least one upcoming sector
				passenger != null && BooleanUtils.isTrue(passenger.isPrimaryPassenger()) &&
				MMBBizruleConstants.CABIN_CLASS_ECON_CLASS.equalsIgnoreCase(segment.getCabinClass()) &&
				BizRulesUtil.isFlight(segment.getAirCraftType()) &&
				!segment.isFlown()
			);
		}
		if (ProductTypeEnum.SEAT_ASR_REGULAR.equals(product.getProductType())) {
			passengerSegmentsAdditionalInfo.setAsrBannerEligible(BooleanUtils.isTrue(passengerSegment.getMmbSeatSelection().isEligible()) && !passengerSegment.getMmbSeatSelection().isAsrFOC() && judgePaxHasSeat(passengerSegment));
		}

	}
	
   /**
    * judge Pax Has Seat
    * @param passengerSegment
    * @return
    */
	private boolean judgePaxHasSeat(PassengerSegment passengerSegment) {
		if(passengerSegment.getSeat() == null){
			return true;
		}
		return StringUtils.isEmpty(passengerSegment.getSeat().getSeatNo());
	}

	/**
	 * build SegmentId List Of Seat ， excludes the flights with status = flown
	 * or the flight is within checkin window e.g ETD-48hrs
	 * 
	 * @param segments
	 * @author jiajian.guo
	 */
	private List<String> buildSegmentIdListOfSeat(List<Segment> segments) {

		return segments.stream().filter(segment -> !segment.isFlown() && !isSegmentInCheckInWindow(segment))
				.map(Segment::getSegmentID).collect((Collectors.toList()));
	}

	/**
	 * judge Segment is In 48hrs
	 * 
	 * @param segments
	 * @author jiajian.guo
	 */
	private boolean isSegmentInCheckInWindow(Segment segment) {

		boolean isSegmentInChecinWindow = false;
		try {
			Date departureTime = DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM,
					segment.getDepartureTime().getTime(), segment.getDepartureTime().getTimeZoneOffset());
			Date beforeTime = new Date();
			Date afterTime = new Date();
			beforeTime.setTime(System.currentTimeMillis());
			afterTime.setTime(System.currentTimeMillis() + olciConfig.getCheckInWindowUpper());
			if ((departureTime.before(afterTime) && departureTime.after(beforeTime))
					|| departureTime.compareTo(afterTime) == 0) {
				isSegmentInChecinWindow = true;
			}
		} catch (ParseException e) {
			logger.error("Error to convert depaeture time");
		}

		return isSegmentInChecinWindow;
	}

	private void buildPlusgradeInfo(LoginInfo loginInfo, Booking booking, BookingAdditionalInfoResponseDTO responseDTO,
			String language, RetrievePnrBooking retrievePnrBooking) {
		List<Segment> upgradeBidEligibleSegments = upgradeBidEligibleHelper.getUpgradeBidEligibleSegments(booking);

		List<String> segments = upgradeBidEligibleSegments.stream().map(Segment::getSegmentID)
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(segments)) {
			return;
		}
		
		String url = plusGradeService.getPlusGradeUrl(booking, segments, loginInfo, language, retrievePnrBooking, false);
		String olciurl = plusGradeService.getPlusGradeUrl(booking, segments, loginInfo, language, retrievePnrBooking, true);
		
		// set UpgradeBidEligible
		responseDTO.getSegments().stream().forEach(seg -> {
			if (segments.contains(seg.getSegmentId())) {
				if (StringUtils.isEmpty(url) && StringUtils.isEmpty(olciurl)) {
					seg.setUpgradeBidEligible(false);
				} else {
					seg.setUpgradeBidEligible(true);
				}
			}
		});
		
		responseDTO.setPlusgradeUrl(url);
		responseDTO.setOlciPlusgradeUrl(olciurl);
	}

	/**
	 * build required info
	 * 
	 * @return
	 */
	private BookingBuildRequired buildBookingPropertiesRequired() {
		BookingBuildRequired required = new BookingBuildRequired();
		required.setMemberAward(false);
		required.setSeatSelection(true);
		required.setCprCheck(true);
		required.setUseCprSession(true);
		
		// Booking Properties get WaiverBaggage from sk list, so no need build
		// BaggageAllowances
		required.setBaggageAllowances(false);
		required.setCountryOfResidence(false);
		required.setMealSelection(false);
		required.setPassenagerContactInfo(false);
		required.setEmergencyContactInfo(false);
		return required;
	}
	
	private Segment getSegmentById(List<Segment> segments, String segmentId) {
		if (CollectionUtils.isEmpty(segments) || StringUtils.isEmpty(segmentId)) {
			return null;
		}
		return segments.stream().filter(seg -> seg != null && segmentId.equals(seg.getSegmentID())).findFirst().orElse(null);
	}
	
	private Passenger getPasssengerById(List<Passenger> passengers, String paxId) {
		if (CollectionUtils.isEmpty(passengers) || StringUtils.isEmpty(paxId)) {
			return null;
		}
		return passengers.stream().filter(pax -> pax != null && paxId.equals(pax.getPassengerId())).findFirst().orElse(null);
	}
}
