package com.cathaypacific.mmbbizrule.controller;

import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.business.RetrievePnrBusiness;
import com.cathaypacific.mmbbizrule.controller.RetrievePnrController;
import com.cathaypacific.mmbbizrule.dto.request.retrievepnr.RetrievePnrByRlocRequestDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;

@RunWith(MockitoJUnitRunner.class)
public class RetrievePnrControllerTest {
	
	@Mock
	private RetrievePnrBusiness retrievePnrService;
	
	@InjectMocks
	private RetrievePnrController controller;
	
	private BookingBuildRequired required;
	
	@Before
	public void setUp() {
		required = new BookingBuildRequired();
	}
	
	@Test
	public void validate_executeRetrieveBookingByReference() throws Exception {
		try {
			RetrievePnrByRlocRequestDTO requestDTO = new RetrievePnrByRlocRequestDTO();
			requestDTO.setRloc("1234567");
			controller.loginByRloc(requestDTO,null);
			Mockito.verify(retrievePnrService, Mockito.times(1)).bookingLoginByReference("1234567", null, null, null, required);
			return;
		} catch (BusinessBaseException e) {
			e.printStackTrace();
		}
		fail("Can't execute retrieveBookingByReference when call /v1/booking/byrloc");
	}

}
