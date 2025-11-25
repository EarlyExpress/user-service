package com.early_express.user_service.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateRequest(
		@Size(min=4, max = 10, message = "아이디는 4~10자여야 합니다.")
		String username,

		@Email(message = "올바른 이메일을 입력해주세요")
		String email,

		@NotBlank(message = "이름을 입력해주세요")
		String name,

		@Size(max = 20, message = "슬랙 ID는 20자 이하입니다")
		String slackId,

		String phoneNumber,

		String address
) {
}
