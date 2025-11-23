package com.early_express.user_service.application;

import com.early_express.user_service.domain.repository.UserRepository;
import com.early_express.user_service.infrastructure.exception.KeycloakException;
import com.early_express.user_service.infrastructure.security.keycloak.KeycloakProperties;
import com.early_express.user_service.infrastructure.security.keycloak.KeycloakUserRegisterService;
import com.early_express.user_service.presentation.dto.UserRegister;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private Keycloak keycloak;

	@Mock
	private RealmResource realmResource;

	@Mock
	private UsersResource usersResource;

	@Mock
	private UserResource userResource;

	@Mock
	private KeycloakProperties properties;

	private AuthService authService;

	private static final Response CREATED_RESPONSE;
	private static final UserRegister DEFAULT_DTO;

	static {
		URI location = URI.create("http://localhost/realms/test-realm/users/30481d4b-d3d2-4dab-8cbd-e73d561f53f6");
		CREATED_RESPONSE = Response.created(location).build();

		DEFAULT_DTO = new UserRegister(
				"test1234", "Password1!", "Password1!",
				"test@example.com", "테스트", "2c238593-3265-4462-bd73-2614f8ea9de4", null
		);
	}

	@BeforeEach
	void setup() {
		when(properties.getRealm()).thenReturn("test-realm");
		when(keycloak.realm(anyString())).thenReturn(realmResource);
		when(realmResource.users()).thenReturn(usersResource);
		lenient().when(usersResource.get(anyString())).thenReturn(userResource);

		KeycloakUserRegisterService userRegisterService = new KeycloakUserRegisterService(properties, keycloak);
		authService = new AuthService(null, userRegisterService, userRepository);
	}

	@Test
	@DisplayName("정상 회원가입: Keycloak 사용자 생성 + 비밀번호 설정, 로컬 DB에 사용자 정보 저장")
	void register_success() {
		// given
		when(usersResource.create(any())).thenReturn(CREATED_RESPONSE);

		// when
		authService.register(DEFAULT_DTO);

		// then
		verify(usersResource, times(1)).create(any());
		verify(userResource, times(1)).resetPassword(any());
		verify(userRepository, times(1)).save(any());
	}

	@Test
	@DisplayName("회원가입 실패: Keycloak 사용자 생성 실패")
	void register_fail_keycloakCreateError() {
		// given
		when(usersResource.create(any())).thenReturn(Response.status(400).build());

		// expect
		assertThatThrownBy(() -> authService.register(DEFAULT_DTO)).isInstanceOf(KeycloakException.class);

		verify(usersResource, times(1)).create(any());
		verify(userResource, never()).resetPassword(any());
		verify(userRepository, never()).save(any());
		verify(userResource, never()).remove();
	}

	@Test
	@DisplayName("회원가입 실패: Keycloak 사용자의 비밀번호 설정 실패 -> Keycloak 내부 롤백")
	void register_fail_setPasswordError() {
		// given
		when(usersResource.create(any())).thenReturn(CREATED_RESPONSE);
		doThrow(new RuntimeException("비밀번호 설정 오류"))
				.when(userResource).resetPassword(any());

		// expect
		assertThatThrownBy(() -> authService.register(DEFAULT_DTO)).isInstanceOf(KeycloakException.class);

		verify(usersResource, times(1)).create(any());
		verify(userResource, times(1)).resetPassword(any());
		verify(userResource, times(1)).remove();
		verify(userRepository, never()).save(any());
	}

	@Test
	@DisplayName("회원가입 실패: Keycloak 회원가입은 성공했지만 로컬 DB 저장 실패")
	void register_fail_dbSaveError() {
		// given
		when(usersResource.create(any())).thenReturn(CREATED_RESPONSE);
		doThrow(new RuntimeException("DB 오류")).when(userRepository).save(any());

		// expect
		assertThatThrownBy(() -> authService.register(DEFAULT_DTO));

		verify(usersResource, times(1)).create(any());
		verify(userResource, times(1)).resetPassword(any());
		verify(userResource, times(1)).remove();
	}
}