package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author tommy.shuai.wang
 *
 */
public class HotelBookingDTO implements Serializable {

	private static final long serialVersionUID = -3872574474999921227L;
	
	private String bookingStatus;

	private Boolean isStayed;
	
	private List<HotelDTO> details;
	
	private Boolean isCompleted;
	
	@JsonIgnore
	private Boolean isAllInVaild;//true: All hotel checkoutDate > 4 days

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public Boolean isStayed() {
		return isStayed;
	}

	public void setIsStayed(Boolean isStayed) {
		this.isStayed = isStayed;
	}

	public List<HotelDTO> getDetails() {
		return details;
	}

	public void setDetails(List<HotelDTO> details) {
		this.details = details;
	}

	public Boolean isCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public Boolean isAllInVaild() {
		return isAllInVaild;
	}

	public void setIsAllInVaild(Boolean isAllInVaild) {
		this.isAllInVaild = isAllInVaild;
	}
	
}
