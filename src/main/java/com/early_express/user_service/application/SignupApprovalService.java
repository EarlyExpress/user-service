package com.early_express.user_service.application;

import com.early_express.user_service.application.exception.UserException;
import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.repository.UserRepository;
import com.early_express.user_service.domain.vo.Role;
import com.early_express.user_service.domain.vo.SignupStatus;
import com.early_express.user_service.global.common.utils.PageUtils;
import com.early_express.user_service.global.common.utils.UuidUtils;
import com.early_express.user_service.global.presentation.dto.PageResponse;
import com.early_express.user_service.infrastructure.messaging.event.EmployeeCreatedEvent;
import com.early_express.user_service.infrastructure.messaging.event.SignupAcceptedEvent;
import com.early_express.user_service.infrastructure.messaging.event.SignupRejectedEvent;
import com.early_express.user_service.infrastructure.messaging.producer.UserEventProducer;
import com.early_express.user_service.infrastructure.security.keycloak.KeycloakUserRegisterService;
import com.early_express.user_service.presentation.dto.SignupRequestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static com.early_express.user_service.application.exception.UserErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SignupApprovalService {
	private final UserRepository userRepository;
	private final KeycloakUserRegisterService userRegisterService;
	private final UserEventProducer userEventProducer;

	@Transactional(readOnly = true)
	public PageResponse<SignupRequestResponse> getPendingSignupRequests(Pageable pageable) {
		Page<User> userPages = userRepository.findBySignupStatusOrderByCreatedAtDesc(SignupStatus.PENDING, pageable);
		return PageUtils.toPageResponse(userPages, SignupRequestResponse::from);
	}

	public void approveSignup(String userId, Role role, String approvedByEmail, String approvedByUUID) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

		if (user.getSignupStatus() == SignupStatus.ACCEPTED) {
			throw new UserException(ALREADY_APPROVED_USER);
		}

		try {
			user.approveSignup(role);
			userRegisterService.activateUser(userId, role);
		} catch (Exception e) {
			log.error("로그 오류: ", e);
			throw new UserException(SIGNUP_APPROVAL_FAILED);
		}

		if (StringUtils.hasText(user.getSlackId())) {
			SignupAcceptedEvent signupAcceptedEvent = new SignupAcceptedEvent(UuidUtils.generate(),
					"SIGNUP_ACCEPTED", user.getName(), user.getEmail(), user.getRole().getDescription(),
					user.getSlackId(), LocalDateTime.now(), approvedByEmail);

			userEventProducer.publishSignupAccepted(signupAcceptedEvent);

			EmployeeCreatedEvent employeeCreatedEvent = new EmployeeCreatedEvent(UuidUtils.generate(),
					"EMPLOYEE_CREATED", user.getKeycloakId(), user.getRole().name(), user.getHubId(),
					user.getCompanyId(), LocalDateTime.now(), approvedByUUID);

			userEventProducer.publishEmployeeCreated(employeeCreatedEvent);
		}
	}

	public void rejectSignup(String userId, String reason, String rejectedByEmail, String rejectedByUUID) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
		user.rejectSignup(rejectedByUUID);

		if (StringUtils.hasText(user.getSlackId())) {
			SignupRejectedEvent signupRejectedEvent = new SignupRejectedEvent(UuidUtils.generate(),
					"SIGNUP_REJECTED", user.getName(), user.getEmail(), reason, user.getSlackId(), LocalDateTime.now(), rejectedByEmail);
			userEventProducer.publishSignupRejected(signupRejectedEvent);
		}
	}
}
