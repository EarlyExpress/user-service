package com.early_express.user_service.application;

import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.repository.UserRepository;
import com.early_express.user_service.infrastructure.security.keycloak.KeycloakUserRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SignupApprovalServiceIntegrationTest {
	@Autowired
	private SignupApprovalService signupApprovalService;

	@Autowired
	private UserRepository userRepository;

	@MockitoBean
	private KeycloakUserRegisterService userRegisterService;

	private User pendingUser;
	private User approvedUser;

	@BeforeEach
	void setup() {
		userRepository.deleteAll();

	}

	@Test
	@DisplayName("승인되지 않은 가입 요청 목록 조회 성공")
	void getPendingSignupRequests_success() {

	}
}