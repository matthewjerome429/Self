package com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product;

import java.io.Serializable;
import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common.PassengerInfoDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common.PriceDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common.ProductInfoDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common.ProductTypeEnum;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common.SegmentInfoDTO;

import io.swagger.annotations.ApiModelProperty;

public class ProductDTO implements SegmentInfoDTO, PassengerInfoDTO, ProductInfoDTO, Serializable {

	private static final long serialVersionUID = 7388375427338121411L;

	@ApiModelProperty(value = "Product type corresponding to product ID", required = true, dataType = "string")
	private ProductTypeEnum productType;

	@ApiModelProperty(value = "Segment ID list. For seat and lounge, only one segment. For baggage, segments of journey",
			required = true, example = "[1, 2]")
	private List<String> segmentIds;

	@ApiModelProperty(value = "Passenger ID", required = true, example = "1")
	private String passengerId;
	
	@ApiModelProperty(value = "Unit of baggage", required = false, example = "PC")
	private String unit;
	
	@ApiModelProperty(value = "Flag indicating product doesn't support online purchase", required = true)
	private boolean sellOnOffline;

	@ApiModelProperty(value = "Purchasable items of this product, used for baggage", required = false)
	private List<ProductValueDTO> productValues;
	
	@ApiModelProperty(value = "Purchasable item price of this product, used for seat, lounge", required = false)
	private PriceDTO price;

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

	public boolean isSellOnOffline() {
		return sellOnOffline;
	}

	public void setSellOnOffline(boolean sellOnOffline) {
		this.sellOnOffline = sellOnOffline;
	}

	public List<ProductValueDTO> getProductValues() {
		return productValues;
	}

	public void setProductValues(List<ProductValueDTO> productValues) {
		this.productValues = productValues;
	}

	public PriceDTO getPrice() {
		return price;
	}

	public void setPrice(PriceDTO productPrice) {
		this.price = productPrice;
	}

}
