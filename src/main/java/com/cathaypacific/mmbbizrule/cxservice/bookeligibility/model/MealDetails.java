package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class MealDetails {

    private String psCode;

    private String dishCode;

    private Boolean offMenu;

    private DishName dishName;

    private List<String> tag;

    public String getPsCode() {
        return psCode;
    }

    public void setPsCode(String psCode) {
        this.psCode = psCode;
    }

    public String getDishCode() {
        return dishCode;
    }

    public void setDishCode(String dishCode) {
        this.dishCode = dishCode;
    }

    public Boolean getOffMenu() {
        return offMenu;
    }

    public void setOffMenu(Boolean offMenu) {
        this.offMenu = offMenu;
    }

    public DishName getDishName() {
        return dishName;
    }

    public void setDishName(DishName dishName) {
        this.dishName = dishName;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public void addTag(String ta) {
        if (CollectionUtils.isEmpty(this.tag)) {
            this.tag = new ArrayList<>();
        }
        this.tag.add(ta);
    }
}
