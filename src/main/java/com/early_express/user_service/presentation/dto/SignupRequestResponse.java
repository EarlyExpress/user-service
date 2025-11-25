package com.early_express.user_service.presentation.dto;

import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.vo.SignupStatus;

import java.time.LocalDateTime;

public record SignupRequestResponse(
	String userId,
	String username,
	String email,
	String name,
	String hubId,
	String companyId,
	SignupStatus signupStatus,
	LocalDateTime createdAt
) {
	public static SignupRequestResponse from(User user) {
		return new SignupRequestResponse(
				user.getKeycloakId(),
				user.getUsername(),
				user.getEmail(),
				user.getName(),
				user.getHubId(),
				user.getCompanyId(),
				user.getSignupStatus(),
				user.getCreatedAt()
		);
	}
}
