package com.early_express.user_service.infrastructure.jpa;

import com.early_express.user_service.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, String> {

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);
}
