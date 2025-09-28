package com.billyow.app.boardang.user.DTO;

import lombok.Data;

@Data
public class LoginResponse {
    public String token;
    public String tokenType;
    private Long expiresIn;
}
