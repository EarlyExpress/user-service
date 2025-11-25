package com.early_express.user_service.presentation.dto;

import com.early_express.user_service.domain.vo.Role;
import jakarta.validation.constraints.NotNull;

public record ApproveSignupRequest(
		@NotNull(message = "역할을 선택해주세요")
		Role role
) {
}
