package com.billyow.app.boardang.user.repository;
import com.billyow.app.boardang.user.model.User;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public class JPAUserRepositoryImpl implements IUserRepository {
    private final IJPAUserRepository userJpaRepository;
    JPAUserRepositoryImpl(IJPAUserRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    public Optional<User> findById(Long id){
        return userJpaRepository.findById(id);
    }

    public User save(User user) {
        return userJpaRepository.save(user);
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmailAndIsActiveTrue(String email) {
        return userJpaRepository.findByEmailAndIsActiveTrue(email);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
