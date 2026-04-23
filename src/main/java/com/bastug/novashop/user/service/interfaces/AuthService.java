package com.bastug.novashop.user.service.interfaces;

import com.bastug.novashop.user.dto.LoginRequest;
import com.bastug.novashop.user.dto.RegisterRequest;
import com.bastug.novashop.user.dto.UserResponse;

public interface AuthService {
    public UserResponse register(RegisterRequest registerRequest);
    public String login(LoginRequest loginRequest);

}
