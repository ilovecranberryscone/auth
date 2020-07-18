package com.auth.exception;

import com.auth.vo.ErrorCodes;

public class BizException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final ErrorCodes errcode;
	
	public BizException(ErrorCodes errorcode, String message){
		super(message);
		errcode = errorcode;
	}
	
	public ErrorCodes getErrorCode() {
		return errcode;
	}
	
}
