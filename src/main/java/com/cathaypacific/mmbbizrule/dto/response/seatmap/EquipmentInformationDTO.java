
package com.cathaypacific.mmbbizrule.dto.response.seatmap;

import java.util.List;

public class EquipmentInformationDTO {

    private List<CabinClassDetailDTO> cabinClassDetails = null;

    public List<CabinClassDetailDTO> getCabinClassDetails() {
        return cabinClassDetails;
    }

    public void setCabinClassDetails(List<CabinClassDetailDTO> cabinClassDetails) {
        this.cabinClassDetails = cabinClassDetails;
    }

}
