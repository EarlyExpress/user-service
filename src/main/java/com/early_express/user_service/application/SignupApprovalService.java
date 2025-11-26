package com.early_express.user_service.application;

import com.early_express.user_service.application.exception.UserException;
import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.repository.UserRepository;
import com.early_express.user_service.domain.vo.Role;
import com.early_express.user_service.domain.vo.SignupStatus;
import com.early_express.user_service.global.common.utils.PageUtils;
import com.early_express.user_service.global.common.utils.UuidUtils;
import com.early_express.user_service.global.presentation.dto.PageResponse;
import com.early_express.user_service.infrastructure.dto.CreateHubDriverRequest;
import com.early_express.user_service.infrastructure.dto.CreateLastMileDriverRequest;
import com.early_express.user_service.infrastructure.dto.CreateManagerRequest;
import com.early_express.user_service.infrastructure.feign.CompanyFeignClient;
import com.early_express.user_service.infrastructure.feign.HubDriverFeignClient;
import com.early_express.user_service.infrastructure.feign.LastMileDriverFeignClient;
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
	private final HubDriverFeignClient hubDriverFeignClient;
	private final LastMileDriverFeignClient lastMileDriverFeignClient;
	private final CompanyFeignClient companyFeignClient;

	@Transactional(readOnly = true)
	public PageResponse<SignupRequestResponse> getPendingSignupRequests(Pageable pageable) {
		Page<User> userPages = userRepository.findBySignupStatusOrderByCreatedAtDesc(SignupStatus.PENDING, pageable);
		return PageUtils.toPageResponse(userPages, SignupRequestResponse::from);
	}

	public void approveSignup(String userId, Role role, String approvedByEmail) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

		if (user.getSignupStatus() == SignupStatus.ACCEPTED) {
			throw new UserException(ALREADY_APPROVED_USER);
		}

		try {
			// 로컬 DB에서 가입 상태 업데이트
			user.approveSignup(role);

			// 직원 생성 요청 (업체 배송 담당자, 허브 배송 담당자, 업체 담당자만 회원가입을 한다고 가정)
			if (role == Role.DELIVERY) {
				if (!StringUtils.hasText(user.getHubId()))
					hubDriverFeignClient.createDriver(new CreateHubDriverRequest(userId, user.getName()));
				else
					lastMileDriverFeignClient.createDriver(new CreateLastMileDriverRequest(user.getHubId(), userId, user.getName()));
			} else if (role == Role.COMPANY) {
				companyFeignClient.createManager(new CreateManagerRequest(user.getCompanyId(), userId, user.getName()));
			}

			// Keycloak 서버에서 활성화
			userRegisterService.activateUser(userId, role);
		} catch (Exception e) {
			log.error("회원가입 승인 오류: ", e);
			throw new UserException(SIGNUP_APPROVAL_FAILED);
		}

		// 알림 서비스에 이벤트 발행
		if (StringUtils.hasText(user.getSlackId())) {
			SignupAcceptedEvent signupAcceptedEvent = new SignupAcceptedEvent(UuidUtils.generate(),
					"SIGNUP_ACCEPTED", user.getName(), user.getEmail(), user.getRole().getDescription(),
					user.getSlackId(), LocalDateTime.now(), approvedByEmail);

			userEventProducer.publishSignupAccepted(signupAcceptedEvent);
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
