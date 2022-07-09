package com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1;

import java.util.List;

public class AEPProduct {

	private String productId;

	private String type;

	private String displayName;

	private boolean sellOnOffline;

	private String characteristics;

	private AEPBaggageInfo baggageInfo;

	private List<AEPAirProduct> airProduct;

	private AEPPriceItem totalValue;

	private List<AEPProductValue> productValues;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isSellOnOffline() {
		return sellOnOffline;
	}

	public void setSellOnOffline(boolean sellOnOffline) {
		this.sellOnOffline = sellOnOffline;
	}

	public String getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(String characteristics) {
		this.characteristics = characteristics;
	}

	public AEPBaggageInfo getBaggageInfo() {
		return baggageInfo;
	}

	public void setBaggageInfo(AEPBaggageInfo baggageInfo) {
		this.baggageInfo = baggageInfo;
	}

	public List<AEPAirProduct> getAirProduct() {
		return airProduct;
	}

	public void setAirProduct(List<AEPAirProduct> airProduct) {
		this.airProduct = airProduct;
	}

	public AEPPriceItem getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(AEPPriceItem totalValue) {
		this.totalValue = totalValue;
	}

	public List<AEPProductValue> getProductValues() {
		return productValues;
	}

	public void setProductValues(List<AEPProductValue> productValues) {
		this.productValues = productValues;
	}

}
