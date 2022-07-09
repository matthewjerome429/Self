package com.cathaypacific.mbcommon.aws.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="aws.s3.log")
public class AwsS3LogerConfig {
	
	
	//-----S3 start------
	
	private String techSwitch;
	
	private String keepKibanaLog;
	
	private String region;
	
	private String bucket;

	private String accessKeyID;

	private String secretAccessKey;

	private long maxBufferSizeInByte = 1024;
	
	private int workerNum;
	
	public String getTechSwitch() {
		return techSwitch;
	}

	public void setTechSwitch(String techSwitch) {
		this.techSwitch = techSwitch;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getAccessKeyID() {
		return accessKeyID;
	}

	public void setAccessKeyID(String accessKeyID) {
		this.accessKeyID = accessKeyID;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}

	public long getMaxBufferSizeInByte() {
		return maxBufferSizeInByte;
	}

	public void setMaxBufferSizeInByte(long maxBufferSizeInByte) {
		this.maxBufferSizeInByte = maxBufferSizeInByte;
	}

	public int getWorkerNum() {
		return workerNum;
	}

	public void setWorkerNum(int workerNum) {
		this.workerNum = workerNum;
	}

	public String getKeepKibanaLog() {
		return keepKibanaLog;
	}

	public void setKeepKibanaLog(String keepKibanaLog) {
		this.keepKibanaLog = keepKibanaLog;
	}

	//-----S3 end------
	
	 
}
