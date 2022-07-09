package com.cathaypacific.mmbbizrule.cxservice.aep.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.aep.AEPProductIdEnum;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPAirProduct;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPPassengerWithName;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProduct;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProductsResponse;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPSegment;
import com.cathaypacific.mmbbizrule.cxservice.aep.service.AEPService;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;

@Service
public class AEPServiceImpl implements AEPService {

	@Autowired
	private AEPServiceCacheHelper aepServiceCacheHelper;
	
	@Autowired
	private AEMService aemService;

	@Async
	@Override
	@LogPerformance(message = "Time required to start async method.")
	public Future<AEPProductsResponse> asyncGetBookingProducts(String bookingRef, String pos)
			throws BusinessBaseException {
		return new AsyncResult<>(getBookingProducts(bookingRef, pos));
	}

	public AEPProductsResponse getBookingProducts(String bookingRef, String pos) throws BusinessBaseException {
		return aepServiceCacheHelper.getBookingProducts(bookingRef, pos);
	}
	
	@Deprecated
	public AEPProductsResponse getBookingProductsForBaggage(Booking booking) throws BusinessBaseException {
		if(booking == null) {
			return null;
		}
		AEPProductsResponse aepProductsResponse = null;
		// if there is only one segment in the booking, mock the response to retrieve baggage info
		if(!CollectionUtils.isEmpty(booking.getSegments()) && booking.getSegments().size() == 1) {
			aepProductsResponse = mockAEPResponseForSingleSegBooking(booking);
		} else {
			aepProductsResponse = aepServiceCacheHelper.getBookingProducts(booking.getOneARloc(), booking.getPos());
		}
		return aepProductsResponse;
	}
	
	/**
	 * if there is only one segment in the booking, mock the AEP response for baggage allowance call
	 * @param booking
	 * @return AEPProductsResponse
	 */
	private AEPProductsResponse mockAEPResponseForSingleSegBooking(Booking booking) {
		
		AEPProductsResponse aepProductsResponse = new AEPProductsResponse();
		aepProductsResponse.setProducts(new ArrayList<>());
		
		if(booking == null || CollectionUtils.isEmpty(booking.getSegments()) 
				|| CollectionUtils.isEmpty(booking.getPassengers()) || booking.getSegments().get(0) == null) {
			return aepProductsResponse;
		}
		
		// the single segment in booking
		Segment segment = booking.getSegments().get(0);

		// Check market company because market company is used in BTU of baggage allowance API request.
		if (!OneAConstants.COMPANY_CX.equals(segment.getMarketCompany()) && !OneAConstants.COMPANY_KA.equals(segment.getMarketCompany())) {
			return aepProductsResponse;
		}
		
		for(Passenger pax : booking.getPassengers()) {	
			if(pax == null || StringUtils.isEmpty(pax.getPassengerId()) || pax.getPassengerId().contains(PnrResponseParser.PASSENGER_INFANT_ID_SUFFIX)) {
				continue;
			}

			AEPProduct aepProduct = new AEPProduct();
			aepProduct.setAirProduct(new ArrayList<>());
			aepProduct.setProductId(calculateBaggageProductId(segment));
			aepProduct.setAirProduct(new ArrayList<>());
			
			AEPAirProduct aepAirProduct = new AEPAirProduct();
			aepAirProduct.setPassengers(new ArrayList<>());
			aepAirProduct.setSegments(new ArrayList<>());
			
			// set passener
			AEPPassengerWithName aepPassenger = new AEPPassengerWithName();
			aepPassenger.setPassengerRef(Integer.valueOf(pax.getPassengerId()));
			aepAirProduct.getPassengers().add(aepPassenger);
			
			// set segment
			AEPSegment aepSegment = new AEPSegment();
			aepSegment.setSegmentRef(Integer.valueOf(segment.getSegmentID()));
			aepSegment.setOrigin(segment.getOriginPort());
			aepSegment.setDestination(segment.getDestPort());
			aepSegment.setMarketingCarrier(segment.getMarketCompany());
			aepAirProduct.getSegments().add(aepSegment);
			
			aepProduct.getAirProduct().add(aepAirProduct);
			
			aepProductsResponse.getProducts().add(aepProduct);
		}
		
		return aepProductsResponse;
	}

	/**
	 * calculate baggage product id by segment O/D
	 * @param segment
	 * @return String
	 */
	private String calculateBaggageProductId(Segment segment) {
		if(segment == null || StringUtils.isEmpty(segment.getOriginPort()) || StringUtils.isEmpty(segment.getDestPort())) {
			return null;
		}
		String originCountry = aemService.getCountryCodeByPortCode(segment.getOriginPort());
		String destCountry = aemService.getCountryCodeByPortCode(segment.getDestPort());
		List<String> stopsCountry = new ArrayList<>();
		if(segment.getStops() != null) {
			stopsCountry =  Arrays.asList(segment.getStops()).stream().map(port -> aemService.getCountryCodeByPortCode(port)).collect(Collectors.toList());
		}
		
		if(StringUtils.isEmpty(originCountry) || StringUtils.isEmpty(destCountry)) {
			return null;
		}
		// if O/D contains US port, the product id is 3
		else if(MMBBizruleConstants.COUNTRY_CODE_USA.equals(originCountry) || MMBBizruleConstants.COUNTRY_CODE_USA.equals(destCountry) 
				|| stopsCountry.contains(MMBBizruleConstants.COUNTRY_CODE_USA)) {
			return AEPProductIdEnum.BAGGAGE_USA.getId();
		} 
		// if O/D is HKG from/to New Zealand, the product id is 4
		else if ((MMBBizruleConstants.COUNTRY_CODE_HONGKONG.equals(originCountry) && MMBBizruleConstants.COUNTRY_CODE_NEWZEALAND.equals(destCountry))
				|| (MMBBizruleConstants.COUNTRY_CODE_NEWZEALAND.equals(originCountry) && MMBBizruleConstants.COUNTRY_CODE_HONGKONG.equals(destCountry))) {
			return AEPProductIdEnum.BAGGAGE_NZL.getId();
		} 
		// else the product id is 2
		else {
			return AEPProductIdEnum.BAGGAGE_COMMON.getId();
		}
	}
}
