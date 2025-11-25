package com.early_express.user_service.domain.exception;


import com.early_express.user_service.global.presentation.exception.ErrorCode;
import com.early_express.user_service.global.presentation.exception.GlobalException;

public class UserDomainException extends GlobalException {
	public UserDomainException(ErrorCode errorCode) {
		super(errorCode);
	}
}
