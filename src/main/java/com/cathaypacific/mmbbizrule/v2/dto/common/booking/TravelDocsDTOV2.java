package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

import com.cathaypacific.mmbbizrule.dto.common.GroupBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class TravelDocsDTOV2 extends GroupBaseDTO implements Serializable{

	private static final long serialVersionUID = 2745481524178493401L;
	
	private TravelDocDTOV2 priTravelDoc;
	
	private TravelDocDTOV2 secTravelDoc;
	
	public TravelDocDTOV2 getPriTravelDoc() {
		return priTravelDoc;
	}
	
	public TravelDocDTOV2 findPriTravelDoc() {
		if(priTravelDoc == null) {
			priTravelDoc = new TravelDocDTOV2();
		}
		return priTravelDoc;
	}

	public void setPriTravelDoc(TravelDocDTOV2 priTravelDoc) {
		this.priTravelDoc = priTravelDoc;
	}

	public TravelDocDTOV2 getSecTravelDoc() {
		return secTravelDoc;
	}
	
	public TravelDocDTOV2 findSecTravelDoc() {
		if(secTravelDoc == null) {
			secTravelDoc = new TravelDocDTOV2();
		}
		return secTravelDoc;
	}

	public void setSecTravelDoc(TravelDocDTOV2 secTravelDoc) {
		this.secTravelDoc = secTravelDoc;
	}

	@JsonIgnore
	public boolean isEmpty(){
		
		if((priTravelDoc != null && !priTravelDoc.isEmpty()) || 
				(secTravelDoc != null && !secTravelDoc.isEmpty())){
			return false;
		} else {
			return true;
		}
		
	}

}
