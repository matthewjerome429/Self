package com.cathaypacific.mmbbizrule.oneaservice.convertOARloc.service.impl;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.oneaservice.convertoarloc.RetrieveByOARlocRequestBuilder;
import com.cathaypacific.oneaconsumer.model.request.paoarq_11_1_1a.ObjectFactory;
import com.cathaypacific.oneaconsumer.model.request.paoarq_11_1_1a.PNRRetrieveByOARloc;
import com.cathaypacific.oneaconsumer.model.request.paoarq_11_1_1a.ReservationControlInformationDetailsType;
import com.cathaypacific.oneaconsumer.model.request.paoarq_11_1_1a.ReservationControlInformationType;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveByOARlocRequestBuilderTest {
	
	@InjectMocks
	private RetrieveByOARlocRequestBuilder retrieveByOARlocRequestBuilder;
	
	@Mock
	private ObjectFactory objFactory;
	
	@Test
	public void test() {
		String rloc="MK57D";
		PNRRetrieveByOARloc pnrRetrieveByOARloc=new PNRRetrieveByOARloc();
		when( objFactory.createPNRRetrieveByOARloc()).thenReturn(pnrRetrieveByOARloc);
		ReservationControlInformationType reservationOrProfileIdentifier=new ReservationControlInformationType();
		when(objFactory.createReservationControlInformationType()).thenReturn(reservationOrProfileIdentifier);
		ReservationControlInformationDetailsType reservation=new ReservationControlInformationDetailsType();
		when(objFactory.createReservationControlInformationDetailsType()).thenReturn(reservation);
		PNRRetrieveByOARloc pNRRetrieveByOARloc=retrieveByOARlocRequestBuilder.buildRlocRequest(rloc);
		Assert.assertEquals("MK57D", pNRRetrieveByOARloc.getReservationInfo().getReservation().getControlNumber());
	}

}
