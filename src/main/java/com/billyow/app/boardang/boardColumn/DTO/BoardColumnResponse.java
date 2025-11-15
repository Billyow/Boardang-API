package com.billyow.app.boardang.boardColumn.DTO;

public record BoardColumnResponse(
        Long id,
        String title,
        Integer position,
        Long boardId
){}