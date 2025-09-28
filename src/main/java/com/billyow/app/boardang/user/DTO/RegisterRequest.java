package com.billyow.app.boardang.user.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;
    @NotBlank(message = "El correo no puede estar vacío")
    private String email;
    @NotBlank
    private String password;
}
