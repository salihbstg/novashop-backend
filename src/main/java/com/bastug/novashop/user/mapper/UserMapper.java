package com.bastug.novashop.user.mapper;

import com.bastug.novashop.user.dto.authdto.RegisterRequest;
import com.bastug.novashop.user.dto.userdto.UserResponse;
import com.bastug.novashop.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // DTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest request);

    // Entity -> Response DTO
    UserResponse toUserResponse(User user);
}
