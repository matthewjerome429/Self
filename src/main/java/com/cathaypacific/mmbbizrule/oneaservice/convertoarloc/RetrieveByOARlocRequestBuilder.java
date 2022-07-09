package com.cathaypacific.mmbbizrule.oneaservice.convertoarloc;

import com.cathaypacific.oneaconsumer.model.request.paoarq_11_1_1a.ObjectFactory;
import com.cathaypacific.oneaconsumer.model.request.paoarq_11_1_1a.PNRRetrieveByOARloc;
import com.cathaypacific.oneaconsumer.model.request.paoarq_11_1_1a.ReservationControlInformationDetailsType;
import com.cathaypacific.oneaconsumer.model.request.paoarq_11_1_1a.ReservationControlInformationType;

public class RetrieveByOARlocRequestBuilder {
	private ObjectFactory objFactory = new ObjectFactory();

	public PNRRetrieveByOARloc buildRlocRequest(String rloc){
		
		PNRRetrieveByOARloc pnrRetrieveByOARloc = objFactory.createPNRRetrieveByOARloc();

		//add rloc info
		ReservationControlInformationType reservationOrProfileIdentifier = objFactory.createReservationControlInformationType();
		pnrRetrieveByOARloc.setReservationInfo(reservationOrProfileIdentifier);
		
		ReservationControlInformationDetailsType reservation = objFactory.createReservationControlInformationDetailsType();
		reservationOrProfileIdentifier.setReservation(reservation);
		reservation.setControlNumber(rloc);
		
		return pnrRetrieveByOARloc;
	}
}
