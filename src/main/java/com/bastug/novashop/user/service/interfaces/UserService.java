package com.bastug.novashop.user.service.interfaces;

import com.bastug.novashop.user.dto.userdto.UpdateUserRequest;
import com.bastug.novashop.user.dto.userdto.UserResponse;
import org.jspecify.annotations.Nullable;

public interface UserService {
    UserResponse findByUsername(String username);

    @Nullable UserResponse getCurrentUser();

    @Nullable UserResponse updateCurrentUser(UpdateUserRequest request);
}
