package com.cathaypacific.mmbbizrule.oneaservice.model.common.util;

import java.util.List;
import org.springframework.util.StringUtils;

import com.cathaypacific.mmbbizrule.oneaservice.model.common.OneAError;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ErrorGroupType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ErrorGroupType212789G;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.ErrorGroupType223552G;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.FOPRepresentationType;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.DataElementsMaster.DataElementsIndiv;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.OriginDestinationDetails;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.OriginDestinationDetails.ItineraryInfo;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.OriginDestinationDetails.ItineraryInfo.TypicalCarData;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply.TravellerInfo;


public class OneAErrorParserUtil {

    private OneAErrorParserUtil(){
		
	}
	
	public static void parserOneAErrorFromGeneralErrorInfo(List<OneAError> errorDetails, List<ErrorGroupType212789G> generalErrorInfo) {
		
		for(ErrorGroupType212789G generalError : generalErrorInfo){
			OneAError oneAError=new OneAError();		
			oneAError.setErrorCategory(generalError.getErrorOrWarningCodeDetails().getErrorDetails().getErrorCategory());						
			oneAError.setErrorCodeOwner(generalError.getErrorOrWarningCodeDetails().getErrorDetails().getErrorCodeOwner());			
			oneAError.setErrorCode(generalError.getErrorOrWarningCodeDetails().getErrorDetails().getErrorCode());		
			oneAError.setErrorMessage(generalError.getErrorWarningDescription().getFreeText().get(0));
				
			errorDetails.add(oneAError);
		}
	}

	public static void parserOneAErrorFromTravellerInfo(List<OneAError> errorDetails,
			List<TravellerInfo> travellerInfo) {
		
		for(TravellerInfo traveller : travellerInfo){
		  if(traveller.getNameError() != null){
			OneAError oneAError=new OneAError();			
			oneAError.setErrorCategory(traveller.getNameError().getErrorOrWarningCodeDetails().getErrorDetails().getErrorCategory());						
			oneAError.setErrorCodeOwner(traveller.getNameError().getErrorOrWarningCodeDetails().getErrorDetails().getErrorCodeOwner());				
			oneAError.setErrorCode(traveller.getNameError().getErrorOrWarningCodeDetails().getErrorDetails().getErrorCode());
			if(traveller.getNameError().getErrorWarningDescription() != null){
			oneAError.setErrorMessage(traveller.getNameError().getErrorWarningDescription().getFreeText().get(0));
			}
			errorDetails.add(oneAError);
		 }
		}
		
	}

	public static void parserOneAErrorFromOriginDestinationDetails(List<OneAError> errorDetails,
			List<OriginDestinationDetails> originDestinationDetails) {
		
		for(OriginDestinationDetails originDestinationDetail : originDestinationDetails){		
			parserOneAError(originDestinationDetail.getItineraryInfo(),errorDetails);		
		}
		
	}

	private static void parserOneAError(List<ItineraryInfo> itineraryInfos, List<OneAError> errorDetails) {
		
		for(ItineraryInfo itineraryInfo : itineraryInfos){
			if(itineraryInfo.getTypicalCarData() != null && itineraryInfo.getTypicalCarData().getErrorWarning() != null){
			parserOneAErrorFromTypicalCarData(itineraryInfo.getTypicalCarData(),errorDetails);
			}
			if(itineraryInfo.getErrorInfo() != null){
			parserOneAErrorFromErrorInfo(itineraryInfo.getErrorInfo(),errorDetails);
			}
		}	
	}

