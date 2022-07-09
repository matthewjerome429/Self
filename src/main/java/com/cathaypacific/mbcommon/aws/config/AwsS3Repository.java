package com.cathaypacific.mbcommon.aws.config;

import java.time.OffsetDateTime;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.cathaypacific.ng.filesystemfacade.IFileSystemFacade;
import com.cathaypacific.ng.filesystemfacade.LargeLogExporter;
import com.cathaypacific.ng.filesystemfacade.S3Facade;

@EnableConfigurationProperties(AwsS3LogerConfig.class)
public class AwsS3Repository {
	
	private AwsS3LogerConfig endpointConfig;
	
	public AwsS3Repository(AwsS3LogerConfig endpointConfig) {
		this.endpointConfig= endpointConfig;
	}
	
	public IFileSystemFacade fileSystemFacade() {
		return new S3Facade(endpointConfig.getAccessKeyID(),endpointConfig.getSecretAccessKey(),endpointConfig.getRegion());//new LocalFileSystemFacade()
	}
	
	public LargeLogExporter largeLogWriter(){
	    //create an exporter with 10M buffer and 4 workers
	    return new LargeLogExporter(endpointConfig.getMaxBufferSizeInByte(), endpointConfig.getWorkerNum(), fileSystemFacade(),
	            //file name builder
	            ()->{
	                //you may also add correlation id as part of file name by using ThreadContext
	                OffsetDateTime now = OffsetDateTime.now();
	                return endpointConfig.getBucket() + String.format("/%s/%s/%s/%s_%s_%s.log",
	                        now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(),
	                        now.getMinute(),System.currentTimeMillis());
	            });
	}
}
