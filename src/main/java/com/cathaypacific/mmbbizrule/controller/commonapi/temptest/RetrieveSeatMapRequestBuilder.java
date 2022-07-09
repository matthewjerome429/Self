package com.cathaypacific.mmbbizrule.controller.commonapi.temptest;

import org.springframework.stereotype.Service;

import com.cathaypacific.oneaconsumer.model.request.smpreq_14_2_1a.*;

/* REMOVE TEST CODE BEFORE GO LIVE R5.0
@Service
*/
public class RetrieveSeatMapRequestBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();

	public AirRetrieveSeatMap build(String departureDateStr, String originAirportCode, String destinationAirportCode,
			String marketingCompany, String flightNum, String bookingClass, String rloc){
		
		AirRetrieveSeatMap airRetrieveSeatMap = objFactory.createAirRetrieveSeatMap();
		TravelProductInformationTypeI travelProductInformationTypeI = objFactory.createTravelProductInformationTypeI();
		ProductDateTimeTypeI productDateTimeTypeI = objFactory.createProductDateTimeTypeI();
		LocationTypeI boardPointlocationTypeI = objFactory.createLocationTypeI();
		LocationTypeI offPointlocationTypeI = objFactory.createLocationTypeI();
		CompanyIdentificationTypeI companyIdentificationTypeI = objFactory.createCompanyIdentificationTypeI();
		ProductIdentificationDetailsTypeI productIdentificationDetailsTypeI = objFactory.createProductIdentificationDetailsTypeI();
		
		ReservationControlInformationTypeI reservationControlInformationTypeI = objFactory.createReservationControlInformationTypeI();
		ReservationControlInformationDetailsTypeI reservationControlInformationDetailsTypeI =  objFactory.createReservationControlInformationDetailsTypeI();
		
		SeatRequestParametersTypeI seatRequestParametersTypeI = objFactory.createSeatRequestParametersTypeI();
		
		airRetrieveSeatMap.setTravelProductIdent(travelProductInformationTypeI);
		travelProductInformationTypeI.setFlightDate(productDateTimeTypeI);
		travelProductInformationTypeI.setBoardPointDetails(boardPointlocationTypeI);
		travelProductInformationTypeI.setOffpointDetails(offPointlocationTypeI);
		travelProductInformationTypeI.setCompanyDetails(companyIdentificationTypeI);
		travelProductInformationTypeI.setFlightIdentification(productIdentificationDetailsTypeI);
		
		productDateTimeTypeI.setDepartureDate(departureDateStr);
		boardPointlocationTypeI.setTrueLocationId(originAirportCode);
		offPointlocationTypeI.setTrueLocationId(destinationAirportCode);
		companyIdentificationTypeI.setMarketingCompany(marketingCompany);
		productIdentificationDetailsTypeI.setFlightNumber(flightNum);
		productIdentificationDetailsTypeI.setBookingClass(bookingClass);
		
		airRetrieveSeatMap.setResControlInfo(reservationControlInformationTypeI);
		reservationControlInformationTypeI.getReservation().add(reservationControlInformationDetailsTypeI);
		reservationControlInformationDetailsTypeI.setCompanyId(marketingCompany);
		reservationControlInformationDetailsTypeI.setControlNumber(rloc);
		
		airRetrieveSeatMap.setSeatRequestParameters(seatRequestParametersTypeI);
		seatRequestParametersTypeI.setProcessingIndicator("FT");
		
		return airRetrieveSeatMap;
	}
}
