package com.cathaypacific.mmbbizrule.model.detail.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;

public class SegmentMocker {

	private SegmentMocker() {

	}

	public static interface SegmentPropMocker extends Consumer<Segment> {

	}

	public static Segment mock(SegmentPropMocker... propMockers) {
		Segment obj = new Segment();

		for (SegmentPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static SegmentPropMocker segmentID(String segmentID) {
		return segment -> segment.setSegmentID(segmentID);
	}
	
	public static SegmentPropMocker port(String originPort, String destPort) {
		return segment -> {
			segment.setOriginPort(originPort);
			segment.setDestPort(destPort);
		};
	}

	public static SegmentPropMocker operating(String operatingCompany, String operatingNumber) {
		return segment -> {
			segment.setOperateCompany(operatingCompany);
			segment.setOperateSegmentNumber(operatingNumber);
		};
	}

	public static SegmentPropMocker marketing(String marketingCompany, String marketingNumber) {
		return segment -> {
			segment.setMarketCompany(marketingCompany);
			segment.setMarketSegmentNumber(marketingNumber);
		};
	}

	public static SegmentPropMocker cabinClass(String cabinClass) {
		return segment -> segment.setCabinClass(cabinClass);
	}
	
	public static SegmentPropMocker departure(DepartureArrivalTime departureTime) {
		return segment -> segment.setDepartureTime(departureTime);
	}
	
	public static SegmentPropMocker arrival(DepartureArrivalTime arrivalTime) {
		return segment -> segment.setArrivalTime(arrivalTime);
	}

}
