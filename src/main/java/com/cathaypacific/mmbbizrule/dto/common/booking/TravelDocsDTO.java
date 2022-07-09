package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

import com.cathaypacific.mmbbizrule.dto.common.GroupBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class TravelDocsDTO extends GroupBaseDTO implements Serializable{

	private static final long serialVersionUID = 2745481524178493401L;
	
	private TravelDocDTO priTravelDoc;
	
	private TravelDocDTO secTravelDoc;
	
	public TravelDocDTO getPriTravelDoc() {
		return priTravelDoc;
	}
	
	public TravelDocDTO findPriTravelDoc() {
		if(priTravelDoc == null) {
			priTravelDoc = new TravelDocDTO();
		}
		return priTravelDoc;
	}

	public void setPriTravelDoc(TravelDocDTO priTravelDoc) {
		this.priTravelDoc = priTravelDoc;
	}

	public TravelDocDTO getSecTravelDoc() {
		return secTravelDoc;
	}
	
	public TravelDocDTO findSecTravelDoc() {
		if(secTravelDoc == null) {
			secTravelDoc = new TravelDocDTO();
		}
		return secTravelDoc;
	}

	public void setSecTravelDoc(TravelDocDTO secTravelDoc) {
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
