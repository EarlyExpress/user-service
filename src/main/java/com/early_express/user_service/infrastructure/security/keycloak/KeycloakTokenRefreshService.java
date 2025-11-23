package com.early_express.user_service.infrastructure.security.keycloak;

import com.early_express.user_service.application.dto.TokenInfo;
import com.early_express.user_service.global.presentation.exception.AuthErrorCode;
import com.early_express.user_service.global.presentation.exception.GlobalErrorCode;
import com.early_express.user_service.infrastructure.exception.KeycloakErrorCode;
import com.early_express.user_service.infrastructure.exception.KeycloakException;
import com.early_express.user_service.infrastructure.exception.RefreshException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakTokenRefreshService {
	private final KeycloakProperties properties;
	private final RestClient keycloakRestClient;

	public TokenInfo refresh(String refreshToken) {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "refresh_token");
		form.add("client_id", properties.getClientId());
		form.add("client_secret", properties.getClientSecret());
		form.add("refresh_token", refreshToken);

		try {
			return keycloakRestClient.post()
									 .uri(String.format("/realms/%s/protocol/openid-connect/token", properties.getRealm()))
									 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
									 .body(form)
									 .retrieve()
									 .toEntity(TokenInfo.class)
									 .getBody();

		} catch (HttpClientErrorException e) {
			throw new RefreshException(AuthErrorCode.INVALID_REFRESH_TOKEN);
		} catch (HttpServerErrorException e) {
			throw new KeycloakException(KeycloakErrorCode.KEYCLOAK_SERVER_ERROR);
		} catch (Exception e) {
			throw new RefreshException(GlobalErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
