package com.billyow.app.boardang.user.mapper;
import com.billyow.app.boardang.user.DTO.RegisterRequest;
import com.billyow.app.boardang.user.DTO.UserDTO;
import com.billyow.app.boardang.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    User toUser(RegisterRequest dto);
    UserDTO toUserResponse(User user);
}
