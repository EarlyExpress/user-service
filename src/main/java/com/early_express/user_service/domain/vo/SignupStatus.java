package com.early_express.user_service.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SignupStatus {
	PENDING("승인 대기 중"),
	ACCEPTED("가입 요청 승인 완료"),
	REJECTED("가입 요청이 거절되었습니다");

	private final String description;
}
