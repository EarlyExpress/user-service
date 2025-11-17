package com.earlyexpress.userservice.application;

import com.earlyexpress.userservice.application.dto.TokenInfo;

public interface TokenGenerateService {
	TokenInfo generate(String username, String password);
}
