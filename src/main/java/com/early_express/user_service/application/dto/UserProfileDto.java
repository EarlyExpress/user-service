package com.early_express.user_service.application.dto;

import com.early_express.user_service.domain.entity.User;

public record UserProfileDto(
	String username,
	String email,
	String name,
	String role,
	String slackId,
	String phoneNumber,
	String address,
	String hubId,
	String companyId
) {
	public static UserProfileDto from(User user) {
		return new UserProfileDto(user.getUsername(), user.getEmail(), user.getName(), user.getRole().getDescription(),
				user.getSlackId(), user.getPhoneNumber(), user.getAddress(), user.getHubId(), user.getCompanyId());
	}
}
