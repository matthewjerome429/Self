package com.cathaypacific.mmbbizrule.dto.response.tagging;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class TaggingDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 4889382003606770550L;
	
	private List<MealTagMappingDTO> mealTagMappings;
	
	private List<AncillaryBannerDeeplinkDataDTO> ancillaryBannerDeeplinkData;

	public List<MealTagMappingDTO> getMealTagMappings() {
		return mealTagMappings;
	}

	public void setMealTagMappings(List<MealTagMappingDTO> mealTagMappings) {
		this.mealTagMappings = mealTagMappings;
	}

	public List<AncillaryBannerDeeplinkDataDTO> getAncillaryBannerDeeplinkData() {
		return ancillaryBannerDeeplinkData;
	}

	public void setAncillaryBannerDeeplinkData(List<AncillaryBannerDeeplinkDataDTO> ancillaryBannerDeeplinkData) {
		this.ancillaryBannerDeeplinkData = ancillaryBannerDeeplinkData;
	}
	
}
