package com.early_express.user_service.presentation.validator;

import com.early_express.user_service.domain.repository.UserRepository;
import com.early_express.user_service.presentation.dto.UserProfileUpdateRequest;
import com.early_express.user_service.presentation.dto.UserRegister;
import com.early_express.user_service.presentation.exception.SignupException;
import com.early_express.user_service.presentation.exception.UpdateProfileErrorCode;
import com.early_express.user_service.presentation.exception.UpdateProfileException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.early_express.user_service.presentation.exception.SignupErrorCode.*;

@Component
@RequiredArgsConstructor
public class ProfileUpdateValidator implements UsernameValidator, MobileValidator{
	private final UserRepository repository;

	public void validate(UserProfileUpdateRequest req) {
		if (StringUtils.hasText(req.username()) && !validateUsername(req.username())) {
			throw new UpdateProfileException(UpdateProfileErrorCode.INVALID_USERNAME);
		}

		if (StringUtils.hasText(req.phoneNumber()) && !checkMobile(req.phoneNumber())) {
			throw new UpdateProfileException(UpdateProfileErrorCode.INVALID_PHONE_NUMBER);
		}

		if (StringUtils.hasText(req.email()) && repository.existsByEmail(req.email())) {
			throw new UpdateProfileException(UpdateProfileErrorCode.DUPLICATE_EMAIL);
		}

		if (StringUtils.hasText(req.username()) && repository.existsByUsername(req.username())) {
			throw new UpdateProfileException(UpdateProfileErrorCode.DUPLICATE_USERNAME);
		}
	}
}
