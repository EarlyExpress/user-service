package com.early_express.user_service.presentation.controller;

import com.early_express.user_service.application.UserProfileService;
import com.early_express.user_service.application.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/internal/all/profile")
public class UserInternalController {
	private final UserProfileService userProfileService;

	@GetMapping
	public UserProfileDto getUserProfile(@RequestHeader("X-User-Id") String userId) {
		return userProfileService.getUserProfile(userId);
	}
}
