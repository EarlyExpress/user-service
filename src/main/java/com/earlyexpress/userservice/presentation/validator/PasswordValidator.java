package com.earlyexpress.userservice.presentation.validator;

public interface PasswordValidator {

	default boolean checkAlpha(String password, boolean caseInsensitive) {
		if (caseInsensitive) {
			return password.matches(".*[a-zA-Z]+.*");
		}

		return password.matches(".*[a-z]+.*") && password.matches(".*[A-Z]+.*");
	}

	default boolean checkNumber(String password) {
		return password.matches(".*\\d+.*");
	}

	default boolean checkSpecialChars(String password) {
		String pattern = ".*[^0-9a-zA-Zㄱ-ㅎ가-힣]+.*";

		return password.matches(pattern);
	}
}

