package com.billyow.app.boardang.user.service;
import com.billyow.app.boardang.user.DTO.RegisterRequest;
import com.billyow.app.boardang.user.cache.UserCacheService;
import com.billyow.app.boardang.user.model.User;
import com.billyow.app.boardang.user.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
@AllArgsConstructor
@Service
public class UserServiceImpl implements IUserService{
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCacheService userCacheService;


    @Override
    public User register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Ese correo ya existe");
        }
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        return userRepository.save(newUser);
    }

    @Override
    public User CfindByEmail(String email) {

        return userCacheService.getUserByEmail(email)
                .filter(user -> user.getIsActive())
                .orElseGet(() ->{
                    return userRepository.findByEmailAndIsActiveTrue(email)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                });
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public User save(User user) {

        return userRepository.save(user);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

}
