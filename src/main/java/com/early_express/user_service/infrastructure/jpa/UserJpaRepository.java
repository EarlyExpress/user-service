package com.early_express.user_service.infrastructure.jpa;

import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.vo.SignupStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<User, String> {

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	Page<User> findBySignupStatus(SignupStatus signupStatus, Pageable pageable);

	Page<User> findBySignupStatusOrderByCreatedAtDesc(SignupStatus signupStatus, Pageable pageable);
}
