package com.early_express.user_service.domain.entity;

import com.early_express.user_service.domain.exception.UserDomainException;
import com.early_express.user_service.domain.vo.Role;
import com.early_express.user_service.domain.vo.SignupStatus;
import com.early_express.user_service.global.infrastructure.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.early_express.user_service.domain.exception.UserDomainErrorCode.NOT_PENDING_USER;

@Entity
@Table(
	name = "p_user",
	indexes = {
		@Index(name = "idx_signup_status_created_at", columnList = "signupStatus, createdAt DESC"),
		@Index(name = "idx_hub_id", columnList = "hubId"),
		@Index(name = "idx_company_id", columnList = "companyId")
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {
	@Id
	@Column(length = 36, updatable = false, nullable = false)
	private String keycloakId;

	@Column(length = 10, nullable = false, unique = true)
	private String username;

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@Column(length = 30, nullable = false)
	private String name;

	@Column(nullable = true, length = 20)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(nullable = false, length = 8)
	@Enumerated(EnumType.STRING)
	private SignupStatus signupStatus = SignupStatus.PENDING;

	@Column(nullable = true, length = 20)
	private String slackId;

	@Column(nullable = true, length = 36)
	private String hubId;

	@Column(nullable = true, length = 36)
	private String companyId;

	@Column(nullable = true, length = 20)
	private String phoneNumber;

	@Column(nullable = true, length = 100)
	private String address;

	@Builder
	private User(String keycloakId, String username, String name, String email, String hubId, String companyId) {
		this.keycloakId = keycloakId;
		this.username = username;
		this.name = name;
		this.email = email;
		this.hubId = hubId;
		this.companyId = companyId;
	}

	public void approveSignup(Role role) {
		this.signupStatus = SignupStatus.ACCEPTED;
		this.role = role;
	}

	public void rejectSignup(String deletedBy) {
		if (this.signupStatus != SignupStatus.PENDING) {
			throw new UserDomainException(NOT_PENDING_USER);
		}
		this.signupStatus = SignupStatus.REJECTED;
		this.delete(deletedBy);
	}
}
