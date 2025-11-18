package com.early_express.user_service.global.config;

import com.early_express.user_service.infrastructure.security.keycloak.KeycloakClientRoleConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		JwtAuthenticationConverter conv = new JwtAuthenticationConverter();
		conv.setJwtGrantedAuthoritiesConverter(new KeycloakClientRoleConverter());

        http
                // CSRF 비활성화 (REST API이므로)
                .csrf(AbstractHttpConfigurer::disable)

                // CORS 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ⭐ H2 Console을 위한 Headers 설정 추가
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
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
						.requestMatchers("/web/public/signup", "/web/public/login").permitAll()
						// 나머지는 모두 인증 필요
                        .anyRequest().authenticated()
                )
				.oauth2ResourceServer(c -> c.jwt(jwt -> jwt.jwtAuthenticationConverter(conv))
											 .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
											 .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));;

        return http.build();
    }

//    /**
//     * Password Encoder
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    /**
//     * CORS 설정
//     */
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        // 허용할 Origin 설정
//        configuration.setAllowedOriginPatterns(List.of("*"));
//
//        // 허용할 HTTP Method
//        configuration.setAllowedMethods(Arrays.asList(
//                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
//        ));
//
//        // 허용할 Header
//        configuration.setAllowedHeaders(List.of("*"));
//
//        // 인증 정보 포함 허용
//        configuration.setAllowCredentials(true);
//
//        // preflight 캐시 시간
//        configuration.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }
}