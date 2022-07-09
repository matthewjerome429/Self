package com.cathaypacific.mmbbizrule.v2.business.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.cxservice.olci_v2.service.OLCIServiceV2;
import com.cathaypacific.mmbbizrule.dto.request.addbooking.AddBookingRequestDTO;
import com.cathaypacific.mmbbizrule.handler.PnrCprMergeHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.v2.business.LinkBookingBusiness;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.AddLinkedBookingDTO;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.BookingDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.CheckLinkedBookingDTO;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.FlightBookingDTOV2;
import com.cathaypacific.mmbbizrule.v2.handler.DTOConverterV2;
import com.cathaypacific.olciconsumer.model.request.addlinkedbooking.AddLinkedBookingRequestDTO;
import com.cathaypacific.olciconsumer.model.request.checklinkedbooking.CheckLinkedBookingRequestDTO;
import com.cathaypacific.olciconsumer.model.response.LoginResponseDTO;
@Service
public class LinkBookingBusinessImpl implements LinkBookingBusiness {
	
	private static LogAgent logger = LogAgent.getLogAgent(LinkBookingBusinessImpl.class);

    @Autowired
    private PnrInvokeService pnrInvokeService;
    
    @Autowired
    private TicketProcessInvokeService ticketProcessInvokeService;
    
    @Autowired
    private PaxNameIdentificationService paxNameIdentificationService;
    
    @Autowired
    private OLCIServiceV2 olciServiceV2;
    
    @Autowired
    private PnrResponseParser pnrResponseParser;
    
    @Autowired
    private PnrCprMergeHelper pnrCprMergeHelper;
    
    @Autowired
    private DTOConverterV2 dtoConverter;
    
