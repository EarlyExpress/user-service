package com.early_express.user_service.presentation.controller;

import com.early_express.user_service.application.AdminUserProfileService;
import com.early_express.user_service.application.dto.AdminUserProfileDto;
import com.early_express.user_service.application.dto.AdminUserUpdateDto;
import com.early_express.user_service.global.presentation.dto.ApiResponse;
import com.early_express.user_service.global.presentation.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user/web/master/users")
@PreAuthorize("hasRole('MASTER')")
public class UserAdminController {
	private final AdminUserProfileService adminService;

	@GetMapping
	public ResponseEntity<PageResponse<AdminUserProfileDto>> getAllUsers(
			@PageableDefault(
					size = 10,
					sort = "createdAt",
					direction = Sort.Direction.DESC
			) Pageable pageable
	) {
		return ResponseEntity.ok(adminService.getAllUsers(pageable));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<AdminUserProfileDto>> getUser(@PathVariable String userId) {
		return ResponseEntity.ok(adminService.getUser(userId));
	}

	@PatchMapping("/{userId}")
	public ResponseEntity<ApiResponse<AdminUserProfileDto>> updateUser(@PathVariable String userId,
																	   @RequestBody AdminUserUpdateDto req) {
		return ResponseEntity.ok(adminService.updateUser(userId, req));
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse<Void>> deleteUser(@AuthenticationPrincipal Jwt jwt, @PathVariable String userId) {
		String deletedBy = jwt.getClaimAsString("user_id");
		adminService.deleteUser(userId, deletedBy);
		return ResponseEntity.ok(ApiResponse.success(null, "회원 삭제 성공!"));
	}
}
