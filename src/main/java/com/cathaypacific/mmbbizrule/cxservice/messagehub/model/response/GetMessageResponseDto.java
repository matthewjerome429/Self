package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;


import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class GetMessageResponseDto {

	private String jobId;
	
	private List<CollectionDto> collections;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public List<CollectionDto> getCollections() {
		return collections;
	}

	public void setCollections(List<CollectionDto> collections) {
		this.collections = collections;
	}
	
	public List<CollectionDto> findCollections() {
		if(CollectionUtils.isEmpty(this.collections)) {
			this.collections =  new ArrayList<> ();
		}
		return this.collections;
	}
}
