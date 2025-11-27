package com.early_express.user_service.presentation.controller;

import com.early_express.user_service.application.UserProfileService;
import com.early_express.user_service.application.dto.UserProfileDto;
import com.early_express.user_service.global.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/internal/all/profile/{id}")
public class UserInternalController {
	private final UserProfileService userProfileService;

	@GetMapping
	public ResponseEntity<ApiResponse<UserProfileDto>> getUserProfile(@RequestHeader("X-User-Id") String userId) {
		return ResponseEntity.ok(ApiResponse.success(userProfileService.getUserProfile(userId)));
	}
}
