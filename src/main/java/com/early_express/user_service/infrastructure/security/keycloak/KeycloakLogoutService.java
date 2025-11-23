package com.early_express.user_service.infrastructure.security.keycloak;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserSessionRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KeycloakLogoutService {
	private final KeycloakProperties properties;
	private final Keycloak keycloak;

	public void logoutUser(String userId) {
		RealmResource realmResource = keycloak.realm(properties.getRealm());
		UsersResource usersResource = realmResource.users();

		// 현재 사용자의 모든 세션 조회
		List<UserSessionRepresentation> sessions = usersResource.get(userId).getUserSessions();

		if (sessions == null || sessions.isEmpty()) {
			return;
		}

		// 각 세션마다 로그아웃
		for (UserSessionRepresentation session : sessions) {
			realmResource.deleteSession(session.getId(), false);
		}

	}
}
