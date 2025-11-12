package com.billyow.app.boardang.task.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskRequest(
     @NotNull(message = "Owners ID missing")
     Long ownerId,
     @NotBlank(message = "Task title cannot be blank")
     String title,
     String description,
     @NotNull(message = "Must select a priority")
     Integer priority,
     @NotNull(message = "Column id not found")
     Long boardColumnId,
     @NotNull(message = "Board id not found")
     Long boardId
){}
