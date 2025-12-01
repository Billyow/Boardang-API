package com.billyow.app.boardang.task.DTO;

public record MoveTaskRequest(
        String taskId,
        Long newColumnId,
        Long boardId
) {
}
