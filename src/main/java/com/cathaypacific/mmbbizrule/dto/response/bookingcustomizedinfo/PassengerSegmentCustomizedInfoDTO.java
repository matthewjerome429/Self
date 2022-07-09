package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import java.io.Serializable;
import java.util.List;

import com.cathaypacific.mbcommon.enums.upgrade.UpgradeProgressStatus;

import io.swagger.annotations.ApiModelProperty;

public class PassengerSegmentCustomizedInfoDTO implements Serializable{

	private static final long serialVersionUID = -3975605477604381130L;

	/** passenger id */
	private String passengerId;
	
	/** segment id */
	private String segmentId;
	
	private String eticketNumber;
	
	private FQTVCustomizedInfoDTO fqtvInfo;
	
	private ClaimedLoungeDTO claimedLounge;
	
	private PurchasedLoungeDTO purchasedLounge;
	
	private UpgradeProgressStatus bookableUpgradeStatus;
	/** MMB seat selection */
	@ApiModelProperty("MMB seat selection")
	private SeatSelectionCustomizedInfoDTO seatSelection;
	/** OLCI seat selection */
	@ApiModelProperty("OLCI seat selection")
	private SeatSelectionCustomizedInfoDTO olciSeatSelection;
	
	private SeatCustomizedInfoDTO seat;
	
	private List<SeatCustomizedInfoDTO> extraSeats;
	
	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public FQTVCustomizedInfoDTO getFqtvInfo() {
		return fqtvInfo;
	}

	public void setFqtvInfo(FQTVCustomizedInfoDTO fqtvInfo) {
		this.fqtvInfo = fqtvInfo;
	}

	public String getEticketNumber() {
		return eticketNumber;
	}

	public void setEticketNumber(String eticketNumber) {
		this.eticketNumber = eticketNumber;
	}

	public ClaimedLoungeDTO getClaimedLounge() {
		return claimedLounge;
	}

	public void setClaimedLounge(ClaimedLoungeDTO claimedLounge) {
		this.claimedLounge = claimedLounge;
	}

	public UpgradeProgressStatus getBookableUpgradeStatus() {
		return bookableUpgradeStatus;
	}

	public PurchasedLoungeDTO getPurchasedLounge() {
		return purchasedLounge;
	}

	public void setPurchasedLounge(PurchasedLoungeDTO purchasedLounge) {
		this.purchasedLounge = purchasedLounge;
	}

	public void setBookableUpgradeStatus(UpgradeProgressStatus bookableUpgradeStatus) {
		this.bookableUpgradeStatus = bookableUpgradeStatus;
	}

	public SeatSelectionCustomizedInfoDTO getSeatSelection() {
		return seatSelection;
	}

	public void setSeatSelection(SeatSelectionCustomizedInfoDTO seatSelection) {
		this.seatSelection = seatSelection;
	}

	public SeatSelectionCustomizedInfoDTO getOlciSeatSelection() {
		return olciSeatSelection;
	}

	public void setOlciSeatSelection(SeatSelectionCustomizedInfoDTO olciSeatSelection) {
		this.olciSeatSelection = olciSeatSelection;
	}

	public SeatCustomizedInfoDTO getSeat() {
		return seat;
	}

	public void setSeat(SeatCustomizedInfoDTO seat) {
		this.seat = seat;
	}

	public List<SeatCustomizedInfoDTO> getExtraSeats() {
		return extraSeats;
	}

	public void setExtraSeats(List<SeatCustomizedInfoDTO> extraSeats) {
		this.extraSeats = extraSeats;
	}

}
