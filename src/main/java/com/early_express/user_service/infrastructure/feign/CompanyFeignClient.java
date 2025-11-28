package com.early_express.user_service.infrastructure.feign;

import com.early_express.user_service.infrastructure.dto.CreateHubDriverRequest;
import com.early_express.user_service.infrastructure.dto.CreateManagerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="company-service")
public interface CompanyFeignClient {

	@PostMapping("/internal/companies/{companyId}/owner")
	void createManager(@RequestBody CreateManagerRequest request);
}
