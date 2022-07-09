package com.cathaypacific.mmbbizrule.cxservice.eods.service.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.eodsconsumer.webservice.service.client.EODSWebClient;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;

@RunWith(MockitoJUnitRunner.class)
public class EODSBookingServiceImplTest {
	
	@Mock
	private EODSWebClient eodsBookingWSClient;
	
	@InjectMocks
	private EODSBookingServiceImpl eodsBookingServiceImpl;
	
	@Test
	public void testGetBookingList () throws SoapFaultException {
		when(eodsBookingWSClient.getBookingSummary(anyObject())).thenReturn(null);
		try {
			assertNull(eodsBookingServiceImpl.getBookingList("111"));
		} catch (UnexpectedException e) {
			fail();
		}
	}
	
	@Test(expected = UnexpectedException.class)
	public void testGetBookingException () throws SoapFaultException, UnexpectedException {
		when(eodsBookingWSClient.getBookingSummary(anyObject())).thenThrow(new RuntimeException());
		assertNull(eodsBookingServiceImpl.getBookingList("111"));
	}
}
