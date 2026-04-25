package com.bastug.novashop.user.interfaces;

import com.bastug.novashop.user.dto.authdto.*;
import com.bastug.novashop.user.dto.userdto.UserResponse;
import org.jspecify.annotations.Nullable;

public interface AuthService {
    UserResponse register(RegisterRequest registerRequest);

    AuthResponse login(LoginRequest loginRequest);

    AuthResponse refreshToken(RefreshTokenRequest refreshToken);

    String changePassword(ChangePasswordRequest changePasswordRequest);
}
