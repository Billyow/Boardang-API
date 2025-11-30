package com.billyow.app.boardang.boardColumn.DTO;

import jakarta.validation.constraints.NotBlank;

public record BoardColumnCreateRequest(
        Long boardId,
        @NotBlank(message = "title cannot be blank")
        String title) {
}
