package com.cathaypacific.mmbbizrule.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.BooleanUtils;

import com.cathaypacific.mmbbizrule.dto.response.baggage.BaggageProductDTO;
import com.cathaypacific.mmbbizrule.dto.response.baggage.IneligibleReasonEnum;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.PassengerPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.PassengerSegmentPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.SegmentPropertiesDTO;
import com.cathaypacific.mmbbizrule.model.journey.JourneySegment;
import com.cathaypacific.mmbbizrule.model.journey.JourneySummary;

public class BaggageIneligibleUtil {

	private BaggageIneligibleUtil() {
		
	}
	
	/**
	 * Set ineligible reason if no existing higher priority reason.
	 * 
	 * @param responseDTO
	 * @param ineligibleReason
	 */
	private static void setIneligibleReason(@NotNull BaggageProductDTO productDTO, @NotNull IneligibleReasonEnum ineligibleReason) {
		if (productDTO.getIneligibleReason() == null && productDTO.getProductType() == null) {
			productDTO.setIneligibleReason(ineligibleReason);
		}
	}
	
	private static void applyProductBySegmentId(@NotNull List<BaggageProductDTO> productDTOs, @NotNull String segmentId,
			@NotNull Consumer<BaggageProductDTO> action) {
	
		productDTOs.stream().filter(
				product -> product.getSegmentIds().contains(segmentId)
			).forEach(action);
	}
	
	private static void applyProductByPassengerId(@NotNull List<BaggageProductDTO> productDTOs, @NotNull String passengerId,
			@NotNull Consumer<BaggageProductDTO> action) {
	
		productDTOs.stream().filter(
				product -> passengerId.equals(product.getPassengerId())
			).forEach(action);
	}
	
	private static void applyProductByPassengerSegmentId(@NotNull List<BaggageProductDTO> productDTOs,
			@NotNull String passengerId, @NotNull String segmentId, @NotNull Consumer<BaggageProductDTO> action) {
	
		productDTOs.stream().filter(
				product -> passengerId.equals(product.getPassengerId()) && product.getSegmentIds().contains(segmentId)
			).forEach(action);
	}
	
	/**
	 * Check whether all flights in booking is operated by CX or KA.
	 * 
	 * @param productDTOs
	 * @param segmentPropertiesDTOs
	 */
	public static void checkBookingAllCxKaOperating(@NotNull List<BaggageProductDTO> productDTOs,
			@NotNull List<SegmentPropertiesDTO> segmentPropertiesDTOs) {

		boolean allCxKaOperating = segmentPropertiesDTOs.stream().allMatch(
				segment -> BooleanUtils.isTrue(segment.isOperatedByCxKa()));
		if (!allCxKaOperating) {
			Consumer<BaggageProductDTO> setIneligibleReason =
				product -> setIneligibleReason(product, IneligibleReasonEnum.NON_CX_KA_OP_FLIGHT);
			productDTOs.stream().forEach(setIneligibleReason);
		}
	}

	/**
	 * Check whether booking is staff booking.
	 * 
	 * @param productDTOs
	 * @param bookingPropertiesDTO
	 */
	public static void checkStaffBooking(@NotNull List<BaggageProductDTO> productDTOs,
			@NotNull BookingPropertiesDTO bookingPropertiesDTO) {

		if (BooleanUtils.isTrue(bookingPropertiesDTO.getStaffBooking())) {
			Consumer<BaggageProductDTO> setIneligibleReason =
				product -> setIneligibleReason(product, IneligibleReasonEnum.STAFF_BOOKING);
			productDTOs.stream().forEach(setIneligibleReason);
		}
	}

	/**
	 * Check whether booking is group booking.
	 * 
	 * @param productDTOs
	 * @param bookingPropertiesDTO
	 */
	public static void checkGroupBooking(@NotNull List<BaggageProductDTO> productDTOs,
			@NotNull BookingPropertiesDTO bookingPropertiesDTO) {

        if (BooleanUtils.isTrue(bookingPropertiesDTO.getGroupBooking())
                || BooleanUtils.isTrue(bookingPropertiesDTO.getMiceBooking())) {
			Consumer<BaggageProductDTO> setIneligibleReason =
				product -> setIneligibleReason(product, IneligibleReasonEnum.GROUP_BOOKING);
			productDTOs.stream().forEach(setIneligibleReason);
		}
	}

	/**
	 * Check whether passenger has waiver baggage.
	 * 
	 * @param productDTOs
	 * @param passengerPropertiesDTOs
	 * @param passengerSegmentPropertiesDTOs
	 */
	public static void checkPassengerWaiverAllowance(@NotNull List<BaggageProductDTO> productDTOs,
			@NotNull List<PassengerPropertiesDTO> passengerPropertiesDTOs,
			@NotNull List<PassengerSegmentPropertiesDTO> passengerSegmentPropertiesDTOs) {

		Consumer<BaggageProductDTO> setIneligibleReason =
				product -> setIneligibleReason(product, IneligibleReasonEnum.WAVIER_BAGGAGE);

		passengerPropertiesDTOs.stream().filter(
				passenger -> passengerSegmentPropertiesDTOs.stream().anyMatch(
						passengerSegment -> BooleanUtils.isTrue(passengerSegment.getHaveWaiverBaggage()))
			).map(PassengerPropertiesDTO::getPassengerId).forEach(
				passengerId -> applyProductByPassengerId(productDTOs, passengerId, setIneligibleReason)
			);
	}

