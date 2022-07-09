package com.cathaypacific.mmbbizrule.cxservice.dpatcirebook.model.request;


import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class RescheduleEligibleRequest extends BaseResponseDTO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String rloc;

    private String lastName;
    
    private boolean needBody;
    
    private String pnrReply;
    
    private String sessionID;
    
    private boolean maintainSession;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isNeedBody() {
		return needBody;
	}

	public void setNeedBody(boolean needBody) {
		this.needBody = needBody;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public boolean isMaintainSession() {
		return maintainSession;
	}

	public void setMaintainSession(boolean maintainSession) {
		this.maintainSession = maintainSession;
	}

	public String getPnrReply() {
		return pnrReply;
	}

	public void setPnrReply(String pnrReply) {
		this.pnrReply = pnrReply;
	}
    
    
}
