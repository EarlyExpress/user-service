package com.early_express.user_service.infrastructure.security.keycloak;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
	private String serverUrl;
	private String realm;
	private String clientId;
	private String clientSecret;
	private String adminUsername;
	private String adminPassword;
}
