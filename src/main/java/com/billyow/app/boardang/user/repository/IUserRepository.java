package com.billyow.app.boardang.user.repository;
import com.billyow.app.boardang.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IUserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndIsActiveTrue(String email);
    Boolean existsByEmail(String email);
    Optional<User> findById(Long id);
    List<User> findAllById(Set<Long> ids);
}
