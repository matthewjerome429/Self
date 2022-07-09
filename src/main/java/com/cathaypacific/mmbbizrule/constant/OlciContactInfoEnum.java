package com.cathaypacific.mmbbizrule.constant;

public enum OlciContactInfoEnum {

    /**
     * Enum in olci
     * CUSTOMER("CU", "CPR"), PRODUCT("PROD", "CPR"), LASTFLOWN("FLOWN", "CPR"), 
     * RU("RU", "PROFILE"), FQTV("FQTV", "PROFILE"), COMP("COMP", "PROFILE")
     */
    CPR("CPR", "CT"), PROFILE("PROFILE", "PF");

	/**
	 * Means which source it get from in OLCI
	 */
    private String cprSuperSource;
    /**
     * Mean which contact info type in MMB
     */
    private String mmbType;

    private OlciContactInfoEnum(String cprSuperSource, String mmbType) {
        this.cprSuperSource = cprSuperSource;
        this.mmbType = mmbType;
    }

    public String getCprSuperSource() {
        return cprSuperSource;
    }

    public String getMmbType() {
        return mmbType;
    }

    public static String retireveMmbType(String cprSuperSource) {
        for (OlciContactInfoEnum sourceEnum : values()) {
            if (sourceEnum.getCprSuperSource().equals(cprSuperSource)) {
                return sourceEnum.getMmbType();
            }
        }
        return null;
    }
}
