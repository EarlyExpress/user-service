package com.early_express.user_service.infrastructure.security.keycloak;

import com.early_express.user_service.application.dto.TokenInfo;
import com.early_express.user_service.infrastructure.exception.KeycloakException;
import com.early_express.user_service.infrastructure.exception.LoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import static com.early_express.user_service.global.presentation.exception.AuthErrorCode.*;
import static com.early_express.user_service.infrastructure.exception.KeycloakErrorCode.KEYCLOAK_SERVER_ERROR;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
@Slf4j
public class KeycloakTokenGenerateService {
	private final KeycloakProperties properties;
	private final RestClient keycloakRestClient;

	public TokenInfo generate(String username, String password) {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "password");
		form.add("client_id", properties.getClientId());
		form.add("client_secret", properties.getClientSecret());
		form.add("username", username);
		form.add("password", password);
		form.add("scope", "openid custom");

		try {
			return keycloakRestClient.post()
									 .uri(String.format("/realms/%s/protocol/openid-connect/token", properties.getRealm()))
									 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
									 .body(form)
									 .retrieve()
									 .toEntity(TokenInfo.class)
									 .getBody();

		} catch (HttpClientErrorException e) {
			String body = e.getResponseBodyAsString();
			log.info("body = {}", body);
			if (body.contains("Account disabled")) {
				throw new LoginException(ACCOUNT_DISABLED);
			}
			if (body.contains("Account locked")) {
				throw new LoginException(ACCOUNT_LOCKED);
			}
			if (body.contains("credentials_exhausted") || body.contains("password expired")) {
				throw new LoginException(CREDENTIALS_EXPIRED);
			}
			if (body.contains("invalid_grant")) {
				throw new LoginException(INVALID_CREDENTIALS);
			}

			// 알 수 없는 경우
			throw new LoginException(AUTHENTICATION_FAILED);
		}catch (HttpServerErrorException e) {
			// Keycloak 서버 오류
			throw new KeycloakException(KEYCLOAK_SERVER_ERROR, e.getMessage());
		} catch (Exception e) {
			// 예상치 못한 오류
			throw new LoginException(AUTHENTICATION_FAILED, e.getMessage());
		}
	}
}