    @Override
    public CheckLinkedBookingDTO checkLinkedBooking(LoginInfo loginInfo, AddBookingRequestDTO requestDTO)
            throws BusinessBaseException {
        CheckLinkedBookingDTO checkLinkedBookingResponse = new CheckLinkedBookingDTO();
        // request checking
        if (requestDTO == null
                || (StringUtils.isEmpty(requestDTO.getEticket()) && StringUtils.isEmpty(requestDTO.getRloc()))) {
            throw new ExpectedException("Unable to link booking - input e-ticket/rloc are both empty",
                    new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_ETICKET_RLOC_BOTH_NULL));
        }
        // rloc flow
        if (StringUtils.isNotEmpty(requestDTO.getRloc())) {
            checkLinkedBookingByRloc(loginInfo, requestDTO, checkLinkedBookingResponse);
        } else {
        // et flow
            checkLinkedBookingByET(loginInfo, requestDTO, checkLinkedBookingResponse);
        }
        return checkLinkedBookingResponse;
    }
    
    @Override
    public AddLinkedBookingDTO addLinkedBooking(LoginInfo loginInfo, AddBookingRequestDTO requestDTO)
            throws BusinessBaseException {
        AddLinkedBookingDTO addLinkedBookingDTO = new AddLinkedBookingDTO();
        // request checking
        if (requestDTO == null
                || (StringUtils.isEmpty(requestDTO.getEticket()) && StringUtils.isEmpty(requestDTO.getRloc()))) {
            throw new ExpectedException("Unable to link booking - input e-ticket/rloc are both empty",
                    new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_ETICKET_RLOC_BOTH_NULL));
        }
        AddLinkedBookingRequestDTO olciAddLinkedBookingRequestDTO = buildAddLinkedBookingRequest(requestDTO);
        LoginResponseDTO addLinkedBookingResponse = olciServiceV2.addLinkedBooking(olciAddLinkedBookingRequestDTO, loginInfo.getLoginRloc(), loginInfo.getLoginEticket());
        // olci response checking
        // call olci check link booking failed or with error
        olciAddLinkedBookingResponseChecking(addLinkedBookingResponse);
        addLinkedBookingDTO.setLinkSuccess(true);
        
        //OLSS-7536 tagging
        logger.info("Staff booking | Linked | Success");
        
        // rloc flow
        return addLinkedBookingDTO;
    }

    /**
     * build olci check linked booking request
     * @param loginInfo
     * @return
     */
    private AddLinkedBookingRequestDTO buildAddLinkedBookingRequest(AddBookingRequestDTO loginInfo) {
        AddLinkedBookingRequestDTO addLinkedBookingRequestDTO = new AddLinkedBookingRequestDTO();
        addLinkedBookingRequestDTO.setTxtBookingRef(loginInfo.getRloc());
        addLinkedBookingRequestDTO.setTxtFamilyName(loginInfo.getFamilyName());
        addLinkedBookingRequestDTO.setTxtGivenName(loginInfo.getGivenName());
        addLinkedBookingRequestDTO.setTxtETicket(loginInfo.getEticket());
        return addLinkedBookingRequestDTO;
    }

    /**
     * build olci check linked booking request
     * @param loginInfo
     * @return
     */
    private CheckLinkedBookingRequestDTO buildCheckLinkedBookingRequest(String familyName, String givenName, String rloc) {
        CheckLinkedBookingRequestDTO checkLinkedBookingRequestDTO = new CheckLinkedBookingRequestDTO();
        checkLinkedBookingRequestDTO.setTxtBookingRef(rloc);
        checkLinkedBookingRequestDTO.setTxtFamilyName(familyName);
        checkLinkedBookingRequestDTO.setTxtGivenName(givenName);
        return checkLinkedBookingRequestDTO;
    }
    
    /**
     * add booking by e-ticket
     * 
     * @param loginInfo
     * @param requestDTO
     * @return
     * @throws BusinessBaseException
     */
    private void checkLinkedBookingByET(LoginInfo loginInfo, AddBookingRequestDTO requestDTO, CheckLinkedBookingDTO checkLinkedBookingResponse) throws BusinessBaseException {
        String rloc = ticketProcessInvokeService.getRlocByEticket(requestDTO.getEticket());
        if (StringUtils.isEmpty(rloc)) {
            throw new ExpectedException(String.format("Unable to add booking - Cannot find rloc by eticket:%s", requestDTO.getEticket()),
                    new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_NOBOOKINGFOUND));
        }
        
        requestDTO.setRloc(rloc);
        checkLinkedBookingByRloc(loginInfo, requestDTO,checkLinkedBookingResponse);
    }


    /**
     * add booking by RLOC
     * 
     * @param loginInfo
     * @param requestDTO
     * @param checkLinkedBookingResponse 
     * @return
     * @throws BusinessBaseException
     */
    private void checkLinkedBookingByRloc(LoginInfo loginInfo, AddBookingRequestDTO requestDTO, CheckLinkedBookingDTO checkLinkedBookingResponse)
            throws BusinessBaseException {
        BookingDTOV2 bookingDTO = new BookingDTOV2();
        // rloc checking
        // spnr is not support
        String linkedRloc = getOneARloc(requestDTO.getRloc());

        // get pnr info by RLOC
        RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(linkedRloc);
        if (retrievePnrBooking == null) {
            throw new ExpectedException(
                    String.format("Unable to link booking - Cannot find booking by rloc:%s", linkedRloc),
                    new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_NOBOOKINGFOUND));
        }
        // will bolck non-id booking to link
        if(!retrievePnrBooking.isIDBooking()) {
            throw new ExpectedException(
                    String.format("non-id staff booking is not supported, rloc:%s", linkedRloc),
                    new ErrorInfo(ErrorCodeEnum.ERR_LINK_BOOKING_ID_NONID_FAIL));
        }
        // staff id in linked pnr booking
        String linkedStaffId = pnrResponseParser.parseStaffId(retrievePnrBooking);

        // has same staff id bewteen login rloc and linked rloc
        if (hasSameStaffId(loginInfo, linkedStaffId)) {
            // auto-mapping one of the existing passenger names in the link booking
            RetrievePnrPassenger linkedBookingPassenger = retrievePnrBooking.getPassengers().stream().findFirst()
                    .orElse(null);
            if (null != linkedBookingPassenger) {
                String familyName = linkedBookingPassenger.getFamilyName();
                String givenName = linkedBookingPassenger.getGivenName();
                // olci link booking
                olciCheckLinkedBooking(familyName, givenName, linkedRloc, loginInfo, checkLinkedBookingResponse,
                        bookingDTO);
                checkLinkedBookingResponse.setNeedNameChecking(false);
            }
        } else {
            // different staff id will do name checking
            // try to do name checking, but can not find name will indicate FE we need name
            if (StringUtils.isEmpty(requestDTO.getFamilyName()) || StringUtils.isEmpty(requestDTO.getGivenName())) {
                checkLinkedBookingResponse.setNeedNameChecking(true);
            } else {
                // name checking
                checknameMatch(requestDTO, retrievePnrBooking);
                // olci link booking
                olciCheckLinkedBooking(requestDTO.getFamilyName(), requestDTO.getGivenName(), linkedRloc, loginInfo,
                        checkLinkedBookingResponse, bookingDTO);
            }
        }
    }
    
    /**
     * check name match, 
     * the mean while, paxNameIdentificationService will set primary passenger
     * 
     * @param loginInfo
     * @param requestDTO
     * @param booking
     * @throws BusinessBaseException
     */
    private void checknameMatch(AddBookingRequestDTO requestDTO, RetrievePnrBooking booking) throws BusinessBaseException {
        String familyName = requestDTO.getFamilyName();
        String givenName = requestDTO.getGivenName();
        
        try {
            if(StringUtils.isNotEmpty(requestDTO.getEticket())) {
                paxNameIdentificationService.primaryPaxIdentificationForETicket(familyName, givenName, requestDTO.getEticket(), booking);
            } else {
                paxNameIdentificationService.primaryPaxIdentificationForRloc(familyName, givenName, booking);
            }           
        } catch(ExpectedException e) {
            if(ErrorCodeEnum.ERR_LOGIN_NAME_NOT_MATCH.getCode().equals(e.getErrorInfo().getErrorCode())) {
                throw new ExpectedException(String.format("Cannot match pax name, request data[familyName:%s givenName:%s rloc:%s]", familyName, givenName, booking.getOneARloc()),
                        new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_NAME_MISMATCHED));
            } else if(ErrorCodeEnum.ERR_INFANT_TICKET_LOGIN.getCode().equals(e.getErrorInfo().getErrorCode())) {
                throw new ExpectedException(String.format("input eticket:[%s] is infant eticket", requestDTO.getEticket()),
                        new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_INVAILD_INFANT_ETICKET));
            } else {
                throw e;
            }
        }
    }

    /**
     * get oneARloc(flight RLOC) by input RLOC
     * 
     * @param rloc
     * @param loginInfo
     * @return
     * @throws BusinessBaseException
     */
    private String getOneARloc(String rloc) throws BusinessBaseException {
        String oneARloc = null;
        if (rloc.length() == 7) {
            throw new ExpectedException(
                    String.format("Unable to link booking - no need to get Rloc from ojService:%s", rloc),
                    new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_HOTEL_BOOKING_NOT_SUPPORT));
        } else {
            oneARloc = rloc;
        }

        return oneARloc;
    }
    
    /**
     * check the staffid between login rloc and linked rloc
     * @param loginInfo
     * @param linkedStaffId
     * @return
     * @throws BusinessBaseException
     */
    private boolean hasSameStaffId(LoginInfo loginInfo, String linkedStaffId) throws BusinessBaseException {
        String loginRloc = loginInfo.getLoginRloc();
        RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(loginRloc);
        if (retrievePnrBooking == null) {
            throw new ExpectedException(
                    String.format("Unable to link booking - Cannot find booking by rloc:%s", loginRloc),
                    new ErrorInfo(ErrorCodeEnum.ERR_BOOKINGADD_NOBOOKINGFOUND));
        }
        String loginStaffId = pnrResponseParser.parseStaffId(retrievePnrBooking);
        return StringUtils.equals(loginStaffId, linkedStaffId);
    }

    /**
     * olci response checking
     * @param addLinkedBookingResponse
     * @throws ExpectedException
     */
    private void olciAddLinkedBookingResponseChecking(LoginResponseDTO addLinkedBookingResponse)
            throws ExpectedException {
        if (null == addLinkedBookingResponse) {
            throw new ExpectedException("Unable to link booking - call olci add linked booking failed",
                    new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
        }
        if (!CollectionUtils.isEmpty(addLinkedBookingResponse.getErrors())) {
            com.cathaypacific.mbcommon.dto.error.ErrorInfo error = new com.cathaypacific.mbcommon.dto.error.ErrorInfo();
            error.setErrorCode(addLinkedBookingResponse.getErrors().get(0).getErrorCode());
            error.setType(
                    PnrCprMergeHelper.covertOlciErrorTypeToMmb(addLinkedBookingResponse.getErrors().get(0).getType()));
            throw new ExpectedException("Unable to link booking - from olci response", error);
        }
    }

    /**
     * @param loginInfo
     * @param requestDTO
     * @param checkLinkedBookingResponse
     * @param response
     * @param oneARloc
     * @throws ExpectedException
     */
    private void olciCheckLinkedBooking(String familyName, String givenName, String oneARloc,LoginInfo loginInfo,
            CheckLinkedBookingDTO checkLinkedBookingResponse, BookingDTOV2 bookingDTO)
            throws ExpectedException {
        CheckLinkedBookingRequestDTO olciCheckLinkedBookingRequest = buildCheckLinkedBookingRequest(familyName,
                givenName, oneARloc);
        LoginResponseDTO checkLinkedBookingResponseDTO = olciServiceV2.checkLinkedBooking(olciCheckLinkedBookingRequest,
                loginInfo.getLoginRloc(), loginInfo.getLoginEticket());
        // olci response checking
        olciLinkedBookingResponseChecking(oneARloc, checkLinkedBookingResponseDTO);
        // build booking
        Booking oneaBooking = pnrCprMergeHelper.buildBookingModelByCpr(checkLinkedBookingResponseDTO);
        // convert booking to dto
        FlightBookingDTOV2 flightBookingDTO = dtoConverter.convertToBookingDTO(oneaBooking, loginInfo);

        bookingDTO.setFlightBooking(flightBookingDTO);
        checkLinkedBookingResponse.setBooking(bookingDTO);
    }

    /**
     * @param oneARloc
     * @param checkLinkedBookingResponseDTO
     * @throws ExpectedException
     */
    private void olciLinkedBookingResponseChecking(String oneARloc, LoginResponseDTO checkLinkedBookingResponseDTO)
            throws ExpectedException {
        // call olci check link booking failed or with error
        if (null == checkLinkedBookingResponseDTO) {
            throw new ExpectedException(
                    String.format("Unable to link booking - call olci check linked booking failed:%s", oneARloc),
                    new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
        }
        if (!CollectionUtils.isEmpty(checkLinkedBookingResponseDTO.getErrors())) {
            com.cathaypacific.mbcommon.dto.error.ErrorInfo error = new com.cathaypacific.mbcommon.dto.error.ErrorInfo();
            error.setErrorCode(checkLinkedBookingResponseDTO.getErrors().get(0).getErrorCode());
            error.setType(PnrCprMergeHelper
                    .covertOlciErrorTypeToMmb(checkLinkedBookingResponseDTO.getErrors().get(0).getType()));
            throw new ExpectedException(
                    String.format("Unable to link booking - olci check linked booking return error:%s", oneARloc), error);
        }
    }

}
