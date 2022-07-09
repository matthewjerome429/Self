package com.cathaypacific.mbcommon.exception;

public abstract class BusinessBaseException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6691126338026510828L;
	
	BusinessBaseException(String message){
		super(message);
	}
	BusinessBaseException(String message,Throwable cause ){
		super(message,cause);
	}
}
