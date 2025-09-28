package com.billyow.app.boardang.user.repository;

import com.billyow.app.boardang.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


interface IUserRepository {
    public Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndIsActiveTrue(String email);
    Boolean existsByEmail(String email);
}
