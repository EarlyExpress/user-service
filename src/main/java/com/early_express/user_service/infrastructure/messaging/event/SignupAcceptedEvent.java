package com.early_express.user_service.infrastructure.messaging.event;

import java.time.LocalDateTime;

public record SignupAcceptedEvent(
	String eventId,
	String eventType,
	String name,
	String email,
	String role,
	String slackId,
	LocalDateTime approvedAt,
	String approvedByEmail
) {
}
