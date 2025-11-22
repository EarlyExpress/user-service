package com.early_express.user_service.presentation.exception;

import com.early_express.user_service.global.presentation.exception.ErrorCode;
import com.early_express.user_service.global.presentation.exception.GlobalException;

public class SignupException extends GlobalException {
	public SignupException(ErrorCode errorCode) {
		super(errorCode);
	}
}
