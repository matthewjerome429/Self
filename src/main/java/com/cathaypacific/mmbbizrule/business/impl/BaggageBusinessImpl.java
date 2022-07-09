package com.cathaypacific.mmbbizrule.business.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.BaggageBusiness;
import com.cathaypacific.mmbbizrule.business.BookingPropertiesBusiness;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.util.BaggageAllowanceErrorUtil;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductValueDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.service.EcommService;
import com.cathaypacific.mmbbizrule.dto.request.baggage.BaggageAllowanceRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.baggage.ExtraBaggageRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.baggage.BaggageAllowancePassengerSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.response.baggage.BaggageAllowanceResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.baggage.BaggageProductDTO;
import com.cathaypacific.mmbbizrule.dto.response.baggage.BaggageProductValueDTO;
import com.cathaypacific.mmbbizrule.dto.response.baggage.ExtraBaggageResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.baggage.IneligibleReasonEnum;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingCommercePropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.PassengerPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.PassengerSegmentPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.SegmentPropertiesDTO;
import com.cathaypacific.mmbbizrule.handler.JourneyCalculateHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageAllowance;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.CabinBaggage;
import com.cathaypacific.mmbbizrule.model.booking.detail.CheckInBaggage;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.PurchasedBaggageDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.SharedWaiverBaggage;
import com.cathaypacific.mmbbizrule.model.journey.JourneySegment;
import com.cathaypacific.mmbbizrule.model.journey.JourneySummary;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPaymentInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.BaggageAllowanceBuildService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.service.BaggageAllowanceBuildService.BaggageAllowanceInfo;
import com.cathaypacific.mmbbizrule.util.BaggageIneligibleUtil;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.BaggageAllowanceDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.BaggageDetailDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.CheckInBaggageDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.PaymentInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.PurchasedBaggageDetailDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.SharedWaiverBaggageDTOV2;

@Service
public class BaggageBusinessImpl implements BaggageBusiness {

	private static final LogAgent LOGGER = LogAgent.getLogAgent(BaggageBusinessImpl.class);

	private static final String ERR_MSG_NO_PASSENGER_SEGMENT_PROPERTIES_IN_BOOKING_PROPERTIES =
			"No passenger segment properties in booking properties";

	private static final String ERR_MSG_NO_SEGMENT_PROPERTIES_IN_BOOKING_PROPERTIES =
			"No segment properties in booking properties";

	private static final String ERR_MSG_NO_PASSENGER_PROPERTIES_IN_BOOKING_PROPERTIES =
			"No passenger properties in booking properties";

	@Autowired
	private PnrInvokeService pnrInvokeService;

	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;

	@Autowired
	private BookingBuildService bookingBuildService;

	@Autowired
	private BaggageAllowanceBuildService baggageAllowanceBuildService;

	@Autowired
	private EcommService ecommService;

	@Autowired
	private BookingPropertiesBusiness bookingPropertiesBusiness;

	@Autowired
	private JourneyCalculateHelper journeyCalculateHelper;

	@Override
	public BaggageAllowanceResponseDTO getBaggageAllowance(BaggageAllowanceRequestDTO requestDTO, LoginInfo loginInfo)
			throws BusinessBaseException {

		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc());
		if (retrievePnrBooking == null || CollectionUtils.isEmpty(retrievePnrBooking.getPassengers())) {
			throw new ExpectedException(String.format("Cannot find booking by rloc:%s", loginInfo.getLoginRloc()),
					new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}

		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		Booking booking =
				bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, buildBookingPropertiesRequired());

		ProductsResponseDTO ecommProductsResponse = ecommService.getEcommBaggageProducts(requestDTO.getRloc(),
				booking.getPosForAep(), loginInfo.getMmbToken());

