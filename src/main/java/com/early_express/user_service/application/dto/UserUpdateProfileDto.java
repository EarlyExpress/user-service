package com.early_express.user_service.application.dto;

public record UserUpdateProfileDto(
	String userId,
	String username,
	String email,
	String name,
	String slackId,
	String phoneNumber,
	String address
){
}
