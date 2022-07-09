package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.io.Serializable;

public class DishName implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2179767640894402837L;

    // Dish Name in English
    private String en;
    // Dish Name in Traditional Chinese
    private String zh;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

}
