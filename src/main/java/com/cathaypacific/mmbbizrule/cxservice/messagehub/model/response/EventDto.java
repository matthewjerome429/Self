package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.domain.MetaSBR;

public class EventDto {

	/**sample: 2019-10-01T09:35:55Z */
	private String createdAt;
	
	/**sample: 2019-10-01T09:35:55Z */
	private String lastUpdatedAt;
	
	/**sample: 2019-10-01T09:35:55Z */
	private String evtCreateTime;
	
	private String originEvtId;
	
	private String evtId;
	
	private String evtType;
	
	private String srcType;
	
	private MetaSBR meta;
	
	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(String lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public String getEvtCreateTime() {
		return evtCreateTime;
	}

	public void setEvtCreateTime(String evtCreateTime) {
		this.evtCreateTime = evtCreateTime;
	}

	public String getOriginEvtId() {
		return originEvtId;
	}

	public void setOriginEvtId(String originEvtId) {
		this.originEvtId = originEvtId;
	}

	public String getEvtId() {
		return evtId;
	}

	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}

	public String getEvtType() {
		return evtType;
	}

	public void setEvtType(String evtType) {
		this.evtType = evtType;
	}

	public String getSrcType() {
		return srcType;
	}

	public void setSrcType(String srcType) {
		this.srcType = srcType;
	}

	public MetaSBR getMeta() {
		return meta;
	}

	public void setMeta(MetaSBR meta) {
		this.meta = meta;
	}

}
