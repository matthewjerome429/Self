package com.cathaypacific.mmbbizrule.dto.response.memberprofile;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class ProfileTravelDocResponseDTO {

	private List<TravelDocumentRecordDTO> travelDocuments;

	public List<TravelDocumentRecordDTO> getTravelDocuments() {
		return travelDocuments;
	}

	public void setTravelDocuments(List<TravelDocumentRecordDTO> travelDocuments) {
		this.travelDocuments = travelDocuments;
	}

	public void addTravelDocuments(List<TravelDocumentRecordDTO> profileTravelDocs) {
		if (CollectionUtils.isEmpty(profileTravelDocs)) {
			return;
		}
		if (this.travelDocuments == null) {
			this.travelDocuments = new ArrayList<>();
		}
		this.travelDocuments.addAll(profileTravelDocs);
	}

	public void addTravelDocument(TravelDocumentRecordDTO profileTravelDoc){
		if(profileTravelDoc == null){
			return;
		}
		if(this.travelDocuments == null){
			this.travelDocuments = new ArrayList<>();
		}
		travelDocuments.add(profileTravelDoc);
	}
}
