package com.early_express.user_service.application.exception;

import com.early_express.user_service.global.presentation.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
	USER_NOT_FOUND("USER_001", "사용자를 찾을 수 없습니다", 404),
	ALREADY_APPROVED_USER("USER_002", "가입 요청이 이미 승인된 사용자입니다.", 409),
	SIGNUP_APPROVAL_FAILED("USER-003", "회원가입 승인에 실패했습니다.", 500);

	private final String code;
	private final String message;
	private final int status;
}
