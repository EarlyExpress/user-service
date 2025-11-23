package com.early_express.user_service.infrastructure.exception;

import com.early_express.user_service.global.presentation.exception.ErrorCode;
import com.early_express.user_service.global.presentation.exception.GlobalException;

public class LoginException extends GlobalException {

	public LoginException(ErrorCode errorCode) {
		super(errorCode);
	}

	public LoginException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}
