package com.early_express.user_service.infrastructure.security.keycloak;

import com.early_express.user_service.application.dto.TokenInfo;
import com.early_express.user_service.global.presentation.exception.AuthErrorCode;
import com.early_express.user_service.infrastructure.exception.LoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
@Slf4j
public class KeycloakTokenGenerateService {
	private final KeycloakProperties properties;

	public TokenInfo generate(String username, String password) {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "password");
		form.add("client_id", properties.getClientId());
		form.add("client_secret", properties.getClientSecret());
		form.add("username", username);
		form.add("password", password);
		form.add("scope", "openid custom");
		RestClient client = RestClient.create();

		try {
			ResponseEntity<TokenInfo> res = client.post()
												  .uri(String.format("%s/realms/%s/protocol/openid-connect/token",
														  properties.getServerUrl(), properties.getRealm()))
												  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
												  .body(form)
												  .retrieve()
												  .toEntity(TokenInfo.class);

			if (res.getStatusCode().is2xxSuccessful()) {
				return res.getBody();
			}
			throw new LoginException(AuthErrorCode.AUTHENTICATION_FAILED);

		} catch (HttpClientErrorException e) {
			String body = e.getResponseBodyAsString();
			log.info("body = {}", body);
			if (body.contains("Account disabled")) {
				throw new LoginException(AuthErrorCode.ACCOUNT_DISABLED);
			}
			if (body.contains("Account locked")) {
				throw new LoginException(AuthErrorCode.ACCOUNT_LOCKED);
			}
			if (body.contains("credentials_exhausted") || body.contains("password expired")) {
				throw new LoginException(AuthErrorCode.CREDENTIALS_EXPIRED);
			}
			if (body.contains("invalid_grant")) {
				throw new LoginException(AuthErrorCode.INVALID_CREDENTIALS);
			}

			// 알 수 없는 경우
			throw new LoginException(AuthErrorCode.AUTHENTICATION_FAILED, e);
		}
	}
}
