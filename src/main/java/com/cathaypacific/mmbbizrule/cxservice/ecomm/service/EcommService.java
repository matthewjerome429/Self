package com.cathaypacific.mmbbizrule.cxservice.ecomm.service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.RemoveProductsCacheResponseDTO;

public interface EcommService {
	
	public RemoveProductsCacheResponseDTO removeEcommCacheProducts(String rloc, String mmbToken) throws BusinessBaseException;
	
	public ProductsResponseDTO getEcommEligibleProducts(String bookingRef, String pos, String mmbToken) throws BusinessBaseException;
	
	public ProductsResponseDTO getEcommBaggageProducts(String bookingRef, String pos, String mmbToken) throws BusinessBaseException;
	
	public ProductsResponseDTO getEcommSeatProducts(String bookingRef, String pos, String mmbToken) throws BusinessBaseException;
	
}
