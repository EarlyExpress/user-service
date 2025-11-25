package com.early_express.user_service.presentation.exception;

import com.early_express.user_service.global.presentation.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UpdateProfileErrorCode implements ErrorCode {
	INVALID_USERNAME("PROFILE_UPDATE_001", "닉네임은 4~10자의 알파벳 소문자와 숫자만 가능합니다", 400),
	DUPLICATE_USERNAME("PROFILE_UPDATE_002", "이미 존재하는 닉네임입니다", 400),
	DUPLICATE_EMAIL("PROFILE_UPDATE_003", "이미 존재하는 이메일입니다.", 400),
	INVALID_PHONE_NUMBER("PROFILE_UPDATE_004", "올바른 전화번호를 입력해주세요", 400);

	private final String code;
	private final String message;
	private final int status;
}
