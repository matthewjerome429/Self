package com.cathaypacific.mmbbizrule.cxservice.dprebook.model.request;


import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class EncryptDeepLinkRequest extends BaseResponseDTO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String rloc;

    private String givenName;
    
    private String familyName;
    
    private String email;
    
    private String eticket;

    private String flow;
    
    private String languageLocale;
    
    private String paxId;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEticket() {
		return eticket;
	}

	public void setEticket(String eticket) {
		this.eticket = eticket;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getLanguageLocale() {
		return languageLocale;
	}

	public void setLanguageLocale(String languageLocale) {
		this.languageLocale = languageLocale;
	}

	public String getPaxId() {
		return paxId;
	}

	public void setPaxId(String paxId) {
		this.paxId = paxId;
	}
    
}
