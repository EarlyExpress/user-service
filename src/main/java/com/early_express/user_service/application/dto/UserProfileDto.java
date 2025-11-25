package com.early_express.user_service.application.dto;

public record UserProfileDto(
	String username,
	String email,
	String name,
	String role,
	String slackId,
	String phoneNumber,
	String address
) {
}
