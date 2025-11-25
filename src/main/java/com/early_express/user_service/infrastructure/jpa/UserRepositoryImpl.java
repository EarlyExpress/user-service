package com.early_express.user_service.infrastructure.jpa;

import com.early_express.user_service.domain.entity.User;
import com.early_express.user_service.domain.repository.UserRepository;
import com.early_express.user_service.domain.vo.SignupStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
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
	public Page<User> findAll(Pageable pageable) {
		return repository.findAll(pageable);
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

	@Override
	public Page<User> findBySignupStatus(SignupStatus signupStatus, Pageable pageable) {
		return repository.findBySignupStatus(signupStatus, pageable);
	}

	@Override
	public Page<User> findBySignupStatusOrderByCreatedAtDesc(SignupStatus signupStatus, Pageable pageable) {
		return repository.findBySignupStatusOrderByCreatedAtDesc(signupStatus, pageable);
	}

	@Override
	public void deleteAll() {
		repository.deleteAll();
	}


}
