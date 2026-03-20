package com.billyow.app.boardang.user.service;
import com.billyow.app.boardang.exception.ConflictException;
import com.billyow.app.boardang.exception.ResourceNotFoundException;
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
    public void register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ConflictException("email already in use");
        }
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        userRepository.save(newUser);
    }


    @Override
    public User CfindByEmail(String email) {
        long start = System.nanoTime();
         var userCache=userCacheService.getUserByEmail(email)
                .filter(User::getIsActive)
                .orElseGet(() -> {
                    User user = userRepository.findByEmailAndIsActiveTrue(email)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    userCacheService.cacheUserByEmail(email, user); //
                    return user;
                });
         long end = System.nanoTime();
         return userCache;
    }


    public User findByEmail(String email) {
        long start = System.nanoTime();
        var user= userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        long end = System.nanoTime();
        return user;
    }


    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

}
