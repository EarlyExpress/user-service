package com.early_express.user_service.presentation.dto;

public record TokenResponse(
		String accessToken,
		int expiresIn,
		int refreshExpiresIn,
		String refreshToken,
		String tokenType
) {}

