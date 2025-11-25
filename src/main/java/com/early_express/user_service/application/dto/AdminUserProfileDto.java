package com.early_express.user_service.application.dto;

import com.early_express.user_service.domain.entity.User;

import java.time.LocalDateTime;

public record AdminUserProfileDto(
	String userId,
	String username,
	String email,
	String name,
	String role,
	String signupStatus,
	String slackId,
	String hubId,
	String companyId,
	String phoneNumber,
	String address,
	LocalDateTime createdAt,
	String createdBy,
	LocalDateTime updatedAt,
	String updatedBy,
	LocalDateTime deletedAt,
	String deletedBy
) {
	public static AdminUserProfileDto from(User user) {
		return new AdminUserProfileDto(
				user.getKeycloakId(),
				user.getUsername(),
				user.getEmail(),
				user.getName(),
				user.getRole().getDescription(),
				user.getSignupStatus().getDescription(),
				user.getSlackId(),
				user.getHubId(),
				user.getCompanyId(),
				user.getPhoneNumber(),
				user.getAddress(),
				user.getCreatedAt(),
				user.getCreatedBy(),
				user.getUpdatedAt(),
				user.getUpdatedBy(),
				user.getDeletedAt(),
				user.getDeletedBy()
		);
	}
}
