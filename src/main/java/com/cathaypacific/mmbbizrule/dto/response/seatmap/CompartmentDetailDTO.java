
package com.cathaypacific.mmbbizrule.dto.response.seatmap;

import java.math.BigInteger;
import java.util.List;

public class CompartmentDetailDTO {

    private String cabinClass;
    private List<BigInteger> rowRange = null;
    private List<BigInteger> overwingRowRange = null;
    private String cabinZoneCode;
    private String defaultSeatOccupation;
    private List<GridDetailDTO> gridDetails = null;

    public String getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    public List<BigInteger> getRowRange() {
        return rowRange;
    }

    public void setRowRange(List<BigInteger> rowRange) {
        this.rowRange = rowRange;
    }

    public List<BigInteger> getOverwingRowRange() {
        return overwingRowRange;
    }

    public void setOverwingRowRange(List<BigInteger> overwingRowRange) {
        this.overwingRowRange = overwingRowRange;
    }

    public String getCabinZoneCode() {
        return cabinZoneCode;
    }

    public void setCabinZoneCode(String cabinZoneCode) {
        this.cabinZoneCode = cabinZoneCode;
    }

    public String getDefaultSeatOccupation() {
        return defaultSeatOccupation;
    }

    public void setDefaultSeatOccupation(String defaultSeatOccupation) {
        this.defaultSeatOccupation = defaultSeatOccupation;
    }

    public List<GridDetailDTO> getGridDetails() {
        return gridDetails;
    }

    public void setGridDetails(List<GridDetailDTO> gridDetails) {
        this.gridDetails = gridDetails;
    }

}
