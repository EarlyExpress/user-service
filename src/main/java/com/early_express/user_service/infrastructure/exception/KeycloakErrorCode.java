package com.early_express.user_service.infrastructure.exception;

import com.early_express.user_service.global.presentation.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KeycloakErrorCode implements ErrorCode {
	KEYCLOAK_SERVER_ERROR("KEYCLOAK_001", "Keycloak 서버 내부에서 오류가 발생했습니다", 500);

	private final String code;
	private final String message;
	private final int status;
}
