package com.early_express.user_service.infrastructure.messaging.event;

import java.time.LocalDateTime;

public record SignupRejectedEvent(
	String eventId,
	String eventType,
	String name,
	String email,
	String reason,
	String slackId,
	LocalDateTime rejectedAt,
	String rejectedByEmail
) {
}
