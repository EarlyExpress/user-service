package com.early_express.user_service.infrastructure.rest_client;

import com.early_express.user_service.infrastructure.security.keycloak.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
	private final KeycloakProperties properties;

	@Bean
	public RestClient keycloakRestClient() {
		return RestClient.builder()
						 .baseUrl(properties.getServerUrl())
						 .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
						 .build();
	}
}
