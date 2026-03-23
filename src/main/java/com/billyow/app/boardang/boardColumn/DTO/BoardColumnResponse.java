package com.billyow.app.boardang.boardColumn.DTO;

import com.billyow.app.boardang.task.DTO.TaskResponse;

import java.util.List;

public record BoardColumnResponse(
        Long id,
        String title,
        Double position,
        List<TaskResponse> tasks
){}