		UnaryOperator<String> logMessageBuilder = errorCode -> {
			if (errorCode != null && errorCode.startsWith(BaggageAllowanceErrorUtil.BAGGAGE_ALLOWANCE_ERR_PREFIX)) {
				if (BaggageAllowanceErrorUtil.BAGGAGE_ALLOWANCE_ERR_CONNECTION.equals(errorCode)) {
					return "It can’t get the baggage allowance info due to connection error";
				} else if (BaggageAllowanceErrorUtil.BAGGAGE_ALLOWANCE_ERR_UNKNOWN.equals(errorCode)) {
					return "It can’t get the baggage allowance info due to unsupport scenario [unknown]";
				} else {
					return "It can’t get the baggage allowance info due to unsupport scenario [" + errorCode + "]";
				}
			} else {
				return "Failed to get baggage allowance info.";
			}
		};
		
		try {
			
			BaggageAllowanceInfo baggageAllowanceInfo =
					baggageAllowanceBuildService.retrieveBaggageAllowanceInfo(booking, ecommProductsResponse);
			baggageAllowanceBuildService.populateBaggageAllowance(booking, baggageAllowanceInfo);
			
		} catch (UnexpectedException ex) {
			String logMessage = logMessageBuilder.apply(ex.getErrorInfo().getErrorCode());
			LOGGER.warn(logMessage, ex);
		} catch (ExpectedException ex) {
			String logMessage = logMessageBuilder.apply(ex.getErrorInfo().getErrorCode());
			LOGGER.warn(logMessage, ex);
		} catch (Exception ex) {
			String logMessage = logMessageBuilder.apply(null);
			LOGGER.warn(logMessage, ex);
		} finally {
			baggageAllowanceBuildService.setBaggageAllowanceCompleted(booking);
		}

		BaggageAllowanceResponseDTO responseDTO = new BaggageAllowanceResponseDTO();

		List<BaggageAllowancePassengerSegmentDTO> passengerSegmentDTOs = new ArrayList<>();
		for (PassengerSegment passengerSegment : booking.getPassengerSegments()) {
			BaggageAllowancePassengerSegmentDTO passengerSegmentDTO = new BaggageAllowancePassengerSegmentDTO();
			convertBaggageAllowance(passengerSegment, passengerSegmentDTO);
			passengerSegmentDTOs.add(passengerSegmentDTO);
		}

		responseDTO.setPassengerSegments(passengerSegmentDTOs);

