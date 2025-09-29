package com.billyow.app.boardang.auth.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class LoginResponse {
    public String token;
    public String tokenType;
    private Long expiresIn;
}
