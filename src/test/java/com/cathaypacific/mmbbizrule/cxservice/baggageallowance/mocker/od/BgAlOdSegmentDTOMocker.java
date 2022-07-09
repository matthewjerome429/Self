package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.od;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.common.od.BgAlOdSegmentDTO;

public class BgAlOdSegmentDTOMocker {

	private BgAlOdSegmentDTOMocker() {

	}

	public static interface BgAlOdSegmentDTOPropMocker extends Consumer<BgAlOdSegmentDTO> {

	}

	public static BgAlOdSegmentDTO mock(BgAlOdSegmentDTOPropMocker... propMockers) {
		BgAlOdSegmentDTO obj = new BgAlOdSegmentDTO();

		for (BgAlOdSegmentDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static BgAlOdSegmentDTOPropMocker point(String boardPoint, String offPoint) {
		return segmentDTO -> {
			segmentDTO.setBoardPoint(boardPoint);
			segmentDTO.setOffPoint(offPoint);
		};
	}

	public static BgAlOdSegmentDTOPropMocker operatingCarrier(String operatingCarrier) {
		return segmentDTO -> segmentDTO.setOperatingCarrier(operatingCarrier);
	}

	public static BgAlOdSegmentDTOPropMocker marketingCarrier(String marketingCarrier) {
		return segmentDTO -> segmentDTO.setMarketingCarrier(marketingCarrier);
	}

	public static BgAlOdSegmentDTOPropMocker cabinClass(String cabinClass) {
		return segmentDTO -> segmentDTO.setCabinClass(cabinClass);
	}

	public static BgAlOdSegmentDTOPropMocker departure(String date, String time) {
		return segmentDTO -> {
			segmentDTO.setDepartureDate(date);
			segmentDTO.setDepartureTime(time);
		};
	}

	public static BgAlOdSegmentDTOPropMocker arrival(String date, String time) {
		return segmentDTO -> {
			segmentDTO.setArrivalDate(date);
			segmentDTO.setArrivalTime(time);
		};
	}

}
