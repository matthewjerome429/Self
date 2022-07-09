package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessCouponInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ETicket {
	
	/** Format of time*/
	public static final String TIME_FORMAT = "ddMMMyy";
	
	private String number;
	
	private String originatorId;
	
	private List<TicketProcessCouponInfo> couponInfos;
	
	private String date;
	
	private String passengerType;
	
	/** Currency and amount from PNR ET element e.g. FA */
	private String currency;
	private String amount;
	
	/** Currency and amount from 1A ticket process's fare info with T type qualifier (means total) */
	private String ticketProcessFareCurrency;
	private String ticketProcessFareAmount;
	
	/** 
	 * Format: DateUtil.DATE_PATTERN_DDMMYY: "ddMMyy" 
	 * From Ticket_ProcessEDoc
	 * <Ticket_ProcessEDocReply>
	 *	<docGroup>
	 * 		<productInfo>
	 * 			<productDateTimeDetails>
	 * 				<departureDate>210918</departureDate>
	 * 			</productDateTimeDetails>
	 * 		</productInfo>
	 * 	</docGroup>
	 * 	<docGroup>
	 *  	... ...
	 *  </docGroup>
	 * </Ticket_ProcessEDocReply>
	 * */ 
	private String productDepartureDate;
	
	private BigInteger iataNumber;
	
	private String fareRateTariffClass;
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getOriginatorId() {
		return originatorId;
	}

	public void setOriginatorId(String originatorId) {
		this.originatorId = originatorId;
	}
	
	public List<TicketProcessCouponInfo> findCouponInfos() {
		if(couponInfos == null) {
			couponInfos = new ArrayList<>();
		}
		return couponInfos;
	}

	public List<TicketProcessCouponInfo> getCouponInfos() {
		return couponInfos;
	}

	public void setCouponInfos(List<TicketProcessCouponInfo> couponInfos) {
		this.couponInfos = couponInfos;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}
	
	public String getProductDepartureDate() {
		return productDepartureDate;
	}

	public void setProductDepartureDate(String productDepartureDate) {
		this.productDepartureDate = productDepartureDate;
	}

	@JsonIgnore
	public List<String> getCpnNumbers() {
		if(CollectionUtils.isEmpty(couponInfos)) {
			return Collections.emptyList();
		}
		return couponInfos.stream().map(TicketProcessCouponInfo::getCpnNumber).distinct().collect(Collectors.toList());
	}

	/** Currency from PNR ET element */
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/** Amount from PNR ET element */
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public BigInteger getIataNumber() {
		return iataNumber;
	}

	public void setIataNumber(BigInteger iataNumber) {
		this.iataNumber = iataNumber;
	}

	public String getFareRateTariffClass() {
		return fareRateTariffClass;
	}

	public void setFareRateTariffClass(String fareRateTariffClass) {
		this.fareRateTariffClass = fareRateTariffClass;
	}

	/** Currency from 1A ticket process's fare info with T type qualifier (means total) */
	public String getTicketProcessFareCurrency() {
		return ticketProcessFareCurrency;
	}

	public void setTicketProcessFareCurrency(String ticketProcessFareCurrency) {
		this.ticketProcessFareCurrency = ticketProcessFareCurrency;
	}

	/** Amount from 1A ticket process's fare info with T type qualifier (means total) */
	public String getTicketProcessFareAmount() {
		return ticketProcessFareAmount;
	}

	public void setTicketProcessFareAmount(String ticketProcessFareAmount) {
		this.ticketProcessFareAmount = ticketProcessFareAmount;
	}
	
}