	/**
	 * Check whether segment is waitlisted.
	 * 
	 * @param productDTOs
	 * @param segmentPropertiesDTOs
	 */
	public static void checkSegmentWaitlist(@NotNull List<BaggageProductDTO> productDTOs,
			@NotNull List<SegmentPropertiesDTO> segmentPropertiesDTOs) {

		Consumer<BaggageProductDTO> setIneligibleReason =
				product -> setIneligibleReason(product, IneligibleReasonEnum.WAITLIST);

		segmentPropertiesDTOs.stream().filter(
				segment -> BooleanUtils.isTrue(segment.isWaitlisted())
			).map(SegmentPropertiesDTO::getSegmentId).forEach(
				segmentId -> applyProductBySegmentId(productDTOs, segmentId, setIneligibleReason)
			);
	}

	/**
	 * Check whether passenger segment association has issued E-ticket.
	 * 
	 * @param productDTOs
	 * @param passengerSegmentPropertiesDTOs
	 */
	public static void checkETicketIssued(@NotNull List<BaggageProductDTO> productDTOs,
			@NotNull List<PassengerSegmentPropertiesDTO> passengerSegmentPropertiesDTOs) {

		Consumer<BaggageProductDTO> setIneligibleReason =
				product -> setIneligibleReason(product, IneligibleReasonEnum.TICKET_NOT_ISSUED);

		passengerSegmentPropertiesDTOs.stream().filter(
				passengerSegment -> BooleanUtils.isNotTrue(passengerSegment.isEticketIssued())
			).forEach(
				passengerSegment -> applyProductByPassengerSegmentId(productDTOs, passengerSegment.getPassengerId(), passengerSegment.getSegmentId(),
						setIneligibleReason)
			);
	}

	/**
	 * Check whether passenger segment association E-ticket is CX or KA ticket.
	 * 
	 * @param productDTOs
	 * @param passengerSegmentPropertiesDTOs
	 */
	public static void checkCxKaETicket(@NotNull List<BaggageProductDTO> productDTOs,
			@NotNull List<PassengerSegmentPropertiesDTO> passengerSegmentPropertiesDTOs) {

		Consumer<BaggageProductDTO> setIneligibleReason =
				product -> setIneligibleReason(product, IneligibleReasonEnum.NON_CX_KA_ETICKET);

		passengerSegmentPropertiesDTOs.stream().filter(
				passengerSegment -> BooleanUtils.isNotTrue(passengerSegment.isCxKaET())
			).forEach(
				passengerSegment -> applyProductByPassengerSegmentId(productDTOs, passengerSegment.getPassengerId(), passengerSegment.getSegmentId(),
						setIneligibleReason)
			);
	}

	/**
	 * Check whether segment is belong to journey that within 24 hours of departure time.
	 * 
	 * @param productDTOs
	 * @param segmentPropertiesDTOs
	 * @param journeyResponseDTO
	 */
	public static void checkCloseToDepartureSegment(@NotNull List<BaggageProductDTO> productDTOs,
			@NotNull List<SegmentPropertiesDTO> segmentPropertiesDTOs, @NotNull List<JourneySummary> journeySummaries) {
		
		// Segment ID to journey segment ID list mapping.
		Map<String, List<String>> segmentJourneySegmentsMap = new HashMap<>();
		for (JourneySummary journeyDTO : journeySummaries) {
			List<String> segmentIds = journeyDTO.getSegments().stream().map(
					JourneySegment::getSegmentId
				).collect(Collectors.toList());
			for (String segmentId : segmentIds) {
				segmentJourneySegmentsMap.put(segmentId, segmentIds);
			}
		}

		Consumer<BaggageProductDTO> setIneligibleReason =
				product -> setIneligibleReason(product, IneligibleReasonEnum.CLOSE_TO_DEPARTURE);
	
		segmentPropertiesDTOs.stream().filter(
				// Find out close to departure segment.
				segment -> BooleanUtils.isFalse(segment.isBefore24H())
			).map(SegmentPropertiesDTO::getSegmentId).collect(
				// Find out close to departure journey which contains segment found in previous step.
				Collectors.toMap(segmentId -> segmentId, segmentJourneySegmentsMap::get)
			).values().stream().flatMap(
				// Merge segment ID list of previous step to single stream.
				List::stream
			).distinct().forEach(
				// Apply ineligible reason to product DTO which contain segment in previous step.
				segmentId -> applyProductBySegmentId(productDTOs, segmentId, setIneligibleReason)
			);
	}
	
}
