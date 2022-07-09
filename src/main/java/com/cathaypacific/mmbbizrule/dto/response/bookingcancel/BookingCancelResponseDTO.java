package com.cathaypacific.mmbbizrule.dto.response.bookingcancel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class BookingCancelResponseDTO extends BaseResponseDTO {
 
	private static final long serialVersionUID = 8337185149269048727L;
	
	/** The identifier of cancel status */
	private boolean cancelled = false;
	
	/** The identifier of refund status */
	private boolean refunded = false;

	/** The identifier of loggged out status */
	private boolean loggedOut = false;
	
	/** do refund or not from requestDTO*/
	private boolean requestedRefund;
	
	private List<ErrorInfo> blockReasons; 

	/** the special scenario need to be skip Refund*/
	@JsonIgnore
	private boolean skipRefund = false;
	
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean isRefunded() {
		return refunded;
	}

	public void setRefunded(boolean refunded) {
		this.refunded = refunded;
	}

	public boolean isLoggedOut() {
		return loggedOut;
	}

	public void setLoggedOut(boolean loggedOut) {
		this.loggedOut = loggedOut;
	}

	public boolean isRequestedRefund() {
		return requestedRefund;
	}

	public void setRequestedRefund(boolean requestedRefund) {
		this.requestedRefund = requestedRefund;
	}

	public List<ErrorInfo> getBlockReasons() {
		return blockReasons;
	}

	public void setBlockReasons(List<ErrorInfo> blockReasons) {
		this.blockReasons = blockReasons;
	}
	
	public void addBlockReason(ErrorInfo blockReason) {
		if(CollectionUtils.isEmpty(blockReasons)) {
			blockReasons = new ArrayList<>();
		}
		blockReasons.add(blockReason);
	}

	public boolean isSkipRefund() {
		return skipRefund;
	}

	public void setSkipRefund(boolean skipRefund) {
		this.skipRefund = skipRefund;
	}
	
}
