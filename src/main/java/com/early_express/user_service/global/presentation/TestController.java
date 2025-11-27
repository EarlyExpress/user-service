package com.early_express.user_service.global.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Slf4j
public class TestController {

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/test")
    public String test() {
        return "Default Server is working!";
    }

	@GetMapping("/auth/test/headers")
	public void test5(@RequestHeader(name="X-User-Id", required = false) String userId,
					  @RequestHeader(name="X-Username", required = false) String userName,
					  @RequestHeader(name="X-User-Email", required = false) String email,
					  @RequestHeader(name="X-User-Role", required = false) String role) {

		log.info("X-User-Id: {}, X-User-Name: {}, X-User-Email: {}, X-User-Role: {}", userId, userName, email, role);

	}
}
