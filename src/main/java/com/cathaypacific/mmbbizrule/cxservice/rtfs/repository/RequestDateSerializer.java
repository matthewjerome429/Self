package com.cathaypacific.mmbbizrule.cxservice.rtfs.repository;

import java.lang.reflect.Type;
import java.util.Date;

import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.constant.FlightStatusConstants;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class RequestDateSerializer implements JsonSerializer<Date> {
	@Override
	public JsonElement serialize(Date paramT, Type paramType, 
			JsonSerializationContext paramJsonSerializationContext) {
		if (paramT == null) {
			return JsonNull.INSTANCE;
		}

		String date = DateUtil.getDate2Str(FlightStatusConstants.REQUEST_TRAVEL_DATE_FORMAT, paramT);
		return new JsonPrimitive(date);
	}
}
