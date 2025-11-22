package com.early_express.user_service.domain.entity;

import com.early_express.user_service.domain.vo.Role;
import com.early_express.user_service.domain.vo.SignupStatus;
import com.early_express.user_service.global.infrastructure.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_user")
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

}
