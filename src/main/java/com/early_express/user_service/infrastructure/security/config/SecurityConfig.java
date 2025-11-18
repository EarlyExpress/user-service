package com.early_express.user_service.infrastructure.security.config;

import com.early_express.user_service.infrastructure.security.keycloak.KeycloakClientRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		JwtAuthenticationConverter conv = new JwtAuthenticationConverter();
		conv.setJwtGrantedAuthoritiesConverter(new KeycloakClientRoleConverter());

		http.csrf(c -> c.disable())
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers("/oauth2/**", "/login/**").permitAll()
					.requestMatchers("/profile/**", "/password/**", "/role/**").hasRole("USER")
					.anyRequest().permitAll())
			.oauth2ResourceServer(c -> c.jwt(jwt -> jwt.jwtAuthenticationConverter(conv))
					.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
					.accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

		return http.build();
	}

}
