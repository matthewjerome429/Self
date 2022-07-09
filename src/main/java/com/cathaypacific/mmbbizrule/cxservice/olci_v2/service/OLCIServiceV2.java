package com.cathaypacific.mmbbizrule.cxservice.olci_v2.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import com.cathaypacific.olciconsumer.model.request.addlinkedbooking.AddLinkedBookingRequestDTO;
import com.cathaypacific.olciconsumer.model.request.checklinkedbooking.CheckLinkedBookingRequestDTO;
import com.cathaypacific.olciconsumer.model.request.regulatorycheck.RegulatoryCheckRequestDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdatePassengerDetailRequestDTO;
import com.cathaypacific.olciconsumer.model.request.updatepassenger.UpdatePassengersDetailRequestDTO;
import com.cathaypacific.olciconsumer.model.response.LoginResponseDTO;
import com.cathaypacific.olciconsumer.model.response.db.*;
import com.cathaypacific.olciconsumer.model.response.regulatorycheck.RegulatoryCheckResponseDTO;
import com.cathaypacific.olciconsumer.model.response.updatepassenger.UpdatePassengerDetailResponseDTO;
import com.cathaypacific.olciconsumer.model.response.updatepassenger.UpdatePassengersDetailResponseDTO;

public interface OLCIServiceV2 {
	/**
	 * Get CPR info from OLCI, one of rloc and ticket is required. 
	 * @param rloc		 (not required)
	 * @param ticket	 (not required)
	 * @param givenName  (required)
	 * @param familyName (required)
	 * @param memberId   (not required)
	 * @param useSession 
	 * @param filerMergePNR
	 * @return
	 */
	public LoginResponseDTO retrieveCPRBooking(String rloc, String eticket, String givenName, String familyName, String memberId, boolean useSession, boolean filerMergePNR);
	
	/**
	 * Async get CPR info from OLCI, and login to OLCI, one of rloc and ticket is required
	 * @param rloc		 (not required)
	 * @param ticket	 (not required)
	 * @param givenName  (required)
	 * @param familyName (required)
	 * @param memberId   (not required)
	 * @param useSession
	 * @param filerMergePNR
	 * @return
	 */
	public Future<LoginResponseDTO> asyncRetrieveCPRBooking(String rloc,String eticket, String givenName, String familyName, String memberId, boolean useSession, boolean filerMergePNR);

	/**
	 * Update passenger detail to CPR
	 * @param requestDTO
	 * @return
	 */
	@Deprecated
	public UpdatePassengerDetailResponseDTO updatePassengerDetail(UpdatePassengerDetailRequestDTO requestDTO);
	
	/**
	 * Update multi passenger detail to CPR
	 * @param requestDTO
	 * @param rloc
	 * @param eticket
	 * @return
	 */
	public UpdatePassengersDetailResponseDTO updatePassengerDetails(UpdatePassengersDetailRequestDTO requestDTO, String rloc, String eticket);

	/**
	 * regulatory check
	 * @param requestDTO
	 * @param rloc
	 * @param eticket
	 * @return
	 */
	public RegulatoryCheckResponseDTO regulatorycheck(RegulatoryCheckRequestDTO requestDTO, String rloc, String eticket);
	
	/**
	 * check linked booking
	 * @param requestDTO
	 * @return
	 */
	public LoginResponseDTO checkLinkedBooking(CheckLinkedBookingRequestDTO requestDTO, String rloc, String eticket);
	
	/**
     * add linked booking
     * @param requestDTO
     * @return
     */
    public LoginResponseDTO addLinkedBooking(AddLinkedBookingRequestDTO requestDTO, String rloc, String eticket);

	public OpenCloseTime retrieveOpenCloseTimeFromDB(String origin, String airlineCode, String appCode, String paxType, Date departDate);

	public TBTravelDocDisplayDTO retrieveTravelDocDisplayFromDB();

	public TBTravelDocCodeByVersionDTO retrieveTravelDocCodeByVersionFromDB(int travelDocVersion, List<String> travelDocTypes);

	public TBDocVersionByCodeAndTypeDTO retrieveDocVersionByCodeAndTypeFromDB(String travelDocCode, String travelDocType);

	public TBVersionByTypeGroupByCodeDTO retrieveVersionByTypeGroupByCodeFromDB(String travelDocType);

	public TBTravelDocListDTO retrieveTravelDocListFromDB();

	public Integer retrieveTravelDocVersionFromDB(String appCode, List<String> origins, List<String> destinations);

	public TBTravelDocODByAppCodeInAndStartDateBeforeDTO retrieveTravelDocODByAppCodeInAndStartDateBeforeFromDB(Collection<String> appCode, java.sql.Date startDate);

	public TBDocCoisByAppCodeDTO retrieveDocCoisByAppCode(String appCode);
}
