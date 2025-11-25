package com.early_express.user_service.infrastructure.security.config;

import com.early_express.user_service.global.infrastructure.security.filter.UserHeaderAuthenticationFilter;
import com.early_express.user_service.infrastructure.security.keycloak.KeycloakClientRoleConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final UserHeaderAuthenticationFilter userHeaderAuthenticationFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// JWT → Authentication으로 변환
		JwtAuthenticationConverter conv = new JwtAuthenticationConverter();
		// Keycloak의 Role을 Spring security의 Role로 변경
		conv.setJwtGrantedAuthoritiesConverter(new KeycloakClientRoleConverter());

		http
				// CSRF 비활성화 (REST API이므로)
				.csrf(AbstractHttpConfigurer::disable)

				// H2 Console을 위한 Headers 설정 추가
				.headers(headers -> headers
						.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
				)

				// 세션 사용하지 않음 (Stateless)
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)

				// 모든 요청 허용 (개발 단계)
				// TODO: 실제 운영 환경에서는 적절한 권한 설정 필요
				.authorizeHttpRequests(auth -> auth
						// Actuator endpoints
						.requestMatchers("/actuator/**").permitAll()
						// Swagger UI
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
						// Health check
						.requestMatchers("/health", "/info").permitAll()
						// H2 Console 추가
						.requestMatchers("/h2-console/**").permitAll()
						// 인증/인가
						.requestMatchers("/auth/web/public/**").permitAll()
						.requestMatchers("/auth/web/master-hub-manager/**").hasAnyRole("MASTER", "HUB_MANAGER")
						.requestMatchers("/user/web/master/**").hasRole("MASTER")
						// 나머지는 모두 인증 필요
						.anyRequest().authenticated()
				)
				.oauth2ResourceServer(c -> c.jwt(jwt -> jwt.jwtAuthenticationConverter(conv))
											.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
											.accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
				.addFilterAfter(userHeaderAuthenticationFilter, BearerTokenAuthenticationFilter.class);

		return http.build();
	}

}
