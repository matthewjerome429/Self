package com.cathaypacific.mmbbizrule.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.OneAErrorsException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.controller.RetrievePnrController;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateAdultSegmentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateBasicSegmentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateFFPInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateInfantDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateTravelDocDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.TravelDoc;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;
import com.cathaypacific.mmbbizrule.oneaservice.updatepassenger.UpdatePassengerRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.updatepassenger.service.AddPassengerInfoService;
import com.cathaypacific.mmbbizrule.service.UpdatePassengerService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.google.gson.Gson;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to update passenger info
 * @author fengfeng.jiang
 * @date Jan 8, 2018 5:42:46 PM
 * @version V1.0
 */
@Service
public class UpdatePassengerServiceImpl implements UpdatePassengerService{
	
	private static LogAgent logger = LogAgent.getLogAgent(RetrievePnrController.class);
	
	@Autowired
	private DeletePnrService deletePnrService;
	
	@Autowired
	private AddPassengerInfoService addPassengerInfoService;
	
	@Override
	public RetrievePnrBooking updatePassenger(UpdatePassengerDetailsRequestDTO requestDTO, RetrievePnrBooking pnrBooking, Booking booking, TransferStatus ffpTransferStatus, TransferStatus travelDocTransferStatus) throws BusinessBaseException{
		logger.debug(String.format("request json:{%s}",new Gson().toJson(requestDTO)));
		
		// if need to transfer FFP from customer level to product level
		boolean needTransferFFP = checkNeedUpdateFFP(requestDTO, booking.getSegments())
				&& checkPaxOnlyHasCusLevelFfpInBooking(requestDTO.getPassengerId(), booking);
		
		boolean needTransferTravelDoc = checkNeedUpdateTravelDoc(requestDTO, booking.getSegments())
				&& checkAnySegmentNeedTravelDocTransfer(requestDTO, booking);
		
		RetrievePnrBooking updateResult;
		if (!needTransferFFP && !needTransferTravelDoc) { // if don't need to transfer FFP & travel doc, just update the info		
			updateResult = updateWithoutTransfer(requestDTO, pnrBooking, booking);
		} else { // if need to transfer, first update with transfer, if 1A error detected, then try to update without transfer, and then transfer for segments one by one
			// need to transfer FFP & travel doc from customer level to product level, set the flag
			ffpTransferStatus.setNeedTransfer(needTransferFFP);
			travelDocTransferStatus.setNeedTransfer(needTransferTravelDoc);
			updateResult = updateWithTransfer(requestDTO, pnrBooking, booking, ffpTransferStatus, travelDocTransferStatus);
		}
		
		return updateResult;
	}

	/**
	 * check if need to update travel doc
	 * @param requestDTO
	 * @param list 
	 * @return boolean
	 */
	private boolean checkNeedUpdateTravelDoc(UpdatePassengerDetailsRequestDTO requestDTO, List<Segment> segments) {
		if (requestDTO == null || CollectionUtils.isEmpty(segments)) {
			return false;
		}
		
		return (!CollectionUtils.isEmpty(requestDTO.getSegments()) 
				&& requestDTO.getSegments().stream().anyMatch(seg -> (seg.getPrimaryTravelDoc() != null || seg.getSecondaryTravelDoc() != null) && !segmentPassedCheckInOpenTime(seg.getSegmentId(), segments)))
				|| (requestDTO.getInfant() != null && !CollectionUtils.isEmpty(requestDTO.getInfant().getSegments())
						&& requestDTO.getInfant().getSegments().stream().anyMatch(seg -> (seg.getPrimaryTravelDoc() != null || seg.getSecondaryTravelDoc() != null) && !segmentPassedCheckInOpenTime(seg.getSegmentId(), segments)));
	}
	
