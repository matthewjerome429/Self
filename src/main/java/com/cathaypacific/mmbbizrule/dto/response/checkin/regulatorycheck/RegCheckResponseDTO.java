package com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck;

import java.util.List;

public class RegCheckResponseDTO extends RegCheckBaseResponseDTO {
	
	private static final long serialVersionUID = 1699088169592187501L;
	
	
	private RegCheckCprJourneyDTO cprJourney;
	
	public RegCheckResponseDTO() {
		super();
	}
	 
	public RegCheckResponseDTO(RegCheckError errorInfo) {
		super();
		this.addError(errorInfo);
	}
	
	public RegCheckResponseDTO(List<RegCheckError> errorInfos) {
		super();
		this.addAllErrors(errorInfos);
	}

	public RegCheckCprJourneyDTO getCprJourney() {
		return cprJourney;
	}

	public void setCprJourney(RegCheckCprJourneyDTO cprJourney) {
		this.cprJourney = cprJourney;
	}

}
