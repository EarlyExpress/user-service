package com.earlyexpress.userservice.presentation.controller;

import com.earlyexpress.userservice.application.TokenGenerateService;
import com.earlyexpress.userservice.application.dto.TokenInfo;
import com.earlyexpress.userservice.presentation.dto.TokenRequest;
import com.earlyexpress.userservice.presentation.dto.TokenResponse;
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
