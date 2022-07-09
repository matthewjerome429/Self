package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.util;

import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorTypeEnum;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.BaggageAllowanceResponseBaseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.ErrorInfoDTO;
import com.google.gson.Gson;

public class BaggageAllowanceErrorUtil {

	private static final String BAGGAGE_ALLOWANCE_FIELD_NAME = "BaggageAllowanceAPI";
	
	public static final String BAGGAGE_ALLOWANCE_ERR_PREFIX = "BAG_ERR";
	
	public static final String BAGGAGE_ALLOWANCE_ERR_UNKNOWN = "BAG_ERR_000";
	
	public static final String BAGGAGE_ALLOWANCE_ERR_CONNECTION = "BAG_ERR_CONN";
	
	private static final Gson GSON = new Gson();

	private BaggageAllowanceErrorUtil() {

	}

	/**
	 * Parse error info of baggage allowance API, set error code to MMB error info.
	 * 
	 * @param ex
	 * @return
	 */
	public static ErrorInfo parseError(Exception ex) {

		ErrorInfo mmbErrorInfo = null;
		if (ex instanceof HttpClientErrorException) {
			HttpClientErrorException httpClientErrorException = (HttpClientErrorException) ex;
			String responseBody = httpClientErrorException.getResponseBodyAsString();

			try {
				BaggageAllowanceResponseBaseDTO responseDTO =
						GSON.fromJson(responseBody, BaggageAllowanceResponseBaseDTO.class);
				ErrorInfoDTO errorInfoDTO = responseDTO.getError();
				mmbErrorInfo = new ErrorInfo();

				String baggageAllowanceErrorCode = errorInfoDTO.getCode();
				if (!StringUtils.isEmpty(baggageAllowanceErrorCode)
						&& baggageAllowanceErrorCode.startsWith(BAGGAGE_ALLOWANCE_ERR_PREFIX)) {
					mmbErrorInfo.setErrorCode(errorInfoDTO.getCode());
				} else {
					mmbErrorInfo.setErrorCode(BAGGAGE_ALLOWANCE_ERR_UNKNOWN);
				}
				
				mmbErrorInfo.setFieldName(BAGGAGE_ALLOWANCE_FIELD_NAME);
				mmbErrorInfo.setType(ErrorTypeEnum.BUSERROR);
			} catch (Exception e) {
				mmbErrorInfo = new ErrorInfo();
				mmbErrorInfo.setErrorCode(BAGGAGE_ALLOWANCE_ERR_UNKNOWN);
				mmbErrorInfo.setFieldName(BAGGAGE_ALLOWANCE_FIELD_NAME);
				mmbErrorInfo.setType(ErrorTypeEnum.BUSERROR);
			}
		} else {
			mmbErrorInfo = new ErrorInfo();
			mmbErrorInfo.setErrorCode(BAGGAGE_ALLOWANCE_ERR_CONNECTION);
			mmbErrorInfo.setFieldName(BAGGAGE_ALLOWANCE_FIELD_NAME);
			mmbErrorInfo.setType(ErrorTypeEnum.SYSERROR);
		}

		return mmbErrorInfo;
	}

}
