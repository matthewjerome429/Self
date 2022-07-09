package com.cathaypacific.mmbbizrule.business.commonapi.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cathaypacific.mmbbizrule.db.service.TbTravelDocNatCoiCheckCacheHelper;
import com.cathaypacific.olciconsumer.model.response.db.TravelDocNatCoiMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mmbbizrule.business.commonapi.TravelDocBusiness;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocListCacheHelper;
import com.cathaypacific.mmbbizrule.dto.response.commonapi.traveldoc.TBTravelDocNatCoiMappingDTO;
import com.cathaypacific.mmbbizrule.dto.response.commonapi.traveldoc.TravedocTypesResponseDTO;

@Service
public class TravelDocBusinessImpl implements TravelDocBusiness{
	
	@Autowired
	private TbTravelDocListCacheHelper travelDocListCacheHelper;
	
	@Autowired
	private TbTravelDocNatCoiCheckCacheHelper travelDocNatCoiCheckCacheHelper;
	
	@Override
	public TravedocTypesResponseDTO getTravedocTypesByApiVersion(Integer version) {
		TravedocTypesResponseDTO response = new TravedocTypesResponseDTO();
		if(version==null){
			return response;
		}
		response.setUsablePrimaryTavelDocs(travelDocListCacheHelper.findTravelDocCodeByVersion(version,Arrays.asList(TBConstants.TRAVEL_DOC_PRIMARY)));
		response.setUsableSecondaryTavelDocs(travelDocListCacheHelper.findTravelDocCodeByVersion(version,Arrays.asList(TBConstants.TRAVEL_DOC_SECONDARY)));
		return response;
	}
	@Override
	public List<TBTravelDocNatCoiMappingDTO> getNATAndCOI() {
		List<TravelDocNatCoiMapping>  travelDocNatCoiMappings = travelDocNatCoiCheckCacheHelper.findDocCoisByAppCode(MMBConstants.APP_CODE);
		List<TBTravelDocNatCoiMappingDTO>  travelDocNatCoiList  = new ArrayList<>();
		for(TravelDocNatCoiMapping tbTravelDocNatCoi : travelDocNatCoiMappings) {
			TBTravelDocNatCoiMappingDTO travelDocNatCoiMappingsDTO  = new TBTravelDocNatCoiMappingDTO();
			travelDocNatCoiMappingsDTO.setCoi(tbTravelDocNatCoi.getCoi());
			travelDocNatCoiMappingsDTO.setDocType(tbTravelDocNatCoi.getDocType());
			travelDocNatCoiMappingsDTO.setNat(tbTravelDocNatCoi.getNat());
			travelDocNatCoiMappingsDTO.setReminder(tbTravelDocNatCoi.getReminder());
			travelDocNatCoiList.add(travelDocNatCoiMappingsDTO);
		}
		
		return travelDocNatCoiList;
	}

}
