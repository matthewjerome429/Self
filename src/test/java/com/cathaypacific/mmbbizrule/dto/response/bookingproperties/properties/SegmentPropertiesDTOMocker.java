package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties;

import java.util.function.Consumer;

public class SegmentPropertiesDTOMocker {

	private SegmentPropertiesDTOMocker() {

	}

	public static interface SegmentPropertiesDTOPropMocker extends Consumer<SegmentPropertiesDTO> {

	}

	public static SegmentPropertiesDTO mock(SegmentPropertiesDTOPropMocker... propMockers) {
		SegmentPropertiesDTO obj = new SegmentPropertiesDTO();

		for (SegmentPropertiesDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static SegmentPropertiesDTOPropMocker segmentId(String segmentId) {
		return segmentPropDTO -> segmentPropDTO.setSegmentId(segmentId);
	}

	public static SegmentPropertiesDTOPropMocker marketedByCxKa(Boolean marketedByCxKa) {
		return segmentPropDTO -> segmentPropDTO.setMarketedByCxKa(marketedByCxKa);
	}

	public static SegmentPropertiesDTOPropMocker operatedByCxKa(Boolean operatedByCxKa) {
		return segmentPropDTO -> segmentPropDTO.setOperatedByCxKa(operatedByCxKa);
	}

	public static SegmentPropertiesDTOPropMocker before24H(Boolean before24H) {
		return segmentPropDTO -> segmentPropDTO.setBefore24H(before24H);
	}
	
	public static SegmentPropertiesDTOPropMocker waitlisted(Boolean waitlisted) {
		return segmentPropDTO -> segmentPropDTO.setWaitlisted(waitlisted);
	}

}