	private static void parserOneAErrorFromErrorInfo(ErrorGroupType223552G errorInfo,
			List<OneAError> errorDetails) {
		OneAError oneAError = new OneAError();
		oneAError.setErrorCategory(errorInfo.getErrorOrWarningCodeDetails().getErrorDetails().getErrorCategory());
		oneAError.setErrorCodeOwner(errorInfo.getErrorOrWarningCodeDetails().getErrorDetails().getErrorCodeOwner());
		oneAError.setErrorCode(errorInfo.getErrorOrWarningCodeDetails().getErrorDetails().getErrorCode());
		if(errorInfo.getErrorWarningDescription() != null){
		oneAError.setErrorMessage(errorInfo.getErrorWarningDescription().getFreeText().get(0));
		}
		errorDetails.add(oneAError);
		
	}
		
	
	private static void parserOneAErrorFromTypicalCarData(TypicalCarData typicalCarData, List<OneAError> errorDetails) { 
		OneAError oneAError=new OneAError();
		if(typicalCarData.getErrorWarning() != null && typicalCarData.getErrorWarning().getApplicationError() != null 
				&& !StringUtils.isEmpty(typicalCarData.getErrorWarning().getApplicationError().getErrorDetails().getErrorCategory())){
			oneAError.setErrorCategory(typicalCarData.getErrorWarning().getApplicationError()
					.getErrorDetails().getErrorCategory());
		}
		if(typicalCarData.getErrorWarning() != null && typicalCarData.getErrorWarning().getApplicationError() != null 
				&& !StringUtils.isEmpty(typicalCarData.getErrorWarning().getApplicationError()
				.getErrorDetails().getErrorCodeOwner())){
			oneAError.setErrorCodeOwner(typicalCarData.getErrorWarning().getApplicationError()
					.getErrorDetails().getErrorCodeOwner());
		}
		oneAError.setErrorCode(typicalCarData.getErrorWarning().getApplicationError()
				.getErrorDetails().getErrorCode());		
		if(typicalCarData.getErrorWarning() != null && typicalCarData.getErrorWarning().getErrorFreeText() != null){
		oneAError.setErrorMessage(typicalCarData.getErrorWarning().getErrorFreeText().getFreeText().get(0));
		}
		errorDetails.add(oneAError);
		
	}

	public static void parserOneAErrorFromDataElementsIndiv(List<OneAError> errorDetails,
			List<DataElementsIndiv> dataElementsIndivs) {
		
		for(DataElementsIndiv dataElementsIndiv : dataElementsIndivs){		
			if(dataElementsIndiv.getElementErrorInformation() != null){
			parserOneAErrorFromErrorInformation(dataElementsIndiv.getElementErrorInformation(),errorDetails);
			}			
			parserOneAErrorFromStructuredFop(dataElementsIndiv.getStructuredFop(),errorDetails);			
		}
		
	}

	private static void parserOneAErrorFromStructuredFop(List<FOPRepresentationType> structuredFops,
			List<OneAError> errorDetails) {
		for(FOPRepresentationType structuredFop : structuredFops){
			if(structuredFop.getPaymentModule() != null && structuredFop.getPaymentModule().getMopDetailedData() != null
					&& structuredFop.getPaymentModule().getMopDetailedData().getCreditCardDetailedData() != null 
					&& structuredFop.getPaymentModule().getMopDetailedData().getCreditCardDetailedData().getTransactionStatus() != null){
			parserOneAErrorFromTransactionStatus(structuredFop.getPaymentModule().getMopDetailedData().getCreditCardDetailedData()
					.getTransactionStatus(),errorDetails);
		 }
		}
	}

	private static void parserOneAErrorFromTransactionStatus(List<ErrorGroupType> transactionStatus,
			List<OneAError> errorDetails) {
		
		for(ErrorGroupType transactionStatu : transactionStatus){
		  if(transactionStatu.getErrorOrWarningCodeDetails() != null){
			OneAError oneAError=new OneAError();			
			oneAError.setErrorCategory(transactionStatu.getErrorOrWarningCodeDetails().getErrorDetails().getErrorCategory());					
			oneAError.setErrorCode(transactionStatu.getErrorOrWarningCodeDetails().getErrorDetails().getErrorCode());		
			if(transactionStatu.getErrorWarningDescription() != null){
			oneAError.setErrorMessage(transactionStatu.getErrorWarningDescription().getFreeText());
			}
			errorDetails.add(oneAError);
		}	
		}		
	}

	private static void parserOneAErrorFromErrorInformation(ErrorGroupType223552G elementErrorInformation,
			List<OneAError> errorDetails) {
		OneAError oneAError=new OneAError();
		
		oneAError.setErrorCategory(elementErrorInformation.getErrorOrWarningCodeDetails().getErrorDetails().getErrorCategory());		
		oneAError.setErrorCodeOwner(elementErrorInformation.getErrorOrWarningCodeDetails().getErrorDetails().getErrorCodeOwner());
		oneAError.setErrorCode(elementErrorInformation.getErrorOrWarningCodeDetails().getErrorDetails().getErrorCode());		
		if(elementErrorInformation.getErrorWarningDescription() != null){
		oneAError.setErrorMessage(elementErrorInformation.getErrorWarningDescription().getFreeText().get(0));
		}
		errorDetails.add(oneAError);
		
	}

}
