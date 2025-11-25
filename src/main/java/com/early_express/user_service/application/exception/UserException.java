package com.early_express.user_service.application.exception;


import com.early_express.user_service.global.presentation.exception.ErrorCode;
import com.early_express.user_service.global.presentation.exception.GlobalException;

public class UserException extends GlobalException {
	public UserException(ErrorCode errorCode) {
		super(errorCode);
	}
}
