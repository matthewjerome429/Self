package com.cathaypacific.mmbbizrule.service.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.enums.upgrade.UpgradeBidStatus;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.aep.AEPProductIdEnum;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPAirProduct;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPPassenger;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProduct;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProductsResponse;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPSegment;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.common.od.BgAlOdSegmentDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu.BgAlBtuRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu.BtuRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.od.BgAlOdBookingRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.od.BgAlOdRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu.BgAlBtuSegmentDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.BaggageAllowanceDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinBaggageDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinBaggagePieceDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinBaggageWeightDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinSmallItemDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CheckinBaggageDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CheckinBaggagePieceDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CheckinBaggageWeightDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.btu.BgAlBtuResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.btu.BtuResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdBookingResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdJourneyDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.service.BaggageAllowanceService;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.AirportUpgradeInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageAllowance;
import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.CabinBaggage;
import com.cathaypacific.mmbbizrule.model.booking.detail.CheckInBaggage;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.UpgradeInfo;
import com.cathaypacific.mmbbizrule.service.BaggageAllowanceBuildService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;

@Service
public class BaggageAllowanceBuildServiceImpl implements BaggageAllowanceBuildService {
	
	private static final LogAgent LOGGER = LogAgent.getLogAgent(BaggageAllowanceBuildServiceImpl.class);
	
	public static final String MEMBER_TIER_DEFAULT = "DEFAULT";

	public static final String BAGGAGE_CABIN_CLASS_FIRST_CLASS = "FIRST";
	public static final String BAGGAGE_CABIN_CLASS_BUSINESS_CLASS = "BIZ";
	public static final String BAGGAGE_CABIN_CLASS_PEY_CLASS = "PEY";
	public static final String BAGGAGE_CABIN_CLASS_ECON_CLASS = "ECON";
	
	private static final List<String> BAGGAGE_PRODUCT_ID_LIST;
	
	@Autowired
	private BaggageAllowanceService baggageAllowanceService;
	
	static {
		List<String> baggageProductIdList = Arrays.asList(AEPProductIdEnum.BAGGAGE_COMMON.getId(),
				AEPProductIdEnum.BAGGAGE_USA.getId(), AEPProductIdEnum.BAGGAGE_NZL.getId());
		BAGGAGE_PRODUCT_ID_LIST = Collections.unmodifiableList(baggageProductIdList);
	}

	static class DateAndTime {
		private String date;
		private String time;

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
	}

	@Async
	@Override
	public Future<BaggageAllowanceInfo> asyncRetrieveBaggageAllowanceInfo(Booking booking,
			Future<AEPProductsResponse> futureAEPProductsResponse) throws BusinessBaseException {

		AEPProductsResponse aepProductsResponse = null;
		try {
			aepProductsResponse = futureAEPProductsResponse.get();
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if (cause instanceof Exception) {
				LOGGER.warn("Get AEP products failed", (Exception) cause);
			} else {
				LOGGER.warn("Get AEP products failed. " + cause.getMessage());
			}
		} catch (Exception e) {
			LOGGER.warn("Get AEP products failed", e);
		}
		
		return new AsyncResult<>(this.retrieveBaggageAllowanceInfo(booking, aepProductsResponse));
	}

	@Override
	public BaggageAllowanceInfo retrieveBaggageAllowanceInfo(Booking booking, AEPProductsResponse aepProductsResponse)
			throws BusinessBaseException {

		BaggageAllowanceInfo baggageAllowanceInfo = new BaggageAllowanceInfo();

		// Filter baggage product from AEP products.
		List<AEPProduct> baggageAEPProducts = filterBaggageProduct(aepProductsResponse);
		baggageAllowanceInfo.setBaggageAEPProducts(baggageAEPProducts);
		int totalSegmentCount = 0;
		if (!CollectionUtils.isEmpty(baggageAEPProducts)) {
			BgAlBtuRequestDTO baggageAllowanceRequestDTO = buildBaggageAllowanceBtuRequestByAEP(booking, baggageAEPProducts);

			BgAlBtuResponseDTO baggageAllowanceResponseDTO =
					baggageAllowanceService.getBaggageAllowanceByBtu(baggageAllowanceRequestDTO);

			baggageAllowanceInfo.setBtuDTOs(baggageAllowanceResponseDTO.getBtu());
			totalSegmentCount = baggageAEPProducts.stream().filter(p->p.getAirProduct()!=null)
					.map(p->p.getAirProduct())
					.map(p->p.size())
					.collect(Collectors.summingInt(Integer::intValue));
		} 
		 
		if (booking.getSegments().size()!=totalSegmentCount) {
			BgAlOdRequestDTO baggageAllowanceRequestDTO;
			if (isAnySegmentDiffCabinExist(booking)) {
				baggageAllowanceRequestDTO = buildBaggageAllowanceOdRequestDTOByPassenger(booking);
			} else {
				baggageAllowanceRequestDTO = buildBaggageAllowanceOdRequestDTO(booking);	
			}
			try {
				BgAlOdResponseDTO baggageAllowanceResponseDTO =
						baggageAllowanceService.getBaggageAllowanceByOd(baggageAllowanceRequestDTO);
				baggageAllowanceInfo.setBookingDTOs(baggageAllowanceResponseDTO.getBookings());
			}catch (BusinessBaseException e) {
				if (CollectionUtils.isEmpty(baggageAEPProducts)) {
					throw e;
				}else {
					LOGGER.warn("fail to get baggage from baggageAllowanceService.getBaggageAllowanceByOd", e);
				}
			}
			
			
		}

		return baggageAllowanceInfo;
	}
	
	private List<AEPProduct> filterBaggageProduct(AEPProductsResponse aepProductsResponse) {
		
		if (aepProductsResponse == null || CollectionUtils.isEmpty(aepProductsResponse.getProducts())) {
			return Collections.emptyList();
		}
		
		return aepProductsResponse.getProducts().stream().filter(
				aepProduct -> BAGGAGE_PRODUCT_ID_LIST.contains(aepProduct.getProductId())
			).collect(Collectors.toList());
	}

