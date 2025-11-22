package com.early_express.user_service.infrastructure.exception;

import com.early_express.user_service.global.presentation.exception.ErrorCode;
import com.early_express.user_service.global.presentation.exception.GlobalException;

public class KeycloakException extends GlobalException {
	public KeycloakException(ErrorCode errorCode) {
		super(errorCode);
	}

	public KeycloakException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}
