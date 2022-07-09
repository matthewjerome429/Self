package com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common;

import com.cathaypacific.mmbbizrule.constant.AEPConstants;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductTypeEnum {
	
	UNKNOWN("0", ""),

	SEAT_EXTRA_LEG_ROOM("1", AEPConstants.CATEGORY_SEAT),

	BAGGAGE_COMMON("2", AEPConstants.CATEGORY_BAGGAGE),

	BAGGAGE_USA("3", AEPConstants.CATEGORY_BAGGAGE),

	BAGGAGE_NZL("4", AEPConstants.CATEGORY_BAGGAGE),

	SEAT_ASR_REGULAR("7", AEPConstants.CATEGORY_SEAT),
	
	LOUNGE_BUSINESS("8", AEPConstants.CATEGORY_LOUNGE),
	
	LOUNGE_FIRST("9", AEPConstants.CATEGORY_LOUNGE),
	
	SEAT_ASR_WINDOW("10", AEPConstants.CATEGORY_SEAT),
	
	SEAT_ASR_AISLE("11", AEPConstants.CATEGORY_SEAT);

	private String productId;

	private String category;

	private ProductTypeEnum(String productId, String category) {
		this.productId = productId;
		this.category = category;
	}

	public String getProductId() {
		return productId;
	}

	public String getCategory() {
		return category;
	}
	
	/**
	 * Returns the enum constant corresponding to enum name.
	 * @param productTypeName
	 * @return
	 */
	@JsonCreator
	public static ProductTypeEnum valueOfString(String productTypeName) {
		for (ProductTypeEnum productType : values()) {
			if (productType.toString().equalsIgnoreCase(productTypeName)) {
				return productType;
			}
		}
		return null;
	}

	/**
	 * Returns the enum constant of the specified product ID.
	 *
	 * @param productId
	 * @return
	 */
	public static ProductTypeEnum valueOfProductId(String productId) {
		for (ProductTypeEnum productType : values()) {
			if (productType.getProductId().equals(productId)) {
				return productType;
			}
		}
		
		return UNKNOWN;
	}

}