	/**
	 * When AEP returns baggage product item with journey, build BTU request by AEP response.
	 * 
	 * @param booking
	 * @param baggageAEPProducts
	 * @return
	 * @throws BusinessBaseException
	 */
	private BgAlBtuRequestDTO buildBaggageAllowanceBtuRequestByAEP(Booking booking,
			List<AEPProduct> baggageAEPProducts) throws BusinessBaseException {

		Map<String, Segment> segmentMap = booking.getSegments().stream().collect(
				Collectors.toMap(
						Segment::getSegmentID, 
						segment -> segment));
		
		List<BtuRequestDTO> btuList = new ArrayList<>();
		// BTU ID must be 1 based continuous integer.
		int btuId = 1;
		
		for (AEPProduct aepProduct : baggageAEPProducts) {
			BtuRequestDTO btuDTO = new BtuRequestDTO();
			
			List<BgAlBtuSegmentDTO> segmentDTOs = new ArrayList<>();
			AEPAirProduct aepAirProduct = aepProduct.getAirProduct().get(0);
			AEPPassenger aepPassenger = aepAirProduct.getPassengers().get(0);
			for (AEPSegment aepSegment : aepAirProduct.getSegments()) {
				
				Segment segment = segmentMap.get(aepSegment.getSegmentRef().toString());
				
				BgAlBtuSegmentDTO segmentDTO = new BgAlBtuSegmentDTO();
				segmentDTO.setBoardPoint(aepSegment.getOrigin());
				segmentDTO.setOffPoint(aepSegment.getDestination());
				segmentDTO.setOperatingCarrier(aepSegment.getMarketingCarrier());
				
				AirportUpgradeInfo airportUpgradeInfo = booking.getPassengerSegments().stream().filter(
						passengerSegment -> passengerSegment.getSegmentId().equals(aepSegment.getSegmentRef().toString()) && 
							passengerSegment.getPassengerId().equals(aepPassenger.getPassengerRef().toString())
					).findFirst().map(PassengerSegment::getAirportUpgradeInfo).orElse(null);

				
				UpgradeInfo upgradeInfo = segment.getUpgradeInfo();
				
				if (airportUpgradeInfo != null && !StringUtils.isEmpty(airportUpgradeInfo.getNewCabinClass())) {
					
					// Use airport upgrade cabin
					segmentDTO.setCabinClass(airportUpgradeInfo.getNewCabinClass());
					
				} else if (upgradeInfo != null && upgradeInfo.getBidUpgradeInfo() != null &&
						upgradeInfo.getBidUpgradeInfo().getStatus().equals(UpgradeBidStatus.SUCCESS)&&
						segment.getCabinClass().equals(upgradeInfo.getToCabinClass())) {
					
					// Use original cabin instead of upgrade bid cabin
					segmentDTO.setCabinClass(upgradeInfo.getFromCabinClass());
					
				} else {
					segmentDTO.setCabinClass(segment.getCabinClass());
				}
				
				DateAndTime departureDateAndTime = getDateAndTime(segment.getDepartureTime());
				segmentDTO.setDepartureDate(departureDateAndTime.getDate());
				segmentDTO.setDepartureTime(departureDateAndTime.getTime());
				
				segmentDTOs.add(segmentDTO);
			}
			btuDTO.setSegments(segmentDTOs);
			
			btuDTO.setBaggageAllowanceType(aepProduct.getProductId());
			btuDTO.setBtuId(Integer.toString(btuId));
			btuList.add(btuDTO);
			
			btuId++;
		}
		
		BgAlBtuRequestDTO baggageAllowanceRequestDTO = new BgAlBtuRequestDTO();
		baggageAllowanceRequestDTO.setBtu(btuList);
		
		return baggageAllowanceRequestDTO;
	}
	
