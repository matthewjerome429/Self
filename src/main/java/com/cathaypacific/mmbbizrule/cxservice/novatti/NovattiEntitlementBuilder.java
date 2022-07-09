package com.cathaypacific.mmbbizrule.cxservice.novatti;

import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.novatticonsumer.model.entitlement.AuthenticationByAgent;
import com.cathaypacific.novatticonsumer.model.entitlement.ObjectFactory;
import com.cathaypacific.novatticonsumer.model.entitlement.RequestHeaderWithAgentAuthentication;
import com.cathaypacific.novatticonsumer.model.entitlement.SoapEntitlementInfoRequest;

public class NovattiEntitlementBuilder {
	
	private static final String STRING_1 = "1";
	private static final String EN = "en";
	private static final String VERSION_1_0 = "1.0";
	private static final String GMT = "GMT";
	private static final String VERSION_1_12 = "1.12";

	public SoapEntitlementInfoRequest buildEntitlementInfoRequest(String entitlementId) throws BusinessBaseException{
		ObjectFactory objectFactory = new ObjectFactory();
		SoapEntitlementInfoRequest soapEntitlementInfoRequest = objectFactory.createSoapEntitlementInfoRequest();
		RequestHeaderWithAgentAuthentication headerWithAgentAuthentication = objectFactory.createRequestHeaderWithAgentAuthentication();
		soapEntitlementInfoRequest.setHeader(headerWithAgentAuthentication);	
		
		AuthenticationByAgent authentication = objectFactory.createAuthenticationByAgent();
		authentication.setSessionToken(StringUtils.EMPTY);
		headerWithAgentAuthentication.setAuthentication(authentication);
		headerWithAgentAuthentication.setVersion(VERSION_1_12);
		headerWithAgentAuthentication.setAgentVersion(VERSION_1_0);
		headerWithAgentAuthentication.setAgentTransactionId(STRING_1);
		headerWithAgentAuthentication.setAgentTerminalId(STRING_1);
		headerWithAgentAuthentication.setAgentTimeStamp(getCurrentGMTDate());
		headerWithAgentAuthentication.setLanguageCode(EN);
		headerWithAgentAuthentication.setLanguageId(1);
		soapEntitlementInfoRequest.setEntitlementId(entitlementId);
		return soapEntitlementInfoRequest;
	}
	
	/**
	 * Get current GMTDate
	 * 
	 * @return XMLGregorianCalendar
	 * @throws BusinessBaseException 
	 */
	private XMLGregorianCalendar getCurrentGMTDate() throws BusinessBaseException {
		XMLGregorianCalendar xmlDateTime = null;
		try {
			xmlDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		} catch (DatatypeConfigurationException e) {
			throw new UnexpectedException("GET DatatypeFactory failed", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		xmlDateTime.setTimezone(0);
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(GMT));
		cal.set(Calendar.MILLISECOND, 0);
		xmlDateTime.setYear(cal.get(Calendar.YEAR));
		xmlDateTime.setMonth(cal.get(Calendar.MONTH) + 1);
		xmlDateTime.setDay(cal.get(Calendar.DAY_OF_MONTH));
		xmlDateTime.setHour(cal.get(Calendar.HOUR_OF_DAY));
		xmlDateTime.setMinute(cal.get(Calendar.MINUTE));
		xmlDateTime.setSecond(cal.get(Calendar.SECOND));
		return xmlDateTime;
	}
}
