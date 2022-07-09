package com.cathaypacific.mbcommon.utils;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.ng.filesystemfacade.LargeLogExporter;
public class LogUtils {

	private static final LogAgent LOGGER = LogAgent.getLogAgent(LogUtils.class);
	/**
	 * Empty constructor.
	 */
	private LogUtils() {
		// do nothing
	}

	/**
	 * generate S3 fileNameb by tokenId and path
	 * 
	 * @param tokenId
	 * @param path
	 * @return
	 */
	public static String genSoapFileName(String tokenId, String path, String type) {
		StringBuffer bucket = new StringBuffer("");
		bucket.append(path);
		String uuId = "";
		if (StringUtils.isBlank(tokenId)) {
			tokenId = "nullToken";
		}
		if (StringUtils.isBlank(uuId)) {
			uuId = UUID.randomUUID().toString();
		}
		OffsetDateTime now = OffsetDateTime.now();
		String dest = String.format(bucket.toString() + "/1A/%s/%s/%s/%s/" + tokenId + "/", now.getYear(),
				now.getMonthValue(), now.getDayOfMonth(), now.getHour());
		return dest + type + "-" + uuId + ".log.gz";
	}


	/**
	 * soap log to AWS S3
	 * 
	 * @param exporter
	 * @param request
	 * @param path
	 * @return
	 */
	public static String genSoap2S3(LargeLogExporter exporter, String request, String path, String type) {
		String fileName = "";
		try {
			fileName = LogUtils.genSoapFileName(MMBUtil.getCurrentMMBToken(), path, type);
			exporter.export(fileName, request);
		} catch (Exception e) {
			fileName = null;
			LOGGER.warn("Cannot store log to aws s3.", e);
		}

		return fileName;
	}
}