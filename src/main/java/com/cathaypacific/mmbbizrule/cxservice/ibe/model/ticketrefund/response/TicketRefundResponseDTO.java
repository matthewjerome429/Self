package com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.response;

import java.io.Serializable;
import java.util.List;

public class TicketRefundResponseDTO implements Serializable {
	
	private static final long serialVersionUID = -1235664875488415608L;

	private List<ErrorDTO> errors;
	
	private List<String> successfulTicketNumbers;
	
	private List<String> failTicketNumbers;

	public List<ErrorDTO> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorDTO> errors) {
		this.errors = errors;
	}

	public List<String> getSuccessfulTicketNumbers() {
		return successfulTicketNumbers;
	}

	public void setSuccessfulTicketNumbers(List<String> successfulTicketNumbers) {
		this.successfulTicketNumbers = successfulTicketNumbers;
	}

	public List<String> getFailTicketNumbers() {
		return failTicketNumbers;
	}

	public void setFailTicketNumbers(List<String> failTicketNumbers) {
		this.failTicketNumbers = failTicketNumbers;
	}

}
