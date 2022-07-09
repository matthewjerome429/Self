package com.cathaypacific.mmbbizrule.model.booking.detail;

public class RtfsLegSummary {

	/** The sequence ID. */
	private Integer sequenceId;

	/** The origin. */
	private String originPort;

	/** The destination. */
	private String destPort;

	/** The scenario ID. */
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
