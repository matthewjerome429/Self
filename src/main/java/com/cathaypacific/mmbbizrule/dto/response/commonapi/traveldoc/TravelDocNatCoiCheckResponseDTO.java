package com.cathaypacific.mmbbizrule.dto.response.commonapi.traveldoc;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class TravelDocNatCoiCheckResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 7749963612929866623L;

	private List<TBTravelDocNatCoiMappingDTO> docNatCoiCheckList;
	
	public List<TBTravelDocNatCoiMappingDTO> getDocNatCoiCheckList() {
		return docNatCoiCheckList;
	}

	public void setDocNatCoiCheckList(List<TBTravelDocNatCoiMappingDTO> docNatCoiCheckList) {
		this.docNatCoiCheckList = docNatCoiCheckList;
	}
	
}
