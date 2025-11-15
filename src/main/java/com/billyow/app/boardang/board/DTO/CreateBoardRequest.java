package com.billyow.app.boardang.board.DTO;

import jakarta.validation.constraints.NotBlank;

public record CreateBoardRequest(
        @NotBlank String title,
        String description
        ) {
}
