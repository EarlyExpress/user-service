package com.early_express.user_service.presentation.validator;

public interface MobileValidator {
	default boolean checkMobile(String mobile) {
		mobile = mobile.replaceAll("\\D", "");
		String pattern = "^01[016]\\d{3,4}\\d{4}$";
		return mobile.matches(pattern);
	}
}
