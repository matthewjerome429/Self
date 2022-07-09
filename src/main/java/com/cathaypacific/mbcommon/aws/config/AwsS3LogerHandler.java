package com.cathaypacific.mbcommon.aws.config;

import org.apache.commons.lang.StringUtils;

import com.cathaypacific.mbcommon.utils.LogUtils;
import com.cathaypacific.ng.filesystemfacade.LargeLogExporter;

public class AwsS3LogerHandler {

	private AwsS3LogerConfig endpointConfig;
	
	private LargeLogExporter exporter;
	
	public AwsS3LogerHandler(AwsS3LogerConfig endpointConfig, LargeLogExporter exporter){
		this.endpointConfig=endpointConfig;
		this.exporter=exporter;
	}
	
	public AwsS3LogerHandler(){
	// do nothing
	}
	
	/**
	 * store Logging to aws S3
	 * @param payload
	 * @param logType
	 * @return
	 */
	public String storeLoggingS3(String log, String logType) {
		if (!endpointConfig.getTechSwitch().equalsIgnoreCase("true")) {
			return log;
		}
		
		String fileName = LogUtils.genSoap2S3(exporter, log, endpointConfig.getBucket(), logType);
		if (StringUtils.isEmpty(fileName)) {
			return log;
		} else if (Boolean.parseBoolean(endpointConfig.getKeepKibanaLog())){
			return fileName + " | " + log;
		} else {
			return fileName;
		}
	}
}
