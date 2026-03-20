package com.billyow.app.boardang.task.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskRequest(
     @NotBlank(message = "Task title cannot be blank")
     String title,
     String description,
     @NotNull(message = "Must select a priority")
     Integer priority,
     @NotNull(message = "Column id not found")
     Long columnId
){}
