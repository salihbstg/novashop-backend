package com.bastug.novashop.user;

import com.bastug.novashop.user.dto.authdto.AuthResponse;
import com.bastug.novashop.user.dto.authdto.LoginRequest;
import com.bastug.novashop.user.dto.authdto.RegisterRequest;
import com.bastug.novashop.user.dto.userdto.UserResponse;
import com.bastug.novashop.user.entity.User;
import com.bastug.novashop.user.enums.Role;
import com.bastug.novashop.exception.ApplicationExceptionImpl;
import com.bastug.novashop.user.mapper.UserMapper;
import com.bastug.novashop.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_shouldCreateUserAndReturnUserResponse() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("123");
        user.setEmail("email@email.com");
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setId(32L);
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPhone("05554555555");
        user.setAddress("Address");


        RegisterRequest request = new RegisterRequest(
                "username",
                "123",
                "email@gmail.com",
                "test",
                "test",
                "+905555555555",
                "address"
        );

        UserResponse mockResponse = new UserResponse(
                32L,
                "username",
                "email@gmail.com",
                "test",
                "test",
                "+905555555555",
                "address",
                "USER"
        );

        when(userRepository.existsByEmail("email@gmail.com"))
                .thenReturn(false);

        when(userRepository.existsByUsername("username"))
                .thenReturn(false);

        when(userRepository.existsByPhone("+905555555555"))
                .thenReturn(false);

        when(userMapper.toEntity(request))
                .thenReturn(user);

        when(userRepository.save(user))
                .thenReturn(user);

        when(passwordEncoder.encode(request.password()))
                .thenReturn("encoded-password");


        when(userMapper.toUserResponse(user))
                .thenReturn(mockResponse);

        UserResponse result = authService.register(request);

        assertThat(result.email()).isEqualTo("email@gmail.com");
        assertThat(result.username()).isEqualTo("username");
        assertThat(result.role()).isEqualTo("USER");
        assertThat(result.firstName()).isEqualTo("test");
        assertThat(result.lastName()).isEqualTo("test");

    }
    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {

        RegisterRequest request = new RegisterRequest(
                "username",
                "123",
                "email@gmail.com",
                "test",
                "test",
                "+905555555555",
                "address"
        );

        when(userRepository.existsByEmail("email@gmail.com"))
                .thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ApplicationExceptionImpl.class)
                .hasMessage("Mail adresi zaten kayıtlı");
    }
    @Test
    void register_shouldThrowException_whenPhoneAlreadyExists() {

        RegisterRequest request = new RegisterRequest(
                "username",
                "123",
                "email@gmail.com",
                "test",
                "test",
                "+905555555555",
                "address"
        );

        when(userRepository.existsByPhone("+905555555555"))
                .thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ApplicationExceptionImpl.class)
                .hasMessage("Telefon numarası zaten kayıtlı!");
    }
    @Test
    void register_shouldThrowException_whenUsernameAlreadyExists() {

        RegisterRequest request = new RegisterRequest(
                "username",
                "123",
                "email@gmail.com",
                "test",
                "test",
                "+905555555555",
                "address"
        );

        when(userRepository.existsByUsername("username"))
                .thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ApplicationExceptionImpl.class)
                .hasMessage("Kullanıcı adı zaten kayıtlı!");
    }
    @Test
    void login_success_shouldReturnAuthResponse() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("raw-password");

        User user = new User();
        user.setUsername("user");
        user.setPassword("encoded-password");
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("raw-password", "encoded-password"))
                .thenReturn(true);

        when(jwtService.generateToken("user", Role.ROLE_USER))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken("user"))
                .thenReturn("refresh-token");

        AuthResponse authResponse = authService.login(loginRequest);

        assertThat(authResponse.accessToken()).isEqualTo("access-token");
        assertThat(authResponse.refreshToken()).isEqualTo("refresh-token");
        assertThat(authResponse.tokenType()).isEqualTo("Bearer");
    }
    @Test
    void login_userNotFound_shouldThrowException() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("missing-user");
        loginRequest.setPassword("raw-password");

        when(userRepository.findByUsername("missing-user"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(ApplicationExceptionImpl.class);

    }
}
