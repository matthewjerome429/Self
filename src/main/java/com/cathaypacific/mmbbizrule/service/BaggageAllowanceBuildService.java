package com.cathaypacific.mmbbizrule.service;

import java.util.List;
import java.util.concurrent.Future;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProduct;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProductsResponse;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.btu.BtuResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdBookingResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;

public interface BaggageAllowanceBuildService {

	public BaggageAllowanceInfo retrieveBaggageAllowanceInfo(Booking booking, AEPProductsResponse aepProductsResponse)
			throws BusinessBaseException;

	public Future<BaggageAllowanceInfo> asyncRetrieveBaggageAllowanceInfo(Booking booking,
			Future<AEPProductsResponse> futureAEPProductsResponse) throws BusinessBaseException;

	public BaggageAllowanceInfo retrieveBaggageAllowanceInfo(Booking booking, ProductsResponseDTO ecommProductsResponse)
			throws BusinessBaseException;

	public void populateBaggageAllowance(Booking booking, BaggageAllowanceInfo baggageAllowanceInfo);

	public void populateBaggageAllowance(Booking booking, Future<BaggageAllowanceInfo> futureBaggageAllowanceInfo)
			throws BusinessBaseException;
	
	public void setBaggageAllowanceCompleted(Booking booking);

	public static class BaggageAllowanceInfo {

		private List<AEPProduct> baggageAEPProducts;
		
		private List<ProductDTO> baggageEcommProducts;

		private List<BtuResponseDTO> btuDTOs;
		
		private List<BgAlOdBookingResponseDTO> bookingDTOs; 

		public List<AEPProduct> getBaggageAEPProducts() {
			return baggageAEPProducts;
		}

		public void setBaggageAEPProducts(List<AEPProduct> baggageAEPProducts) {
			this.baggageAEPProducts = baggageAEPProducts;
		}

		public List<ProductDTO> getBaggageEcommProducts() {
			return baggageEcommProducts;
		}

		public void setBaggageEcommProducts(List<ProductDTO> baggageEcommProducts) {
			this.baggageEcommProducts = baggageEcommProducts;
		}

		public List<BtuResponseDTO> getBtuDTOs() {
			return btuDTOs;
		}

		public void setBtuDTOs(List<BtuResponseDTO> btuDTOs) {
			this.btuDTOs = btuDTOs;
		}

		public List<BgAlOdBookingResponseDTO> getBookingDTOs() {
			return bookingDTOs;
		}

		public void setBookingDTOs(List<BgAlOdBookingResponseDTO> bookingDTOs) {
			this.bookingDTOs = bookingDTOs;
		}

	}

}
