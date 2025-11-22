package com.early_express.user_service.presentation.validator;

import com.early_express.user_service.domain.repository.UserRepository;
import com.early_express.user_service.presentation.dto.UserRegister;
import com.early_express.user_service.presentation.exception.SignupException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.early_express.user_service.presentation.exception.SignupErrorCode.*;

@Component
@RequiredArgsConstructor
public class SignupValidator implements PasswordValidator, UsernameValidator{
	private final UserRepository repository;

	public void validate(UserRegister dto) {
		String email = dto.email();
		String username = dto.username();
		String password = dto.password();
		String confirmPassword = dto.confirmPassword();

		if (!password.equals(confirmPassword)) {
			throw new SignupException(INVALID_SIGNUP_CONFIRM_PASSWORD);
		}

		if (!validateUsername(username)) {
			throw new SignupException(INVALID_SIGNUP_USERNAME);
		}

		if (!validatePassword(password)) {
			throw new SignupException(INVALID_SIGNUP_PASSWORD);
		}

		if (repository.existsByEmail(email)) {
			throw new SignupException(DUPLICATE_SIGNUP_EMAIL);
		}

		if (repository.existsByUsername(username)) {
			throw new SignupException(DUPLICATE_SIGNUP_USERNAME);
		}
	}
}
