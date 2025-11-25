package com.early_express.user_service.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
		@NotBlank(message = "닉네임/이메일이 비어있습니다")
		String username,
		@NotBlank(message = "비밀번호가 비어있습니다")
		String password
) {
}
