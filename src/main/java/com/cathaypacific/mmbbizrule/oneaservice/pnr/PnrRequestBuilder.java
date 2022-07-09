package com.cathaypacific.mmbbizrule.oneaservice.pnr;

import java.math.BigInteger;

import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.ObjectFactory;
import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.PNRRetrieve;
import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.PNRRetrieve.RetrievalFacts;
import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.ReservationControlInformationDetailsType;
import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.ReservationControlInformationType;
import com.cathaypacific.oneaconsumer.model.request.pnrget_16_1_1a.RetrievePNRType;

public class PnrRequestBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();

	public PNRRetrieve buildRlocRequest(String rloc){
		
		PNRRetrieve pnrRetrieve = objFactory.createPNRRetrieve();
		RetrievalFacts retrievalFacts = objFactory.createPNRRetrieveRetrievalFacts();
		pnrRetrieve.setRetrievalFacts(retrievalFacts);
		//pnr retrieve type 2:Retrieve by record locator
		RetrievePNRType retrieve =  objFactory.createRetrievePNRType();
		retrievalFacts.setRetrieve(retrieve);
		retrieve.setType(new BigInteger("2"));

		//add rloc info
		ReservationControlInformationType reservationOrProfileIdentifier = objFactory.createReservationControlInformationType();
		retrievalFacts.setReservationOrProfileIdentifier(reservationOrProfileIdentifier);
		
		ReservationControlInformationDetailsType reservation = objFactory.createReservationControlInformationDetailsType();
		reservationOrProfileIdentifier.getReservation().add(reservation);
		reservation.setControlNumber(rloc);
		
		return pnrRetrieve;
	}
}
