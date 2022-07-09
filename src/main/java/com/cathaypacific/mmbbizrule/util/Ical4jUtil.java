package com.cathaypacific.mmbbizrule.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.validate.ValidationException;

public class Ical4jUtil {
	
	private Ical4jUtil() {

	}
	
	/**
	 * Add property VERSION_2_0, GREGORIAN, ProdId
	 * 
	 * @param calendar
	 */
	public static void setDefaultProperties(Calendar cal) {
		PropertyList<Property> calProperties = cal.getProperties();
		calProperties.add(Version.VERSION_2_0);
		calProperties.add(CalScale.GREGORIAN);
		calProperties.add(new ProdId());
	}
	
	/**
	 * Get RandomUid using RandomUidGenerator
	 * 
	 * @return Uid
	 */
	public static Uid getRandomUid() {
		return new Uid(new RandomUidGenerator().generateUid().getValue());
	}

	/**
	 * Add events into calendar
	 * 
	 * @param cal
	 * @param events
	 */
	public static void setEvents(Calendar cal, List<VEvent> events) {
		cal.getComponents().addAll(events);
	}

	/**
	 * Output the calendar by outPutStream
	 * 
	 * @param cal
	 * @param out
	 * @throws ValidationException
	 * @throws IOException
	 */
	public static void output(Calendar cal, OutputStream out) throws ValidationException, IOException {
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(cal, out);
	}
	
}
