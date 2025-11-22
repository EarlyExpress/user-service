package com.early_express.user_service.infrastructure.jpa;

import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository repository;

	@Override
	public void save(User user) {
		repository.save(user);
	}

	@Override
	public Optional<User> findById(String id) {
		return repository.findById(id);
	}

	@Override
	public boolean existsByEmail(String email) {
		return repository.existsByEmail(email);
	}

	@Override
	public boolean existsByUsername(String username) {
		return repository.existsByUsername(username);
	}


}
