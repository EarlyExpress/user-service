package com.early_express.user_service.infrastructure.init;

import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.repository.UserRepository;
import com.early_express.user_service.domain.vo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
	private final UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		// AuditorAware 구현체가 인증 객체를 필요
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("시스템", null, Collections.emptyList());
		SecurityContextHolder.getContext().setAuthentication(auth);

		User master = User.builder()
							  .keycloakId("d18c1faa-6cfe-4343-95a3-fcb59d203b92")
							  .username("peter8790")
							  .email("peter8790@naver.com")
							  .name("강동현")
							  .hubId(null)
							  .companyId(null)
							  .slackId("ndf9320fdnjks23")
							  .build();
		master.approveSignup(Role.MASTER);
		userRepository.save(master);

		// 초기화 후 임시 인증 제거
		SecurityContextHolder.clearContext();
	}
}
