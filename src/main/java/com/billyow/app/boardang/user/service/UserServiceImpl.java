package com.billyow.app.boardang.user.service;
import com.billyow.app.boardang.user.model.User;
import com.billyow.app.boardang.user.repository.IUserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class UserServiceImpl implements IUserService{
    private final IUserRepository userRepository;

    UserServiceImpl(IUserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
