package com.early_express.user_service.presentation.controller;

import com.early_express.user_service.application.SignupApprovalService;
import com.early_express.user_service.global.presentation.dto.ApiResponse;
import com.early_express.user_service.global.presentation.dto.PageResponse;
import com.early_express.user_service.presentation.dto.ApproveSignupRequest;
import com.early_express.user_service.presentation.dto.RejectSignupRequest;
import com.early_express.user_service.presentation.dto.SignupRequestResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/auth/web/master-hub-manager/signup-requests")
@RequiredArgsConstructor
public class AuthAdminController {
	private final SignupApprovalService signupApprovalService;

	@GetMapping
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<PageResponse<SignupRequestResponse>> getPendingRequests(
			@PageableDefault(
					size = 10,
					sort = "createdAt",
					direction = Sort.Direction.DESC
			) Pageable pageable) {

		PageResponse<SignupRequestResponse> response = signupApprovalService.getPendingSignupRequests(pageable);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{userId}/approve")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Void>> approveSignup(@PathVariable String userId,
														   @Valid @RequestBody ApproveSignupRequest request,
														   @AuthenticationPrincipal Jwt jwt) {
		String approvedByEmail = jwt.getClaimAsString("email");
		signupApprovalService.approveSignup(userId, request.role(), approvedByEmail);
		return ResponseEntity.ok().body(ApiResponse.success(null, "회원가입 요청 승인이 완료됐습니다"));
	}

	@PostMapping("/{userId}/reject")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Void>> rejectSignup(@PathVariable String userId,
														  @RequestBody RejectSignupRequest request,
														  @AuthenticationPrincipal Jwt jwt) {
		String rejectedByUUID = jwt.getClaimAsString("user_id");
		String rejectedByEmail = jwt.getClaimAsString("email");
		signupApprovalService.rejectSignup(userId, request.reason(), rejectedByEmail, rejectedByUUID);
		return ResponseEntity.ok().body(ApiResponse.success(null, "회원가입 요청 거절이 완료됐습니다"));
	}
}