	private DateAndTime getDateAndTime(DepartureArrivalTime departureArrivalTime) throws BusinessBaseException {
		
		try {
			Date date = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT,
					departureArrivalTime.getPnrTime(), departureArrivalTime.getTimeZoneOffset());

			DateAndTime dateAndTime = new DateAndTime();
			dateAndTime.setDate(DateUtil.getDate2Str(DateUtil.DATE_PATTERN_DDMMYY, ZoneOffset.UTC.getId(), date));
			dateAndTime.setTime(DateUtil.getDate2Str(DateUtil.TIME_PATTERN_HHMM, ZoneOffset.UTC.getId(), date));
			return dateAndTime;
			
		} catch (ParseException e) {
			throw new UnexpectedException("Convert departure time to UTC time failed.",
					new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM), e);
		}
	}

	@Override
	public BaggageAllowanceInfo retrieveBaggageAllowanceInfo(Booking booking, ProductsResponseDTO ecommProductsResponse)
			throws BusinessBaseException {
		
		BaggageAllowanceInfo baggageAllowanceInfo = new BaggageAllowanceInfo();

		// Filter baggage product from AEP products.
		List<ProductDTO> baggageEcommProducts = ecommProductsResponse.getProducts();
		baggageAllowanceInfo.setBaggageEcommProducts(baggageEcommProducts);
		int totalSegmentCount = 0;
		if (!CollectionUtils.isEmpty(baggageEcommProducts)) {
			BgAlBtuRequestDTO baggageAllowanceRequestDTO = buildBaggageAllowanceBtuRequestByEcomm(booking, baggageEcommProducts);

			BgAlBtuResponseDTO baggageAllowanceResponseDTO =
					baggageAllowanceService.getBaggageAllowanceByBtu(baggageAllowanceRequestDTO);

			baggageAllowanceInfo.setBtuDTOs(baggageAllowanceResponseDTO.getBtu());
			totalSegmentCount = baggageEcommProducts.stream()
					.filter(p->p.getSegmentIds()!=null)
					.map(p->p.getSegmentIds())				
					.map(p->p.size())
					.collect(Collectors.summingInt(Integer::intValue));
		} 

		if (booking.getSegments().size()!=totalSegmentCount) {
			BgAlOdRequestDTO baggageAllowanceRequestDTO;
			if (isAnySegmentDiffCabinExist(booking)) {
				baggageAllowanceRequestDTO = buildBaggageAllowanceOdRequestDTOByPassenger(booking);
			} else {
				baggageAllowanceRequestDTO = buildBaggageAllowanceOdRequestDTO(booking);	
			}
			
			try {
				BgAlOdResponseDTO baggageAllowanceResponseDTO =
						baggageAllowanceService.getBaggageAllowanceByOd(baggageAllowanceRequestDTO);
				baggageAllowanceInfo.setBookingDTOs(baggageAllowanceResponseDTO.getBookings());
			}catch (BusinessBaseException e) {
				if (CollectionUtils.isEmpty(baggageEcommProducts)) {
					throw e;
				}else {
					LOGGER.warn("fail to get baggage from baggageAllowanceService.getBaggageAllowanceByOd", e);
				}
			}
		}

		return baggageAllowanceInfo;
	}

	/**
	 * When Ecomm service returns baggage product item with journey, build BTU request by Ecomm response.
	 * 
	 * @param booking
	 * @param baggageAEPProducts
	 * @return
	 * @throws BusinessBaseException
	 */
	private BgAlBtuRequestDTO buildBaggageAllowanceBtuRequestByEcomm(Booking booking,
			List<ProductDTO> baggageEcommProducts) throws BusinessBaseException {

		Map<String, Segment> segmentMap = booking.getSegments().stream().collect(
				Collectors.toMap(
						Segment::getSegmentID, 
						segment -> segment));
		
		List<BtuRequestDTO> btuList = new ArrayList<>();
		// BTU ID must be 1 based continuous integer.
		int btuId = 1;
		
		for (ProductDTO ecommProduct : baggageEcommProducts) {
			BtuRequestDTO btuDTO = new BtuRequestDTO();
			
			List<BgAlBtuSegmentDTO> segmentDTOs = new ArrayList<>();
			String passengerId = ecommProduct.getPassengerId();
			for (String segmentId : ecommProduct.getSegmentIds()) {
				
				Segment segment = segmentMap.get(segmentId);
				
				BgAlBtuSegmentDTO segmentDTO = new BgAlBtuSegmentDTO();
				segmentDTO.setBoardPoint(segment.getOriginPort());
				segmentDTO.setOffPoint(segment.getDestPort());
				segmentDTO.setOperatingCarrier(segment.getMarketCompany());
				
				AirportUpgradeInfo airportUpgradeInfo = booking.getPassengerSegments().stream().filter(
						passengerSegment -> passengerSegment.getSegmentId().equals(segmentId) && 
							passengerSegment.getPassengerId().equals(passengerId)
					).findFirst().map(PassengerSegment::getAirportUpgradeInfo).orElse(null);

				
				UpgradeInfo upgradeInfo = segment.getUpgradeInfo();
				
				if (airportUpgradeInfo != null && !StringUtils.isEmpty(airportUpgradeInfo.getNewCabinClass())) {
					
					// Use airport upgrade cabin
					segmentDTO.setCabinClass(airportUpgradeInfo.getNewCabinClass());
					
				} else if (upgradeInfo != null && upgradeInfo.getBidUpgradeInfo() != null &&
						upgradeInfo.getBidUpgradeInfo().getStatus().equals(UpgradeBidStatus.SUCCESS)&&
						segment.getCabinClass().equals(upgradeInfo.getToCabinClass())) {
					
					// Use original cabin instead of upgrade bid cabin
					segmentDTO.setCabinClass(upgradeInfo.getFromCabinClass());
					
				} else {
					segmentDTO.setCabinClass(segment.getCabinClass());
				}
				
				DateAndTime departureDateAndTime = getDateAndTime(segment.getDepartureTime());
				segmentDTO.setDepartureDate(departureDateAndTime.getDate());
				segmentDTO.setDepartureTime(departureDateAndTime.getTime());
				
				segmentDTOs.add(segmentDTO);
			}
			btuDTO.setSegments(segmentDTOs);
			
			btuDTO.setBaggageAllowanceType(ecommProduct.getProductType().getProductId());
			btuDTO.setBtuId(Integer.toString(btuId));
			btuList.add(btuDTO);
			
			btuId++;
		}
		
		BgAlBtuRequestDTO baggageAllowanceRequestDTO = new BgAlBtuRequestDTO();
		baggageAllowanceRequestDTO.setBtu(btuList);
		
		return baggageAllowanceRequestDTO;
	}
	
	/**
	 * When AEP baggage product item doesn't exist, build OD request by booking segments.
	 * 
	 * @param booking
	 * @return
	 * @throws BusinessBaseException
	 */
	private BgAlOdRequestDTO buildBaggageAllowanceOdRequestDTO(Booking booking) throws BusinessBaseException {
		
		BgAlOdBookingRequestDTO baggageAllowanceBookingDTO = new BgAlOdBookingRequestDTO();
		baggageAllowanceBookingDTO.setBookingId(booking.getOneARloc());
		
		List<Segment> segments = booking.getSegments();
		List<BgAlOdSegmentDTO> baggageAllowanceOdSegmentDTOs = new ArrayList<>();
		for (Segment segment : segments) {
			BgAlOdSegmentDTO segmentDTO = new BgAlOdSegmentDTO();
			segmentDTO.setBoardPoint(segment.getOriginPort());
			segmentDTO.setOffPoint(segment.getDestPort());
			segmentDTO.setOperatingCarrier(segment.getOperateCompany());
			segmentDTO.setMarketingCarrier(segment.getMarketCompany());
			segmentDTO.setCabinClass(getBaggageApiCabinClass(segment.getCabinClass()));
			DateAndTime departureDateAndTime = getDateAndTime(segment.getDepartureTime());
			segmentDTO.setDepartureDate(departureDateAndTime.getDate());
			segmentDTO.setDepartureTime(departureDateAndTime.getTime());
			DateAndTime arrivalDateAndTime = getDateAndTime(segment.getArrivalTime());
			segmentDTO.setArrivalDate(arrivalDateAndTime.getDate());
			segmentDTO.setArrivalTime(arrivalDateAndTime.getTime());
			
			baggageAllowanceOdSegmentDTOs.add(segmentDTO);
		}
		
		baggageAllowanceBookingDTO.setSegments(baggageAllowanceOdSegmentDTOs);
		
		BgAlOdRequestDTO baggageAllowanceRequestDTO = new BgAlOdRequestDTO();
		baggageAllowanceRequestDTO.setBookings(Arrays.asList(baggageAllowanceBookingDTO));
		return baggageAllowanceRequestDTO;
	}
	
	/**
	 * When AEP baggage product item doesn't exist and cabin class is upgraded, build OD request by booking segments and
	 * new cabin class separating passenger into different booking.
	 * 
	 * @param booking
	 * @return
	 * @throws BusinessBaseException
	 */
	private BgAlOdRequestDTO buildBaggageAllowanceOdRequestDTOByPassenger(Booking booking) throws BusinessBaseException {
		
		List<BgAlOdBookingRequestDTO> baggageAllowanceBookingDTOs = new ArrayList<>();
		List<Passenger> passengers = booking.getPassengers();
		List<String> passengerIds = getBaggageAllowanceOdPassengerIds(passengers);
		List<Segment> segments = booking.getSegments();
		for (String passengerId : passengerIds) {
			BgAlOdBookingRequestDTO baggageAllowanceBookingDTO = new BgAlOdBookingRequestDTO();
			baggageAllowanceBookingDTO.setBookingId(booking.getOneARloc());
			
			List<BgAlOdSegmentDTO> baggageAllowanceOdSegmentDTOs = new ArrayList<>();
			for (Segment segment : segments) {
				BgAlOdSegmentDTO segmentDTO = new BgAlOdSegmentDTO();
				segmentDTO.setBoardPoint(segment.getOriginPort());
				segmentDTO.setOffPoint(segment.getDestPort());
				segmentDTO.setOperatingCarrier(segment.getOperateCompany());
				segmentDTO.setMarketingCarrier(segment.getMarketCompany());
				
				AirportUpgradeInfo airportUpgradeInfo = booking.getPassengerSegments().stream().filter(
						passengerSegment -> passengerSegment.getSegmentId().equals(segment.getSegmentID()) && 
							passengerSegment.getPassengerId().equals(passengerId)
					).findFirst().map(PassengerSegment::getAirportUpgradeInfo).orElse(null);
				
				if (airportUpgradeInfo != null && !StringUtils.isEmpty(airportUpgradeInfo.getNewCabinClass())) {
					segmentDTO.setCabinClass(getBaggageApiCabinClass(airportUpgradeInfo.getNewCabinClass()));
				} else {
					segmentDTO.setCabinClass(getBaggageApiCabinClass(segment.getCabinClass()));	
				}
				
				DateAndTime departureDateAndTime = getDateAndTime(segment.getDepartureTime());
				segmentDTO.setDepartureDate(departureDateAndTime.getDate());
				segmentDTO.setDepartureTime(departureDateAndTime.getTime());
				DateAndTime arrivalDateAndTime = getDateAndTime(segment.getArrivalTime());
				segmentDTO.setArrivalDate(arrivalDateAndTime.getDate());
				segmentDTO.setArrivalTime(arrivalDateAndTime.getTime());
				
				baggageAllowanceOdSegmentDTOs.add(segmentDTO);
			}
			
			baggageAllowanceBookingDTO.setSegments(baggageAllowanceOdSegmentDTOs);
			baggageAllowanceBookingDTOs.add(baggageAllowanceBookingDTO);
		}
		
		BgAlOdRequestDTO baggageAllowanceRequestDTO = new BgAlOdRequestDTO();
		baggageAllowanceRequestDTO.setBookings(baggageAllowanceBookingDTOs);
		return baggageAllowanceRequestDTO;		
	}

	private String getBaggageApiCabinClass(String cabinClass) {
		String bgApiCabinClass;
		switch (cabinClass) {
		case MMBBizruleConstants.CABIN_CLASS_ECON_CLASS:
			bgApiCabinClass = BAGGAGE_CABIN_CLASS_ECON_CLASS;
			break;
		case MMBBizruleConstants.CABIN_CLASS_PEY_CLASS:
			bgApiCabinClass = BAGGAGE_CABIN_CLASS_PEY_CLASS;
			break;
		case MMBBizruleConstants.CABIN_CLASS_BUSINESS_CLASS:
			bgApiCabinClass = BAGGAGE_CABIN_CLASS_BUSINESS_CLASS;
			break;
		case MMBBizruleConstants.CABIN_CLASS_FIRST_CLASS:
			bgApiCabinClass = BAGGAGE_CABIN_CLASS_FIRST_CLASS;
			break;
		default:
			bgApiCabinClass = null;
			break;
		}
		return bgApiCabinClass;
	}
	
	/**
	 * Check if there is any segment with different cabin class with original value.<br>
	 * 
	 * @param booking
	 * @return
	 */
	private boolean isAnySegmentDiffCabinExist(Booking booking) {
		for (Segment segment : booking.getSegments()) {
			
			String segmentId = segment.getSegmentID();
			String segmentCabinClass = segment.getCabinClass();
			
			boolean newCabinClassExists = booking.getPassengerSegments().stream().filter(
				passengerSegment -> passengerSegment.getSegmentId().equals(segmentId)
			).map(
				PassengerSegment::getAirportUpgradeInfo
			).filter(
				Objects::nonNull
			).map(
				AirportUpgradeInfo::getNewCabinClass
			).filter(
				Objects::nonNull
			).anyMatch(
				newCabinClass -> !segmentCabinClass.equals(newCabinClass)
			);
			
			if (newCabinClassExists) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get passenger ID list without infant passenger.
	 * 
	 * @param passengers
	 * @return
	 */
	private List<String> getBaggageAllowanceOdPassengerIds(List<Passenger> passengers) {
		return passengers.stream().map(
				Passenger::getPassengerId
			).filter(
				passengerId -> !passengerId.endsWith("I")
			).sorted().collect(Collectors.toList());
	}
	
	@Override
	public void populateBaggageAllowance(Booking booking, Future<BaggageAllowanceInfo> futureBaggageAllowanceInfo)
			throws BusinessBaseException {

		try {
			BaggageAllowanceInfo baggageAllowanceInfo = null;
			try {
				baggageAllowanceInfo = futureBaggageAllowanceInfo.get();
			} catch (ExecutionException e) {
				Throwable cause = e.getCause();
				if (cause instanceof BusinessBaseException) {
					throw (BusinessBaseException) cause;
				} else {
					throw new UnexpectedException("Call baggage allowance API failed",
							new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM), cause);
				}
			} catch (Exception e) {
				throw new UnexpectedException("Call baggage allowance API failed",
						new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM), e);
			}

			populateBaggageAllowance(booking, baggageAllowanceInfo);
			
		} finally {
			setBaggageAllowanceCompleted(booking);
		}
	}

	/**
	 * Consider baggage allowance data is always completed on non CX KA segment, flown segment, cancelled segment.
	 * 
	 * @param booking
	 */
	@Override
	public void setBaggageAllowanceCompleted(Booking booking) {
		Predicate<Segment> segmentPredict = segment -> {
			boolean isCXKASegment = OneAConstants.COMPANY_CX.equals(segment.getMarketCompany())
					|| OneAConstants.COMPANY_KA.equals(segment.getMarketCompany());
			
			return !isCXKASegment || segment.isFlown() || 
					FlightStatusEnum.CANCELLED.equals(segment.getSegmentStatus().getStatus());
		};
		
		List<String> segmentIds = booking.getSegments().stream().filter(segmentPredict
			).map(Segment::getSegmentID).collect(Collectors.toList());
		
		booking.getPassengerSegments().stream().filter(
				passengerSegment -> segmentIds.contains(passengerSegment.getSegmentId())
			).forEach(
				passengerSegment -> passengerSegment.findBaggageAllowance().setCompleted(true));
	}
	
	@Override
	public void populateBaggageAllowance(Booking booking, BaggageAllowanceInfo baggageAllowanceInfo) {

		/*
		 *  Cabin baggage availability
		 *  This is to populate if cabin baggage is not available due to our known reasons (marked in populateCabinBaggageAvailability comment)
		 *  Doesn't depends on AEP reply
		 */
		for (PassengerSegment passengerSegment : booking.getPassengerSegments()) {
			
			// Find the segment
			Segment segment = booking.getSegments().stream().filter(
								s -> s.getSegmentID().equals(passengerSegment.getSegmentId())
							).findFirst().orElse(null);
			
			// Populate cabin baggage availability
			populateCabinBaggageAvailability(
					passengerSegment, segment, 
					booking.isStaffBooking(), booking.isGroupBooking(),
					passengerSegment.findBaggageAllowance());
		}
		/*
		 * End of Cabin baggage availability
		 */
		
		
		List<AEPProduct> aepProducts = baggageAllowanceInfo.getBaggageAEPProducts();
		List<ProductDTO> ecommProducts = baggageAllowanceInfo.getBaggageEcommProducts();
		List<BtuResponseDTO> btuDTOs = baggageAllowanceInfo.getBtuDTOs(); 
		List<BgAlOdBookingResponseDTO> bookingDTOs = baggageAllowanceInfo.getBookingDTOs();

		List<String> completedAepSegmentId = new ArrayList<String>() ;
		
		if (btuDTOs != null) {
			if (!CollectionUtils.isEmpty(aepProducts)) {
				populateBaggageAllowanceByBtuByAEP(booking, aepProducts, btuDTOs, completedAepSegmentId);
			} else {
				populateBaggageAllowanceByBtuByEcomm(booking, ecommProducts, btuDTOs, completedAepSegmentId);
			}
		}
		 
		if (bookingDTOs != null) {
			if (isAnySegmentDiffCabinExist(booking)) {
				populateBaggageAllowanceByOdByPassenger(booking, bookingDTOs, completedAepSegmentId);
			} else {
				populateBaggageAllowanceByOd(booking, bookingDTOs, completedAepSegmentId);
			}
		}
	}
	
	/**
	 * This method is to populate a indicator to indicate cabin baggage availability
	 * The cabin baggage must be unavailable if 
	 * (1) group/MICE/staff booking and
	 * (2) ticket stock not 160/043 (P.S. DOESN'T mean CX/KA marketing company) and
	 * (3) flight within STD-24hrs
	 * 
	 * This indicator is used in front-end behavior.
	 * Please note that if not fulfill one of them, doesn't mean cabin baggage is available.
	 * 
	 * @param passengerSegment
	 * @param baggageAllowance
	 */
	private void populateCabinBaggageAvailability(
			PassengerSegment passengerSegment, Segment segment,
			boolean isStaffBooking, boolean isGroupBooking,
			BaggageAllowance baggageAllowance
		) {
		
		// Condition 1. group/MICE/staff booking
		boolean isGroupOrMiceOrStaffBooking = isStaffBooking || isGroupBooking;
		
		// Condition 2. ticket stock not 160/043
		boolean isCXKATicket = BookingBuildUtil.isCxKaET(passengerSegment.getEticketNumber());
		
		// Condition 3. flight within STD-24hrs
		boolean isWithinTwentyFourHrs = (segment != null) ? segment.isWithinTwentyFourHrs() : false;
		
		// Set the cabin baggage
		baggageAllowance.setCabinBaggageUnavailable(isGroupOrMiceOrStaffBooking && !isCXKATicket && isWithinTwentyFourHrs);
	}

	private void populateBaggageAllowanceByBtuByAEP(Booking booking, List<AEPProduct> aepProducts, List<BtuResponseDTO> btuDTOs, List<String> completedSegmentId) {
		if (CollectionUtils.isEmpty(btuDTOs)) {
			LOGGER.warn("Baggage allowance by BTU response is empty");
			return;
		}
		
		int productCount = aepProducts.size();
		for (int i = 0; i < productCount; i++) {
			/*
			 * Get passenger ID and segment ID list of AEP product.
			 */
			AEPProduct aepProduct = aepProducts.get(i);
			AEPAirProduct aepAirProduct = aepProduct.getAirProduct().get(0);
			String passengerId = aepAirProduct.getPassengers().get(0).getPassengerRef().toString();
			List<String> segmentIds = aepAirProduct.getSegments().stream().map(
					aepSegment -> aepSegment.getSegmentRef().toString()
				).collect(Collectors.toList());
			
			// Find PassengerSegment objects matching AEP product.
			List<PassengerSegment> passengerSegments = booking.getPassengerSegments().stream().filter(
					passengerSegment -> passengerId.equals(passengerSegment.getPassengerId())
						&& segmentIds.contains(passengerSegment.getSegmentId())
				).collect(Collectors.toList());
			
			passengerSegments.stream().forEach(p->{completedSegmentId.add(p.getSegmentId());});
			// Get DTO of baggage allowance API corresponding to AEP product.
			BtuResponseDTO btuDTO = btuDTOs.get(i);
			
			/*
			 * Populate baggage allowance.
			 */
			for (PassengerSegment passengerSegment : passengerSegments) {
				// Find associated infant PassengerSegment objects.
				String infantPassengerId = passengerId + "I";
				PassengerSegment infantPassengerSegment = booking.getPassengerSegments().stream().filter(
						ps -> infantPassengerId.equals(ps.getPassengerId()) && 
						passengerSegment.getSegmentId().equals(ps.getSegmentId())
					).findFirst().orElse(null);
				
				populateBaggageAllowanceToPS(passengerSegment, infantPassengerSegment, booking.isStaffBooking(),
						btuDTO, aepProduct.getProductId());
			}
		}
	}

	private void populateBaggageAllowanceByBtuByEcomm(Booking booking, List<ProductDTO> ecommProducts, List<BtuResponseDTO> btuDTOs, List<String> completedSegmentId) {
		if (CollectionUtils.isEmpty(btuDTOs)) {
			LOGGER.warn("Baggage allowance by BTU response is empty");
			return;
		}
		
		int productCount = ecommProducts.size();
		for (int i = 0; i < productCount; i++) {
			/*
			 * Get passenger ID and segment ID list of AEP product.
			 */
			ProductDTO ecommProduct = ecommProducts.get(i);
			String passengerId = ecommProduct.getPassengerId();
			List<String> segmentIds = ecommProduct.getSegmentIds();
			completedSegmentId.addAll(segmentIds);
			// Find PassengerSegment objects matching AEP product.
			List<PassengerSegment> passengerSegments = booking.getPassengerSegments().stream().filter(
					passengerSegment -> passengerId.equals(passengerSegment.getPassengerId())
						&& segmentIds.contains(passengerSegment.getSegmentId())
				).collect(Collectors.toList());

			// Get DTO of baggage allowance API corresponding to AEP product.
			BtuResponseDTO btuDTO = btuDTOs.get(i);
			
			/*
			 * Populate baggage allowance.
			 */
			for (PassengerSegment passengerSegment : passengerSegments) {
				// Find associated infant PassengerSegment objects.
				String infantPassengerId = passengerId + "I";
				PassengerSegment infantPassengerSegment = booking.getPassengerSegments().stream().filter(
						ps -> infantPassengerId.equals(ps.getPassengerId()) && 
						passengerSegment.getSegmentId().equals(ps.getSegmentId())
					).findFirst().orElse(null);
				
				populateBaggageAllowanceToPS(passengerSegment, infantPassengerSegment, booking.isStaffBooking(),
						btuDTO, ecommProduct.getProductType().getProductId());
			}
		}
	}
	
	private void populateBaggageAllowanceToPS(PassengerSegment passengerSegment, PassengerSegment infantPassengerSegment,
			boolean isStaffBooking,	BtuResponseDTO btuDTO, String productId) {
		
		// Find baggage allowance by tier.
		String tier = Optional.of(passengerSegment).map(PassengerSegment::getFqtvInfo).map(FQTVInfo::getTierLevel).orElse(null);
		BaggageAllowanceDTO baggageAllowanceDTO = findTierBaggageAllowance(btuDTO.getBaggageAllowance(), tier);
		
		// If baggage allowance API doesn't return data of corresponding tier, do nothing.
		if(baggageAllowanceDTO == null) {
			return;
		}
		
		/*
		 * If unit of check in standard baggage (from E-ticket) doesn't match product ID, do nothing. 
		 */
		BaggageUnitEnum actualUnit = Optional.ofNullable(passengerSegment.getBaggageAllowance())
			.map(BaggageAllowance::getCheckInBaggage)
			.map(CheckInBaggage::getStandardBaggage)
			.map(BaggageDetail::getUnit).orElse(null);
		if (AEPProductIdEnum.BAGGAGE_COMMON.getId().equals(productId)) {
			if (BaggageUnitEnum.BAGGAGE_PIECE_UNIT.equals(actualUnit)) {
				LOGGER.warn("Expected baggage allowance unit (KG) doesn't match check in baggage standard allowance unit in ET (PC), ignore other baggage allowance");
				return;
			}
		} else {
			if (BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT.equals(actualUnit)) {
				LOGGER.warn("Expected baggage allowance unit (PC) doesn't match check in baggage standard allowance unit in ET (KG), ignore other baggage allowance");
				return;
			}
		}

		/*
		 * Standard check in baggage value is from TicketProcess service. When segment is airport upgrade with new cabin
		 * class, use baggage allowance API value instead.
		 */
		boolean overwriteStandardCheckInBaggage = false;
		AirportUpgradeInfo airportUpgradeInfo = passengerSegment.getAirportUpgradeInfo();
		if (airportUpgradeInfo != null && !StringUtils.isEmpty(airportUpgradeInfo.getNewCabinClass())) {
			overwriteStandardCheckInBaggage = true;
		}
		
		BaggageAllowance baggageAllowance = passengerSegment.findBaggageAllowance();
		baggageAllowance.setCompleted(true);
		populateCabinBaggage(baggageAllowance, baggageAllowanceDTO, productId, isStaffBooking);
		populateCheckInBaggage(baggageAllowance, baggageAllowanceDTO, productId, isStaffBooking,
				overwriteStandardCheckInBaggage);

		if (infantPassengerSegment != null) {
			BaggageAllowance infantBaggageAllowance = infantPassengerSegment.findBaggageAllowance();
			infantBaggageAllowance.setCompleted(true);
			populateInfantCheckInBaggage(infantBaggageAllowance, baggageAllowanceDTO, productId);
		}
	}
	
	private void populateBaggageAllowanceByOd(Booking booking, List<BgAlOdBookingResponseDTO> bookingDTOs, List<String> completedSegmentId) {
		if (CollectionUtils.isEmpty(bookingDTOs)) {
			LOGGER.warn("Baggage allowance by OD response is empty");
			return;
		}
		
		List<BgAlOdJourneyDTO> baggageAllowanceJourneyDTOs = bookingDTOs.get(0).getJourneys();		
		for (BgAlOdJourneyDTO baggageAllowanceJourneyDTO : baggageAllowanceJourneyDTOs) {
			/* 
			 * Get segment ID list of this journey. 
			 */
			List<BgAlOdSegmentDTO> baggageAllowanceSegmentDTOs = baggageAllowanceJourneyDTO.getSegments();
			List<String> segmentIds = booking.getSegments().stream().filter(
					segment -> baggageAllowanceSegmentDTOs.stream().anyMatch(segmentDTO -> isMatchedSegment(segment, segmentDTO))
				).map(Segment::getSegmentID).collect(Collectors.toList());
			
			for (String segmentId : segmentIds) {
				
				// skip baggage allowance it is baggage allowance completed in BTU 
				if (completedSegmentId.contains(segmentId)) {
					continue;
				}
				
				// Find PassengerSegment objects matching segment ID.
				List<PassengerSegment> passengerSegments = booking.getPassengerSegments().stream().filter(
						passengerSegment -> segmentId.equals(passengerSegment.getSegmentId())
					).collect(Collectors.toList());
				
				/*
				 * Populate baggage allowance.
				 */
				for (PassengerSegment passengerSegment : passengerSegments) {
					String passengerId = passengerSegment.getPassengerId();
					if (passengerId.endsWith("I")) {
						// Skip infant PassengerSegment objects.
						continue;
					}
					
					// Find associated infant PassengerSegment objects.
					String infantPassengerId = passengerId + "I";
					PassengerSegment infantPassengerSegment = booking.getPassengerSegments().stream().filter(
							ps -> infantPassengerId.equals(ps.getPassengerId()) && 
							passengerSegment.getSegmentId().equals(ps.getSegmentId())
						).findFirst().orElse(null);
					
					populateBaggageAllowanceToPS(passengerSegment, infantPassengerSegment, booking.isStaffBooking(),
							baggageAllowanceJourneyDTO);
				}
			}
		}
	}
	
	private void populateBaggageAllowanceByOdByPassenger(Booking booking, List<BgAlOdBookingResponseDTO> bookingDTOs, List<String> completedSegmentId) {
		if (CollectionUtils.isEmpty(bookingDTOs)) {
			LOGGER.warn("Baggage allowance by OD response is empty");
			return;
		}
		
		List<Passenger> passengers = booking.getPassengers();
		List<String> passengerIds = getBaggageAllowanceOdPassengerIds(passengers);
		if (bookingDTOs.size() != passengerIds.size()) {
			LOGGER.warn("Size of baggage allowance by OD response doesn't match passenger count");
			return;
		}
		
		for (int i = 0; i < passengerIds.size(); i++) {
			String passengerId = passengerIds.get(i);
			BgAlOdBookingResponseDTO bookingDTO = bookingDTOs.get(i);
			
			List<BgAlOdJourneyDTO> baggageAllowanceJourneyDTOs = bookingDTO.getJourneys();
			
			for (BgAlOdJourneyDTO baggageAllowanceJourneyDTO : baggageAllowanceJourneyDTOs) {
				/* 
				 * Get segment ID list of this journey. 
				 */
				List<BgAlOdSegmentDTO> baggageAllowanceSegmentDTOs = baggageAllowanceJourneyDTO.getSegments();
				List<String> segmentIds = booking.getSegments().stream().filter(
						segment -> baggageAllowanceSegmentDTOs.stream().anyMatch(segmentDTO -> isMatchedSegment(segment, segmentDTO))
					).map(Segment::getSegmentID).collect(Collectors.toList());
				
				for (String segmentId : segmentIds) {
					
					// skip baggage allowance it is baggage allowance completed in BTU 
					if (completedSegmentId.contains(segmentId)) {
						continue;
					}
					
					// Find PassengerSegment objects matching segment ID and passenger ID.
					PassengerSegment passengerSegment = booking.getPassengerSegments().stream().filter(
							ps -> segmentId.equals(ps.getSegmentId()) &&
												passengerId.equals(ps.getPassengerId())
						).findFirst().orElse(null);
					
					/*
					 * Populate baggage allowance.
					 */
					if (passengerSegment != null) {						
						// Find associated infant PassengerSegment objects.
						String infantPassengerId = passengerId + "I";
						PassengerSegment infantPassengerSegment = booking.getPassengerSegments().stream().filter(
								ps -> infantPassengerId.equals(ps.getPassengerId()) && 
								passengerSegment.getSegmentId().equals(ps.getSegmentId())
							).findFirst().orElse(null);
						
						populateBaggageAllowanceToPS(passengerSegment, infantPassengerSegment, booking.isStaffBooking(),
								baggageAllowanceJourneyDTO);
					}
				}
			}
		}
	}
	
	private boolean isMatchedSegment(Segment segment, BgAlOdSegmentDTO baggageAllowanceSegmentDTO) {
		if (segment == null || baggageAllowanceSegmentDTO == null) {
			return false;
		}
		
		if (!Objects.equals(segment.getOriginPort(), baggageAllowanceSegmentDTO.getBoardPoint())) {
			return false;
		}
		
		if (!Objects.equals(segment.getDestPort(), baggageAllowanceSegmentDTO.getOffPoint())) {
			return false;
		}
		
		if (!Objects.equals(segment.getMarketCompany(), baggageAllowanceSegmentDTO.getMarketingCarrier())) {
			return false;
		}
		
		if (!Objects.equals(segment.getOperateCompany(), baggageAllowanceSegmentDTO.getOperatingCarrier())) {
			return false;
		}
		
		try {
			DateAndTime departureDateAndTime = getDateAndTime(segment.getDepartureTime());
			if (!Objects.equals(departureDateAndTime.getDate(), baggageAllowanceSegmentDTO.getDepartureDate()) ||
					!Objects.equals(departureDateAndTime.getTime(), baggageAllowanceSegmentDTO.getDepartureTime())) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.warn("Date format error in baggage allowance response.", e);
			return false;
		}
		
		try {
			DateAndTime arrivalDateAndTime = getDateAndTime(segment.getArrivalTime());
			if (!Objects.equals(arrivalDateAndTime.getDate(), baggageAllowanceSegmentDTO.getArrivalDate()) ||
					!Objects.equals(arrivalDateAndTime.getTime(), baggageAllowanceSegmentDTO.getArrivalTime())) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.warn("Date format error in baggage allowance response.", e);
			return false;
		}		
		
		return true;
	}
	
	private void populateBaggageAllowanceToPS(PassengerSegment passengerSegment, PassengerSegment infantPassengerSegment,
			boolean isStaffBooking,	BgAlOdJourneyDTO baggageAllowanceJourneyDTO) {

		// Find baggage allowance by tier.
		String tier = Optional.of(passengerSegment).map(PassengerSegment::getFqtvInfo).map(FQTVInfo::getTierLevel).orElse(null);
		BaggageAllowanceDTO baggageAllowanceDTO = findTierBaggageAllowance(baggageAllowanceJourneyDTO.getBaggageAllowance(), tier);

		// If baggage allowance API doesn't return data of corresponding tier, do nothing.
		if (baggageAllowanceDTO == null) {
			return;
		}
		
		/*
		 * If unit of check in standard baggage (from E-ticket) doesn't match baggage allowance type, do nothing. 
		 */
		BaggageUnitEnum actualUnit = Optional.ofNullable(passengerSegment.getBaggageAllowance())
			.map(BaggageAllowance::getCheckInBaggage)
			.map(CheckInBaggage::getStandardBaggage)
			.map(BaggageDetail::getUnit).orElse(null);
		if (AEPProductIdEnum.BAGGAGE_COMMON.getId().equals(baggageAllowanceJourneyDTO.getBaggageAllowanceType())) {
			if (BaggageUnitEnum.BAGGAGE_PIECE_UNIT.equals(actualUnit)) {
				LOGGER.warn("Expected baggage allowance unit (KG) doesn't match check in baggage standard allowance unit in ET (PC), ignore other baggage allowance");
				return;
			}
		} else {
			if (BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT.equals(actualUnit)) {
				LOGGER.warn("Expected baggage allowance unit (PC) doesn't match check in baggage standard allowance unit in ET (KG), ignore other baggage allowance");
				return;
			}
		}

		/*
		 * Standard check in baggage value is from TicketProcess service. When segment is airport upgrade with new cabin
		 * class, use baggage allowance API value instead.
		 */
		boolean overwriteStandardCheckInBaggage = false;
		AirportUpgradeInfo airportUpgradeInfo = passengerSegment.getAirportUpgradeInfo();
		if (airportUpgradeInfo != null && !StringUtils.isEmpty(airportUpgradeInfo.getNewCabinClass())) {
			overwriteStandardCheckInBaggage = true;
		}

		BaggageAllowance baggageAllowance = passengerSegment.findBaggageAllowance();
		baggageAllowance.setCompleted(true);
		populateCabinBaggage(baggageAllowance, baggageAllowanceDTO,
				baggageAllowanceJourneyDTO.getBaggageAllowanceType(), isStaffBooking);
		populateCheckInBaggage(baggageAllowance, baggageAllowanceDTO,
				baggageAllowanceJourneyDTO.getBaggageAllowanceType(), isStaffBooking,
				overwriteStandardCheckInBaggage);

		if (infantPassengerSegment != null) {
			BaggageAllowance infantBaggageAllowance = infantPassengerSegment.findBaggageAllowance();
			infantBaggageAllowance.setCompleted(true);
			populateInfantCheckInBaggage(infantBaggageAllowance, baggageAllowanceDTO,
				baggageAllowanceJourneyDTO.getBaggageAllowanceType());
		}
		
	}
	
	private BaggageAllowanceDTO findTierBaggageAllowance(List<BaggageAllowanceDTO> baggageAllowanceDTOs, String tier) {
		
		String baggageAllowanceTier;
		if (StringUtils.isEmpty(tier)) {
			baggageAllowanceTier = MEMBER_TIER_DEFAULT;
		} else {
			baggageAllowanceTier = tier;
		}
		
		return baggageAllowanceDTOs.stream().filter(
				// Search tier matched baggage allowance
				baggageAllowance -> Objects.equals(baggageAllowanceTier, baggageAllowance.getMemberTier())
			).findFirst().orElseGet(() ->

				// Search default baggage allowance when tier is not found in response (e.g. AM)
				baggageAllowanceDTOs.stream().filter(
					baggageAllowance -> Objects.equals(MEMBER_TIER_DEFAULT, baggageAllowance.getMemberTier())
				).findFirst().orElse(null)
			);
	}

	private void populateCabinBaggage(BaggageAllowance baggageAllowance, BaggageAllowanceDTO baggageAllowanceDTO,
			String baggageAllowanceType, boolean isStaffBooking) {
		
		CabinBaggage cabinBaggage = baggageAllowance.findCabinBaggage();
		
		CabinBaggageDTO cabinBaggageDTO = baggageAllowanceDTO.getCabinBaggage();
		if (cabinBaggageDTO == null) {
			baggageAllowance.setCompleted(false);
			return;
		}
		
		CabinBaggageWeightDTO cabinBaggageWeightDTO = cabinBaggageDTO.getWeight();
		CabinBaggagePieceDTO cabinBaggagePieceDTO = cabinBaggageDTO.getPiece();
		
		if (cabinBaggageWeightDTO == null || cabinBaggagePieceDTO == null) {
			baggageAllowance.setCompleted(false);
			return;
		}
		
		if (AEPProductIdEnum.BAGGAGE_COMMON.getId().equals(baggageAllowanceType)) {
			/*
			 * Weight system
			 */
			BaggageDetail standardCabinBaggage = cabinBaggage.findStandardBaggage();
			standardCabinBaggage.setAmount(BigInteger.valueOf(cabinBaggageWeightDTO.getStandard()));
			standardCabinBaggage.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);

			if (cabinBaggageWeightDTO.getMember() > 0
					&& !MEMBER_TIER_DEFAULT.equals(baggageAllowanceDTO.getMemberTier()) 
					&& !isStaffBooking) {
				BaggageDetail memberCabinBaggage = cabinBaggage.findMemberBaggage();
				memberCabinBaggage.setAmount(BigInteger.valueOf(cabinBaggageWeightDTO.getMember()));
				memberCabinBaggage.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
			}
		} else {
			/*
			 * Piece system
			 */
			BaggageDetail standardCabinBaggage = cabinBaggage.findStandardBaggage();
			standardCabinBaggage.setAmount(BigInteger.valueOf(cabinBaggagePieceDTO.getStandard()));
			standardCabinBaggage.setUnit(BaggageUnitEnum.BAGGAGE_PIECE_UNIT);

			if (cabinBaggagePieceDTO.getMember() > 0
					&& !MEMBER_TIER_DEFAULT.equals(baggageAllowanceDTO.getMemberTier())
					&& !isStaffBooking) {
				BaggageDetail memberCabinBaggage = cabinBaggage.findMemberBaggage();
				memberCabinBaggage.setAmount(BigInteger.valueOf(cabinBaggagePieceDTO.getMember()));
				memberCabinBaggage.setUnit(BaggageUnitEnum.BAGGAGE_PIECE_UNIT);
			}
		}
		
		/*
		 * Total and limit is always piece system.
		 */
		BaggageDetail cabinBaggageTotal = cabinBaggage.findTotal();
		cabinBaggageTotal.setAmount(BigInteger.valueOf(
				cabinBaggagePieceDTO.getStandard() + cabinBaggagePieceDTO.getMember()));
		cabinBaggageTotal.setUnit(BaggageUnitEnum.BAGGAGE_PIECE_UNIT);
		
		BaggageDetail cabinBaggageLimit = cabinBaggage.findLimit();
		cabinBaggageLimit.setAmount(BigInteger.valueOf(
				(cabinBaggageWeightDTO.getStandard() + cabinBaggageWeightDTO.getMember())));
		cabinBaggageLimit.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
		
		CabinSmallItemDTO cabinSmallItemDTO = baggageAllowanceDTO.getCabinBaggage().getSmallItem();
		cabinBaggage.setSmallItem(cabinSmallItemDTO.getStandard() + cabinSmallItemDTO.getMember());
	}

	private void populateCheckInBaggage(BaggageAllowance baggageAllowance, BaggageAllowanceDTO baggageAllowanceDTO,
			String baggageAllowanceType, boolean isStaffBooking, boolean overwriteStandard) {
		
		CheckInBaggage checkInBaggage = baggageAllowance.findCheckInBaggage();
		
		CheckinBaggageDTO checkinBaggageDTO = baggageAllowanceDTO.getCheckinBaggage();
		if (checkinBaggageDTO == null) {
			baggageAllowance.setCompleted(false);
			return;
		}
		
		CheckinBaggageWeightDTO checkinBaggageWeightDTO = checkinBaggageDTO.getWeight();
		CheckinBaggagePieceDTO checkinBaggagePieceDTO = checkinBaggageDTO.getPiece();
		
		if (checkinBaggageWeightDTO == null || checkinBaggagePieceDTO == null) {
			baggageAllowance.setCompleted(false);
			return;
		}

		if (AEPProductIdEnum.BAGGAGE_COMMON.getId().equals(baggageAllowanceType)) {
			/*
			 * Weight system
			 */
			if (checkinBaggageWeightDTO.getMember() >= 0
					&& !MEMBER_TIER_DEFAULT.equals(baggageAllowanceDTO.getMemberTier())
					&& !isStaffBooking) {
				BaggageDetail memberCheckInBaggage = checkInBaggage.findMemberBaggage();
				memberCheckInBaggage.setAmount(BigInteger.valueOf(checkinBaggageWeightDTO.getMember()));
				memberCheckInBaggage.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
			}
			
			if (overwriteStandard) {
				BaggageDetail standardCheckInBaggage = checkInBaggage.findStandardBaggage();
				standardCheckInBaggage.setAmount(BigInteger.valueOf(checkinBaggageWeightDTO.getStandard()));
				standardCheckInBaggage.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
			}

			BaggageDetail checkInBaggageLimit = checkInBaggage.findLimit();
			checkInBaggageLimit.setAmount(BigInteger.valueOf(
					(checkinBaggagePieceDTO.getStandard() + checkinBaggagePieceDTO.getMember())));
			checkInBaggageLimit.setUnit(BaggageUnitEnum.BAGGAGE_PIECE_UNIT);
		} else {
			/*
			 * Piece system
			 */
			if (checkinBaggagePieceDTO.getMember() >= 0
					&& !MEMBER_TIER_DEFAULT.equals(baggageAllowanceDTO.getMemberTier())
					&& !isStaffBooking) {
				BaggageDetail memberCheckInBaggage = checkInBaggage.findMemberBaggage();
				memberCheckInBaggage.setAmount(BigInteger.valueOf(checkinBaggagePieceDTO.getMember()));
				memberCheckInBaggage.setUnit(BaggageUnitEnum.BAGGAGE_PIECE_UNIT);
			}
			
			if (overwriteStandard) {
				BaggageDetail standardCheckInBaggage = checkInBaggage.findStandardBaggage();
				standardCheckInBaggage.setAmount(BigInteger.valueOf(checkinBaggagePieceDTO.getStandard()));
				standardCheckInBaggage.setUnit(BaggageUnitEnum.BAGGAGE_PIECE_UNIT);
			}

			BaggageDetail checkInBaggageLimit = checkInBaggage.findLimit();
			checkInBaggageLimit.setAmount(BigInteger.valueOf(
					(checkinBaggageWeightDTO.getStandard() + checkinBaggageWeightDTO.getMember())));
			checkInBaggageLimit.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
		}
	}
	
	private void populateInfantCheckInBaggage(BaggageAllowance infantBaggageAllowance, BaggageAllowanceDTO baggageAllowanceDTO,
			String baggageAllowanceType) {
		
		CheckInBaggage checkInBaggage = infantBaggageAllowance.findCheckInBaggage();
		CheckinBaggageWeightDTO checkinBaggageWeightDTO = baggageAllowanceDTO.getCheckinBaggage().getWeight();
		CheckinBaggagePieceDTO checkinBaggagePieceDTO = baggageAllowanceDTO.getCheckinBaggage().getPiece();
		
		if (AEPProductIdEnum.BAGGAGE_COMMON.getId().equals(baggageAllowanceType)) {
			/*
			 * Weight system
			 */
			BaggageDetail checkInBaggageLimit = checkInBaggage.findLimit();
			checkInBaggageLimit.setAmount(BigInteger.valueOf(checkinBaggagePieceDTO.getInfant()));
			checkInBaggageLimit.setUnit(BaggageUnitEnum.BAGGAGE_PIECE_UNIT);
		} else {
			/*
			 * Piece system
			 */
			BaggageDetail checkInBaggageLimit = checkInBaggage.findLimit();
			checkInBaggageLimit.setAmount(BigInteger.valueOf(checkinBaggageWeightDTO.getInfant()));
			checkInBaggageLimit.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
		}
	}
	
}
