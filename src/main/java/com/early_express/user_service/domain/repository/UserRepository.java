package com.early_express.user_service.domain.repository;

import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.vo.SignupStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
	void save(User user);

	Optional<User> findById(String id);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	Page<User> findBySignupStatus(SignupStatus signupStatus, Pageable pageable);

	Page<User> findBySignupStatusOrderByCreatedAtDesc(SignupStatus signupStatus, Pageable pageable);
}
