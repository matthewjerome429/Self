package com.cathaypacific.mmbbizrule.dto.request.email;

import java.io.Serializable;

/**
 * Transfer the FFPInfoDTO Info fields for 15below
 *
 * @author fangfang.zhang
 */
public class FFPInfoDTO implements Serializable{
	
    /**
	 * generated serial version ID.
	 */
	private static final long serialVersionUID = 7836161418204962056L;
	
	/** the memberTier. */
	private String memberTier;
	
	/** the memberNo. */
    private String memberNo;
    
	public String getMemberTier() {
        return memberTier;
    }

    public void setMemberTier(String memberTier) {
        this.memberTier = memberTier;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    @Override
    public String toString() {
        return "FFPInfoDTO [memberTier=" + memberTier + ", memberNo=" + memberNo + "]";
    }


}
