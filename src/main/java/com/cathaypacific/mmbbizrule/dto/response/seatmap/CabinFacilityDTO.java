
package com.cathaypacific.mmbbizrule.dto.response.seatmap;

import java.util.List;

public class CabinFacilityDTO {

    private String rowLocation;
    private List<CabinFacilityDetailDTO> cabinFacilityDetails = null;

    public String getRowLocation() {
        return rowLocation;
    }

    public void setRowLocation(String rowLocation) {
        this.rowLocation = rowLocation;
    }

    public List<CabinFacilityDetailDTO> getCabinFacilityDetails() {
        return cabinFacilityDetails;
    }

    public void setCabinFacilityDetails(List<CabinFacilityDetailDTO> cabinFacilityDetails) {
        this.cabinFacilityDetails = cabinFacilityDetails;
    }

}
