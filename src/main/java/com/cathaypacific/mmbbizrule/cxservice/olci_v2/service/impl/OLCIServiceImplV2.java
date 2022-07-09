package com.cathaypacific.mmbbizrule.cxservice.olci_v2.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import com.cathaypacific.olciconsumer.model.response.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.cathaypacific.mmbbizrule.cxservice.olci_v2.service.OLCIServiceV2;
import com.cathaypacific.olciconsumer.model.request.addlinkedbooking.AddLinkedBookingRequestDTO;
import com.cathaypacific.olciconsumer.model.request.checklinkedbooking.CheckLinkedBookingRequestDTO;
import com.cathaypacific.olciconsumer.model.request.regulatorycheck.RegulatoryCheckRequestDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdatePassengerDetailRequestDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdatePassengersDetailRequestDTO;
import com.cathaypacific.olciconsumer.model.response.LoginResponseDTO;
import com.cathaypacific.olciconsumer.model.response.regulatorycheck.RegulatoryCheckResponseDTO;
import com.cathaypacific.olciconsumer.model.response.updatepassenger.UpdatePassengerDetailResponseDTO;
import com.cathaypacific.olciconsumer.model.response.updatepassenger.UpdatePassengersDetailResponseDTO;
import com.cathaypacific.olciconsumer.service.client.OlciClient;

@Service
public class OLCIServiceImplV2 implements OLCIServiceV2 {
	
	@Autowired
	OlciClient olciClient;

	@Override
	public LoginResponseDTO retrieveCPRBooking(String rloc,String eticket, String givenName, String familyName, String memberId, boolean useSession, boolean filerMergePNR) {
		ResponseEntity<LoginResponseDTO> responseEntity = olciClient.retrieveBookings(rloc, givenName, familyName, memberId, eticket, useSession, filerMergePNR);
		return responseEntity.getBody();
	}
	
	@Override
	@Async
	public Future<LoginResponseDTO> asyncRetrieveCPRBooking(String rloc,String eticket, String givenName, String familyName, String memberId, boolean useSession, boolean filerMergePNR) {
		return new AsyncResult<>(retrieveCPRBooking(rloc, eticket, givenName, familyName, memberId, useSession, filerMergePNR));
	}

	@Override
	@Deprecated
	public UpdatePassengerDetailResponseDTO updatePassengerDetail(UpdatePassengerDetailRequestDTO requestDTO) {
		ResponseEntity<UpdatePassengerDetailResponseDTO> responseEntity = olciClient.updatePassengerDetail(requestDTO);
		return responseEntity.getBody();
	}
	
	@Override
	public UpdatePassengersDetailResponseDTO updatePassengerDetails(UpdatePassengersDetailRequestDTO requestDTO, String rloc, String eticket) {
		ResponseEntity<UpdatePassengersDetailResponseDTO> responseEntity = olciClient.updatePassengerDetails(requestDTO, rloc, eticket);
		return responseEntity.getBody();
	}
	
	@Override
	public RegulatoryCheckResponseDTO regulatorycheck(RegulatoryCheckRequestDTO requestDTO, String rloc, String eticket) {
		ResponseEntity<RegulatoryCheckResponseDTO> responseEntity = olciClient.regulatorycheck(requestDTO, rloc, eticket);
		return responseEntity.getBody();
	}

    @Override
    public LoginResponseDTO checkLinkedBooking(CheckLinkedBookingRequestDTO requestDTO, String rloc, String eticket) {
        ResponseEntity<LoginResponseDTO> responseEntity = olciClient.checkLinkedBooking(requestDTO, rloc, eticket);
        return responseEntity.getBody();
    }

    @Override
    public LoginResponseDTO addLinkedBooking(AddLinkedBookingRequestDTO requestDTO, String rloc, String eticket) {
        ResponseEntity<LoginResponseDTO> responseEntity = olciClient.addLinkedBooking(requestDTO, rloc, eticket);
        return responseEntity.getBody();
    }

	@Override
	public OpenCloseTime retrieveOpenCloseTimeFromDB(String origin, String airlineCode, String appCode, String paxType, Date departDate) {
		ResponseEntity<OpenCloseTime> responseEntity = olciClient.retrieveOpenCloseTimeFromDB(origin,airlineCode,appCode,paxType,departDate);
		return responseEntity.getBody();
	}

	@Override
	public TBTravelDocDisplayDTO retrieveTravelDocDisplayFromDB() {
		ResponseEntity<TBTravelDocDisplayDTO> responseEntity = olciClient.retrieveTravelDocDisplayFromDB();
		return responseEntity.getBody();
	}

	@Override
	public TBTravelDocCodeByVersionDTO retrieveTravelDocCodeByVersionFromDB(int travelDocVersion, List<String> travelDocTypes) {
		ResponseEntity<TBTravelDocCodeByVersionDTO> responseEntity = olciClient.retrieveTravelDocCodeByVersionFromDB(travelDocVersion,travelDocTypes);
		return responseEntity.getBody();
	}

	@Override
	public TBDocVersionByCodeAndTypeDTO retrieveDocVersionByCodeAndTypeFromDB(String travelDocCode, String travelDocType) {
		ResponseEntity<TBDocVersionByCodeAndTypeDTO> responseEntity = olciClient.retrieveDocVersionByCodeAndTypeFromDB(travelDocCode,travelDocType);
		return responseEntity.getBody();
	}

	@Override
	public TBVersionByTypeGroupByCodeDTO retrieveVersionByTypeGroupByCodeFromDB(String travelDocType) {
		ResponseEntity<TBVersionByTypeGroupByCodeDTO> responseEntity = olciClient.retrieveVersionByTypeGroupByCodeFromDB(travelDocType);
		return responseEntity.getBody();
	}

	@Override
	public TBTravelDocListDTO retrieveTravelDocListFromDB() {
		ResponseEntity<TBTravelDocListDTO> responseEntity = olciClient.retrieveTravelDocListFromDB();
		return responseEntity.getBody();
	}

	@Override
	public Integer retrieveTravelDocVersionFromDB(String appCode, List<String> origins, List<String> destinations) {
		ResponseEntity<Integer> responseEntity = olciClient.retrieveTravelDocVersionFromDB(appCode,origins,destinations);
		return responseEntity.getBody();
	}

	@Override
	public TBTravelDocODByAppCodeInAndStartDateBeforeDTO retrieveTravelDocODByAppCodeInAndStartDateBeforeFromDB(Collection<String> appCode, java.sql.Date startDate) {
		ResponseEntity<TBTravelDocODByAppCodeInAndStartDateBeforeDTO> responseEntity = olciClient.retrieveTravelDocODByAppCodeInAndStartDateBeforeFromDB(appCode,startDate);
		return responseEntity.getBody();
	}

	@Override
	public TBDocCoisByAppCodeDTO retrieveDocCoisByAppCode(String appCode) {
		ResponseEntity<TBDocCoisByAppCodeDTO> responseEntity = olciClient.retrieveDocCoisByAppCodeFromDB(appCode);
		return responseEntity.getBody();
	}
}
