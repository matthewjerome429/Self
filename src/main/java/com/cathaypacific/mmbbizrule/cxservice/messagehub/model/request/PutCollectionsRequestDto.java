package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PutCollectionsRequestDto implements Serializable {


	private static final long serialVersionUID = -532754716485582972L;

	@NotEmpty
	private String jobId;

	List<PutMessageRequestDto> collections;

	public List<PutMessageRequestDto> getCollections() {
		return collections;
	}

	public void setCollections(List<PutMessageRequestDto> collections) {
		this.collections = collections;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
}
