package com.cathaypacific.mmbbizrule.oneaservice.pnrsearch;

import java.util.Arrays;
import java.util.List;

import com.cathaypacific.oneaconsumer.model.request.pausrq_16_1_1a.CodedAttributeInformationType;
import com.cathaypacific.oneaconsumer.model.request.pausrq_16_1_1a.CodedAttributeType;
import com.cathaypacific.oneaconsumer.model.request.pausrq_16_1_1a.FrequentTravellerIdentificationCodeType;
import com.cathaypacific.oneaconsumer.model.request.pausrq_16_1_1a.FrequentTravellerIdentificationType;
import com.cathaypacific.oneaconsumer.model.request.pausrq_16_1_1a.ObjectFactory;
import com.cathaypacific.oneaconsumer.model.request.pausrq_16_1_1a.PNRSearch;

public class PnrSearchRequestBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();

	public PNRSearch buildRlocRequest(String memberId){
		
		PNRSearch pnrSearch = objFactory.createPNRSearch();
		//option 1
		pnrSearch.getOptions().add(buildOption("SEA",Arrays.asList("FF")));
		//option 2
		pnrSearch.getOptions().add(buildOption("OUT",Arrays.asList("ALL")));
		
		//ffp info
		FrequentTravellerIdentificationCodeType frequentTravellerInformation = objFactory.createFrequentTravellerIdentificationCodeType();
		pnrSearch.setFrequentTravellerInformation(frequentTravellerInformation);
		FrequentTravellerIdentificationType frequentTravellerDetails = objFactory.createFrequentTravellerIdentificationType();
		frequentTravellerInformation.setFrequentTravellerDetails(frequentTravellerDetails);
		frequentTravellerDetails.setCarrier("CX");
		frequentTravellerDetails.setNumber(memberId);
		
		return pnrSearch;
	}
	
	/**
	 * build option：/Body/PNR_Search/options
	 * @param attributeFunction
	 * @param attributeTypes
	 * @return
	 */
	private CodedAttributeType buildOption(String attributeFunction, List<String> attributeTypes){
		
		CodedAttributeType option = objFactory.createCodedAttributeType();
		option.setAttributeFunction(attributeFunction);
		
		for (String  attributeType : attributeTypes) {
			CodedAttributeInformationType attributeDetail = objFactory.createCodedAttributeInformationType();
			option.getAttributeDetails().add(attributeDetail);
			attributeDetail.setAttributeType(attributeType);
		}
		return option;
	}
}
