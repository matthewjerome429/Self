package com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1;

import java.util.List;

public class AEPProductsResponse extends AEPBaseEntity {

	private List<AEPProduct> products;

	public List<AEPProduct> getProducts() {
		return products;
	}

	public void setProducts(List<AEPProduct> products) {
		this.products = products;
	}

}
