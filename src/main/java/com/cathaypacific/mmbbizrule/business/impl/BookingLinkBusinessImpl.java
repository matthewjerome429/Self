package com.cathaypacific.mmbbizrule.business.impl;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.BookingLinkBusiness;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.service.OJBookingService;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.LinkTempBookingResponseDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;

@Service
public class BookingLinkBusinessImpl implements BookingLinkBusiness {

	private static LogAgent logger = LogAgent.getLogAgent(BookingLinkBusinessImpl.class);
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Autowired
	private TempLinkedBookingRepository linkTempBookingRepository;
	
	
	@Autowired
	private OJBookingService ojBookingService;

	@Override
	public LinkTempBookingResponseDTO linkBooking(LoginInfo loginInfo, String rloc, String familyName, String givenName) throws BusinessBaseException {
		
		LinkTempBookingResponseDTO linkTempBookingResponseDTO = new LinkTempBookingResponseDTO();
		try {
			
			String mappedOneArloc = null;
			// OJ rloc
			if (!BizRulesUtil.isFlightRloc(rloc)) {

				OJBooking ojBooking = ojBookingService.getBooking(givenName, familyName, rloc);
				if (ojBooking == null) {
					throw new ExpectedException(String.format("Unable to add booking - Cannot find OJBooking by rloc from ojSErvice:%s", rloc),
							new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_NOBOOKINGFOUND));
				}

				if (ojBooking.getFlightBooking() != null && StringUtils.isNotEmpty(ojBooking.getFlightBooking().getBookingReference())) {
					mappedOneArloc = ojBooking.getFlightBooking().getBookingReference();
				} else {
					linkTempBookingRepository.addExternalBooking(rloc, familyName, givenName, null, loginInfo.getMmbToken());
					linkTempBookingResponseDTO.setSuccess(true);
					return linkTempBookingResponseDTO;
				}
			}

			String oneARloc = StringUtils.isEmpty(mappedOneArloc) ? rloc : mappedOneArloc;
			// retrieve pnr, support GDS and 1A rloc
			RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(oneARloc);
			// name check, find primary passenger
			paxNameIdentificationService.primaryPaxIdentificationForRloc(familyName, givenName, pnrBooking);

			// find out primary passenger, pnrPax cannot null because will
			// throw exception if name matching failed
			RetrievePnrPassenger pnrPax = pnrBooking.getPassengers().stream()
					.filter(pax -> BooleanUtils.isTrue(pax.isPrimaryPassenger())).findFirst().orElse(null);

			if (pnrPax == null) {
				logger.warn("Can not find matched passenger.");
				throw new UnexpectedException("Can not find matched passenger.", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
			}

			linkTempBookingRepository.addExternalBooking(pnrBooking.getOneARloc(), pnrPax.getFamilyName(),pnrPax.getGivenName(), pnrPax.getPassengerID(), loginInfo.getMmbToken());
			linkTempBookingResponseDTO.setSuccess(true);

		} catch (ExpectedException e) {
			 logger.warn("Link booking failed,Expected error."+e.getMessage());
			linkTempBookingResponseDTO.addError(e.getErrorInfo());
			linkTempBookingResponseDTO.setSuccess(false);
		}catch (UnexpectedException e) {
			 logger.warn("Link booking failed,unExpected error."+e.getMessage());
			linkTempBookingResponseDTO.addError(e.getErrorInfo());
			linkTempBookingResponseDTO.setSuccess(false);
		}
		
		return linkTempBookingResponseDTO;
	}
	
}
