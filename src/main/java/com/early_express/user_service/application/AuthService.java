package com.early_express.user_service.application;

import com.early_express.user_service.application.dto.KeycloakRegisterDto;
import com.early_express.user_service.application.dto.TokenInfo;
import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.repository.UserRepository;
import com.early_express.user_service.infrastructure.security.keycloak.KeycloakTokenGenerateService;
import com.early_express.user_service.infrastructure.security.keycloak.KeycloakUserRegisterService;
import com.early_express.user_service.presentation.dto.UserRegister;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
	private final KeycloakTokenGenerateService tokenGenerateService;
	private final KeycloakUserRegisterService userRegisterService;
	private final UserRepository repository;

	public TokenInfo generate(String username, String password) {
		return tokenGenerateService.generate(username, password);
	}

	public void register(UserRegister dto) {
		KeycloakRegisterDto keycloakRegisterDto = new KeycloakRegisterDto(dto.username(), dto.password(), dto.email());

		String keycloakId = null;

		try {
			// 사용자 Keycloak에 저장
			keycloakId = userRegisterService.registerUser(keycloakRegisterDto);

			// 로컬 DB에 사용자 저장
			User userEntity = User.builder()
								  .keycloakId(keycloakId)
								  .username(dto.username())
								  .email(dto.email())
								  .name(dto.name())
								  .hubId(dto.hubId())
								  .companyId(dto.companyId()).build();
			repository.save(userEntity);
		} catch (Exception e) {
			// DB 저장 실패 시 Keycloak 서버에서 사용자 삭제
			if (keycloakId != null) {
				try {
					userRegisterService.deleteUser(keycloakId);
				} catch (Exception ex) {
					// 삭제 실패는 일단 로그만 남김. 나중에 모니터링/알림 추가?
					log.error("Keycloak 서버에서 사용자 삭제 실패: {}", keycloakId);
				}
			}
			throw e;
		}
	}
}
