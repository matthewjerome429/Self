package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OLCIConfig {

	@Value("${endpoint.path.journey.rloc}")
	private String journeyResponseByRLOC;
	
	@Value("${disability.code.s}")
	private String disabilityCodeS;
	
	@Value("${disability.code.k}")
	private String disabilityCodeK;
	
	@Value("${olci.checkin.window.lower}")
	private long checkInWindowLower;
	
	@Value("${olci.checkin.window.upper}")
	private long checkInWindowUpper;

	@Value("${endpoint.path.olci.nonmemberlogin}")
	private String nonMemberLogin;

	@Value("${endpoint.path.olci.cancelacceptance}")
	private String cancelCheckIn;
	
	@Value("${endpoint.path.olci.timezone}")
	private String timezoneURL;
	
	@Value("${endpoint.path.olci.sendBPFlight}")
	private String sendBPFlightURL;
	
	@Value("${endpoint.baggageBanner.displayTime}")
	private long baggageBannerDisplayTime;

	@Value("${endpoint.path.olci.sendBPEmail}")
	private String sendBPEmailURL;

	@Value("${endpoint.path.olci.sendBPSms}")
	private String sendBPSmsURL;

	@Value("${endpoint.path.olci.boardingPass}")
	private String boardingPassURL;


	@Value("${olci.priorityCheckIn.window}")
	private String priorityCheckInExamineTime;

	public String getJourneyResponseByRLOC() {
		return journeyResponseByRLOC;
	}

	public void setJourneyResponseByRLOC(String journeyResponseByRLOC) {
		this.journeyResponseByRLOC = journeyResponseByRLOC;
	}

	public String getDisabilityCodeS() {
		return disabilityCodeS;
	}

	public void setDisabilityCodeS(String disabilityCodeS) {
		this.disabilityCodeS = disabilityCodeS;
	}

	public String getDisabilityCodeK() {
		return disabilityCodeK;
	}

	public void setDisabilityCodeK(String disabilityCodeK) {
		this.disabilityCodeK = disabilityCodeK;
	}

	public long getCheckInWindowLower() {
		return checkInWindowLower;
	}

	public void setCheckInWindowLower(long checkInWindowLower) {
		this.checkInWindowLower = checkInWindowLower;
	}

	public long getCheckInWindowUpper() {
		return checkInWindowUpper;
	}

	public void setPriorityCheckInExamineTime(String priorityCheckInExamineTime) {this.priorityCheckInExamineTime = priorityCheckInExamineTime;}

	public long getPriorityCheckInExamineTime() { return Long.parseLong(priorityCheckInExamineTime); }

	public void setCheckInWindowUpper(long checkInWindowUpper) {
		this.checkInWindowUpper = checkInWindowUpper;
	}

	public String getNonMemberLogin() {
		return nonMemberLogin;
	}

	public void setNonMemberLogin(String nonMemberLogin) {
		this.nonMemberLogin = nonMemberLogin;
	}

	public String getCancelCheckIn() {
		return cancelCheckIn;
	}

	public void setCancelCheckIn(String cancelCheckIn) {
		this.cancelCheckIn = cancelCheckIn;
	}
	public String getTimezoneURL() {
		return timezoneURL;
	}

	public void setTimezoneURL(String timezoneURL) {
		this.timezoneURL = timezoneURL;
	}
	public long getBaggageBannerDisplayTime() {
		return baggageBannerDisplayTime;
	}

	public void setBaggageBannerDisplayTime(long baggageBannerDisplayTime) {
		this.baggageBannerDisplayTime = baggageBannerDisplayTime;
	}

	public String getSendBPFlightURL() {
		return sendBPFlightURL;
	}

	public void setSendBPFlightURL(String sendBPFlightURL) {
		this.sendBPFlightURL = sendBPFlightURL;
	}

	public String getSendBPEmailURL() {
		return sendBPEmailURL;
	}

	public void setSendBPEmailURL(String sendBPEmailURL) {
		this.sendBPEmailURL = sendBPEmailURL;
	}

	public String getSendBPSmsURL() {
		return sendBPSmsURL;
	}

	public void setSendBPSmsURL(String sendBPSmsURL) {
		this.sendBPSmsURL = sendBPSmsURL;
	}

	public String getBoardingPassURL() {
		return boardingPassURL;
	}

	public void setBoardingPassURL(String boardingPassURL) {
		this.boardingPassURL = boardingPassURL;
	}

}
