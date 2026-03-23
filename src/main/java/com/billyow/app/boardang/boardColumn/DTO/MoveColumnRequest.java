package com.billyow.app.boardang.boardColumn.DTO;

public record MoveColumnRequest(
        Long afterColumnId,
        Long beforeColumnId
) {}
