package com.early_express.user_service.infrastructure.messaging.event;

import java.time.LocalDateTime;

public record EmployeeCreatedEvent(
	String eventId,
	String eventType,
	String userId,
	String role,
	String hubId,
	String companyId,
	LocalDateTime approvedAt,
	String approvedByUUID
) {
}