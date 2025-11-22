package com.early_express.user_service.application.dto;

public record KeycloakRegisterDto(
		String username,
		String password,
		String email
) {
}
