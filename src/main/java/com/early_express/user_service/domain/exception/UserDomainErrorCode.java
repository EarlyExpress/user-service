package com.early_express.user_service.domain.exception;

import com.early_express.user_service.global.presentation.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserDomainErrorCode implements ErrorCode {
	NOT_PENDING_USER("USER_DOMAIN_001", "대기 중인 가입 요청만 거절할 수 있습니다.", 409);

	private final String code;
	private final String message;
	private final int status;
}
