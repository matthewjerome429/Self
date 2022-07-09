
package com.cathaypacific.mmbbizrule.dto.response.seatmap;

import java.util.List;

public class SeatMapDTO {

    private FlightDateDTO flightDate;
    private String origin;
    private String destination;
    private String flightNumber;
    private String bookingClass;
    private String aircraftTypeCode;
    private EquipmentInformationDTO equipmentInformation;
    private List<CabinDTO> cabins;
    private List<GridRowDTO> rows = null;
    private List<CustomerDataDTO> customerDatas;

    public FlightDateDTO getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(FlightDateDTO flightDate) {
        this.flightDate = flightDate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(String bookingClass) {
        this.bookingClass = bookingClass;
    }

    public String getAircraftTypeCode() {
        return aircraftTypeCode;
    }

    public void setAircraftTypeCode(String aircraftTypeCode) {
        this.aircraftTypeCode = aircraftTypeCode;
    }

    public EquipmentInformationDTO getEquipmentInformation() {
        return equipmentInformation;
    }

    public void setEquipmentInformation(EquipmentInformationDTO equipmentInformation) {
        this.equipmentInformation = equipmentInformation;
    }

    public List<GridRowDTO> getRows() {
        return rows;
    }

    public void setRows(List<GridRowDTO> rows) {
        this.rows = rows;
    }

	public List<CabinDTO> getCabins() {
		return cabins;
	}

	public void setCabins(List<CabinDTO> cabins) {
		this.cabins = cabins;
	}

	public List<CustomerDataDTO> getCustomerDatas() {
		return customerDatas;
	}

	public void setCustomerDatas(List<CustomerDataDTO> customerDatas) {
		this.customerDatas = customerDatas;
	}
}
