package com.cathaypacific.mmbbizrule.constant;

/**
 * Meal option enumerate to indicate meal option
 */
public enum MealOption {

	/**
	 * MA = Meal request available
	 */
	MA,
	/**
	 * NA = Meal request not available
	 */ 
	NA,
	/**
	 * BO = Beverage only and meal request is not available
	 */
	BO,
	/**
	 * BP = Beverage and pastries
	 */
	BP,
	/**
	 * SM = Snack box and meal request available
	 */
	SM;
	;
	
	private MealOption(){
	}
}
