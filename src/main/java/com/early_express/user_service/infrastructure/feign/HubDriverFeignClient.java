package com.early_express.user_service.infrastructure.feign;

import com.early_express.user_service.infrastructure.dto.CreateHubDriverRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="hub-driver-service")
public interface HubDriverFeignClient {

	@PostMapping("/internal/drivers")
	void createDriver(@RequestBody CreateHubDriverRequest request);
}
