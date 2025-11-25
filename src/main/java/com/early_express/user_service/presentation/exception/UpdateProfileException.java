package com.early_express.user_service.presentation.exception;

import com.early_express.user_service.global.presentation.exception.ErrorCode;
import com.early_express.user_service.global.presentation.exception.GlobalException;

public class UpdateProfileException extends GlobalException {
	public UpdateProfileException(ErrorCode errorCode) {
		super(errorCode);
	}
}
