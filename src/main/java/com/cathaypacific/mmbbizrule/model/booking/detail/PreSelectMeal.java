package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.DishName;

public class PreSelectMeal {
	
	private String type;
	
	private boolean offMenu;

	private DishName dishName;
	
	private List<String> tag;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOffMenu() {
        return offMenu;
    }

    public void setOffMenu(boolean offMenu) {
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
	
	
}
