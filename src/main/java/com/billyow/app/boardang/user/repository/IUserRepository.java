package com.billyow.app.boardang.user.repository;
import com.billyow.app.boardang.user.model.User;
import java.util.Optional;
public interface IUserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndIsActiveTrue(String email);
    Boolean existsByEmail(String email);
}
