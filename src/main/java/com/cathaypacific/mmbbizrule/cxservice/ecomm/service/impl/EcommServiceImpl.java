package com.cathaypacific.mmbbizrule.cxservice.ecomm.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mmbbizrule.config.EcommServiceConfig;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.RemoveProductsCacheResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.service.EcommService;
import com.cathaypacific.mmbbizrule.util.HttpClientService;
import com.cathaypacific.mmbbizrule.util.HttpResponse;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
@Service
public class EcommServiceImpl implements EcommService{

	private static final String PARAM_RLOC = "rloc";

	private static final String PARAM_POS = "pos";
	
	private static final Gson GSON = new Gson();
	
	@Autowired
	private EcommServiceConfig ecommServiceConfig;

	@Autowired
	private HttpClientService httpClientService;
	
	@Override
	public RemoveProductsCacheResponseDTO removeEcommCacheProducts(String rloc, String mmbToken) throws BusinessBaseException {
		
		String url = UriComponentsBuilder.fromHttpUrl(ecommServiceConfig.getEcommCacheProductsUrl())
				.queryParam(PARAM_RLOC, rloc).build().toString();
		
		HttpResponse<RemoveProductsCacheResponseDTO> ecommServiceProducts;

		try {
			Map<String, String> header = Maps.newHashMap();
			header.put("MMB-Token", mmbToken);
			Map<String, String> paramMappings = Maps.newHashMap();
			ecommServiceProducts = httpClientService.deleteForObject(RemoveProductsCacheResponseDTO.class, url, header, paramMappings);
		} catch (Exception e) {
			throw new ExpectedException("Cannot connect ecomm service." + e.getMessage(),
					new ErrorInfo(ErrorCodeEnum.ERR_AEP_CONNECTION), e);
		}

		return GSON.fromJson(ecommServiceProducts.getOriginalBody(), RemoveProductsCacheResponseDTO.class);
	}
	
	@Override
	public ProductsResponseDTO getEcommEligibleProducts(String rloc, String pos, String mmbToken) throws BusinessBaseException {
		
		
		String url = UriComponentsBuilder.fromHttpUrl(ecommServiceConfig.getEcommEligibleProductsUrl())
				.queryParam(PARAM_RLOC, rloc).queryParam(PARAM_POS, pos).build().toString();

		
		HttpResponse<ProductsResponseDTO> ecommServiceProducts;

		try {
			Map<String, String> header = Maps.newHashMap();
			header.put("MMB-Token", mmbToken);
			Map<String, String> paramMappings = Maps.newHashMap();
			ecommServiceProducts = httpClientService.getForObject(ProductsResponseDTO.class, url, header, paramMappings);
		} catch (Exception e) {
			throw new ExpectedException("Cannot connect ecomm service." + e.getMessage(),
					new ErrorInfo(ErrorCodeEnum.ERR_AEP_CONNECTION), e);
		}

		return GSON.fromJson(ecommServiceProducts.getOriginalBody(), ProductsResponseDTO.class);
	}
	
	@Override
	public ProductsResponseDTO getEcommBaggageProducts(String rloc, String pos, String mmbToken) throws BusinessBaseException {
		
		
		String url = UriComponentsBuilder.fromHttpUrl(ecommServiceConfig.getEcommBaggageProductsUrl())
				.queryParam(PARAM_RLOC, rloc).queryParam(PARAM_POS, pos).build().toString();

		
		HttpResponse<ProductsResponseDTO> ecommServiceProducts;

		try {
			Map<String, String> header = Maps.newHashMap();
			header.put("MMB-Token", mmbToken);
			Map<String, String> paramMappings = Maps.newHashMap();
			ecommServiceProducts = httpClientService.getForObject(ProductsResponseDTO.class, url, header, paramMappings);
		} catch (Exception e) {
			throw new ExpectedException("Cannot connect ecomm service." + e.getMessage(),
					new ErrorInfo(ErrorCodeEnum.ERR_AEP_CONNECTION), e);
		}

		return GSON.fromJson(ecommServiceProducts.getOriginalBody(), ProductsResponseDTO.class);
	}
	
	@Override
	public ProductsResponseDTO getEcommSeatProducts(String rloc, String pos, String mmbToken) throws BusinessBaseException {
		
		
		String url = UriComponentsBuilder.fromHttpUrl(ecommServiceConfig.getEcommSeatProductsUrl())
				.queryParam(PARAM_RLOC, rloc).queryParam(PARAM_POS, pos).build().toString();

		
		HttpResponse<ProductsResponseDTO> ecommServiceProducts;

		try {
			Map<String, String> header = Maps.newHashMap();
			header.put("MMB-Token", mmbToken);
			Map<String, String> paramMappings = Maps.newHashMap();
			ecommServiceProducts = httpClientService.getForObject(ProductsResponseDTO.class, url, header, paramMappings);
		} catch (Exception e) {
			throw new ExpectedException("Cannot connect ecomm service." + e.getMessage(),
					new ErrorInfo(ErrorCodeEnum.ERR_AEP_CONNECTION), e);
		}

		return GSON.fromJson(ecommServiceProducts.getOriginalBody(), ProductsResponseDTO.class);
	}

}
