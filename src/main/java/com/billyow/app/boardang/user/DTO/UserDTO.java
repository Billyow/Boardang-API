package com.billyow.app.boardang.user.DTO;

import com.billyow.app.boardang.user.model.Role;
import java.util.Date;

public record UserDTO(
        Long id,
        String name,
        String email,
        Date createdAt,
        Date updatedAt,
        Boolean isActive,
        Role role) {
}
