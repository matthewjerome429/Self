package com.cathaypacific.mmbbizrule.dto.response.baggage;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common.ProductTypeEnum;
import com.cathaypacific.mmbbizrule.util.CollectionUtil;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class BaggageProductDTOVerifier extends Verifier<BaggageProductDTO> {
	
	private BaggageProductDTOVerifier() {

	}

	public static interface BaggageProductDTOPropVerifier extends Consumer<BaggageProductDTO> {

	}

	public static BaggageProductDTOVerifier expect(BaggageProductDTOPropVerifier... propVerifiers) {
		BaggageProductDTOVerifier verifier = new BaggageProductDTOVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}
	
	public static BaggageProductDTOPropVerifier productType(ProductTypeEnum productType) {
		return product -> assertEquals(productType, product.getProductType());
	}
	
	public static BaggageProductDTOPropVerifier segmentIds(String... segmentIds) {
		return product -> CollectionUtil.isEqualList(Arrays.asList(segmentIds), product.getSegmentIds());
	}
	
	public static BaggageProductDTOPropVerifier passengerId(String passengerId) {
		return product -> assertEquals(passengerId, product.getPassengerId());
	}
	
	public static BaggageProductDTOPropVerifier unit(String unit) {
		return product -> assertEquals(unit, product.getUnit());
	}
	
	public static BaggageProductDTOPropVerifier ineligibleReason(IneligibleReasonEnum ineligibleReason) {
		return product -> assertEquals(ineligibleReason, product.getIneligibleReason());
	}
	
	@SafeVarargs
	public static BaggageProductDTOPropVerifier productValues(Verifier<BaggageProductValueDTO>... productValueVerifiers) {
		return product -> verifyList(productValueVerifiers, product.getProductValues());
	}

}
