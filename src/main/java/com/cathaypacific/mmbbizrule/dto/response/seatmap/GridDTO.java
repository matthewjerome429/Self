
package com.cathaypacific.mmbbizrule.dto.response.seatmap;

import java.util.List;

public class GridDTO {

    private String column;
    private String gridType;
    private List<String> facilities;
    private SeatDetailsDTO seatDetails;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getGridType() {
        return gridType;
    }

    public void setGridType(String gridType) {
        this.gridType = gridType;
    }

    public SeatDetailsDTO getSeatDetails() {
        return seatDetails;
    }

    public void setSeatDetails(SeatDetailsDTO seatDetails) {
        this.seatDetails = seatDetails;
    }

	public List<String> getFacilities() {
		return facilities;
	}

	public void setFacilities(List<String> facilities) {
		this.facilities = facilities;
	}

    
}
