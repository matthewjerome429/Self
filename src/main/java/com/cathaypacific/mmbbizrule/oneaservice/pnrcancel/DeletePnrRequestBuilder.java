package com.cathaypacific.mmbbizrule.oneaservice.pnrcancel;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.pnrxcl_16_1_1a.CancelPNRElementType;
import com.cathaypacific.oneaconsumer.model.request.pnrxcl_16_1_1a.ElementIdentificationType;
import com.cathaypacific.oneaconsumer.model.request.pnrxcl_16_1_1a.ObjectFactory;
import com.cathaypacific.oneaconsumer.model.request.pnrxcl_16_1_1a.OptionalPNRActionsType;
import com.cathaypacific.oneaconsumer.model.request.pnrxcl_16_1_1a.PNRCancel;
import com.cathaypacific.oneaconsumer.model.request.pnrxcl_16_1_1a.ReservationControlInformationDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnrxcl_16_1_1a.ReservationControlInformationType;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this class is used to convert front end DTO to oneA request model
 * @author fengfeng.jiang
 * @date Jan 8, 2018 5:28:29 PM
 * @version V1.0
 */
public class DeletePnrRequestBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();
	
	
	public PNRCancel buildCancelBookingRequest(String rloc, Session session){
		PNRCancel request = objFactory.createPNRCancel();
		
		request.setReservationInfo(createReservationInfo(rloc));
		
		// set pnrActions
		String actionCode = "11";
		if(session != null && !SessionStatus.END.getStatus().equals(session.getStatus()) ) {
			actionCode = "0";
		} 
		request.setPnrActions(createPnrActions(actionCode));
		
		//entry type
		request.getCancelElements().add(createEntryType("I"));
		
		return request;
	}

	/**
	 * 
	* @Description convert front end DTO to oneA request model
	* @param
	* @return PNRAddMultiElements
	* @author fengfeng.jiang
	 * @param map 
	 */
	public PNRCancel buildRequest(String rloc, Map<String, List<String>> map, Session session){
		/**** build request start ****/
		PNRCancel request = objFactory.createPNRCancel();
		
		if (session == null || SessionStatus.START.getStatus().equals(session.getStatus())) {
			/* add rloc */
			request.setReservationInfo(createReservationInfo(rloc));
		}
		/*add pnrActions*/
		String actionCode = "11";
		if(session != null && !SessionStatus.END.getStatus().equals(session.getStatus())) {
			actionCode = "0";
		}
		request.setPnrActions(createPnrActions(actionCode));
		
		/*add cancelElements*/
		addCancelElements(request,map);
		
		
		return request;
	}
	
	/**
	 * 
	* @Description create pnrActions element bean
	* @param
	* @return OptionalPNRActionsType
	* @author fengfeng.jiang
	 */
	private OptionalPNRActionsType createPnrActions(String optionCode) {
		OptionalPNRActionsType pnrActions = objFactory.createOptionalPNRActionsType();
		pnrActions.getOptionCode().add(new BigInteger(optionCode));
		
		return pnrActions;
	}
	
	/**
	 * 
	 * @Description create reservationInfo element bean
	 * @param
	 * @return ReservationControlInformationTypeI
	 * @author fengfeng.jiang
	 */
	private ReservationControlInformationType createReservationInfo(String rloc) {
		ReservationControlInformationDetailsTypeI reservation = objFactory.createReservationControlInformationDetailsTypeI();
		reservation.setControlNumber(rloc);
		ReservationControlInformationType reservationInfo = objFactory.createReservationControlInformationType();
		reservationInfo.setReservation(reservation);
		
		return reservationInfo;
	}
	
	/**
	 * 
	 * @Description create cancelElements element bean
	 * @param
	 * @return ReservationControlInformationTypeI
	 * @author fengfeng.jiang
	 */
	private void addCancelElements(PNRCancel request, Map<String, List<String>> deleteMap) {
		CancelPNRElementType cancelType = objFactory.createCancelPNRElementType();
		cancelType.setEntryType("E");
		
		//add elements
		for(Entry<String, List<String>> entry:deleteMap.entrySet()) {
			List<String> otList = entry.getValue();
			String identifier = entry.getKey();
			for(String otNo:otList) {
				ElementIdentificationType element = objFactory.createElementIdentificationType();
				element.setIdentifier(identifier);
				element.setNumber(new BigInteger(otNo));
				
				cancelType.getElement().add(element);
			}
		}
		
		request.getCancelElements().add(cancelType);
	}
	
	/**
	 * /PNR_Cancel/cancelElements/entryType
	 * @param EntryCode
	 * @return
	 */
	private CancelPNRElementType createEntryType(String entryCode) {
		CancelPNRElementType cancelType = objFactory.createCancelPNRElementType();
		cancelType.setEntryType(entryCode);
		return cancelType;
	}
}
