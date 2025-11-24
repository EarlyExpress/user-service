package com.early_express.user_service.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
		@NotBlank(message = "갱신 토큰이 비어있습니다")
		String refreshToken
) {
}
