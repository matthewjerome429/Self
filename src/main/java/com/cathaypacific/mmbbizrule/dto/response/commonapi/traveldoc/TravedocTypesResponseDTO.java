package com.cathaypacific.mmbbizrule.dto.response.commonapi.traveldoc;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class TravedocTypesResponseDTO extends BaseResponseDTO{
 
	private static final long serialVersionUID = 7749963612929866625L;
	
	private List<String> usablePrimaryTavelDocs;

	private List<String> usableSecondaryTavelDocs;

	public List<String> getUsablePrimaryTavelDocs() {
		return usablePrimaryTavelDocs;
	}

	public void setUsablePrimaryTavelDocs(List<String> usablePrimaryTavelDocs) {
		this.usablePrimaryTavelDocs = usablePrimaryTavelDocs;
	}

	public List<String> getUsableSecondaryTavelDocs() {
		return usableSecondaryTavelDocs;
	}

	public void setUsableSecondaryTavelDocs(List<String> usableSecondaryTavelDocs) {
		this.usableSecondaryTavelDocs = usableSecondaryTavelDocs;
	} 

}
