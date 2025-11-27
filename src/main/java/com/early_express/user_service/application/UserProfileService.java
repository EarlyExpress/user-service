package com.early_express.user_service.application;

import com.early_express.user_service.application.dto.UserProfileDto;
import com.early_express.user_service.application.dto.UserUpdateProfileDto;
import com.early_express.user_service.application.exception.UserErrorCode;
import com.early_express.user_service.application.exception.UserException;
import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileService {
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public UserProfileDto getUserProfile(String userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

		return UserProfileDto.from(user);
	}

	public UserProfileDto updateUserProfile(UserUpdateProfileDto dto) {
		User user = userRepository.findById(dto.userId()).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
		user.updateProfile(dto.username(), dto.email(), dto.name(), dto.slackId(),dto.phoneNumber(), dto.address());

		return UserProfileDto.from(user);
	}
}
