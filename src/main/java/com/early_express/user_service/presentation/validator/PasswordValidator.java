package com.early_express.user_service.presentation.validator;

public interface PasswordValidator {

	default boolean validatePassword(String password) {
		return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$");
	}
}

