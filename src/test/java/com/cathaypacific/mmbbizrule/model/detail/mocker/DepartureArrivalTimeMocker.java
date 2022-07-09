package com.cathaypacific.mmbbizrule.model.detail.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;

public class DepartureArrivalTimeMocker {

	private DepartureArrivalTimeMocker() {

	}

	public static interface DepartureArrivalTimePropMocker extends Consumer<DepartureArrivalTime> {

	}

	public static DepartureArrivalTime mock(DepartureArrivalTimePropMocker... propMockers) {
		DepartureArrivalTime obj = new DepartureArrivalTime();

		for (DepartureArrivalTimePropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static DepartureArrivalTimePropMocker timeZoneOffset(String timeZoneOffset) {
		return time -> time.setTimeZoneOffset(timeZoneOffset);
	}
	
	public static DepartureArrivalTimePropMocker pnrTime(String pnrTime) {
		return time -> time.setPnrTime(pnrTime);
	}
	
}
