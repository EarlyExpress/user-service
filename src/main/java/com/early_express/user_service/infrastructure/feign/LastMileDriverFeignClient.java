package com.early_express.user_service.infrastructure.feign;

import com.early_express.user_service.infrastructure.dto.CreateLastMileDriverRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="last-mile-driver-service")
public interface LastMileDriverFeignClient {
	@PostMapping("/internal/drivers")
	void createDriver(@RequestBody CreateLastMileDriverRequest request);
}
