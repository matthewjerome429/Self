package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class RtfsLegSummaryDTOV2 implements Serializable {

	private static final long serialVersionUID = 477546065982331856L;

	@ApiModelProperty(value = "The sequence ID.", required = true)
	private Integer sequenceId;

	@ApiModelProperty(value = "The origin.", required = true)
	private String originPort;

	@ApiModelProperty(value = "The destination.", required = true)
	private String destPort;

	@ApiModelProperty(value = "The scenario ID.", required = true)
	private Integer scenarioId;

	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public String getOriginPort() {
		return originPort;
	}

	public void setOriginPort(String originPort) {
		this.originPort = originPort;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public Integer getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(Integer scenarioId) {
		this.scenarioId = scenarioId;
	}

}
