package com.early_express.user_service.domain.repository;

import com.early_express.user_service.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
	void save(User user);

	Optional<User> findById(String id);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);
}
