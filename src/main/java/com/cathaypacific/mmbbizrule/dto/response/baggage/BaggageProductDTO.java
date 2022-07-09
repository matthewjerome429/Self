package com.cathaypacific.mmbbizrule.dto.response.baggage;

import java.io.Serializable;
import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common.ProductTypeEnum;

import io.swagger.annotations.ApiModelProperty;

public class BaggageProductDTO implements Serializable {
	
	private static final long serialVersionUID = 5712151752158807811L;

	@ApiModelProperty(value = "Product type corresponding to product ID", required = true, dataType = "string")
	private ProductTypeEnum productType;
	
	@ApiModelProperty(value = "Segment ID list of segments of journey", required = true, example = "[1, 2]")
	private List<String> segmentIds;
	
	@ApiModelProperty(value = "Passenger ID", required = true, example = "1")
	private String passengerId;
	
	@ApiModelProperty(value = "Unit of baggage", required = false, example = "PC", allowableValues = "PC,K")
	private String unit;
	
	@ApiModelProperty(value = "Ineligible reason of no baggage product", required = false, dataType = "string")
	private IneligibleReasonEnum ineligibleReason;
	
	@ApiModelProperty(value = "Purchasable items of this product", required = true)
	private List<BaggageProductValueDTO> productValues;

	public ProductTypeEnum getProductType() {
		return productType;
	}

	public void setProductType(ProductTypeEnum productType) {
		this.productType = productType;
	}

	public List<String> getSegmentIds() {
		return segmentIds;
	}

	public void setSegmentIds(List<String> segmentIds) {
		this.segmentIds = segmentIds;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public IneligibleReasonEnum getIneligibleReason() {
		return ineligibleReason;
	}

	public void setIneligibleReason(IneligibleReasonEnum ineligibleReason) {
		this.ineligibleReason = ineligibleReason;
	}

	public List<BaggageProductValueDTO> getProductValues() {
		return productValues;
	}

	public void setProductValues(List<BaggageProductValueDTO> productValues) {
		this.productValues = productValues;
	}
	
}
