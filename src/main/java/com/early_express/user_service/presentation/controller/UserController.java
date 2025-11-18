package com.early_express.user_service.presentation.controller;

import com.early_express.user_service.application.TokenGenerateService;
import com.early_express.user_service.application.dto.TokenInfo;
import com.early_express.user_service.presentation.dto.TokenRequest;
import com.early_express.user_service.presentation.dto.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final TokenGenerateService tokenService;

	@PostMapping("token")
	public TokenResponse generateToken(@Valid @RequestBody TokenRequest req) {
		TokenInfo tokenInfo = tokenService.generate(req.username(), req.password());

		return new TokenResponse(tokenInfo.access_token(), tokenInfo.expires_in(), tokenInfo.refresh_expires_in(), tokenInfo.refresh_token(), tokenInfo.token_type());
	}
}
