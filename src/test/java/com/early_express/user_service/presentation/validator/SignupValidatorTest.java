package com.early_express.user_service.presentation.validator;

import com.early_express.user_service.domain.repository.UserRepository;
import com.early_express.user_service.presentation.dto.UserRegister;
import com.early_express.user_service.presentation.exception.SignupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.early_express.user_service.presentation.exception.SignupErrorCode.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SignupValidatorTest {

	@InjectMocks
	private SignupValidator validator;

	@Mock
	private UserRepository repository;

	private UserRegister createDto(String username, String password, String confirmPassword) {
		return new UserRegister(username, password, confirmPassword, "test@example.com", "테스트", null, null);
	}

	@Test
	@DisplayName("유효한 입력 - 검증 통과")
	void validateCorrectInput() {
		// given
		UserRegister dto = createDto("test1234", "Password1!", "Password1!");
		given(repository.existsByEmail("test@example.com")).willReturn(false);
		given(repository.existsByUsername("test1234")).willReturn(false);

		// when & then
		assertDoesNotThrow(() -> validator.validate(dto));
	}

	@Test
	@DisplayName("비밀번호 불일치 - 예외 발생")
	void validatePasswordMismatch() {
		// given
		UserRegister dto = createDto("test1234", "Password1!", "Different1!");

		// when & then
		assertThatThrownBy(() -> validator.validate(dto)).isInstanceOf(SignupException.class)
														 .extracting("errorCode")
														 .isEqualTo(INVALID_SIGNUP_CONFIRM_PASSWORD);
	}

	@Test
	@DisplayName("이메일 중복 - 예외 발생")
	void validateDuplicateEmail() {
		// given
		UserRegister dto = createDto("test1234", "Password1!", "Password1!");
		given(repository.existsByEmail("test@example.com")).willReturn(true);

		// when & then
		assertThatThrownBy(() -> validator.validate(dto)).isInstanceOf(SignupException.class)
														 .extracting("errorCode").isEqualTo(DUPLICATE_SIGNUP_EMAIL);
	}

	@Test
	@DisplayName("아이디 중복 - 예외 발생")
	void validateDuplicateUsername() {
		// given
		UserRegister dto = createDto("test1234", "Password1!", "Password1!");
		given(repository.existsByEmail("test@example.com")).willReturn(false);
		given(repository.existsByUsername("test1234")).willReturn(true);

		// when & then
		assertThatThrownBy(() -> validator.validate(dto)).isInstanceOf(SignupException.class)
														 .extracting("errorCode").isEqualTo(DUPLICATE_SIGNUP_USERNAME);
		;
	}

	@Test
	@DisplayName("아이디에 대문자 포함 - 예외 발생")
	void validateUsernameWithUppercase() {
		// given
		UserRegister dto = createDto("TestUser", "Password1!", "Password1!");

		// when & then
		assertThatThrownBy(() -> validator.validate(dto)).isInstanceOf(SignupException.class)
														 .extracting("errorCode").isEqualTo(INVALID_SIGNUP_USERNAME);
	}

	@Test
	@DisplayName("아이디에 특수문자 포함 - 예외 발생")
	void validateUsernameWithSpecialChar() {
		// given
		UserRegister dto = createDto("test1234!", "Password1!", "Password1!");

		// when & then
		assertThatThrownBy(() -> validator.validate(dto)).isInstanceOf(SignupException.class)
														 .extracting("errorCode").isEqualTo(INVALID_SIGNUP_USERNAME);
	}

	@Test
	@DisplayName("비밀번호에 대문자 없음 - 예외 발생")
	void validatePasswordWithoutUppercase() {
		// given
		UserRegister dto = createDto("test1234", "password1!", "password1!");

		// when & then
		assertThatThrownBy(() -> validator.validate(dto)).isInstanceOf(SignupException.class)
														 .extracting("errorCode").isEqualTo(INVALID_SIGNUP_PASSWORD);
	}

	@Test
	@DisplayName("비밀번호에 소문자 없음 - 예외 발생")
	void validatePasswordWithoutLowercase() {
		// given
		UserRegister dto = createDto("test1234", "PASSWORD1!", "PASSWORD1!");

		// when & then
		assertThatThrownBy(() -> validator.validate(dto)).isInstanceOf(SignupException.class)
														 .extracting("errorCode").isEqualTo(INVALID_SIGNUP_PASSWORD);
	}

	@Test
	@DisplayName("비밀번호에 숫자 없음 - 예외 발생")
	void validatePasswordWithoutDigit() {
		// given
		UserRegister dto = createDto("test1234", "Password!!", "Password!!");

		// when & then
		assertThatThrownBy(() -> validator.validate(dto)).isInstanceOf(SignupException.class)
														 .extracting("errorCode").isEqualTo(INVALID_SIGNUP_PASSWORD);
	}

	@Test
	@DisplayName("비밀번호에 특수문자 없음 - 예외 발생")
	void validatePasswordWithoutSpecialChar() {
		// given
		UserRegister dto = createDto("test1234", "Password12", "Password12");

		// when & then
		assertThatThrownBy(() -> validator.validate(dto)).isInstanceOf(SignupException.class)
														 .extracting("errorCode").isEqualTo(INVALID_SIGNUP_PASSWORD);
	}
}