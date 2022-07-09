package com.cathaypacific.mmbbizrule.cxservice.rtfs.repository;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.constant.FlightStatusConstants;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

class ResponseDateDeserializer implements JsonDeserializer<Date> {
	@Override
	public Date deserialize(JsonElement paramJsonElement, Type paramType,
			JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException {
		if (paramJsonElement instanceof JsonPrimitive) {
			try {
				return DateUtil.getStrToDate(FlightStatusConstants.RESPONSE_DATE_FORMAT, paramJsonElement.getAsString());
			} catch (ParseException e) {
				throw new JsonParseException("Cannot parse date in flight status response.", e);
			}
		}
		return null;
	}
}
