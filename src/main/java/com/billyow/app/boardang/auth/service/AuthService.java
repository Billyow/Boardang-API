package com.billyow.app.boardang.auth.service;
import com.billyow.app.boardang.auth.DTO.LoginRequest;
import com.billyow.app.boardang.auth.DTO.LoginResponse;
import com.billyow.app.boardang.auth.jwt.JwtProperties;
import com.billyow.app.boardang.auth.jwt.JwtService;
import com.billyow.app.boardang.user.model.User;
import com.billyow.app.boardang.user.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@AllArgsConstructor
@Service
public class AuthService {

    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndIsActiveTrue(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                Map.of("userId", user.getId(), "name", user.getName())
        );

        return new LoginResponse(
                token,
                "Bearer",
                jwtProperties.getExpiration()
        );
    }
}
