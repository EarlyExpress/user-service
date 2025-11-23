package com.early_express.user_service.presentation.controller;

import com.early_express.user_service.application.AuthService;
import com.early_express.user_service.application.dto.TokenInfo;
import com.early_express.user_service.global.presentation.dto.ApiResponse;
import com.early_express.user_service.presentation.dto.TokenRequest;
import com.early_express.user_service.presentation.dto.TokenResponse;
import com.early_express.user_service.presentation.dto.UserRegister;
import com.early_express.user_service.presentation.validator.SignupValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final SignupValidator validator;

	@PostMapping("web/public/login")
	public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody TokenRequest req) {
		TokenInfo tokenInfo = authService.generate(req.username(), req.password());
		TokenResponse tokenResponse = new TokenResponse(tokenInfo.access_token(), tokenInfo.expires_in(),
				tokenInfo.refresh_expires_in(), tokenInfo.refresh_token(), tokenInfo.token_type());

		return ResponseEntity.ok().body(ApiResponse.success(tokenResponse, "로그인이 완료되었습니다"));
	}

	@PostMapping("web/public/signup")
	public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody UserRegister dto) {
		validator.validate(dto);
		authService.register(dto);
		return ResponseEntity.ok().body(ApiResponse.success(null, "회원가입이 완료되었습니다. 검토 후 승인 여부를 슬랙으로 보내드리겠습니다."));
	}

	@PostMapping("web/all/logout")
	public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal Jwt jwt) {
		String userId = jwt.getClaimAsString("user_id");
		return ResponseEntity.ok().body(authService.logout(userId));
	}
}
