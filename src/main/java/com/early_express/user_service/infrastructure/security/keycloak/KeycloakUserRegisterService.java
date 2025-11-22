package com.early_express.user_service.infrastructure.security.keycloak;

import com.early_express.user_service.application.dto.KeycloakRegisterDto;
import com.early_express.user_service.domain.vo.SignupStatus;
import com.early_express.user_service.infrastructure.exception.KeycloakErrorCode;
import com.early_express.user_service.infrastructure.exception.KeycloakException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KeycloakUserRegisterService {
	private final KeycloakProperties properties;
	private final Keycloak keycloak;

	public String registerUser(KeycloakRegisterDto dto) {
		UsersResource usersResource = keycloak.realm(properties.getRealm()).users();

		// 사용자 표현 객체 생성
		UserRepresentation user = buildUserRepresentation(dto);

		// Keycloak DB에 사용자 생성
		Response response = usersResource.create(user);

		if (response.getStatus() != 201) {
			throw new KeycloakException(KeycloakErrorCode.KEYCLOAK_REGISTER_ERROR);
		}

		// Keycloak한테서 uuid 받아오기
		String keycloakId = CreatedResponseUtil.getCreatedId(response);

		try {
			// Keycloak에 비밀번호 설정
			setPassword(dto.password(), usersResource, keycloakId);
			return keycloakId;
		} catch (Exception e) {
			usersResource.get(keycloakId).remove();
			throw new KeycloakException(KeycloakErrorCode.KEYCLOAK_REGISTER_ERROR);
		}
	}

	public void deleteUser(String keycloakId) {
		keycloak.realm(properties.getRealm()).users().get(keycloakId).remove();
	}

	private void setPassword(String password, UsersResource usersResource, String keycloakId) {
		CredentialRepresentation passwordCred = new CredentialRepresentation();
		passwordCred.setTemporary(false);
		passwordCred.setType(CredentialRepresentation.PASSWORD);
		passwordCred.setValue(password);

		usersResource.get(keycloakId).resetPassword(passwordCred);
	}

	private UserRepresentation buildUserRepresentation(KeycloakRegisterDto dto) {
		UserRepresentation user = new UserRepresentation();
		user.setEnabled(false);
		user.setUsername(dto.username());
		user.setEmail(dto.email());
		user.setEmailVerified(true);

		// 사용자 속성 설정
		Map<String, List<String>> attributes = new HashMap<>();
		attributes.put("status", List.of(SignupStatus.PENDING.name()));
		user.setAttributes(attributes);
		return user;
	}
}
