package com.early_express.user_service.infrastructure.exception;

import com.early_express.user_service.global.presentation.exception.ErrorCode;
import com.early_express.user_service.global.presentation.exception.GlobalException;

public class RefreshException extends GlobalException {

	public RefreshException(ErrorCode errorCode) {
		super(errorCode);
	}

	public RefreshException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}
