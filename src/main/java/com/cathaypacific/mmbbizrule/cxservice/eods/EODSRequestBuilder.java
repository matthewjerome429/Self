package com.cathaypacific.mmbbizrule.cxservice.eods;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.cathaypacific.eodsconsumer.model.request.bookingsummary_v1.BookingSummaryRQ;
import com.cathaypacific.eodsconsumer.model.request.bookingsummary_v1.ObjectFactory;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.utils.DateUtil;

public class EODSRequestBuilder {
	   //Add the start / end time in the EODS WS request for EODS getBooking request with timestamp filtering
		private static long stdStartDiff= 4 * 24 * 60 * 60 * 1000;
		private static String stdEndDiff="2999-11-30T10:00:00";
		
		public BookingSummaryRQ buildRequest(String memberId) throws UnexpectedException{
			BookingSummaryRQ bookingRQ = null;
			SimpleDateFormat df = DateUtil.getDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			try {
				ObjectFactory objf= new ObjectFactory();
				bookingRQ = objf.createBookingSummaryRQ();
				bookingRQ.setCLSID(Integer.valueOf(memberId));
				bookingRQ.setStdstart(df.format(new Timestamp(System.currentTimeMillis()- stdStartDiff)));
				bookingRQ.setStdend(stdEndDiff);
			} catch (NumberFormatException e) {
				throw new UnexpectedException("Build EODS booking summary request body failed.",new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW),e);
			}
		return bookingRQ;
	}
}
