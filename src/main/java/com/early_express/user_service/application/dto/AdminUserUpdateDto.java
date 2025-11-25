package com.early_express.user_service.application.dto;

import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.vo.Role;
import com.early_express.user_service.domain.vo.SignupStatus;

public record AdminUserUpdateDto(
		String username,
		String email,
		String name,
		Role role,
		SignupStatus signupStatus,
		String slackId,
		String hubId,
		String companyId,
		String phoneNumber,
		String address
) {
	public static AdminUserUpdateDto from(User user) {
		return new AdminUserUpdateDto(
				user.getUsername(),
				user.getEmail(),
				user.getName(),
				user.getRole(),
				user.getSignupStatus(),
				user.getSlackId(),
				user.getHubId(),
				user.getCompanyId(),
				user.getPhoneNumber(),
				user.getAddress()
		);
	}
}
