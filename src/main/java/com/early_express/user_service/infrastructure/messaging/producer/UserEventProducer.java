package com.early_express.user_service.infrastructure.messaging.producer;

import com.early_express.user_service.infrastructure.messaging.event.SignupAcceptedEvent;
import com.early_express.user_service.infrastructure.messaging.event.SignupRejectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventProducer {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Value("${spring.application.name:user-service}")
	private String applicationName;

	private static final String EVENTS_TOPIC_SUFFIX = "-events";

	public void publishSignupAccepted(SignupAcceptedEvent event) {
		String topic = applicationName + EVENTS_TOPIC_SUFFIX;

		CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, event.email(), event);

		future.whenComplete((result, ex) -> {
			if (ex == null) {
				log.info("SignupAcceptedEvent 발행 성공: email={}, role={}, approvedBy={}, partition={}, offset={}",
						event.email(),
						event.role(),
						event.approvedByEmail(),
						result.getRecordMetadata().partition(),
						result.getRecordMetadata().offset());
			} else {
				log.error("SignupAcceptedEvent 발행 실패: email={}, error={}",
						event.email(), ex.getMessage(), ex);
			}
		});
	}

	public void publishSignupRejected(SignupRejectedEvent event) {
		String topic = applicationName + EVENTS_TOPIC_SUFFIX;

		CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, event.email(), event);

		future.whenComplete((result, ex) -> {
			if (ex == null) {
				log.info("SignupRejectedEvent 발행 성공: email={}, reason={}, rejectedBy={}, partition={}, offset={}",
						event.email(),
						event.reason(),
						event.rejectedByEmail(),
						result.getRecordMetadata().partition(),
						result.getRecordMetadata().offset());
			} else {
				log.error("SignupRejectedEvent 발행 실패: email={}, error={}",
						event.email(), ex.getMessage(), ex);
			}
		});
	}
}