	/**
	 * 
	 * check if the segment has entered check in time and after
	 * @param segmentId
	 * @return boolean
	 */
	private boolean segmentPassedCheckInOpenTime(String segmentId, List<Segment> segments) {
		Segment seg = segments.stream().filter(s -> s != null && !StringUtils.isEmpty(s.getSegmentID()) && s.getSegmentID().equals(segmentId)).findFirst().orElse(null);
		if(seg == null){
			return false;
		}
		return seg.isOpenToCheckIn() || seg.isWithinNinetyMins() || seg.isFlown();
	}

	/**
	 * check if there's any segment need to transfer travel doc to product level
	 * @param requestDTO
	 * @param booking 
	 * @return boolean
	 */
	private boolean checkAnySegmentNeedTravelDocTransfer(UpdatePassengerDetailsRequestDTO requestDTO, Booking booking) {
		if (requestDTO == null || booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments()) || CollectionUtils.isEmpty(booking.getPassengers()) || CollectionUtils.isEmpty(booking.getSegments())) {
			return false;
		}
		
		if (!CollectionUtils.isEmpty(requestDTO.getSegments())) {
			Passenger passenger = booking.getPassengers().stream().filter(pax -> requestDTO.getPassengerId().equals(pax.getPassengerId())).findFirst().orElse(null);
			// if passenger have any primary travel doc in customer level
			boolean cusLevelPriTravelDocExist = passenger == null ? false : !CollectionUtils.isEmpty(passenger.getPriTravelDocs());
			// if passenger have any secondary travel doc in customer level
			boolean cusLevelSecTravelDocExist = passenger == null ? false : !CollectionUtils.isEmpty(passenger.getSecTravelDocs());
			List<PassengerSegment> psListOfThePassenger = booking.getPassengerSegments().stream().filter(ps -> requestDTO.getPassengerId().equals(ps.getPassengerId())).collect(Collectors.toList());
			
			for (PassengerSegment ps : psListOfThePassenger) {
				// if the passengerSegment don't have any travel doc and there's travel doc in customer level and this travel doc is not updated and the sector hasn't passed check-in open time, then need to transfer to product level
				if (((ps.getPriTravelDoc() == null && cusLevelPriTravelDocExist && !checkIsTravelDocUpdated(requestDTO.getSegments(), ps.getSegmentId(), true))
						|| (ps.getSecTravelDoc() == null && cusLevelSecTravelDocExist && !checkIsTravelDocUpdated(requestDTO.getSegments(), ps.getSegmentId(), false)))
						&& !segmentPassedCheckInOpenTime(ps.getSegmentId(), booking.getSegments())) {
					return true;
				}
			}
		}
		
