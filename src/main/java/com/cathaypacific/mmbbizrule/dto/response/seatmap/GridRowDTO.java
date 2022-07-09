
package com.cathaypacific.mmbbizrule.dto.response.seatmap;

import java.util.List;

public class GridRowDTO {

    private int rowNum;
    private List<String> rowCharacteristics;
    private List<CabinFacilityDTO> cabinFacilities = null;
    private List<GridDTO> grids = null;

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public List<String> getRowCharacteristics() {
        return rowCharacteristics;
    }

    public void setRowCharacteristic(List<String> rowCharacteristics) {
        this.rowCharacteristics = rowCharacteristics;
    }

    public List<CabinFacilityDTO> getCabinFacilities() {
        return cabinFacilities;
    }

    public void setCabinFacilities(List<CabinFacilityDTO> cabinFacilities) {
        this.cabinFacilities = cabinFacilities;
    }

    public List<GridDTO> getGrids() {
        return grids;
    }

    public void setGrids(List<GridDTO> grids) {
        this.grids = grids;
    }

}
