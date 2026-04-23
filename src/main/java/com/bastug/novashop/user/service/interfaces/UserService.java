package com.bastug.novashop.user.service.interfaces;

import com.bastug.novashop.user.dto.UserResponse;
import org.jspecify.annotations.Nullable;

public interface UserService {
    UserResponse findByUsername(String username);

    @Nullable UserResponse getCurrentUser();
}
