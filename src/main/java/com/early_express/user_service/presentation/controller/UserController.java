package com.early_express.user_service.presentation.controller;


import com.early_express.user_service.application.UserProfileService;
import com.early_express.user_service.application.dto.UserProfileDto;
import com.early_express.user_service.application.dto.UserUpdateProfileDto;
import com.early_express.user_service.global.presentation.dto.ApiResponse;
import com.early_express.user_service.presentation.dto.UserProfileUpdateRequest;
import com.early_express.user_service.presentation.validator.ProfileUpdateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/web/all/profile")
@RequiredArgsConstructor
public class UserController {
	private final UserProfileService userProfileService;
	private final ProfileUpdateValidator updateValidator;

	@GetMapping
	public ResponseEntity<ApiResponse<UserProfileDto>> getUserProfile(@AuthenticationPrincipal Jwt jwt) {
		String userId = jwt.getClaimAsString("user_id");
		return ResponseEntity.ok(ApiResponse.success(userProfileService.getUserProfile(userId)));
	}

	@PatchMapping
	public ResponseEntity<ApiResponse<UserProfileDto>> updateUserProfile(@AuthenticationPrincipal Jwt jwt,
																		 @RequestBody UserProfileUpdateRequest request) {

		updateValidator.validate(request);
		String userId = jwt.getClaimAsString("user_id");
		UserUpdateProfileDto updateProfileDto = new UserUpdateProfileDto(userId, request.username(), request.email(),
				request.name(), request.slackId(), request.phoneNumber(), request.address());
		return ResponseEntity.ok(ApiResponse.success(userProfileService.updateUserProfile(updateProfileDto)));
	}
}
