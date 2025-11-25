package com.early_express.user_service.application;

import com.early_express.user_service.application.dto.AdminUserProfileDto;
import com.early_express.user_service.application.dto.AdminUserUpdateDto;
import com.early_express.user_service.application.exception.UserErrorCode;
import com.early_express.user_service.application.exception.UserException;
import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.repository.UserRepository;
import com.early_express.user_service.global.common.utils.PageUtils;
import com.early_express.user_service.global.presentation.dto.ApiResponse;
import com.early_express.user_service.global.presentation.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserProfileService {
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public PageResponse<AdminUserProfileDto> getAllUsers(Pageable pageable) {
		Page<User> userPages = userRepository.findAll(pageable);
		return PageUtils.toPageResponse(userPages, AdminUserProfileDto::from);
	}

	@Transactional(readOnly = true)
	public ApiResponse<AdminUserProfileDto> getUser(String userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
		return ApiResponse.success(AdminUserProfileDto.from(user));
	}


	public ApiResponse<AdminUserProfileDto> updateUser(String userId, AdminUserUpdateDto dto) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
		user.updateProfileAdmin(dto.username(), dto.email(), dto.name(), dto.role(), dto.signupStatus(), dto.slackId(),
				dto.hubId(), dto.companyId(), dto.phoneNumber(), dto.address());
		return ApiResponse.success(AdminUserProfileDto.from(user));
	}

	public void deleteUser(String userId, String deletedBy) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
		user.delete(deletedBy);
	}
}
