package com.cathaypacific.mmbbizrule.controller;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mmbbizrule.business.PassengerDetailsBusiness;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;

@RunWith(MockitoJUnitRunner.class)
public class PassengerDetailsControllerTest {
	
	@Mock
	private PassengerDetailsBusiness passengerDetailsBusiness;
	
	@InjectMocks
	private PassengerDetailsController controller;
	
	@Mock
	private MbTokenCacheRepository mbTokenCacheRepository;
 
	
	private BookingBuildRequired required;
	
	@Before
	public void setUp() {
		required = new BookingBuildRequired();
	}
	
	@Test
	public void validate_executeRetrieveBookingByReference() {
		try {
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setLoginType(LoginInfo.LOGINTYPE_MEMBER);
			loginInfo.setMemberId("1234567");
			loginInfo.setMmbToken("testtoken");
			when(mbTokenCacheRepository.get(any(), any(), any(), any())).thenReturn(loginInfo);
			UpdatePassengerDetailsRequestDTO requestDTO = new UpdatePassengerDetailsRequestDTO();
			requestDTO.setPassengerId("1234567");
			controller.updatePassengerDetails(requestDTO, loginInfo);
			Mockito.verify(passengerDetailsBusiness, Mockito.times(1)).updatePaxDetails(loginInfo, requestDTO, required);
			return;
		} catch (BusinessBaseException e) {
			e.printStackTrace();
		}
		fail("Can't execute updatePassengerDetails when call put /v1/passengerdetails");
	}

}
