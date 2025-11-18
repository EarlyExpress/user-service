package com.early_express.user_service.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
		@NotBlank
		String username,
		@NotBlank
		String password
) {
}
