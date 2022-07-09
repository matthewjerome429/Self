
package com.cathaypacific.mmbbizrule.dto.response.seatmap;

import java.util.List;

public class CabinDTO {

    private List<CabinFacilityDTO> cabinFacilities;
    
    private CompartmentDetailDTO compartmentDetail;

	public List<CabinFacilityDTO> getCabinFacilities() {
		return cabinFacilities;
	}

	public void setCabinFacilities(List<CabinFacilityDTO> cabinFacilities) {
		this.cabinFacilities = cabinFacilities;
	}

	public CompartmentDetailDTO getCompartmentDetail() {
		return compartmentDetail;
	}

	public void setCompartmentDetail(CompartmentDetailDTO compartmentDetail) {
		this.compartmentDetail = compartmentDetail;
	}
}
