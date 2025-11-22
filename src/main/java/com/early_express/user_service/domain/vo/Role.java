package com.early_express.user_service.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@RequiredArgsConstructor
@Slf4j
public enum Role {
	DELIVERY("배송 담당자"),
	COMPANY("업체 담당자"),
	HUB_MANAGER("허브 관리자"),
	MASTER("마스터 관리자");


	private final String description;
}
