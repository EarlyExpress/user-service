package com.early_express.user_service.presentation.validator;

public interface UsernameValidator {
	default boolean validateUsername(String username) {
		return username.matches("^[a-z0-9]+$");
	}
}