		return responseDTO;
	}

	/**
	 * Convert baggage allowance to DTO.
	 * 
	 * @param passengerSegment
	 * @param passengerSegmentDTO
	 */
	private void convertBaggageAllowance(PassengerSegment passengerSegment,
			BaggageAllowancePassengerSegmentDTO passengerSegmentDTO) {

		passengerSegmentDTO.setPassengerId(passengerSegment.getPassengerId());
		passengerSegmentDTO.setSegmentId(passengerSegment.getSegmentId());

		if (passengerSegment.getBaggageAllowance() != null) {
			BaggageAllowance baggageAllowance = passengerSegment.getBaggageAllowance();
			BaggageAllowanceDTOV2 baggageAllowanceDTO = passengerSegmentDTO.findBaggageAllowance();
			baggageAllowanceDTO.setCompleted(baggageAllowance.isCompleted());
			baggageAllowanceDTO.setCabinBaggageUnavailable(baggageAllowance.isCabinBaggageUnavailable());

			// check-in baggage
			if (baggageAllowance.getCheckInBaggage() != null) {
				convertCheckInBaggage(passengerSegment, passengerSegmentDTO, baggageAllowance);
			}

			// cabin baggage
			if (baggageAllowance.getCabinBaggage() != null) {
				convertCabinBaggage(passengerSegmentDTO, baggageAllowance);
			}
		}
	}

	private void convertCheckInBaggage(PassengerSegment passengerSegment,
			BaggageAllowancePassengerSegmentDTO passengerSegmentDTO, BaggageAllowance baggageAllowance) {

		CheckInBaggage checkInBaggage = baggageAllowance.getCheckInBaggage();

		// standard baggage
		if (checkInBaggage.getStandardBaggage() != null) {
			passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findStandardBaggage().setAmount(
					checkInBaggage.getStandardBaggage().getAmount());
			passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findStandardBaggage().setUnit(
					checkInBaggage.getStandardBaggage().getUnit());
		}
		// waiver baggage
		if (checkInBaggage.getWaiverBaggage() != null) {
			passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findWaiverBaggage().setAmount(
					checkInBaggage.getWaiverBaggage().getAmount());
			passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findWaiverBaggage().setUnit(
					checkInBaggage.getWaiverBaggage().getUnit());
		}
		// purchased baggage
		if (checkInBaggage.getPurchasedBaggages() != null && !checkInBaggage.getPurchasedBaggages().isEmpty()) {
			BigInteger totalAmount =
					checkInBaggage.getPurchasedBaggages().stream().filter(pb -> pb.getAmount() != null).map(
							PurchasedBaggageDetail::getAmount).reduce((sum, amount) -> sum.add(amount)).orElse(null);
			PurchasedBaggageDetailDTOV2 purchasedBaggage =
					passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findPurchasedBaggage();
			purchasedBaggage.setAmount(totalAmount);
			purchasedBaggage.setUnit(checkInBaggage.getPurchasedBaggages().get(0).getUnit());

			List<RetrievePnrPaymentInfo> paymentList =
					checkInBaggage.getPurchasedBaggages().stream().filter(pb -> pb.getPaymentInfo() != null).map(
							PurchasedBaggageDetail::getPaymentInfo).collect(Collectors.toList());
			if (!paymentList.isEmpty()) {
				PaymentInfoDTOV2 pament = new PaymentInfoDTOV2();
				pament.setAmount(paymentList.stream().filter(pl -> pl.getAmount() != null).map(
						RetrievePnrPaymentInfo::getAmount).reduce((sum, amount) -> sum.add(amount)).orElse(null));
				pament.setCurrency(paymentList.get(0).getCurrency());
				purchasedBaggage.setPaymentInfo(pament);
			}

		}
		// shared purchased baggage
		if (!CollectionUtils.isEmpty(checkInBaggage.getSharedWaiverBaggages())) {
			// set shared waiver baggage
			convertSharedWaiverBaggage(passengerSegmentDTO, checkInBaggage);
		}
		// member baggage
		// if passenger is a member with additional member baggage, new the memberBaggage for frontend display logic
		if (passengerSegment.getFqtvInfo() != null && MMBBizruleConstants.TIERS_WITH_MEMBER_BAGGAGE_ALLOWANCE.contains(
				passengerSegment.getFqtvInfo().getTierLevel())) {
			passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().setMemberBaggage(new BaggageDetailDTOV2());
		}
		if (checkInBaggage.getMemberBaggage() != null) {
			passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findMemberBaggage().setAmount(
					checkInBaggage.getMemberBaggage().getAmount());
			passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findMemberBaggage().setUnit(
					checkInBaggage.getMemberBaggage().getUnit());
		}

		// if not all kinds of check in baggage is empty, calculate the total amount
		calculateCheckInBaggageTotal(passengerSegmentDTO);

		// limit
		if (checkInBaggage.getLimit() != null) {
			passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findLimit().setAmount(
					checkInBaggage.getLimit().getAmount());
			passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findLimit().setUnit(
					checkInBaggage.getLimit().getUnit());
		}
	}

	private void calculateCheckInBaggageTotal(BaggageAllowancePassengerSegmentDTO passengerSegmentDTO) {
		
		if (passengerSegmentDTO.getBaggageAllowance() != null
				&& isCheckInBaggageExist(passengerSegmentDTO.getBaggageAllowance().getCheckInBaggage())) {
			
			CheckInBaggageDTOV2 checkInBaggageDTO = passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage();
			List<BaggageDetailDTOV2> breakdownBaggages = new ArrayList<>();
			if (checkInBaggageDTO.getStandardBaggage() != null
					&& checkInBaggageDTO.getStandardBaggage().getAmount() != null) {
				breakdownBaggages.add(checkInBaggageDTO.getStandardBaggage());
			}
			if (checkInBaggageDTO.getWaiverBaggage() != null
					&& checkInBaggageDTO.getWaiverBaggage().getAmount() != null) {
				breakdownBaggages.add(checkInBaggageDTO.getWaiverBaggage());
			}
			if (checkInBaggageDTO.getPurchasedBaggage() != null
					&& checkInBaggageDTO.getPurchasedBaggage().getAmount() != null) {
				breakdownBaggages.add(checkInBaggageDTO.getPurchasedBaggage());
			}
			// need to check if the amount is null because we would create a empty memberBaggage if the passenger has a
			// tier with member baggage
			if (checkInBaggageDTO.getMemberBaggage() != null
					&& checkInBaggageDTO.getMemberBaggage().getAmount() != null) {
				breakdownBaggages.add(checkInBaggageDTO.getMemberBaggage());
			}
			passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findTotal().setAmount(
					breakdownBaggages.stream().map(BaggageDetailDTOV2::getAmount).reduce(new BigInteger("0"),
							(a, b) -> {
								return (b != null) ? a.add(b) : a;
							}));
			passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findTotal().setUnit(
					breakdownBaggages.stream().map(BaggageDetailDTOV2::getUnit).findFirst().orElse(null));
		}
	}
	
	private boolean isCheckInBaggageExist(CheckInBaggageDTOV2 checkInBaggageDTO) {
		if (checkInBaggageDTO == null) {
			return false;
		}
		
		return checkInBaggageDTO.getStandardBaggage() != null
				|| checkInBaggageDTO.getWaiverBaggage() != null
				|| checkInBaggageDTO.getPurchasedBaggage() != null
				|| (checkInBaggageDTO.getMemberBaggage() != null
						&& checkInBaggageDTO.getMemberBaggage().getAmount() != null); // memberBaggage might be unit only
	}

	private void convertCabinBaggage(BaggageAllowancePassengerSegmentDTO passengerSegmentDTO,
			BaggageAllowance baggageAllowance) {

		CabinBaggage cabinBaggage = baggageAllowance.getCabinBaggage();

		// standard baggage
		if (cabinBaggage.getStandardBaggage() != null) {
			passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findStandardBaggage().setAmount(
					cabinBaggage.getStandardBaggage().getAmount());
			passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findStandardBaggage().setUnit(
					cabinBaggage.getStandardBaggage().getUnit());
		}
		// member baggage
		if (cabinBaggage.getMemberBaggage() != null) {
			passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findMemberBaggage().setAmount(
					cabinBaggage.getMemberBaggage().getAmount());
			passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findMemberBaggage().setUnit(
					cabinBaggage.getMemberBaggage().getUnit());
		}
		// total
		if (cabinBaggage.getTotal() != null) {
			passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findTotal().setAmount(
					cabinBaggage.getTotal().getAmount());
			passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findTotal().setUnit(
					cabinBaggage.getTotal().getUnit());
		}
		// limit
		if (cabinBaggage.getLimit() != null) {
			passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findLimit().setAmount(
					cabinBaggage.getLimit().getAmount());
			passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findLimit().setUnit(
					cabinBaggage.getLimit().getUnit());
		}
		// small item
		if (cabinBaggage.getSmallItem() != null) {
			passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().setSmallItem(cabinBaggage.getSmallItem());
		}
	}

	/**
	 * Convert shared waiver baggage to DTO.
	 * 
	 * @param passengerSegmentDTO
	 * @param checkInBaggage
	 */
	private void convertSharedWaiverBaggage(BaggageAllowancePassengerSegmentDTO passengerSegmentDTO,
			CheckInBaggage checkInBaggage) {
		
		List<SharedWaiverBaggageDTOV2> sharedWaiverBaggageDTOs = new ArrayList<>();
		for (SharedWaiverBaggage sharedWaiverBaggage : checkInBaggage.getSharedWaiverBaggages()) {
			if (sharedWaiverBaggage != null && sharedWaiverBaggage.getGroupId() != null
					&& sharedWaiverBaggage.getTotalBaggage() != null) {
				SharedWaiverBaggageDTOV2 sharedWaiverBaggageDTO = new SharedWaiverBaggageDTOV2();
				sharedWaiverBaggageDTO.setGroupId(sharedWaiverBaggage.getGroupId());
				sharedWaiverBaggageDTO.findTotalBaggage().setAmount(sharedWaiverBaggage.getTotalBaggage().getAmount());
				sharedWaiverBaggageDTO.findTotalBaggage().setUnit(sharedWaiverBaggage.getTotalBaggage().getUnit());

				sharedWaiverBaggageDTOs.add(sharedWaiverBaggageDTO);
			}
		}
		passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().setSharedWaiverBaggages(
				sharedWaiverBaggageDTOs);
	}

	private BookingBuildRequired buildBookingPropertiesRequired() {
		BookingBuildRequired required = new BookingBuildRequired();
		required.setMemberAward(false);
		required.setSeatSelection(false);
		required.setCprCheck(false);
		required.setBaggageAllowances(false);
		required.setCountryOfResidence(false);
		required.setMealSelection(false);
		required.setPassenagerContactInfo(false);
		required.setEmergencyContactInfo(false);
		return required;
	}

	@Override
	public ExtraBaggageResponseDTO getExtraBaggage(ExtraBaggageRequestDTO requestDTO, LoginInfo loginInfo)
			throws BusinessBaseException {

		ExtraBaggageResponseDTO responseDTO = new ExtraBaggageResponseDTO();

		BookingCommercePropertiesDTO bookingPropertiesDTO =
				bookingPropertiesBusiness.getBookingCommerceProperties(loginInfo, requestDTO.getRloc());

		Future<List<JourneySummary>> futureJourneySummaries =
				journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(requestDTO.getRloc());

		ProductsResponseDTO ecommProductsResponse = ecommService.getEcommBaggageProducts(requestDTO.getRloc(),
				bookingPropertiesDTO.getPos(), loginInfo.getMmbToken());

		List<BaggageProductDTO> baggageProductDTOs = parseBaggageProducts(ecommProductsResponse.getProducts());
		responseDTO.setProducts(baggageProductDTOs);

		List<JourneySummary> journeySummaries = null;
		try {
			journeySummaries = futureJourneySummaries.get();
		} catch (Exception e) {
			LOGGER.warn("Get journey summary failed.", e);
		}

		if (journeySummaries != null) {
			buildIneligibleReason(responseDTO, bookingPropertiesDTO, journeySummaries);
		}

		return responseDTO;
	}

	private List<BaggageProductDTO> parseBaggageProducts(List<ProductDTO> ecommProducts) {

		if (CollectionUtils.isEmpty(ecommProducts)) {
			return Collections.emptyList();
		}

		List<BaggageProductDTO> baggageProductDTOs = new ArrayList<>();

		for (ProductDTO ecommProduct : ecommProducts) {

			BaggageProductDTO baggageProductDTO = createBaggageProductDTO(ecommProduct);
			baggageProductDTOs.add(baggageProductDTO);
		}

		return baggageProductDTOs;
	}

	private BaggageProductDTO createBaggageProductDTO(ProductDTO ecommProduct) {

		BaggageProductDTO baggageProductDTO = new BaggageProductDTO();

		baggageProductDTO.setProductType(ecommProduct.getProductType());
		baggageProductDTO.setSegmentIds(ecommProduct.getSegmentIds());
		baggageProductDTO.setPassengerId(ecommProduct.getPassengerId());
		baggageProductDTO.setUnit(ecommProduct.getUnit());

		if (ecommProduct.isSellOnOffline()) {
			baggageProductDTO.setIneligibleReason(IneligibleReasonEnum.SELL_OFFLINE);
		}

		if (CollectionUtils.isEmpty(ecommProduct.getProductValues())) {
			baggageProductDTO.setIneligibleReason(IneligibleReasonEnum.MAX_REACHED);
		} else {
			List<BaggageProductValueDTO> baggageProductValueDTOs = new ArrayList<>();
			for (ProductValueDTO ecommProductValue : ecommProduct.getProductValues()) {
				baggageProductValueDTOs.add(createProductValueDTO(ecommProductValue));
			}
			baggageProductDTO.setProductValues(baggageProductValueDTOs);
		}

		return baggageProductDTO;
	}

	private BaggageProductValueDTO createProductValueDTO(ProductValueDTO ecommProductValue) {
		BaggageProductValueDTO baggageProductValueDTO = new BaggageProductValueDTO();

		baggageProductValueDTO.setValue(ecommProductValue.getValue());
		baggageProductValueDTO.setAmount(ecommProductValue.getAmount());
		baggageProductValueDTO.setCurrency(ecommProductValue.getCurrency());
		baggageProductValueDTO.setValue(ecommProductValue.getValue());

		return baggageProductValueDTO;
	}

	/**
	 * Build ineligible reason.<br>
	 * Reason is speculated according to {@code BookingProperties}.
	 * 
	 * @param responseDTO
	 * @param bookingPropertiesDTO
	 * @throws BusinessBaseException
	 */
	private void buildIneligibleReason(ExtraBaggageResponseDTO responseDTO, BookingPropertiesDTO bookingPropertiesDTO,
			List<JourneySummary> journeySummaries) throws BusinessBaseException {

		if (bookingPropertiesDTO == null) {
			throw new UnexpectedException("Booking properties is null",
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}

		List<PassengerSegmentPropertiesDTO> passengerSegmentPropertiesDTOs =
				bookingPropertiesDTO.getPassengerSegmentProperties();

		if (CollectionUtils.isEmpty(passengerSegmentPropertiesDTOs)) {
			throw new UnexpectedException(ERR_MSG_NO_PASSENGER_SEGMENT_PROPERTIES_IN_BOOKING_PROPERTIES,
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}

		List<SegmentPropertiesDTO> segmentPropertiesDTOs = bookingPropertiesDTO.getSegmentProperties();

		if (CollectionUtils.isEmpty(segmentPropertiesDTOs)) {
			throw new UnexpectedException(ERR_MSG_NO_SEGMENT_PROPERTIES_IN_BOOKING_PROPERTIES,
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}

		List<PassengerPropertiesDTO> passengerPropertiesDTOs = bookingPropertiesDTO.getPassengerProperties();

		if (CollectionUtils.isEmpty(passengerPropertiesDTOs)) {
			throw new UnexpectedException(ERR_MSG_NO_PASSENGER_PROPERTIES_IN_BOOKING_PROPERTIES,
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}

		List<BaggageProductDTO> productDTOs = responseDTO.getProducts();
		productDTOs = createMissingProductDTO(productDTOs, passengerSegmentPropertiesDTOs, journeySummaries);
		responseDTO.setProducts(productDTOs);

		// Check criteria to set ineligible reason.
		BaggageIneligibleUtil.checkBookingAllCxKaOperating(productDTOs, segmentPropertiesDTOs);
		BaggageIneligibleUtil.checkStaffBooking(productDTOs, bookingPropertiesDTO);
		BaggageIneligibleUtil.checkGroupBooking(productDTOs, bookingPropertiesDTO);
		BaggageIneligibleUtil.checkPassengerWaiverAllowance(productDTOs, passengerPropertiesDTOs,
				passengerSegmentPropertiesDTOs);
		BaggageIneligibleUtil.checkSegmentWaitlist(productDTOs, segmentPropertiesDTOs);
		BaggageIneligibleUtil.checkETicketIssued(productDTOs, passengerSegmentPropertiesDTOs);
		BaggageIneligibleUtil.checkCxKaETicket(productDTOs, passengerSegmentPropertiesDTOs);
		BaggageIneligibleUtil.checkCloseToDepartureSegment(productDTOs, segmentPropertiesDTOs, journeySummaries);

		if (!CollectionUtils.isEmpty(responseDTO.getProducts())) {
			for (BaggageProductDTO productDTO : responseDTO.getProducts()) {
				if (CollectionUtils.isEmpty(productDTO.getProductValues())
						&& productDTO.getIneligibleReason() == null) {
					productDTO.setIneligibleReason(IneligibleReasonEnum.DEFAULT);
				}
			}
		}
	}

	/**
	 * If passenger segment association is not included in AEP products response, create corresponding product DTO with
	 * ineligible reason.
	 * 
	 * @param parsedProductDTOs
	 *            original product DTO list created by AEP response.
	 * @param passengerSegmentPropertiesDTOs
	 * @param journeySummaries
	 * @return new created product list.
	 */
	private List<BaggageProductDTO> createMissingProductDTO(List<BaggageProductDTO> parsedProductDTOs,
			List<PassengerSegmentPropertiesDTO> passengerSegmentPropertiesDTOs, List<JourneySummary> journeySummaries) {

		List<BaggageProductDTO> productDTOs = new ArrayList<>(parsedProductDTOs.size());
		productDTOs.addAll(parsedProductDTOs);

		// Journey ID to segment ID list mapping.
		Map<String, List<String>> journeySegmentMap = journeySummaries.stream().collect(Collectors.toMap(
				// Key is journey ID.
				JourneySummary::getJourneyId,
				// Value is segment ID list of journey.
				journey -> journey.getSegments().stream().map(JourneySegment::getSegmentId).collect(
						Collectors.toList()),
				// Merge segment ID list if duplicated journey ID exists.
				(a, b) -> Stream.of(a, b).flatMap(List::stream).distinct().collect(Collectors.toList()),
				// Save result in LinkedHashMap.
				LinkedHashMap::new));

		// Segment ID to journey ID mapping.
		Map<String, String> segmentJourneyMap = new HashMap<>();
		for (Entry<String, List<String>> entry : journeySegmentMap.entrySet()) {
			String journeyId = entry.getKey();
			List<String> segmentIds = entry.getValue();
			segmentIds.stream().forEach(segmentId -> segmentJourneyMap.put(segmentId, journeyId));
		}

		// Loop passenger segment associations, ensure everyone is represented by corresponding product DTO.
		for (PassengerSegmentPropertiesDTO passengerSegmentPropertiesDTO : passengerSegmentPropertiesDTOs) {

			String passengerId = passengerSegmentPropertiesDTO.getPassengerId();
			// Skip infant passenger.
			if (passengerId.endsWith("I")) {
				continue;
			}

			String segmentId = passengerSegmentPropertiesDTO.getSegmentId();

			// Find corresponding product DTO.
			BaggageProductDTO productDTO =
					productDTOs.stream().filter(product -> product.getPassengerId().equals(passengerId)
							&& product.getSegmentIds().contains(segmentId)).findFirst().orElse(null);

			// If product DTO is not found, create product without product value.
			if (productDTO == null) {

				String journeyId = segmentJourneyMap.get(segmentId);
				List<String> journeySegmentIds = journeySegmentMap.get(journeyId);
				if (StringUtils.isEmpty(journeySegmentIds)) {
					// If segment is missing in journey info, consider it as a single segment journey.
					journeySegmentIds = Arrays.asList(segmentId);
				}

				productDTO = new BaggageProductDTO();
				productDTO.setPassengerId(passengerId);
				productDTO.setSegmentIds(journeySegmentIds);
				productDTOs.add(productDTO);
			}
		}

		return Collections.unmodifiableList(productDTOs);
	}

}
