package com.billyow.app.boardang.auth.jwt;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * Secret key for signing and verifying JWTs.
     */
    private String secret;

    /**
     * JWT token expiration time in milliseconds.
     */
    private long expiration;
}
