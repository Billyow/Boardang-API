package com.billyow.app.boardang.board.DTO;

import java.util.Date;

public record BoardSummaryResponse(
        Long id,
        String title,
        String description,
        Date createdAt,
        Date updatedAt
) {
}
