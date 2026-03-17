package com.billyow.app.boardang.auth.service;
import com.billyow.app.boardang.auth.DTO.LoginRequest;
import com.billyow.app.boardang.auth.DTO.LoginResponse;
import com.billyow.app.boardang.auth.DTO.RefreshTokenRequest;
import com.billyow.app.boardang.auth.jwt.JwtProperties;
import com.billyow.app.boardang.auth.jwt.JwtService;
import com.billyow.app.boardang.auth.jwt.PrincipalUser;
import com.billyow.app.boardang.user.DTO.RegisterRequest;
import com.billyow.app.boardang.user.model.User;
import com.billyow.app.boardang.user.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;
    private final UserServiceImpl userServiceImpl;

    public LoginResponse login(LoginRequest request) {
        User user = userService.CfindByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        Map<String, Object> claims = Map.of("userId", user.getId(), "name", user.getName(), "role", user.getRole().name());
        String accessToken = jwtService.generateAccessToken(user.getEmail(), claims);
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return new LoginResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtProperties.getExpiration(),
                jwtProperties.getRefreshExpiration()
        );
    }

    public LoginResponse refresh(RefreshTokenRequest request) {
        String token = request.getRefreshToken();

        if (!jwtService.isRefreshTokenValid(token)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String email = jwtService.extractSubject(token);
        User user = userService.CfindByEmail(email);

        Map<String, Object> claims = Map.of("userId", user.getId(), "name", user.getName(), "role", user.getRole().name());
        String newAccessToken = jwtService.generateAccessToken(email, claims);
        String newRefreshToken = jwtService.generateRefreshToken(email);

        return new LoginResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                jwtProperties.getExpiration(),
                jwtProperties.getRefreshExpiration()
        );
    }

    public void register(RegisterRequest request) {
        userServiceImpl.register(request);
    }

    //retrieves the PrincipalUser in the security context holder
    public Long getCurrentUserId() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        var currentUser = authentication.getPrincipal();
        if (currentUser instanceof PrincipalUser) {
            return ((PrincipalUser) currentUser).getId();
        }
        throw new RuntimeException("User not authenticated");
    }
}