		if (requestDTO.getInfant() != null && !StringUtils.isEmpty(requestDTO.getInfant().getPassengerId()) && !CollectionUtils.isEmpty(requestDTO.getInfant().getSegments())) {
			Passenger infPassenger = booking.getPassengers().stream().filter(pax -> requestDTO.getInfant().getPassengerId().equals(pax.getPassengerId())).findFirst().orElse(null);
			// if passenger have any primary travel doc in customer level
			boolean cusLevelPriTravelDocExist = infPassenger == null ? false : !CollectionUtils.isEmpty(infPassenger.getPriTravelDocs());
			// if passenger have any secondary travel doc in customer level
			boolean cusLevelSecTravelDocExist = infPassenger == null ? false : !CollectionUtils.isEmpty(infPassenger.getSecTravelDocs());
			List<PassengerSegment> psListOfThePassenger = booking.getPassengerSegments().stream().filter(ps -> requestDTO.getInfant().getPassengerId().equals(ps.getPassengerId())).collect(Collectors.toList());
			
			for (PassengerSegment ps : psListOfThePassenger) {
				// if the passengerSegment don't have any travel doc and there's travel doc in customer level and this travel doc is not updated and the sector hasn't passed check-in open time, then need to transfer to product level
				if (((ps.getPriTravelDoc() == null && cusLevelPriTravelDocExist && !checkIsTravelDocUpdated(requestDTO.getInfant().getSegments(), ps.getSegmentId(), true))
						|| (ps.getSecTravelDoc() == null && cusLevelSecTravelDocExist && !checkIsTravelDocUpdated(requestDTO.getInfant().getSegments(), ps.getSegmentId(), false)))
						&& !segmentPassedCheckInOpenTime(ps.getSegmentId(), booking.getSegments())) {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * check if the travel doc is updated
	 * @param segments
	 * @param segmentId
	 * @param isPrimary
	 * @return boolean
	 */
	private boolean checkIsTravelDocUpdated(List<? extends UpdateBasicSegmentInfoDTO> segments, String segmentId, boolean isPrimary) {
		if (isPrimary) {
			return segments.stream().anyMatch(seg -> !StringUtils.isEmpty(seg.getSegmentId()) && seg.getSegmentId().equals(segmentId) && seg.getPrimaryTravelDoc() != null);
		} else {
			return segments.stream().anyMatch(seg -> !StringUtils.isEmpty(seg.getSegmentId()) && seg.getSegmentId().equals(segmentId) && seg.getSecondaryTravelDoc() != null);
		}
	}

	/**
	 * update the pax info without transfer
	 * @param requestDTO
	 * @param pnrBooking
	 * @param booking
	 * @return RetrievePnrBooking
	 * @throws BusinessBaseException
	 */
	private RetrievePnrBooking updateWithoutTransfer(UpdatePassengerDetailsRequestDTO requestDTO,
			RetrievePnrBooking pnrBooking, Booking booking)
			throws BusinessBaseException {
		RetrievePnrBooking updateResult;
		UpdatePassengerRequestBuilder builder = new UpdatePassengerRequestBuilder();
		//Store mapping OT id which need to be deleted 
		Map<String, List<String>> deleteMap = new HashMap<>();
		PNRAddMultiElements request = builder.buildRequest(requestDTO, pnrBooking, booking, deleteMap, false, false);
		//delete PNR
		Session session = deleteElements(requestDTO, deleteMap, request);
		updateResult = addPassengerInfoService.addPassengerInfo(request, session);
		return updateResult;
	}

	/**
	 * update the pax info with transfer
	 * @param requestDTO
	 * @param pnrBooking
	 * @param booking
	 * @param ffpTransferStatus
	 * @param travelDocTransferStatus 
	 * @return RetrievePnrBooking
	 * @throws BusinessBaseException
	 */
	private RetrievePnrBooking updateWithTransfer(UpdatePassengerDetailsRequestDTO requestDTO,
			RetrievePnrBooking pnrBooking, Booking booking, TransferStatus ffpTransferStatus, TransferStatus travelDocTransferStatus) throws BusinessBaseException {
		UpdatePassengerRequestBuilder builder = new UpdatePassengerRequestBuilder();	
		//Store mapping OT id which need to be deleted 
		Map<String, List<String>> deleteMap = new HashMap<>();	
		Session session;
		RetrievePnrBooking updateResult;
		PNRAddMultiElements request = builder.buildRequest(requestDTO, pnrBooking, booking, deleteMap, BooleanUtils.isTrue(ffpTransferStatus.isNeedTransfer()), BooleanUtils.isTrue(travelDocTransferStatus.isNeedTransfer()));
		try { // try update with transfer
			//delete PNR
			session = deleteElements(requestDTO, deleteMap, request);
			updateResult = addPassengerInfoService.addPassengerInfo(request, session);
		} catch (OneAErrorsException e) { // if update fails
			logger.warn(String.format("update passenger info with transfer failed for booking: %s, try to update and transfer separately", requestDTO.getRloc()), e);
			deleteMap.clear();
			builder = new UpdatePassengerRequestBuilder();
			// try update without transfer first
			request = builder.buildRequest(requestDTO, pnrBooking, booking, deleteMap, false, false);
			//delete PNR
			session = deleteElements(requestDTO, deleteMap, request);
			updateResult = addPassengerInfoService.addPassengerInfo(request, session);
		
			// mock request list for segment those need to transfer FFP
			List<UpdatePassengerDetailsRequestDTO> ffpTransferRequestList = mockFfpTransferRequestList(booking, requestDTO);	
			// transfer FFP one by one 
			for (UpdatePassengerDetailsRequestDTO ffpTransferRequest : ffpTransferRequestList) {
				builder = new UpdatePassengerRequestBuilder();
				try {
					request = builder.buildRequest(ffpTransferRequest, updateResult, booking, deleteMap, false, false);
					updateResult = addPassengerInfoService.addPassengerInfo(request, null);
				} catch (Exception e2) {
					// there is FFP transfer failed, set the flag
					ffpTransferStatus.setAnyTransferFailed(true);
					logger.warn(String.format("Transfer FFP failed for rloc: %s, passengerId: %s, segmentId: %s", ffpTransferRequest.getRloc(), ffpTransferRequest.getPassengerId(),
							CollectionUtils.isEmpty(ffpTransferRequest.getSegments()) ? "": ffpTransferRequest.getSegments().get(0).getSegmentId()), e2);
				}
			}
			
			// mock request list for segment those need to transfer travel doc
			List<UpdatePassengerDetailsRequestDTO> travelDocTransferRequestList = mockTravelDocTransferRequestList(booking, requestDTO);	
			// transfer travel doc one by one 
			for (UpdatePassengerDetailsRequestDTO travelDocTransferRequest : travelDocTransferRequestList) {
				builder = new UpdatePassengerRequestBuilder();
				try {
					request = builder.buildRequest(travelDocTransferRequest, updateResult, booking, deleteMap, false, false);
					updateResult = addPassengerInfoService.addPassengerInfo(request, null);
				} catch (Exception e2) {
					// there is travel doc transfer failed, set the flag
					travelDocTransferStatus.setAnyTransferFailed(true);
					logger.warn(String.format("Transfer travel doc failed for rloc: %s, passengerId: %s, segmentId: %s", travelDocTransferRequest.getRloc(), travelDocTransferRequest.getPassengerId(),
							CollectionUtils.isEmpty(travelDocTransferRequest.getSegments()) ? "": travelDocTransferRequest.getSegments().get(0).getSegmentId()), e2);
				}
			}
		}
		return updateResult;
	}
	
	/**
	 * mock request list for the transfer of travel doc
	 * @param booking
	 * @param requestDTO
	 * @return List<UpdatePassengerDetailsRequestDTO>
	 */
	private List<UpdatePassengerDetailsRequestDTO> mockTravelDocTransferRequestList(Booking booking,
			UpdatePassengerDetailsRequestDTO requestDTO) {
		List<UpdatePassengerDetailsRequestDTO> mockRequestDTOs = new ArrayList<>();
		if (booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())
				|| CollectionUtils.isEmpty(booking.getPassengers())
				|| requestDTO == null) {
			return mockRequestDTOs;
		}
		
		if (!CollectionUtils.isEmpty(requestDTO.getSegments())) {
			// passengerSegment of the pax in request
			List<PassengerSegment> psListOfThePax = booking.getPassengerSegments().stream()
					.filter(ps -> !StringUtils.isEmpty(ps.getPassengerId())
							&& ps.getPassengerId().equals(requestDTO.getPassengerId()))
					.collect(Collectors.toList());
			
			Passenger pax = booking.getPassengers().stream().filter(p -> requestDTO.getPassengerId().equals(p.getPassengerId())).findFirst().orElse(new Passenger());
			// customer level primary travel doc
			TravelDoc cusLevelPriTravelDoc = !CollectionUtils.isEmpty(pax.getPriTravelDocs()) ? pax.getPriTravelDocs().get(0) : null;
			// customer level secondary travel doc
			TravelDoc cusLevelSecTravelDoc = !CollectionUtils.isEmpty(pax.getSecTravelDocs()) ? pax.getSecTravelDocs().get(0) : null;
			for (PassengerSegment ps : psListOfThePax) {
				// mock for primary travel doc
				mockTravelDocRequestForAdultPax(booking, requestDTO, mockRequestDTOs, cusLevelPriTravelDoc, ps, true);
				// mock for secondary travel doc
				mockTravelDocRequestForAdultPax(booking, requestDTO, mockRequestDTOs, cusLevelSecTravelDoc, ps, false);
			}
		}
		
		if (requestDTO.getInfant() != null && !StringUtils.isEmpty(requestDTO.getInfant().getPassengerId()) && !CollectionUtils.isEmpty(requestDTO.getInfant().getSegments())) {
			// passengerSegment of the pax in request
			List<PassengerSegment> psListOfThePax = booking.getPassengerSegments().stream()
					.filter(ps -> !StringUtils.isEmpty(ps.getPassengerId())
							&& ps.getPassengerId().equals(requestDTO.getInfant().getPassengerId()))
					.collect(Collectors.toList());	

			Passenger pax = booking.getPassengers().stream().filter(p -> requestDTO.getInfant().getPassengerId().equals(p.getPassengerId())).findFirst().orElse(new Passenger());
			// customer level primary travel doc
			TravelDoc cusLevelPriTravelDoc = !CollectionUtils.isEmpty(pax.getPriTravelDocs()) ? pax.getPriTravelDocs().get(0) : null;
			// customer level secondary travel doc
			TravelDoc cusLevelSecTravelDoc = !CollectionUtils.isEmpty(pax.getSecTravelDocs()) ? pax.getSecTravelDocs().get(0) : null;
			
			for (PassengerSegment ps : psListOfThePax) {
				// mock for primary travel doc
				mockTravelDocRequestForInfantPax(booking, requestDTO, mockRequestDTOs, cusLevelPriTravelDoc, ps, true);
				// mock for secondary travel doc
				mockTravelDocRequestForInfantPax(booking, requestDTO, mockRequestDTOs, cusLevelSecTravelDoc, ps, false);
			}
		}
		
		return mockRequestDTOs;
	}

	/**
	 * mock travel doc transfer request for infant passenger
	 * @param booking
	 * @param requestDTO
	 * @param mockRequestDTOs
	 * @param cusLevelTravelDoc
	 * @param passengerSegment
	 * @param isPrimary
	 */
	private void mockTravelDocRequestForInfantPax(Booking booking, UpdatePassengerDetailsRequestDTO requestDTO,
			List<UpdatePassengerDetailsRequestDTO> mockRequestDTOs, TravelDoc cusLevelTravelDoc, PassengerSegment ps,
			boolean isPrimary) {
		if (!checkIsTravelDocUpdated(requestDTO.getInfant().getSegments(), ps.getSegmentId(), isPrimary)
				&& ps.getPriTravelDoc() == null && cusLevelTravelDoc != null
				&& !segmentPassedCheckInOpenTime(ps.getSegmentId(), booking.getSegments())) {
			UpdatePassengerDetailsRequestDTO mockRequestDTO = new UpdatePassengerDetailsRequestDTO();
			
			mockRequestDTO.setRloc(requestDTO.getRloc());
			mockRequestDTO.setPassengerId(ps.getPassengerId());
			UpdateInfantDTO infantDTO = new UpdateInfantDTO();
			List<UpdateBasicSegmentInfoDTO> mockSegmentDTOs = new ArrayList<>();
			UpdateBasicSegmentInfoDTO mockSegmentDTO = new UpdateBasicSegmentInfoDTO();
			mockSegmentDTO.setSegmentId(ps.getSegmentId());
			
			UpdateTravelDocDTO travelDocDTO = new UpdateTravelDocDTO();
			travelDocDTO.setCountryOfIssuance(cusLevelTravelDoc.getCountryOfIssuance());
			travelDocDTO.setCountryOfResidence(cusLevelTravelDoc.getCountryOfResidence());
			travelDocDTO.setDateOfBirth(cusLevelTravelDoc.getBirthDateYear()+"-"+cusLevelTravelDoc.getBirthDateMonth()+"-"+cusLevelTravelDoc.getBirthDateDay());
			travelDocDTO.setDateOfExpire(cusLevelTravelDoc.getExpiryDateYear()+"-"+cusLevelTravelDoc.getExpiryDateMonth()+"-"+cusLevelTravelDoc.getExpiryDateDay());
			travelDocDTO.setFamilyName(cusLevelTravelDoc.getFamilyName());
			travelDocDTO.setGivenName(cusLevelTravelDoc.getGivenName());
			travelDocDTO.setGender(cusLevelTravelDoc.getGender());
			travelDocDTO.setNationality(cusLevelTravelDoc.getNationality());
			travelDocDTO.setTravelDocumentNumber(cusLevelTravelDoc.getTravelDocumentNumber());
			travelDocDTO.setTravelDocumentType(cusLevelTravelDoc.getTravelDocumentType());
			
			if (isPrimary) {
				mockSegmentDTO.setPrimaryTravelDoc(travelDocDTO);
			} else {
				mockSegmentDTO.setSecondaryTravelDoc(travelDocDTO);
			}
			mockSegmentDTOs.add(mockSegmentDTO);
			infantDTO.setSegments(mockSegmentDTOs);
			mockRequestDTO.setInfant(infantDTO);
			mockRequestDTOs.add(mockRequestDTO);
		}
	}
	
	/**
	 * mock travel doc transfer request for adult passenger
	 * @param booking
	 * @param requestDTO
	 * @param mockRequestDTOs
	 * @param cusLevelTravelDoc
	 * @param passengerSegment
	 * @param isPrimary
	 */
	private void mockTravelDocRequestForAdultPax(Booking booking, UpdatePassengerDetailsRequestDTO requestDTO,
			List<UpdatePassengerDetailsRequestDTO> mockRequestDTOs, TravelDoc cusLevelTravelDoc, PassengerSegment ps,
			boolean isPrimary) {
		if (!checkIsTravelDocUpdated(requestDTO.getSegments(), ps.getSegmentId(), isPrimary)
				&& ps.getPriTravelDoc() == null && cusLevelTravelDoc != null
				&& !segmentPassedCheckInOpenTime(ps.getSegmentId(), booking.getSegments())) {
			UpdatePassengerDetailsRequestDTO mockRequestDTO = new UpdatePassengerDetailsRequestDTO();
			
			mockRequestDTO.setRloc(requestDTO.getRloc());
			mockRequestDTO.setPassengerId(ps.getPassengerId());
			
			List<UpdateAdultSegmentInfoDTO> mockSegmentDTOs = new ArrayList<>();
			UpdateAdultSegmentInfoDTO mockSegmentDTO = new UpdateAdultSegmentInfoDTO();
			mockSegmentDTO.setSegmentId(ps.getSegmentId());
			
			UpdateTravelDocDTO travelDocDTO = new UpdateTravelDocDTO();	
			travelDocDTO.setCountryOfIssuance(cusLevelTravelDoc.getCountryOfIssuance());
			travelDocDTO.setCountryOfResidence(cusLevelTravelDoc.getCountryOfResidence());
			travelDocDTO.setDateOfBirth(cusLevelTravelDoc.getBirthDateYear()+"-"+cusLevelTravelDoc.getBirthDateMonth()+"-"+cusLevelTravelDoc.getBirthDateDay());
			travelDocDTO.setDateOfExpire(cusLevelTravelDoc.getExpiryDateYear()+"-"+cusLevelTravelDoc.getExpiryDateMonth()+"-"+cusLevelTravelDoc.getExpiryDateDay());
			travelDocDTO.setFamilyName(cusLevelTravelDoc.getFamilyName());
			travelDocDTO.setGivenName(cusLevelTravelDoc.getGivenName());
			travelDocDTO.setGender(cusLevelTravelDoc.getGender());
			travelDocDTO.setNationality(cusLevelTravelDoc.getNationality());
			travelDocDTO.setTravelDocumentNumber(cusLevelTravelDoc.getTravelDocumentNumber());
			travelDocDTO.setTravelDocumentType(cusLevelTravelDoc.getTravelDocumentType());
			
			if (isPrimary) {
				mockSegmentDTO.setPrimaryTravelDoc(travelDocDTO);
			} else {
				mockSegmentDTO.setSecondaryTravelDoc(travelDocDTO);
			}
			mockSegmentDTOs.add(mockSegmentDTO);
			mockRequestDTO.setSegments(mockSegmentDTOs);
			mockRequestDTOs.add(mockRequestDTO);
		}
	}

	/**
	 * check if the pax only have customer level ffp in the booking
	 * @param passengerId
	 * @param booking
	 * @return boolean
	 */
	private boolean checkPaxOnlyHasCusLevelFfpInBooking(String passengerId, Booking booking) {
		if (StringUtils.isEmpty(passengerId) || booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return false;
		}
		
		boolean hasCusLevelFfp = booking.getPassengerSegments().stream().anyMatch(ps -> passengerId.equals(ps.getPassengerId()) && ps.getFqtvInfo() != null && !ps.getFqtvInfo().isBlank() && !BooleanUtils.isTrue(ps.getFqtvInfo().isProductLevel()));
		boolean hasProLevelFfp = booking.getPassengerSegments().stream().anyMatch(ps -> passengerId.equals(ps.getPassengerId()) && ps.getFqtvInfo() != null && !ps.getFqtvInfo().isBlank() && BooleanUtils.isTrue(ps.getFqtvInfo().isProductLevel()));
		return hasCusLevelFfp && !hasProLevelFfp;
	}
	
	/**
	 * @Description check if the segment is flown
	 * @param segmentId
	 * @return
	 */
	private boolean isSegmentFlown(String segmentId, List<Segment> segments) {
		Segment seg = segments.stream().filter(s -> s != null && s.getSegmentID().equals(segmentId)).findFirst().orElse(null);
		if(seg == null){
			return false;
		}
		return BooleanUtils.isTrue(seg.isFlown());
	}
	
	/**
	 * check if request contains update of FFP
	 * @param requestDTO
	 * @return
	 */
	private boolean checkNeedUpdateFFP(UpdatePassengerDetailsRequestDTO requestDTO, List<Segment> segments) {
		 if (requestDTO == null || CollectionUtils.isEmpty(requestDTO.getSegments()) || CollectionUtils.isEmpty(segments)) {
			return false;
		}
		
		return requestDTO.getSegments().stream().anyMatch(seg -> seg.getFfpInfo() != null && !isSegmentFlown(seg.getSegmentId(), segments));
	}

	/**
	 * mock request list for the transfer of FFP
	 * @param pnrBooking
	 * @param booking 
	 * @param requestDTO
	 * @return List<UpdatePassengerDetailsRequestDTO>
	 */
	private List<UpdatePassengerDetailsRequestDTO> mockFfpTransferRequestList(Booking booking, UpdatePassengerDetailsRequestDTO requestDTO) {
		List<UpdatePassengerDetailsRequestDTO> mockRequestDTOs = new ArrayList<>();
		if (booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())
				|| CollectionUtils.isEmpty(booking.getSegments())
				|| requestDTO == null
				|| CollectionUtils.isEmpty(requestDTO.getSegments())) {
			return mockRequestDTOs;
		}
		// passengerSegment of the pax in request
		List<PassengerSegment> psListOfThePax = booking.getPassengerSegments().stream()
				.filter(ps -> !StringUtils.isEmpty(ps.getPassengerId())
						&& ps.getPassengerId().equals(requestDTO.getPassengerId()))
				.collect(Collectors.toList());
		for (PassengerSegment ps : psListOfThePax) {
			if (!checkFfpOfPsUpdated(ps.getPassengerId(), ps.getSegmentId(), requestDTO) && ps.getFqtvInfo() != null
					&& !ps.getFqtvInfo().isBlank() && !BooleanUtils.isTrue(ps.getFqtvInfo().isProductLevel())
					&& !isSegmentFlown(ps.getSegmentId(), booking.getSegments())) {
				// if FFP of the passengerSegment is from customer level, and FFP of this passengerSegment is not updated in requestDTO and segment is not flown, mock request for this passengerSegment for later transfer
				UpdatePassengerDetailsRequestDTO mockRequestDTO = new UpdatePassengerDetailsRequestDTO();
				
				mockRequestDTO.setRloc(requestDTO.getRloc());
				mockRequestDTO.setPassengerId(ps.getPassengerId());
				
				List<UpdateAdultSegmentInfoDTO> mockSegmentDTOs = new ArrayList<>();
				UpdateAdultSegmentInfoDTO mockSegmentDTO = new UpdateAdultSegmentInfoDTO();
				UpdateFFPInfoDTO mockFfpDTO = new UpdateFFPInfoDTO();
				
				mockFfpDTO.setCompanyId(ps.getFqtvInfo().getCompanyId());
				mockFfpDTO.setMembershipNumber(ps.getFqtvInfo().getMembershipNumber());
				
				mockSegmentDTO.setFfpInfo(mockFfpDTO);
				mockSegmentDTO.setSegmentId(ps.getSegmentId());
				
				mockSegmentDTOs.add(mockSegmentDTO);
				mockRequestDTO.setSegments(mockSegmentDTOs);
				
				mockRequestDTOs.add(mockRequestDTO);
			}
		}
		
		return mockRequestDTOs;
	}

	/**
	 * check if the FFP of the passengerSegment is updated in the requestDTO
	 * @param passengerId
	 * @param segmentId
	 * @param requestDTO
	 * @return boolean
	 */
	private boolean checkFfpOfPsUpdated(String passengerId, String segmentId,
			UpdatePassengerDetailsRequestDTO requestDTO) {
		if (StringUtils.isEmpty(passengerId) || StringUtils.isEmpty(segmentId)) {
			return false;
		}
		return passengerId.equals(requestDTO.getPassengerId()) && requestDTO.getSegments().stream().anyMatch(seg -> seg.getFfpInfo() != null && segmentId.equals(seg.getSegmentId()));
	}

	private Session deleteElements(UpdatePassengerDetailsRequestDTO requestDTO, Map<String, List<String>> deleteMap,
			PNRAddMultiElements request) throws BusinessBaseException {
		Session session;
		if(!deleteMap.isEmpty()) {
			session = new Session();//set begin transaction
			
			Map<String, List<String>> map = buildDeleteOtMap(deleteMap);
			
			RetrievePnrBooking booking = deletePnrService.deletePnr(requestDTO.getRloc(), map, session);
			//check onea error for delete response
			session = booking.getSession();
			session.setStatus(SessionStatus.END.getStatus());//set end transaction
			request.getReservationInfo().getReservation().setControlNumber("");
		}else {
			session = null;
		}
		return session;
	}
	
	/**
	 * build OT map for 1A delete
	 * @param deleteMap
	 * @return map
	 */
	private Map<String, List<String>> buildDeleteOtMap(Map<String, List<String>> deleteMap) {
		Map<String, List<String>> map = new HashMap<>();
		List<String> values = new ArrayList<>();
		for(Entry<String, List<String>> entry : deleteMap.entrySet()) {
			values.addAll(entry.getValue());
		}
		map.put(OneAConstants.OT_QUALIFIER, values);
		return map;
	}
	
}