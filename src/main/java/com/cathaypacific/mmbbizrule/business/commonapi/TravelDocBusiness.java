package com.cathaypacific.mmbbizrule.business.commonapi;

import java.util.List;

import com.cathaypacific.mmbbizrule.dto.response.commonapi.traveldoc.TBTravelDocNatCoiMappingDTO;
import com.cathaypacific.mmbbizrule.dto.response.commonapi.traveldoc.TravedocTypesResponseDTO;

public interface TravelDocBusiness {
	
	/**
	 *  Get the available travel doc type list by api version
	 * @param version
	 * @return
	 */
	public TravedocTypesResponseDTO getTravedocTypesByApiVersion(Integer version);
	
	/**
	 * 
	 * get nat coi info from db
	 * @return
	 */
	public List<TBTravelDocNatCoiMappingDTO> getNATAndCOI();
	

}
