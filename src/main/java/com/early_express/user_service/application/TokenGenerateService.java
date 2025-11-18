package com.early_express.user_service.application;

import com.early_express.user_service.application.dto.TokenInfo;

public interface TokenGenerateService {
	TokenInfo generate(String username, String password);
}
