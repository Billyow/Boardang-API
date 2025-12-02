package com.billyow.app.boardang.user.service;

import com.billyow.app.boardang.user.DTO.RegisterRequest;
import com.billyow.app.boardang.user.model.User;

import java.util.Optional;

public interface IUserService {
    User CfindByEmail(String email);
    User findByEmail(String email);
    Optional<User> findById(Long id);
    void register(RegisterRequest request);
}
